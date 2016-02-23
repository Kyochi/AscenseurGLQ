package outils;

public class Demande {
	private Integer numEtage;
	private Sens sens;
	public Demande() {
		numEtage = null;
	}
	public Demande(int etage, Sens montee) {
		numEtage = etage;
		sens = montee;
	}
	public int etage() { return numEtage; }

//    ___
//   //  7
//  (_,_/\
//   \    \
//    \    \
//    _\    \__
//   (   \     )
//    \___\___/
	
	public boolean estIndefini() {
		return (this.numEtage == null ? true : false);
	}
	public boolean enMontee() {
		return (Sens.MONTEE == sens) ? true : false;
	}
	public boolean enDescente() {
		return (Sens.DESCENTE == sens) ? true : false;
	}
}
