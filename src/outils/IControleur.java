package outils;

public interface IControleur {
	/**
	 * met sens à INDEFINI si la cabine est arrêtée, MONTEE si la cabine monte et DESCENTE si elle descend
	 */
	void MAJSens();
	/**
	 * stocke la demande
	 * @param d représente une Demande
	 */
	void stocker(Demande d);
	/**
	 * incrémente position de 1 si sens vaut MONTEE, décrémente de 1 si sens vaut DESCENTE
	 */
	void MAJPosition();
	/**
	 * vide le stock des demandes
	 */
	void viderStock();
	/**
	 * éteint tous les boutons
	 */
	void eteindreTousBoutons();
	/**
	 * renvoie la demande du stock qui vérifie certaines conditions par rapport à la position et au sens ou au sensPrecedent
	 */
	void interrogerStock();
	/**
	 * enlève la demande du stock
	 * @param d représente une Demande
	 */
	void enleverDuStock(Demande d);
	/**
	 * Signal le changement d'étage
	 */
	void signalerChangementIDEtage();
}
