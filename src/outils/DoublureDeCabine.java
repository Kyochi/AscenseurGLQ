package outils;

public class DoublureDeCabine implements ICabine{
	@Override
	public void monter() {
		// TODO Auto-generated method stub
		System.out.println("monter");
		Controleur ctrl= Controleur.INSTANCE;
		ctrl.signalerChangementIDEtage();
	}

	@Override
	public void descendre() {
		// TODO Auto-generated method stub
		System.out.println("descendre");
		Controleur ctrl= Controleur.INSTANCE;
		ctrl.signalerChangementIDEtage();
	}

	@Override
	public void arreterProchainNiveau() {
		System.out.println("arreter prochain �tage");
		// TODO Auto-generated method stub
		
	}

	@Override
	public void arreter() {
		System.out.println("arreter");
		// TODO Auto-generated method stub
		
	}

}
