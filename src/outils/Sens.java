package outils;

/**
 * Sens représente une énumération des différents sens possible
 * 
 * @author marjorie
 *
 */
public enum Sens {
	DESCENTE, MONTEE, INDEFINI;

	/**
	 * Méthode surcharger de toString
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
