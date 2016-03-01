package outils;

import commande.ListeTrieeCirculaireDeDemandes;

public class Controleur implements IControleur{
	
	private ListeTrieeCirculaireDeDemandes ListeDemande;
	private static Controleur INSTANCE = null;
	
	private Controleur() {
		ListeDemande = new ListeTrieeCirculaireDeDemandes(5);
	}
	
	public static Controleur getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new Controleur();
		}
		return INSTANCE;
	}
	
	@Override
	public void MAJSens() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stocker(Demande d) {
		ListeDemande.inserer(d);
		
	}

	@Override
	public void MAJPosition() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void viderStock() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void eteindreTousBoutons() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void interrogerStock() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enleverDuStock(Demande d) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void signalerChangementIDEtage() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void demander(Demande d) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void arretDUrgence() {
		// TODO Auto-generated method stub
		
	}

}
