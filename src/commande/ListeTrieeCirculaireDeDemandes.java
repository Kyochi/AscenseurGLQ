package commande;

import java.util.ArrayList;

import javax.management.relation.RelationServiceNotRegisteredException;

import outils.Demande;

public class ListeTrieeCirculaireDeDemandes implements IListeTrieeCirculaire{
	private Demande[] listeDemande;
	private int ind;
	public ListeTrieeCirculaireDeDemandes(int i) {
		listeDemande = new Demande[i-1];
		ind = 0;
	}

	@Override
	public int taille() {
		return listeDemande.length;
	}

	@Override
	public boolean estVide() {
		return (ind == 0) ? true : false;
	}

	@Override
	public void vider() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean contient(Object e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void inserer(Object e) {
		if(ind < listeDemande.length){
			listeDemande[ind] = (Demande)e;
			ind++;
		}
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
		String phrase="[";
		for(int i = 0; i< ind; i++){			
			phrase += listeDemande[i].toString();
			if(i != ind-1) phrase += ",";
		}
		System.out.println(phrase + "]");
		return phrase + "]";
	}
}
