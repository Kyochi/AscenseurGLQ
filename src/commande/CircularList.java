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
	 * Retourne la capacit�
	 * @return int repr�sentant la capacit�
	 */
	public int getCapacite() {
		return capacite;
	}

	/**
	 * Affecte la capacit� pass� en param�tre
	 * @param capacite
	 */
	private void setCapacite(int capacite) {
		this.capacite = capacite;
	}

	/**
	 * Constructeur de la classe 
	 * @param capacity repr�sente la capacite de la circularList
	 */
	public CircularList(int capacity) {
		super(capacity);
		setCapacite(capacity);
	}
	
	/**
	 * Renvoi une Demande � l'index sp�cifi�
	 * @param index repr�sente l'index
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
	 * Red�finition de toString 
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
