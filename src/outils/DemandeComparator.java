package outils;

import java.io.Serializable;
import java.util.Comparator;

/**
 * La classe DemandeComparator sert à comparer des Demandes
 * @author Marjorie
 *
 */
public class DemandeComparator implements Comparator<Demande>, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Redéfinition de compare permettant de comparer 2 Demande
	 * @return int
	 */
    @Override
    public int compare(Demande o1, Demande o2) {
        if(o1.sens() == o2.sens() && o1.sens() == Sens.MONTEE) {
        	return o1.etage()-o2.etage();
        } else if(o1.sens() == o2.sens() && o1.sens() == Sens.DESCENTE) {
        	return o2.etage()-o1.etage();
        }
        if(o1.sens() == Sens.MONTEE) {
        	return -1;
        } else {
        	return 1;
        }
    }
}
