package outils;

import commande.IControleur;
import commande.ListeTrieeCirculaireDeDemandes;

/**
 * Classe Controleur.
 * @author Marjorie
 *
 */
public final class Controleur implements IControleur {
	/**
	 * Nombre d'étage.
	 */
	private int nombreEtages;
	/**
	 * Position de l'ascenseur.
	 */
	private int position;
	/**
	 * Sens de l'ascenseur.
	 */
	private Sens sens; // les 3
	/**
	 * Sens précèdent.
	 */
	private Sens sensPrecedent; // sens en monté ou en descente
	/**
	 * Demande courrante.
	 */
	private Demande demande;
	/**
	 * Liste de demande.
	 */
	private ListeTrieeCirculaireDeDemandes listeDemande;
	/**
	 * Instance de controleur.
	 */
	public static Controleur INSTANCE = null;
	/**
	 * iug.
	 */
	private IIUG iug;
	/**
	 * Cabine.
	 */
	private ICabine cabine;

	/**
	 * Constructeur de Controleur.
	 * @param nbEtage nbetage
	 * @param posCabine positionCabine
	 * @param piug iug
	 * @param pcabine cabine
	 */
	private Controleur(final int nbEtage, final int posCabine,
			final IIUG piug, final ICabine pcabine) {
		listeDemande = (new ListeTrieeCirculaireDeDemandes(nbEtage));
		sens = Sens.INDEFINI;
		position = posCabine;
		nombreEtages = nbEtage;
		iug = piug;
		cabine = pcabine;
	}

	/**
	 * Instance de controleur.
	 * @param nbEtage nb etage
	 * @param posCabine position cabine
	 * @param piug iug
	 * @param pcabine cabine
	 * @return Controleur qui est une instance de controleur
	 */
	public static Controleur getInstance(final int nbEtage,
			final int posCabine, final IIUG piug,
			final ICabine pcabine) {
		if (INSTANCE == null) {
			INSTANCE = new Controleur(nbEtage, posCabine,
					piug, pcabine);
		}
		return INSTANCE;
	}

	/**
	 * met sens à INDEFINI si la cabine est arrêtée,.
	 * MONTEE si la cabine monte et DESCENTE si elle descend.
	 */
	@Override
	public void MAJSens() {
		if (sens != sensPrecedent) {
			sensPrecedent = sens;
		}
		if (getListeDemande().estVide()) {
			sens = Sens.INDEFINI;
		} else {
			Demande d = demande;
			if ((Demande) listeDemande.suivantDe(demande) != null) {
				d = (Demande) listeDemande.suivantDe(demande);
			}
			if (d.etage() > position) {
				sens = Sens.MONTEE;
			} else {
				sens = Sens.DESCENTE;
			}
		}
	}

	/**
	 * Stocke la demande.
	 */
	@Override
	public void stocker(final Demande d) {
		if (d.estIndefini()) {
			if (((sens == Sens.MONTEE) || (sens == Sens.DESCENTE))
					&& (d.etage() > getPosition())) {
				d.changeSens(Sens.MONTEE);
			} else if (((sens == Sens.MONTEE)
					|| (sens == Sens.DESCENTE))
					&& (d.etage() < getPosition())) {
				d.changeSens(Sens.DESCENTE);
			} else {
				if (d.etage() > getPosition()) {
					d.changeSens(Sens.MONTEE);
				} else {
					d.changeSens(Sens.DESCENTE);
				}
			}
			//si l'étage demandé et tout en haut ou tout en bas
			if (d.etage() == nombreEtages - 1) {
				d.changeSens(Sens.DESCENTE);
			} else if (d.etage() == 0) {
				d.changeSens(Sens.MONTEE);
			}
			//MAJSens();
		}
		getListeDemande().inserer(d);
		if (iug != null) {
			if (getPosition() == d.etage()) {
				enleverDuStock(d);
			}
		}
	}

	/**
	 * incrémente position de 1 si sens vaut MONTEE,.
	 * décrémente de 1 si sens vaut DESCENTE.
	 */
	@Override
	public void MAJPosition() {
		if (sens == Sens.MONTEE) {
			position = position + 1;
		} else if (sens == Sens.DESCENTE) {
			position = position - 1;
		}
	}

	/**
	 * Vide le stock des demandes.
	 */
	@Override
	public void viderStock() {
		getListeDemande().vider();
	}

