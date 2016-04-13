package commande;



import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import outils.Demande;
import outils.DemandeComparator;
import outils.Sens;
/**
 * La classe ListeTrieeCirculaireDeDemandes repr�sente une liste circulaire de demandes
 * @author Marjorie
 *
 */
public class ListeTrieeCirculaireDeDemandes implements IListeTrieeCirculaire{

	public static HashMap<Integer, Long> maptimers = new HashMap<Integer, Long>();
	public static ArrayList<Long> listTempsAttente = new ArrayList<Long>();
	public static Long tempsAttenteMoyen;
	
	/**
	 * M�thode permettant le calcul/stockage des temps d'attente et du calcul du temps d'attente moyen depuis le 
	 * le d�marrage de l'ascenseur
	 * @param etageServi etage qui vient d'�tre desservi par l'ascenseur
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
	 * @param capacite repr�sente le nombre de pallier
	 */
	public ListeTrieeCirculaireDeDemandes(int capacite) {
		listeTrieeCirculaire = new CircularList<>(capacite);
	}

	/**
	 * Retourne la taille de la listeCirculaire
	 * @return un int repr�sentant la taille de la listeCirculaire
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
	 * M�thode qui permet de vider la listeCirculaire
	 */
	@Override
	public void vider() {
		listeTrieeCirculaire.clear();
	}

	/**
	 * Indique si la listeCirculaire contien l'objet pass� en param�tre
	 * @param e repr�sente un Object
	 * @return true si la listeCirculaire contient l'objet pass� en param�tre
	 */
	@Override
	public boolean contient(Object e) {
		return listeTrieeCirculaire.contains((Demande)e);
	}

	/**
	 * M�thode qui ins�re un objet dans la listeCirculaire
	 * @param e repr�sente un object
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
	 * M�thode qui permet de suprimer un Objet de la listeCirculaire
	 * @param e repr�sente un object
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
	 * M�thode qui renvoi l'objet suivant de l'objet pass� en param�tre
	 * @param courant repr�sente un Object (ou l'on va chercher sont suivant)
	 * @return Object repr�sentant l'objet suivant de celui pass� en param�tre
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
	 * Red�finition de la m�thode toString 
	 * @return String 
	 */
	public String toString(){
		return listeTrieeCirculaire.toString();
		
	}
}
