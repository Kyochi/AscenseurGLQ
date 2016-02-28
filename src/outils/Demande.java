package outils;

/**
 * Une demande repr�sente une demande faite par un utilisateur, interne ou externe � l'ascenseur
 * @author marjorie
 *
 */
public class Demande {
	/**
	 * Le num�ro d'�tage
	 */
	private Integer numEtage;
	/**
	 * Le sens de mont�
	 */
	private Sens sens;
	
	/**
	 * Constructeur vide de Demande
	 */
	public Demande() {
		numEtage = null;
	}
	
	/**
	 * Constructeur de Demande avec deux param�tres, repr�sente une demande � un �tage dans un sens
	 * @param etage, le num�ro de l'�tage
	 * @param montee, le sens de mont�
	 */
	public Demande(int etage, Sens montee) {
		numEtage = etage;
		sens = montee;
	}
	/**
	 * Renvoi le num�ro de l'�tage
	 * @return un int repr�sentant le num�ro de l'�tage
	 */
	public int etage() { return numEtage; }

	/**
	 * Retourne vrai si le num�ro de l'�tage n'est pas d�finit et faux sinon
	 * @return true si le num�ro d'�tage est null et false sinon
	 */
	public boolean estIndefini() {
		return (this.numEtage == null ? true : false);
	}
	/**
	 * Retourne vrai si le sens est en mont� et faux sinon
	 * @return true si le sens est en mont� et faux sinon
	 */
	public boolean enMontee() {
		return (Sens.MONTEE == sens) ? true : false;
	}
	/**
	 * Retourne vrai si le sens est en d�scente et faux sinon
	 * @return true si le sens est en d�scente et faux sinon
	 */
	public boolean enDescente() {
		return (Sens.DESCENTE == sens) ? true : false;
	}

	/**
	 * Passe d'un �tage � un autre, selon le sens augmente ou diminu le num�ro de l'�tage
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
	 * @param s repr�sente le sens
	 */
	public void changeSens(Sens s) {
		this.sens = s;
	}
}
