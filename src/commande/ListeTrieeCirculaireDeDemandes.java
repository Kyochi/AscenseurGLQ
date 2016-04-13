package commande;



import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import outils.Demande;
import outils.DemandeComparator;
import outils.Sens;
/**
 * La classe ListeTrieeCirculaireDeDemandes représente une liste circulaire de demandes
 * @author Marjorie
 *
 */
public class ListeTrieeCirculaireDeDemandes implements IListeTrieeCirculaire{

	public static HashMap<Integer, Long> maptimers = new HashMap<Integer, Long>();
	public static ArrayList<Long> listTempsAttente = new ArrayList<Long>();
	public static Long tempsAttenteMoyen;
	
	/**
	 * Méthode permettant le calcul/stockage des temps d'attente et du calcul du temps d'attente moyen depuis le 
	 * le démarrage de l'ascenseur
	 * @param etageServi etage qui vient d'être desservi par l'ascenseur
	 */
	public static void calculTempsAttente(int etageServi) {
		
		if (maptimers.containsKey(etageServi)) {
			// stockage du temps en seconde
			long tempsAttente = (System.currentTimeMillis() ) - maptimers.get(etageServi);
			listTempsAttente.add(tempsAttente);
			
			// Calcul moyenne temps d'attente
			long totalTime = 0;
			for (Long temps : listTempsAttente) {
				totalTime +=temps;
			}
			
			tempsAttenteMoyen = totalTime / listTempsAttente.size();
			
			maptimers.remove(etageServi);
			System.out.println("Le temps d'attente moyen actuel est de : " + tempsAttenteMoyen);
		}
	}
	
	private CircularList<Demande> listeTrieeCirculaire;

	/**
	 * Constructeur permettant d'initialiser la listeCirculaire avec la capacite
	 * @param capacite représente le nombre de pallier
	 */
	public ListeTrieeCirculaireDeDemandes(int capacite) {
		listeTrieeCirculaire = new CircularList<>(capacite);
	}

	/**
	 * Retourne la taille de la listeCirculaire
	 * @return un int représentant la taille de la listeCirculaire
	 */
	@Override
	public int taille() {
		return listeTrieeCirculaire.size();
	}

	/**
	 * Indique si la ListeCirculaire est vide ou non
	 * @return true si la listeCirculaire est vide et false sinon
	 */
	@Override
	public boolean estVide() {
		return listeTrieeCirculaire.isEmpty();
	}

	/**
	 * Méthode qui permet de vider la listeCirculaire
	 */
	@Override
	public void vider() {
		listeTrieeCirculaire.clear();
	}

	/**
	 * Indique si la listeCirculaire contien l'objet passé en paramètre
	 * @param e représente un Object
	 * @return true si la listeCirculaire contient l'objet passé en paramètre
	 */
	@Override
	public boolean contient(Object e) {
		return listeTrieeCirculaire.contains((Demande)e);
	}

	/**
	 * Méthode qui insère un objet dans la listeCirculaire
	 * @param e représente un object
	 */
	@Override
	public void inserer(Object e) {
		Demande d = (Demande)e;
		if(d.sens() == Sens.INDEFINI) {
			throw new IllegalArgumentException();
		}
		if(d.etage() < 0 || listeTrieeCirculaire.getCapacite() <= d.etage()) {
			throw new IllegalArgumentException();
		}
		if(d.etage() == 0 && d.enDescente()) {
			throw new IllegalArgumentException();
		}
		if(d.etage() == listeTrieeCirculaire.getCapacite()-1 && d.enMontee()) {
			throw new IllegalArgumentException();
		}
		if(contient(e)) {
//			throw new IllegalArgumentException();
			return;
		}
		listeTrieeCirculaire.add((Demande)e);
		
		Collections.sort(listeTrieeCirculaire, new DemandeComparator());
	}

	/**
	 * Méthode qui permet de suprimer un Objet de la listeCirculaire
	 * @param e représente un object
	 */
	@Override
	public void supprimer(Object e) {
		if(!listeTrieeCirculaire.contains(e)) {
			throw new IllegalArgumentException();
		}else {
			
			listeTrieeCirculaire.remove(e);
			System.out.println(listeTrieeCirculaire.toString());
		}
	}

	/**
	 * Méthode qui renvoi l'objet suivant de l'objet passé en paramètre
	 * @param courant représente un Object (ou l'on va chercher sont suivant)
	 * @return Object représentant l'objet suivant de celui passé en paramètre
	 */
	@Override
	public Object suivantDe(Object courant) {
		
		if(listeTrieeCirculaire.size() <= 0) return null;
		if(listeTrieeCirculaire.contains(courant)) {
			return listeTrieeCirculaire.get(listeTrieeCirculaire.indexOf(courant));
		} else {
			ListeTrieeCirculaireDeDemandes liste = new ListeTrieeCirculaireDeDemandes(this.listeTrieeCirculaire.getCapacite());
			for (Demande demande : listeTrieeCirculaire) {
				liste.inserer(demande);
			}
			liste.inserer(courant);
			return liste.listeTrieeCirculaire.get(liste.listeTrieeCirculaire.indexOf(courant)+1);
		}
		
	}
	/**
	 * Redéfinition de la méthode toString 
	 * @return String 
	 */
	public String toString(){
		return listeTrieeCirculaire.toString();
		
	}
}
