package outils;

/**
 * Sens repr�sente une �num�ration des diff�rents sens possible
 * 
 * @author marjorie
 *
 */
public enum Sens {
	DESCENTE, MONTEE, INDEFINI;

	/**
	 * M�thode surcharger de toString
	 * @return un string qui est le sens
	 */
	public String toString() {
		switch (this) {
		case MONTEE:
			return "^";
		case DESCENTE:
			return "v";
		default:
			return "-";

		}
	}
}
