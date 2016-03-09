package outils;

import commande.ListeTrieeCirculaireDeDemandes;

public class Controleur implements IControleur {
	private int nombreEtages = 5;
	private int position;
	private Sens sens;// les 3
	private Sens sensPrecedent;// sens en monté ou en descente
	private Demande demande;
	private ListeTrieeCirculaireDeDemandes ListeDemande;
	private static Controleur INSTANCE = null;
	private IIUG iug = null;
	private ICabine cabine = null;

	private Controleur() {
		ListeDemande = new ListeTrieeCirculaireDeDemandes(nombreEtages);
	}

	public static Controleur getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new Controleur();
		}
		return INSTANCE;
	}

	/**
	 * met sens à INDEFINI si la cabine est arrêtée, MONTEE si la cabine monte
	 * et DESCENTE si elle descend
	 */
	@Override
	public void MAJSens() {
		sensPrecedent = sens;
		if (ListeDemande.estVide()) {
			sens = Sens.INDEFINI;
		} else {
			Demande d = (Demande) ListeDemande.suivantDe(demande);
			sens = d.sens();
		}
	}

	/**
	 * Stocke la demande
	 */
	@Override
	public void stocker(Demande d) {
		if (d.estIndefini()) {
			if (((sens == Sens.MONTEE) || (sens == Sens.DESCENTE)) && (d.etage() > position)) {
				d.changeSens(Sens.MONTEE);
			} else if(((sens == Sens.MONTEE) || (sens == Sens.DESCENTE)) && (d.etage() < position)){
				d.changeSens(Sens.DESCENTE);
			}
		}
		ListeDemande.inserer(d);
		if(iug != null){
			if(position == d.etage()){
				enleverDuStock(d);
				iug.eteindreBouton(d);
			}
		}
	}

	/**
	 * incrémente position de 1 si sens vaut MONTEE, décrémente de 1 si sens
	 * vaut DESCENTE
	 */
	@Override
	public void MAJPosition() {
		if (sens == Sens.MONTEE) {
			position++;
		} else if (sens == Sens.DESCENTE) {
			position--;
		}
	}

	/**
	 * Vide le stock des demandes
	 */
	@Override
	public void viderStock() {
		ListeDemande.vider();
	}

	/**
	 * Eteint TOUS les boutons /!\
	 */
	@Override
	public void eteindreTousBoutons() {
		if (iug != null) {
			for (int etage = 0; etage < nombreEtages; etage++) {
				iug.eteindreBouton(new Demande(etage, Sens.INDEFINI));
			}
		}
	}

	/**
	 * renvoie la demande du stock qui vérifie certaines conditions par rapport
	 * à la position et au sens ou au sensPrecedent
	 */
	@Override
	public Demande interrogerStock(Demande d) {
		return (Demande) ListeDemande.suivantDe(d);
	}

	/**
	 * Enlève la demande du stock
	 */
	@Override
	public void enleverDuStock(Demande d) {
		ListeDemande.supprimer(d);
	}

	/**
	 * Signale le changement d'étage
	 */
	@Override
	public void signalerChangementIDEtage() {
		if (cabine != null) {
			Demande d = (Demande) ListeDemande.suivantDe(demande);
			// Si il est en montée ou en descente et que son étage est
			// strictement le suivant, la cabine s'arrête au suivant
			if (((d.sens() == sens.MONTEE) && (d.etage() == position + 1))
					|| ((d.sens() == sens.DESCENTE) && (d.etage() == position - 1))) {
				cabine.arreterProchainNiveau();
			}
			MAJPosition();
		}
	}

	/**
	 * 
	 */
	@Override
	public void demander(Demande d) {
		if (iug != null && cabine != null) {
			// Si la cabine n'est pas en mouvement c.a.d si il n'y a pas de dmd
			if (ListeDemande.estVide()) {
				if (d.etage() > position) {
					cabine.monter();
				} else if (d.etage() < position) {
					cabine.descendre();
				}
				MAJSens();
				// si l'étage demandé est le suivant de la position actuel
				if (Math.abs(d.etage() - position) == 1) {
					// Vu que l'étage demandé est le suivant stric de la
					// position actuel
					// on s'arret au prochain étage
					cabine.arreterProchainNiveau();
				} else {
					stocker(d);
				}
				iug.allumerBouton(d);
			} else {
				// if (((d.etage() != position) || (d.sens() != sensPrecedent))
				// || ((d.etage() != position + 1) || (d.sens() != sens))) {
				// if(d.etage() != position)
				// if(Math.abs(d.etage() - position) == 1)
				// A OPTIMISER MAIS CA DEVRAIT FONCTIONNER
				stocker(d);
				iug.allumerBouton(d);
			}
		}
	}

	@Override
	public void arretDUrgence() {
		// A OPTIMISER MAIS CA DEVRAIT FONCTIONNER:
		// Si la cabine est arretée on ne fais que viderStock() et
		// eteindreTousBoutons();
		if (!ListeDemande.estVide()) {
			viderStock();
			eteindreTousBoutons();
			cabine.arreter();
			MAJSens();
		}
	}

}
