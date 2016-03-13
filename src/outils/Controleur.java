package outils;

import static org.junit.Assert.assertSame;

import javax.swing.text.Position;

import com.sun.org.apache.bcel.internal.generic.NEW;

import commande.ListeTrieeCirculaireDeDemandes;

public class Controleur implements IControleur {
	private int nombreEtages;
	private int position;
	private Sens sens;// les 3
	private Sens sensPrecedent;// sens en monté ou en descente
	private Demande demande;
	private ListeTrieeCirculaireDeDemandes ListeDemande;
	public static Controleur INSTANCE = null;
	private DoublureDeIUG iug = new DoublureDeIUG();
	private DoublureDeCabine cabine = new DoublureDeCabine();

	private Controleur(int nbEtage, int posCabine) {
		ListeDemande = (new ListeTrieeCirculaireDeDemandes(nbEtage));
		sens = Sens.INDEFINI;
		position = posCabine;
		nombreEtages = nbEtage;
	}

	public static Controleur getInstance(int nbEtage, int posCabine) {
		if (INSTANCE == null) {
			INSTANCE = new Controleur(nbEtage,posCabine);
		}
		return INSTANCE;
	}

	/**
	 * met sens à INDEFINI si la cabine est arrêtée, MONTEE si la cabine monte
	 * et DESCENTE si elle descend
	 */
	@Override
	public void MAJSens() {
		sensPrecedent=sens;
		if (getListeDemande().estVide()) {
			sens=Sens.INDEFINI;
		} else {
			Demande d = demande;
			if((Demande)ListeDemande.suivantDe(demande) != null){
				d = (Demande)ListeDemande.suivantDe(demande);				
			}
			if(d.etage() > position){
				sens=Sens.MONTEE;
			}else{
				sens=Sens.DESCENTE;
			}
		}
	}

	/**
	 * Stocke la demande
	 */
	@Override
	public void stocker(Demande d) {
		if (d.estIndefini()) {
			if (((sens == Sens.MONTEE) || (sens == Sens.DESCENTE)) && (d.etage() > getPosition())) {
				d.changeSens(Sens.MONTEE);
			} else if(((sens == Sens.MONTEE) || (sens == Sens.DESCENTE)) && (d.etage() < getPosition())){
				d.changeSens(Sens.DESCENTE);
			}else{
				if(d.etage() > getPosition()){
					d.changeSens(Sens.MONTEE);
				}else{
					d.changeSens(Sens.DESCENTE);
				}
			}
			//si l'étage demandé et tout en haut ou tout en bas
			if(d.etage() == nombreEtages-1){
				d.changeSens(Sens.DESCENTE);
			}else if(d.etage() == 0){
				d.changeSens(Sens.MONTEE);
			}
		}
		getListeDemande().inserer(d);
		if(iug != null){
			if(getPosition() == d.etage()){
				enleverDuStock(d);
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
			position= position + 1;
		} else if (sens == Sens.DESCENTE) {
			position = position - 1;
		}
	}

	/**
	 * Vide le stock des demandes
	 */
	@Override
	public void viderStock() {
		getListeDemande().vider();
	}

	/**
	 * Eteint TOUS les boutons /!\
	 */
	@Override
	public void eteindreTousBoutons() {
		if (iug != null) {
			if(!ListeDemande.estVide()){
				iug.eteindreBouton(demande);
				enleverDuStock(demande);
				while(!ListeDemande.estVide()){
					Demande uneDmd = interrogerStock();
					iug.eteindreBouton(uneDmd);
					demande = uneDmd;
					enleverDuStock(uneDmd);
				}
			}
		}
	}

	/**
	 * renvoie la demande du stock qui vérifie certaines conditions par rapport
	 * à la position et au sens ou au sensPrecedent
	 */
	@Override
	public Demande interrogerStock() {
		return (Demande) getListeDemande().suivantDe(demande);
	}

	/**
	 * Enlève la demande du stock
	 */
	@Override
	public void enleverDuStock(Demande d) {
		getListeDemande().supprimer(d);
	}

	/**
	 * Signale le changement d'étage
	 */
	@Override
	public void signalerChangementIDEtage() {
		if (cabine != null && iug != null) {
			Demande d;
			if((Demande)ListeDemande.suivantDe(demande) != null)
			{
				d = (Demande)ListeDemande.suivantDe(demande);
			}else {
				d = this.demande;
			}
			if(position != d.etage()){					
				d = (Demande)ListeDemande.suivantDe(new Demande(this.position,this.sens));
				// Si il est en montée ou en descente et que son étage est
				// strictement le suivant, la cabine s'arrête au suivant
				if (((sens == Sens.MONTEE) && (d.etage() == position + 1))
						|| ((sens == Sens.DESCENTE) && (d.etage() == position - 1))) {
					cabine.arreterProchainNiveau();
					enleverDuStock(d);				
				}
				MAJPosition();
				System.out.println("signal de franchissement de palier");
			}
			if(position == d.etage()){
				iug.eteindreBouton(d);
			}
			
			// Met le sens de la cabine à indéfini si il n'y a plus de demande
			// et le sens précèdent avec le sens
			if(ListeDemande.estVide()){
				sensPrecedent = sens;
				sens = Sens.INDEFINI;
			}
		}
	}

	/**
	 * 
	 */
	@Override
	public void demander(Demande d) {
		demande = d;
		System.out.println("appel " +d);
		if (iug != null && cabine != null) {
			// Si la cabine n'est pas en mouvement c.a.d si il n'y a pas de dmd
			if (getListeDemande().estVide()) {
				if(d.etage() != getPosition())iug.allumerBouton(d);
				stocker(d);
				MAJSens();
				if (d.etage() > getPosition()) {
					cabine.monter();
				} else if (d.etage() < getPosition()) {
					cabine.descendre();
				}
				// si l'étage demandé est le suivant de la position actuel
				/*if (Math.abs(d.etage() - getPosition()) == 1) {
					// Vu que l'étage demandé est le suivant stric de la
					// position actuel
					// on s'arret au prochain étage
					cabine.arreterProchainNiveau();			
				} */
			} else {
				//si il y a déjà une demande pour l'étage demandé on allume pas le bouton
				if((!getListeDemande().contient(new Demande(d.etage(),Sens.DESCENTE)))
						&& (!getListeDemande().contient(new Demande(d.etage(),Sens.MONTEE)))){
					iug.allumerBouton(d);
				}
				stocker(d);
			}
		}
	}

	@Override
	public void arretDUrgence() {
		// A OPTIMISER MAIS CA DEVRAIT FONCTIONNER:
		// Si la cabine est arretée on ne fais que viderStock() et
		// eteindreTousBoutons();
		if (!getListeDemande().estVide()) {
			viderStock();
			eteindreTousBoutons();
			cabine.arreter();
			MAJSens();
		}
	}

	public int getPosition() {
		return position;
	}

	public ListeTrieeCirculaireDeDemandes getListeDemande() {
		return ListeDemande;
	}


}
