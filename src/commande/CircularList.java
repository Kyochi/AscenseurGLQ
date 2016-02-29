package commande;

import java.util.ArrayList;

import outils.Demande;

public class CircularList<Demande> extends ArrayList<Demande> {

	public CircularList(int capacity) {
		super(capacity);
	}
	
	public Demande get (int index) {
		if (index < 0) index  = index + this.size();
		
		return this.get(index);
	}
	
}
