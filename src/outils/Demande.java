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
		numEtage = 0;
		sens = Sens.INDEFINI;
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
	 * Retourne vrai si le sens est indefini
	 * @return true si le sens est indefini
	 */
	public boolean estIndefini() {
		return (this.sens == Sens.INDEFINI ? true : false);
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
		return (outils.Sens.DESCENTE == sens) ? true : false;
	}

	/**
	 * Passe d'un �tage � un autre, selon le sens augmente ou diminu le num�ro de l'�tage
	 * @throws ExceptionCabineArretee se d�clenche si la cabine est arr�t�
	 */
	public void passeEtageSuivant() throws ExceptionCabineArretee {
		if(sens == Sens.INDEFINI) throw new ExceptionCabineArretee();
		if(sens == Sens.MONTEE) this.numEtage++;
		else this.numEtage--;
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
	/**
	 * Red�finition de la m�thode ToString()
	 * Renvoi une chaine contenant le num�ro de l'�tage et le sens
	 * @return un String contenant le num�ro de l'�tage et le sens
	 */
	public String toString(){
		String phrase= this.numEtage.toString();
		if(sens == Sens.MONTEE)phrase += "^";
		else if(sens == Sens.DESCENTE)phrase += "v";
		else phrase += "-";
		
		return phrase;		
	}
	
//	/**
//	 * Red�finition de hashCode pour Demande 
//	 * @return int repr�sentant le HashCode de la classe Demande
//	 */
//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + ((numEtage == null) ? 0 : numEtage.hashCode());
//		result = prime * result + ((sens == null) ? 0 : sens.hashCode());
//		return result;
//	}

	/**
	 * Red�finition de equals pour comparer les objets
	 * @param o repr�sente l'objet � comparer
	 * @return true si l'objet de la classe est �gale � celui pass� en param�tre et faux sinon
	 */
	public boolean equals(Object o){
		if(o == null){return false;}
		else if(getClass() != o.getClass()){return false;}
//		else if(hashCode() != o.hashCode()) { return false;}
		else if((this.numEtage.equals(((Demande) o).etage()))&&(this.sens.equals(((Demande) o).sens()))) {
			return true;
		} else {
			return false;
		}
	}
}
