package commande;

import java.util.ArrayList;

import outils.Demande;

public class CircularList<Demande> extends ArrayList<Demande> {
	
	private int capacite;
	
	public int getCapacite() {
		return capacite;
	}

	private void setCapacite(int capacite) {
		this.capacite = capacite;
	}

	public CircularList(int capacity) {
		super(capacity);
		setCapacite(capacity);
	}
	
	public Demande get (int index) {
		System.out.println("Index : " + index + " " + this.toString());
		if(super.size() <= 0) return null;
		if (index < 0) index  = index + super.size();
		if (index >= super.size()) index = super.size() - index;
		return super.get(index);
	}
	
	public String toString() {
		String retour = "[";
		for (Demande demande : this) {
			retour += demande.toString()+",";
		}
		retour += "]";
		return retour.replace(",]","]");
	}
	
}
