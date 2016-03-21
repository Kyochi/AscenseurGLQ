package commande;

import static org.junit.Assert.assertEquals;
import outils.Demande;
import outils.ICabine;
import outils.IIUG;
import outils.Sens;
import static org.junit.Assert.*;

/**
 * Classe Controleur.
 * @author Marjorie
 *
 */
public final class Controleur implements IControleur {
	/**
	 * Nombre d'�tage.
	 */
	private int nombreEtages;
	/**
	 * Position de l'ascenseur.
	 */
	private int position ;
	/**
	 * Sens de l'ascenseur.
	 */
	private Sens sens; // les 3
	/**
	 * Sens pr�c�dent.
	 */
	private Sens sensPrecedent; // sens en mont� ou en descente
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
	 * @param nbEtage nbetage est le nombre d'�tages
	 * @param piug iug est l'iug
	 * @param pcabine pcabine est la cabine
	 * @param liste est la liste de demande
	 */
	public Controleur(final int nbEtage, final IIUG piug, 
			final ICabine pcabine, final IListeTrieeCirculaire<Demande> liste) {
		listeDemande = (ListeTrieeCirculaireDeDemandes) liste;
		sens = Sens.INDEFINI;
		position = 0;
		nombreEtages = nbEtage;
		iug = piug;
		cabine = pcabine;
	}

	/**
	 * met sens � INDEFINI si la cabine est arr�t�e,.
	 * MONTEE si la cabine monte et DESCENTE si elle descend.
	 */
	public void MAJSens() {
		if (sens != sensPrecedent) {
			assertNotEquals(sens,sensPrecedent);
			sensPrecedent = sens;
			assertEquals(sens,sensPrecedent);
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
			//si l'�tage demand� et tout en haut ou tout en bas
			if (d.etage() == nombreEtages - 1) {
				d.changeSens(Sens.DESCENTE);
			} else if (d.etage() == 0) {
				d.changeSens(Sens.MONTEE);
			}
		}
		getListeDemande().inserer(d);
		if (iug != null) {
			if (getPosition() == d.etage()) {
				assertTrue(listeDemande.contient(d));
				enleverDuStock(d);
				assertFalse(listeDemande.contient(d));
			}
		}
	}

	/**
	 * incr�mente position de 1 si sens vaut MONTEE,.
	 * d�cr�mente de 1 si sens vaut DESCENTE.
	 */
	public void MAJPosition() {
		if (sens == Sens.MONTEE) {
			position = position + 1;
			assertEquals(sens, Sens.MONTEE);
		} else if (sens == Sens.DESCENTE) {
			position = position - 1;
			assertEquals(sens, Sens.DESCENTE);
		}
	}

	/**
	 * Vide le stock des demandes.
	 */
	public void viderStock() {
		getListeDemande().vider();
		assertTrue(listeDemande.estVide());
	}

	/**
	 * Eteint TOUS les boutons /!\.
	 */
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
					assertFalse(listeDemande.contient(uneDmd));
				}
			}
		}
	}

	/**
	 * renvoie la demande du stock qui v�rifie certaines.
	 * conditions par rapport � la position et au sens ou au sensPrecedent.
	 */
	public Demande interrogerStock() {
		return (Demande) getListeDemande().suivantDe(demande);
	}

	/**
	 * Enl�ve la demande du stock.
	 */
	public void enleverDuStock(final Demande d) {
		getListeDemande().supprimer(d);
	}

	/**
	 * Signale le changement d'�tage.
	 */
	public void signalerChangementDEtage() {
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
							assertEquals(d,d2);
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
			// Met le sens de la cabine � ind�fini si il n'y a
			// plus de demande et le sens pr�c�dent avec le sens
			if (listeDemande.estVide()) {
				assertTrue(listeDemande.estVide());
				if (sens != sensPrecedent) {
					sensPrecedent = sens;
				}
				sens = Sens.INDEFINI;
			}
		}
	}

	/**
	 * Demander repr�sente une demande � un �tage.
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
				//si il y a d�j� une demande pour l'�tage demand� on allume pas le bouton
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

	/**
	 * M�thode qui arrete la cabine vide le stock de demande et �teind tous les boutons
	 */
	@Override
	public void arretUrgence() {
		if (!getListeDemande().estVide()) {
			cabine.arreter();
			eteindreTousBoutons();
			viderStock();
			MAJSens();
			assertEquals(sens,Sens.INDEFINI);
		}
	}

	/**
	 * getPosition repr�sente la position de la cabine.
	 * @return la position de la cabine
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * getListeDemande repr�sente la liste des demandes.
	 * @return ListeTrieeCirculaireDeDemandes qui est la liste de demandes
	 */
	public ListeTrieeCirculaireDeDemandes getListeDemande() {
		return listeDemande;
	}

}
