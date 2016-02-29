package commande;



import javax.management.relation.RelationServiceNotRegisteredException;

import outils.Demande;

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
		listeTrieeCirculaire.add((Demande)e);
	}

	@Override
	public void supprimer(Object e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object suivantDe(Object courant) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String toString(){
		return listeTrieeCirculaire.toString();
		
	}
}
