package outils;

/**
 * Une demande représente une demande faite par un utilisateur, interne ou externe à l'ascenseur
 * @author marjorie
 *
 */
public class Demande {
	/**
	 * Le numéro d'étage
	 */
	private Integer numEtage;
	/**
	 * Le sens de monté
	 */
	private Sens sens;
	
	/**
	 * Constructeur vide de Demande
	 */
	public Demande() {
		numEtage = null;
	}
	
	/**
	 * Constructeur de Demande avec deux paramètres, représente une demande à un étage dans un sens
	 * @param etage, le numéro de l'étage
	 * @param montee, le sens de monté
	 */
	public Demande(int etage, Sens montee) {
		numEtage = etage;
		sens = montee;
	}
	/**
	 * Renvoi le numéro de l'étage
	 * @return un int représentant le numéro de l'étage
	 */
	public int etage() { return numEtage; }

	/**
	 * Retourne vrai si le numéro de l'étage n'est pas définit et faux sinon
	 * @return true si le numéro d'étage est null et false sinon
	 */
	public boolean estIndefini() {
		return (this.numEtage == null ? true : false);
	}
	/**
	 * Retourne vrai si le sens est en monté et faux sinon
	 * @return true si le sens est en monté et faux sinon
	 */
	public boolean enMontee() {
		return (Sens.MONTEE == sens) ? true : false;
	}
	/**
	 * Retourne vrai si le sens est en déscente et faux sinon
	 * @return true si le sens est en déscente et faux sinon
	 */
	public boolean enDescente() {
		return (Sens.DESCENTE == sens) ? true : false;
	}

	/**
	 * Passe d'un étage à un autre, selon le sens augmente ou diminu le numéro de l'étage
	 */
	public void passeEtageSuivant() {
		if(sens == Sens.MONTEE) this.numEtage++;
		else this.numEtage--;;		
	}

	/**
	 * Retourne le sens
	 * @return le Sens 
	 */
	public Sens sens() {
		return this.sens;
	}
	/**
	 * Change le sens dans lequel va l'ascenseur
	 * @param s représente le sens
	 */
	public void changeSens(Sens s) {
		this.sens = s;
	}
}