	/**
	 * Eteint TOUS les boutons /!\.
	 */
	@Override
	public void eteindreTousBoutons() {
		if (iug != null) {
			if (!listeDemande.estVide()) {
				iug.eteindreBouton(demande);
				enleverDuStock(demande);
				while (!listeDemande.estVide()) {
					Demande uneDmd = interrogerStock();
					iug.eteindreBouton(uneDmd);
					demande = uneDmd;
					enleverDuStock(uneDmd);
				}
			}
		}
	}

	/**
	 * renvoie la demande du stock qui vérifie certaines.
	 * conditions par rapport à la position et au sens ou au sensPrecedent.
	 */
	@Override
	public Demande interrogerStock() {
		return (Demande) getListeDemande().suivantDe(demande);
	}

	/**
	 * Enlève la demande du stock.
	 */
	@Override
	public void enleverDuStock(final Demande d) {
		getListeDemande().supprimer(d);
	}

	/**
	 * Signale le changement d'étage.
	 */
	@Override
	public void signalerChangementIDEtage() {
		if (cabine != null && iug != null) {
			Demande d;
			if ((Demande) listeDemande.suivantDe(demande) != null) {
				d = (Demande) listeDemande.suivantDe(demande);
				if (sens == Sens.DESCENTE) {
					if (getListeDemande().taille() > 1) {
						Demande d2 = (Demande) getListeDemande().suivantDe(new Demande(demande.etage()-1,demande.sens()));
						if ((d.etage() > d2.etage())
								&& (position > d2.etage())
								&& (d2.sens() == d.sens())) {
							d = d2;
						}
					}
				} else {
					d = (Demande) listeDemande.suivantDe(demande);
				}
				
			} else {
				d = this.demande;
			}
			if (position != d.etage()) {	
				d = (Demande)listeDemande.suivantDe(new Demande(this.position,this.sens));
				if (((sens == Sens.MONTEE) && (d.etage() == position + 1))
						|| ((sens == Sens.DESCENTE) && (d.etage() == position - 1))) {
					cabine.arreterProchainNiveau();
					enleverDuStock(d);
				}
			}
			MAJPosition();
			System.out.println("signal de franchissement de palier");
			if (position == d.etage()) {
				iug.eteindreBouton(d);
			}
			// Met le sens de la cabine à indéfini si il n'y a
			// plus de demande et le sens précèdent avec le sens
			if (listeDemande.estVide()) {
				if (sens != sensPrecedent) {
					sensPrecedent = sens;
				}
				sens = Sens.INDEFINI;
			}
		}
	}

	/**
	 * Demander représente une demande à un étage.
	 */
	@Override
	public void demander(final Demande d) {
		demande = d;
		System.out.println("appel " + d);
		if (iug != null && cabine != null) {
			// Si la cabine n'est pas en mouvement c.a.d si il n'y a pas de dmd
			if (getListeDemande().estVide()) {
				if (d.etage() != getPosition()) {
					iug.allumerBouton(d);
				}
				stocker(d);
				MAJSens();
				if (d.etage() > getPosition()) {
					cabine.monter();
				} else if (d.etage() < getPosition()) {
					cabine.descendre();
				}
			} else {
				//si il y a déjà une demande pour l'étage demandé on allume pas le bouton
				if ((!getListeDemande().contient(new Demande(d.etage(), Sens.DESCENTE)))
						&& (!getListeDemande().contient(new Demande(d.etage(), Sens.MONTEE)))) {
					iug.allumerBouton(d);
				}
				stocker(d);
				MAJSens();
				if (sens != sensPrecedent) {
					if (d.etage() > getPosition()) {
						cabine.monter();
					} else if (d.etage() < getPosition()) {
						cabine.descendre();
					}
				}
			}
		}
	}

	@Override
	public void arretDUrgence() {
		// A OPTIMISER MAIS CA DEVRAIT FONCTIONNER:
		// Si la cabine est arretée on ne fais que viderStock() et
		// eteindreTousBoutons();
		if (!getListeDemande().estVide()) {
			cabine.arreter();
			eteindreTousBoutons();
			viderStock();
			MAJSens();
		}
	}

	/**
	 * getPosition représente la position de la cabine.
	 * @return la position de la cabine
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * getListeDemande représente la liste des demandes.
	 * @return ListeTrieeCirculaireDeDemandes qui est la liste de demandes
	 */
	public ListeTrieeCirculaireDeDemandes getListeDemande() {
		return listeDemande;
	}


}
