package commande;

import static org.junit.Assert.assertEquals;
import operative.ICabine;
import operative.IIUG;
import outils.Demande;
import outils.Sens;
import static org.junit.Assert.*;

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
	private int position ;
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

	private boolean arretUrgence = false;
	/**
	 * Constructeur de Controleur.
	 * @param nbEtage nbetage est le nombre d'étages
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
	 * met sens à INDEFINI si la cabine est arrêtée,.
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
			if(sens == sens.INDEFINI)sens = demande.sens();
			if((position == nombreEtages-1)&& (sens == Sens.MONTEE))sens = Sens.DESCENTE;
			if((position == 0)&& (sens == Sens.DESCENTE))sens = Sens.MONTEE;
			if ((Demande) listeDemande.suivantDe(new Demande(position,sens)) != null) {
				d = (Demande) listeDemande.suivantDe(new Demande(position,sens));
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
		System.out.println("appel "+d);
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
	 * incrémente position de 1 si sens vaut MONTEE,.
	 * décrémente de 1 si sens vaut DESCENTE.
	 */
	public void MAJPosition() {
		if (sens == Sens.MONTEE) {
			position = position + 1;
			iug.changerPosition(position);
			cabine.monter();
			assertEquals(sens, Sens.MONTEE);
		} else if (sens == Sens.DESCENTE) {
			position = position - 1;
			iug.changerPosition(position);
			assertEquals(sens, Sens.DESCENTE);
			cabine.descendre();
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
				iug.eteindreBouton(new Demande(demande.etage(),Sens.INDEFINI));
				enleverDuStock(demande);
				while (!listeDemande.estVide()) {
					Demande uneDmd = interrogerStock();
					iug.eteindreBouton(uneDmd);
					iug.eteindreBouton(new Demande(uneDmd.etage(),Sens.INDEFINI));
					demande = uneDmd;
					enleverDuStock(uneDmd);
					assertFalse(listeDemande.contient(uneDmd));
				}
			}
		}
	}

	/**
	 * renvoie la demande du stock qui vérifie certaines.
	 * conditions par rapport à la position et au sens ou au sensPrecedent.
	 */
	 public Demande interrogerStock() {
		return (Demande) getListeDemande().suivantDe(demande);
	}

	/**
	 * Enlève la demande du stock.
	 */
	 public void enleverDuStock(final Demande d) {
		if(getListeDemande().contient(d))
		getListeDemande().supprimer(d);
	}

	/**
	 * Signale le changement d'étage.
	 * @throws Exception 
	 */
	public final synchronized void signalerChangementDEtage() throws Exception {
		System.out.println(listeDemande);
		if (cabine != null && iug != null) {
			Demande d;
			if ((Demande) listeDemande.suivantDe(demande) != null) {
				d = (Demande) listeDemande.suivantDe(demande);
				if (sens == Sens.DESCENTE) {
					if (getListeDemande().taille() > 1) {
						if (position == 0)sens = sens.MONTEE;
						Demande d2 = (Demande) getListeDemande().suivantDe(new Demande(position,sens));
						if ((d.etage() > d2.etage())
								&& (position > d2.etage())
								&& (d2.sens() == d.sens())) {
							d = d2;
							assertEquals(d,d2);
						}
					}
				} else {
					if(position == nombreEtages-1)sens = Sens.DESCENTE;
					d = (Demande) listeDemande.suivantDe(new Demande(position,sens));	
				}
				
			} else {
				d = this.demande;
			}	
			if (position == d.etage().intValue()){
				cabine.arreterProchainNiveau();
				iug.eteindreBouton(d);
				iug.eteindreBouton(new Demande(d.etage().intValue(),Sens.INDEFINI));
				enleverDuStock(d);
				MAJSens();
			}MAJPosition();
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
	 * Demander représente une demande à un étage.
	 */
	@Override
	public void demander(final Demande d) {
		if (!arretUrgence) {
		demande = d;
		if (iug != null && cabine != null) {
				if ((d.etage() != getPosition()) && (!getListeDemande().contient(new Demande(d.etage(), Sens.DESCENTE)))
						&& (!getListeDemande().contient(new Demande(d.etage(), Sens.MONTEE)))) {
					iug.allumerBouton(d);
				}
				if(((position +1 == d.etage().intValue())&&(d.sens() == Sens.MONTEE))
						||((position -1 == d.etage().intValue())) && (d.sens() == Sens.DESCENTE)){
					cabine.arreterProchainNiveau();
				}
				stocker(d);
				MAJSens();
				try {
					signalerChangementDEtage();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		}
	}

	/**
	 * Méthode qui arrete la cabine vide le stock de demande et éteind tous les boutons
	 */
	@Override
	public void arretUrgence() {
		if (!getListeDemande().estVide()) {
			cabine.arreter();
			eteindreTousBoutons();
			viderStock();
			MAJSens();
			assertEquals(sens,Sens.INDEFINI);
			arretUrgence = true;
		}else{
			arretUrgence = false;
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

	@Override
	public void exit() {
		// TODO Auto-generated method stub
		
	}

}
