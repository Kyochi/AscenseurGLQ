package outils;

/**
 * La classe ExceptionCabineArretee extend de Exception
 * @author Marjorie
 *
 */
public class ExceptionCabineArretee extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Constructeur de la classe 
	 */
	public ExceptionCabineArretee() {
		super("La cabine est arreté");
	}
}
