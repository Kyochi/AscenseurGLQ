package outils;

public class ExceptionCabineArretee extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ExceptionCabineArretee() {
		super("La cabine est arreté");
	}
}
