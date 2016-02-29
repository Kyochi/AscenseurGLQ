package outils;

import java.util.Comparator;

public class DemandeComparator implements Comparator<Demande> {
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
