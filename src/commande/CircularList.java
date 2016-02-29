package commande;

import java.util.ArrayList;

import outils.Demande;

/**
 * La classe CircularList extends de ArrayList
 * @author Marjorie
 * @param <Demande> est une Demande
 */
public class CircularList<Demande> extends ArrayList<Demande> {
	
	private int capacite;
	
	/**
	 * Retourne la capacité
	 * @return int représentant la capacité
	 */
	public int getCapacite() {
		return capacite;
	}

	/**
	 * Affecte la capacité passé en paramètre
	 * @param capacite
	 */
	private void setCapacite(int capacite) {
		this.capacite = capacite;
	}

	/**
	 * Constructeur de la classe 
	 * @param capacity représente la capacite de la circularList
	 */
	public CircularList(int capacity) {
		super(capacity);
		setCapacite(capacity);
	}
	
	/**
	 * Renvoi une Demande à l'index spécifié
	 * @param index représente l'index
	 * @return Demande
	 */
	public Demande get (int index) {
		System.out.println("Index : " + index + " " + this.toString());
		if(super.size() <= 0) return null;
		if (index < 0) index  = index + super.size();
		if (index >= super.size()) index = super.size() - index;
		return super.get(index);
	}
	
	/**
	 * Redéfinition de toString 
	 * @return String
	 */
	public String toString() {
		String retour = "[";
		for (Demande demande : this) {
			retour += demande.toString()+",";
		}
		retour += "]";
		return retour.replace(",]","]");
	}
	
}
