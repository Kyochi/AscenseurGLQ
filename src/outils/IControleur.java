package outils;

public interface IControleur {
	/**
	 * met sens � INDEFINI si la cabine est arr�t�e, MONTEE si la cabine monte et DESCENTE si elle descend
	 */
	void MAJSens();
	/**
	 * stocke la demande
	 * @param d repr�sente une Demande
	 */
	void stocker(Demande d);
	/**
	 * incr�mente position de 1 si sens vaut MONTEE, d�cr�mente de 1 si sens vaut DESCENTE
	 */
	void MAJPosition();
	/**
	 * vide le stock des demandes
	 */
	void viderStock();
	/**
	 * �teint tous les boutons
	 */
	void eteindreTousBoutons();
	/**
	 * renvoie la demande du stock qui v�rifie certaines conditions par rapport � la position et au sens ou au sensPrecedent
	 */
	void interrogerStock();
	/**
	 * enl�ve la demande du stock
	 * @param d repr�sente une Demande
	 */
	void enleverDuStock(Demande d);
	/**
	 * Signal le changement d'�tage
	 */
	void signalerChangementIDEtage();
}
