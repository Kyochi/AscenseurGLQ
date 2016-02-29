package commande;



import java.util.Collections;

import javax.management.relation.RelationServiceNotRegisteredException;

import outils.Demande;
import outils.DemandeComparator;
import outils.Sens;

public class ListeTrieeCirculaireDeDemandes implements IListeTrieeCirculaire{
	
	private CircularList<Demande> listeTrieeCirculaire;
	public ListeTrieeCirculaireDeDemandes(int capacite) {
		listeTrieeCirculaire = new CircularList<>(capacite);
	}

	@Override
	public int taille() {
		return listeTrieeCirculaire.size();
	}

	@Override
	public boolean estVide() {
		return listeTrieeCirculaire.isEmpty();
	}

	@Override
	public void vider() {
		listeTrieeCirculaire.removeAll(listeTrieeCirculaire);
		
	}

	@Override
	public boolean contient(Object e) {
		return listeTrieeCirculaire.contains((Demande)e);
	}

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
		if(listeTrieeCirculaire.contains(e)) {
//			throw new IllegalArgumentException();
			return;
		}
		listeTrieeCirculaire.add((Demande)e);
		Collections.sort(listeTrieeCirculaire, new DemandeComparator());
	}

	@Override
	public void supprimer(Object e) {
		if(!listeTrieeCirculaire.contains(e)) {
			throw new IllegalArgumentException();
		}
		listeTrieeCirculaire.remove(e);
	}

	@Override
	public Object suivantDe(Object courant) {
		if(listeTrieeCirculaire.contains(courant)) {
			return listeTrieeCirculaire.get(listeTrieeCirculaire.indexOf(courant)+1);
		} else {
			ListeTrieeCirculaireDeDemandes liste = new ListeTrieeCirculaireDeDemandes(this.listeTrieeCirculaire.getCapacite());
			for (Demande demande : listeTrieeCirculaire) {
				liste.inserer(demande);
			}
			liste.inserer(courant);
			return liste.suivantDe(courant);
		}
	}
	
	public String toString(){
		return listeTrieeCirculaire.toString();
		
	}
}
