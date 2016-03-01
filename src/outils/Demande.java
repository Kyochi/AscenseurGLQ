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
		numEtage = 0;
		sens = Sens.INDEFINI;
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
	 * Retourne vrai si le sens est indefini
	 * @return true si le sens est indefini
	 */
	public boolean estIndefini() {
		return (this.sens == Sens.INDEFINI ? true : false);
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
		return (outils.Sens.DESCENTE == sens) ? true : false;
	}

	/**
	 * Passe d'un étage à un autre, selon le sens augmente ou diminu le numéro de l'étage
	 * @throws ExceptionCabineArretee se déclenche si la cabine est arrêté
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
	 * @param s représente le sens
	 */
	public void changeSens(Sens s) {
		this.sens = s;
	}
	/**
	 * Redéfinition de la méthode ToString()
	 * Renvoi une chaine contenant le numéro de l'étage et le sens
	 * @return un String contenant le numéro de l'étage et le sens
	 */
	public String toString(){
		String phrase= this.numEtage.toString();
		if(sens == Sens.MONTEE)phrase += "^";
		else if(sens == Sens.DESCENTE)phrase += "v";
		else phrase += "-";
		
		return phrase;		
	}
	
//	/**
//	 * Redéfinition de hashCode pour Demande 
//	 * @return int représentant le HashCode de la classe Demande
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
	 * Redéfinition de equals pour comparer les objets
	 * @param o représente l'objet à comparer
	 * @return true si l'objet de la classe est égale à celui passé en paramètre et faux sinon
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
