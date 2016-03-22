package test;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import commande.Controleur;
import commande.IControleur;
import commande.IListeTrieeCirculaire;
import commande.ListeTrieeCirculaireDeDemandes;
import outils.Demande;
import outils.Sens;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class ControleurTest {

	private static OutputStream sortie; // flux de redirection de la sortie
	private static PrintStream original; // flux de sortie initial
	private static String lineSeparator; // saut de ligne
	private static int indiceAffichage; // variable utilisee par la methode
	private static String signalPal = "signal de franchissement de palier";// dernierAffichage
	private static String arretPEtg = "arreter prochain étage";
	private static String monter = "monter";
	private static String desc = "descendre";

	/**
	 * setUpBeforeClass.
	 * 
	 * @throws Exception
	 *             gère les exceptions
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		original = System.out;
		sortie = new ByteArrayOutputStream();
		System.setOut(new PrintStream(sortie));
		lineSeparator = System.lineSeparator();
		indiceAffichage = 0;
	}

	/**
	 * tearDownAfterClass.
	 * 
	 * @throws Exception
	 *             gère les exceptions
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		System.setOut(original);
		original = null;
		sortie = null;
	}

	/**
	 * Renvoie le texte qui vient d'etre affiche sur la sortie . depuis le
	 * dernier appel de la methode.
	 * 
	 * @return String le dernier affichage
	 */
	protected final String dernierAffichage() {
		String resultat = sortie.toString().substring(indiceAffichage);
		indiceAffichage = sortie.toString().length();
		return resultat;
	}

	/**
	 * Controleur.
	 */
	private Controleur ctrl;

	/**
	 * Classe Doublure de cabine.
	 * 
	 * @author Marjorie
	 * 
	 */
	private class DoublureDeCabine implements operative.ICabine {
		public void monter() {
			System.out.println("monter");
		}

		public void descendre() {
			// TODO Auto-generated method stub
			System.out.println("descendre");
		}

		public void arreterProchainNiveau() {
			// TODO Auto-generated method stub
			System.out.println("arreter prochain étage");
		}

		public void arreter() {
			// TODO Auto-generated method stub
			System.out.println("arret d'urgence");
		}

		@Override
		public void assignerControleur(IControleur arg0) {
			// TODO Auto-generated method stub
			
		}

	}

	/**
	 * Classe DoublureDIUG.
	 * 
	 * @author Marjorie
	 * 
	 */
	private class DoublureDIUG implements operative.IIUG {

		@Override
		public void allumerBouton(final Demande d) {
			// TODO Auto-generated method stub
			System.out.println("allumer bouton " + d);
		}

		@Override
		public void eteindreBouton(final Demande d) {
			// TODO Auto-generated method stub
			System.out.println("eteindre bouton " + d);
		}

		@Override
		public void ajouterMessage(final String s) {
			// TODO Auto-generated method stub

		}

		@Override
		public void changerPosition(final int i) {
			// TODO Auto-generated method stub

		}

		@Override
		public void assignerControleur(IControleur arg0) {
			// TODO Auto-generated method stub
			
		}

	}

	/**
	 * setUp se fais au début des tests.
	 */
	@Before
	public final void setUp() {
		IListeTrieeCirculaire<Demande> stock = new ListeTrieeCirculaireDeDemandes(11);
		ctrl = new Controleur(11, new DoublureDIUG(),
				new DoublureDeCabine(), stock);
	}

	/**
	 * Utilisateur au RdC (1), Ascenseur au 2, va en 4.
	 */
	@Test
	public final void cas1() {
		ctrl.demander(new Demande(2, Sens.INDEFINI));
		ctrl.signalerChangementDEtage();// 0
		ctrl.signalerChangementDEtage();// 1
		ctrl.signalerChangementDEtage();// 2
		dernierAffichage();
		
		assertEquals(2, ctrl.getPosition());
		ctrl.demander(new Demande(1, Sens.MONTEE));
		ctrl.signalerChangementDEtage();
		assertEquals(1, new Demande(1, Sens.MONTEE).etage().intValue());
		assertTrue(new Demande(1, Sens.MONTEE).enMontee());
		assertEquals(1, ctrl.getPosition());
		assertTrue(ctrl.getListeDemande().estVide());
		ctrl.demander(new Demande(4, Sens.INDEFINI));
		ctrl.signalerChangementDEtage();
		ctrl.signalerChangementDEtage();
		ctrl.signalerChangementDEtage();
		assertSame(true, ctrl.getListeDemande().estVide());
		assertEquals("appel 1^" + lineSeparator + "allumer bouton 1^"
				+ lineSeparator + desc + lineSeparator + arretPEtg
				+ lineSeparator + signalPal + lineSeparator
				+ "eteindre bouton 1^" + lineSeparator + "appel 4-"
				+ lineSeparator + "allumer bouton 4-" + lineSeparator + monter
				+ lineSeparator + signalPal + lineSeparator + signalPal
				+ lineSeparator + arretPEtg + lineSeparator + signalPal
				+ lineSeparator + "eteindre bouton 4^" + lineSeparator,
				dernierAffichage());
	}

	/**
	 * Les appels de l'ascenseur dans le même sens .
	 * que celui de la cabine en cours de déplacement.
	 */
	@Test
	public final void cas3() {
		//test interédiaire pour amener la cabine 
		//là ou les prochain test indique
		//ou elle devrait être
		ctrl.demander(new Demande(10, Sens.INDEFINI));
		ctrl.signalerChangementDEtage();// 0
		ctrl.signalerChangementDEtage();// 1
		ctrl.signalerChangementDEtage();// 2
		ctrl.signalerChangementDEtage();// 3
		ctrl.signalerChangementDEtage();// 4
		ctrl.signalerChangementDEtage();// 5
		ctrl.signalerChangementDEtage();// 6
		ctrl.signalerChangementDEtage();// 7
		ctrl.signalerChangementDEtage();// 8
		ctrl.signalerChangementDEtage();// 9
		ctrl.signalerChangementDEtage();// 10
		assertEquals(10, ctrl.getPosition());
		dernierAffichage();

		// La cabine est en 10
		ctrl.demander(new Demande(10, Sens.DESCENTE));
		ctrl.demander(new Demande(2, Sens.INDEFINI));
		ctrl.signalerChangementDEtage();// cabine 9
		ctrl.signalerChangementDEtage();// cabine 8
		ctrl.signalerChangementDEtage();// cabine 7
		ctrl.signalerChangementDEtage();// cabine 6
		ctrl.demander(new Demande(4, Sens.DESCENTE));
		ctrl.signalerChangementDEtage();// cabine 5
		ctrl.signalerChangementDEtage();// cabine 4
		ctrl.demander(new Demande(2, Sens.INDEFINI));
		ctrl.signalerChangementDEtage();// cabine 3
		ctrl.signalerChangementDEtage();// cabine 2
		assertEquals("appel 10v" + lineSeparator + "appel 2-" + lineSeparator
				+ "allumer bouton 2-" + lineSeparator + desc + lineSeparator
				+ signalPal + lineSeparator + signalPal + lineSeparator
				+ signalPal + lineSeparator + signalPal + lineSeparator
				+ "appel 4v" + lineSeparator + "allumer bouton 4v"
				+ lineSeparator + signalPal + lineSeparator + arretPEtg
				+ lineSeparator + signalPal + lineSeparator
				+ "eteindre bouton 4v" + lineSeparator + "appel 2-"
				+ lineSeparator + signalPal + lineSeparator + arretPEtg
				+ lineSeparator + signalPal + lineSeparator
				+ "eteindre bouton 2v" + lineSeparator, dernierAffichage());
	}

	/**
	 * Cabine en 6 monte en 10 et redescend en 7.
	 */
	@Test
	public final void cas4() {
		ctrl.demander(new Demande(6, Sens.INDEFINI));
		ctrl.signalerChangementDEtage();// 0
		ctrl.signalerChangementDEtage();// 1
		ctrl.signalerChangementDEtage();// 2
		ctrl.signalerChangementDEtage();// Cabine en 3
		ctrl.signalerChangementDEtage();// Cabine en 4
		ctrl.signalerChangementDEtage();// Cabine en 5
		ctrl.signalerChangementDEtage();// Cabine en 6
		// On s'assure que la cabine est bien en 6
		assertEquals(6, ctrl.getPosition());
		dernierAffichage();

		ctrl.demander(new Demande(10, Sens.DESCENTE));
		ctrl.signalerChangementDEtage();// Cabine en 7
		ctrl.signalerChangementDEtage();// Cabine en 8
		ctrl.signalerChangementDEtage();// Cabine en 9
		ctrl.signalerChangementDEtage();// Cabine en 10
		ctrl.demander(new Demande(9, Sens.INDEFINI));
		ctrl.demander(new Demande(8, Sens.INDEFINI));
		ctrl.demander(new Demande(7, Sens.INDEFINI));
		ctrl.signalerChangementDEtage();// Cabine en 9
		ctrl.signalerChangementDEtage();// Cabine en 8
		ctrl.signalerChangementDEtage();// Cabine en 7
		assertEquals("appel 10v" + lineSeparator + "allumer bouton 10v"
				+ lineSeparator + monter + lineSeparator + signalPal
				+ lineSeparator + signalPal + lineSeparator + signalPal
				+ lineSeparator + arretPEtg + lineSeparator + signalPal
				+ lineSeparator + "eteindre bouton 10v" + lineSeparator
				+ "appel 9-" + lineSeparator + "allumer bouton 9-"
				+ lineSeparator + desc + lineSeparator + "appel 8-"
				+ lineSeparator + "allumer bouton 8-" + lineSeparator
				+ "appel 7-" + lineSeparator + "allumer bouton 7-"
				+ lineSeparator + arretPEtg + lineSeparator + signalPal
				+ lineSeparator + "eteindre bouton 9v" + lineSeparator
				+ arretPEtg + lineSeparator + signalPal + lineSeparator
				+ "eteindre bouton 8v" + lineSeparator + arretPEtg
				+ lineSeparator + signalPal + lineSeparator
				+ "eteindre bouton 7v" + lineSeparator, dernierAffichage());
	}

	/**
	 * deux appels à partir du même palier (palier 2) (cabine en 4).
	 */
	@Test
	public final void cas5() {
		ctrl.demander(new Demande(4, Sens.INDEFINI));
		ctrl.signalerChangementDEtage();// Cabine en 0
		ctrl.signalerChangementDEtage();// Cabine en 1
		ctrl.signalerChangementDEtage();// Cabine en 2
		ctrl.signalerChangementDEtage();// 3
		ctrl.signalerChangementDEtage();// 4
		assertEquals(4, ctrl.getPosition());
		dernierAffichage();

		ctrl.demander(new Demande(2, Sens.MONTEE));
		ctrl.demander(new Demande(2, Sens.MONTEE));
		ctrl.signalerChangementDEtage();// Cabine en 3
		ctrl.signalerChangementDEtage();// Cabine en 2
		assertEquals("appel 2^" + lineSeparator + "allumer bouton 2^"
				+ lineSeparator + desc + lineSeparator + "appel 2^"
				+ lineSeparator + signalPal + lineSeparator + arretPEtg
				+ lineSeparator + signalPal + lineSeparator
				+ "eteindre bouton 2^" + lineSeparator, dernierAffichage());
	}

	/**
	 * deux appels pour le même étage(cabine en 5).
	 */
	@Test
	public final void cas6() {
		ctrl.demander(new Demande(5, Sens.INDEFINI));
		ctrl.signalerChangementDEtage();// Cabine en 0
		ctrl.signalerChangementDEtage();// Cabine en 1
		ctrl.signalerChangementDEtage();// Cabine en 2
		ctrl.signalerChangementDEtage();// Cabine en 3
		ctrl.signalerChangementDEtage();// Cabine en 4
		ctrl.signalerChangementDEtage();// Cabine en 5
		assertEquals(5, ctrl.getPosition());
		dernierAffichage();

		ctrl.demander(new Demande(2, Sens.MONTEE));
		ctrl.demander(new Demande(3, Sens.MONTEE));
		ctrl.signalerChangementDEtage();
		ctrl.signalerChangementDEtage();
		ctrl.signalerChangementDEtage();
		ctrl.demander(new Demande(4, Sens.INDEFINI));
		ctrl.signalerChangementDEtage();// Cabine en 3
		ctrl.demander(new Demande(4, Sens.INDEFINI));
		ctrl.signalerChangementDEtage();// Cabine en 4
		assertEquals("appel 2^" + lineSeparator + "allumer bouton 2^"
				+ lineSeparator + desc + lineSeparator + "appel 3^"
				+ lineSeparator + "allumer bouton 3^" + lineSeparator
				+ signalPal + lineSeparator + signalPal + lineSeparator
				+ arretPEtg + lineSeparator + signalPal + lineSeparator
				+ "eteindre bouton 2^" + lineSeparator + "appel 4-"
				+ lineSeparator + "allumer bouton 4-" + lineSeparator + monter
				+ lineSeparator + arretPEtg + lineSeparator + signalPal
				+ lineSeparator + "eteindre bouton 3^" + lineSeparator
				+ "appel 4-" + lineSeparator + arretPEtg + lineSeparator
				+ signalPal + lineSeparator + "eteindre bouton 4^"
				+ lineSeparator, dernierAffichage());

	}

	/**
	 * les arrêts d'urgence.
	 */
	@Test
	public final void cas7() {
		ctrl.demander(new Demande(3, Sens.INDEFINI));
		ctrl.signalerChangementDEtage();// Cabine en 0
		ctrl.signalerChangementDEtage();// Cabine en 1
		ctrl.signalerChangementDEtage();// Cabine en 2
		ctrl.signalerChangementDEtage();// Cabine en 3
		assertEquals(3, ctrl.getPosition());
		dernierAffichage();

		ctrl.demander(new Demande(2, Sens.MONTEE));
		ctrl.signalerChangementDEtage();
		ctrl.demander(new Demande(4, Sens.INDEFINI));
		ctrl.signalerChangementDEtage();
		ctrl.demander(new Demande(5, Sens.MONTEE));
		ctrl.arretUrgence();
		assertTrue(ctrl.getListeDemande().estVide());
		assertEquals("appel 2^" + lineSeparator + "allumer bouton 2^"
				+ lineSeparator + desc + lineSeparator + arretPEtg
				+ lineSeparator + signalPal + lineSeparator
				+ "eteindre bouton 2^" + lineSeparator + "appel 4-"
				+ lineSeparator + "allumer bouton 4-" + lineSeparator + monter
				+ lineSeparator + signalPal + lineSeparator + "appel 5^"
				+ lineSeparator + "allumer bouton 5^" + lineSeparator
				+ "arret d'urgence" + lineSeparator + "eteindre bouton 5^"
				+ lineSeparator + "eteindre bouton 4^" + lineSeparator,
				dernierAffichage());
	}

	/**
	 * la reprise après arrêt d'urgence.
	 */
	@Test
	public final void cas8() {
		ctrl.demander(new Demande(3, Sens.INDEFINI));
		ctrl.signalerChangementDEtage();// Cabine en 0
		ctrl.signalerChangementDEtage();// Cabine en 1
		ctrl.signalerChangementDEtage();// Cabine en 2
		ctrl.signalerChangementDEtage();// Cabine en 3
		assertEquals(3, ctrl.getPosition());
		dernierAffichage();
		
		ctrl.demander(new Demande(2, Sens.MONTEE));
		ctrl.signalerChangementDEtage();
		ctrl.demander(new Demande(4, Sens.INDEFINI));
		ctrl.signalerChangementDEtage();
		ctrl.arretUrgence();
		ctrl.demander(new Demande(4, Sens.INDEFINI));
		ctrl.signalerChangementDEtage();
		assertEquals("appel 2^" + lineSeparator + "allumer bouton 2^"
				+ lineSeparator + desc + lineSeparator + arretPEtg
				+ lineSeparator + signalPal + lineSeparator
				+ "eteindre bouton 2^" + lineSeparator + "appel 4-"
				+ lineSeparator + "allumer bouton 4-" + lineSeparator + monter
				+ lineSeparator + signalPal + lineSeparator + "arret d'urgence"
				+ lineSeparator + "eteindre bouton 4^" + lineSeparator
				+ "appel 4-" + lineSeparator + "allumer bouton 4-"
				+ lineSeparator + monter + lineSeparator + arretPEtg
				+ lineSeparator + signalPal + lineSeparator
				+ "eteindre bouton 4^" + lineSeparator, dernierAffichage());
	}

	/**
	 * les appels de l'ascenseur après un arrêt prolongé.
	 */
	@Test
	public final void cas9() {
		// Cabine en 0
		ctrl.demander(new Demande(0, Sens.INDEFINI));
		ctrl.signalerChangementDEtage();// Cabine en 3
		ctrl.signalerChangementDEtage();// Cabine en 2
		ctrl.signalerChangementDEtage();// Cabine en 1
		ctrl.signalerChangementDEtage();// Cabine en 0
		assertEquals(0, ctrl.getPosition());
		dernierAffichage();

		ctrl.demander(new Demande(2, Sens.MONTEE));
		ctrl.signalerChangementDEtage(); // Cabine en 1
		ctrl.signalerChangementDEtage(); // Cabine en 2
		ctrl.demander(new Demande(3, Sens.INDEFINI));
		ctrl.signalerChangementDEtage(); // Cabine en 3
		assertEquals("appel 2^" + lineSeparator + "allumer bouton 2^"
				+ lineSeparator + monter + lineSeparator + signalPal
				+ lineSeparator + arretPEtg + lineSeparator + signalPal
				+ lineSeparator + "eteindre bouton 2^" + lineSeparator
				+ "appel 3-" + lineSeparator + "allumer bouton 3-"
				+ lineSeparator + monter + lineSeparator + arretPEtg
				+ lineSeparator + signalPal + lineSeparator
				+ "eteindre bouton 3^" + lineSeparator, dernierAffichage());

		ctrl.demander(new Demande(2, Sens.INDEFINI));
		ctrl.signalerChangementDEtage();// Cabine en 2
		assertEquals(2, ctrl.getPosition());
		dernierAffichage();

		ctrl.demander(new Demande(1, Sens.DESCENTE));
		ctrl.signalerChangementDEtage();// Cabine en 1
		ctrl.demander(new Demande(0, Sens.INDEFINI));
		ctrl.signalerChangementDEtage();// Cabine en 0
		assertEquals("appel 1v" + lineSeparator + "allumer bouton 1v"
				+ lineSeparator + desc + lineSeparator + arretPEtg
				+ lineSeparator + signalPal + lineSeparator
				+ "eteindre bouton 1v" + lineSeparator + "appel 0-"
				+ lineSeparator + "allumer bouton 0-" + lineSeparator + desc
				+ lineSeparator + arretPEtg + lineSeparator + signalPal
				+ lineSeparator + "eteindre bouton 0^" + lineSeparator,
				dernierAffichage());

		// Cabine en 1
		ctrl.demander(new Demande(1, Sens.INDEFINI));
		ctrl.signalerChangementDEtage();// Cabine en 1
		assertEquals(1, ctrl.getPosition());
		dernierAffichage();

		ctrl.demander(new Demande(3, Sens.DESCENTE));
		ctrl.signalerChangementDEtage();// Cabine en 2
		ctrl.signalerChangementDEtage();// Cabine en 3
		ctrl.demander(new Demande(2, Sens.INDEFINI));
		ctrl.signalerChangementDEtage();// Cabine en 2
		assertEquals("appel 3v" + lineSeparator + "allumer bouton 3v"
				+ lineSeparator + monter + lineSeparator + signalPal
				+ lineSeparator + arretPEtg + lineSeparator + signalPal
				+ lineSeparator + "eteindre bouton 3v" + lineSeparator
				+ "appel 2-" + lineSeparator + "allumer bouton 2-"
				+ lineSeparator + desc + lineSeparator + arretPEtg
				+ lineSeparator + signalPal + lineSeparator
				+ "eteindre bouton 2v" + lineSeparator, dernierAffichage());

		// Cabine en 3
		ctrl.demander(new Demande(3, Sens.INDEFINI));
		ctrl.signalerChangementDEtage();// Cabine en 3
		assertEquals(3, ctrl.getPosition());
		dernierAffichage();

		ctrl.demander(new Demande(2, Sens.MONTEE));
		ctrl.signalerChangementDEtage();// Cabine en 2
		ctrl.demander(new Demande(3, Sens.INDEFINI));
		ctrl.signalerChangementDEtage();// Cabine en 3
		assertEquals("appel 2^" + lineSeparator + "allumer bouton 2^"
				+ lineSeparator + desc + lineSeparator + arretPEtg
				+ lineSeparator + signalPal + lineSeparator
				+ "eteindre bouton 2^" + lineSeparator + "appel 3-"
				+ lineSeparator + "allumer bouton 3-" + lineSeparator + monter
				+ lineSeparator + arretPEtg + lineSeparator + signalPal
				+ lineSeparator + "eteindre bouton 3^" + lineSeparator,
				dernierAffichage());

		// Cabine en 2
		ctrl.demander(new Demande(2, Sens.INDEFINI));
		ctrl.signalerChangementDEtage();// Cabine en 2
		assertEquals(2, ctrl.getPosition());
		dernierAffichage();

		ctrl.demander(new Demande(4, Sens.INDEFINI));
		ctrl.signalerChangementDEtage();// Cabine en 3
		ctrl.signalerChangementDEtage();// Cabine en 4
		assertEquals("appel 4-" + lineSeparator + "allumer bouton 4-"
				+ lineSeparator + monter + lineSeparator + signalPal
				+ lineSeparator + arretPEtg + lineSeparator + signalPal
				+ lineSeparator + "eteindre bouton 4^" + lineSeparator,
				dernierAffichage());

		// les appels de l'ascenseur qui sont satisfaits 
		// sans changement de sens, ou avec un changement de
		// sens, ou avec deux changements de sens de la cabine
		// Cabine en 0
		ctrl.demander(new Demande(0, Sens.INDEFINI));
		ctrl.signalerChangementDEtage();// Cabine en 3
		ctrl.signalerChangementDEtage();// Cabine en 2
		ctrl.signalerChangementDEtage();// Cabine en 1
		ctrl.signalerChangementDEtage();// Cabine en 0
		assertEquals(0, ctrl.getPosition());
		dernierAffichage();

		ctrl.demander(new Demande(4, Sens.DESCENTE));
		ctrl.signalerChangementDEtage();// Cabine en 1
		ctrl.demander(new Demande(5, Sens.DESCENTE));
		ctrl.signalerChangementDEtage();// Cabine en 2
		ctrl.signalerChangementDEtage();// Cabine en 3
		ctrl.signalerChangementDEtage();// Cabine en 4
		ctrl.signalerChangementDEtage();// Cabine en 5
		ctrl.demander(new Demande(3, Sens.INDEFINI));
		ctrl.signalerChangementDEtage();// Cabine en 4
		ctrl.signalerChangementDEtage();// Cabine en 3
		assertEquals("appel 4v" + lineSeparator + "allumer bouton 4v"
				+ lineSeparator + monter + lineSeparator + signalPal
				+ lineSeparator + "appel 5v" + lineSeparator
				+ "allumer bouton 5v" + lineSeparator + signalPal
				+ lineSeparator + signalPal + lineSeparator + signalPal
				+ lineSeparator + arretPEtg + lineSeparator + signalPal
				+ lineSeparator + "eteindre bouton 5v" + lineSeparator
				+ "appel 3-" + lineSeparator + "allumer bouton 3-"
				+ lineSeparator + desc + lineSeparator + arretPEtg
				+ lineSeparator + signalPal + lineSeparator
				+ "eteindre bouton 4v" + lineSeparator + arretPEtg
				+ lineSeparator + signalPal + lineSeparator
				+ "eteindre bouton 3v" + lineSeparator, dernierAffichage());

		// un appel pour un étage en cours de service

		// Cabine en 3
		ctrl.demander(new Demande(3, Sens.INDEFINI));
		ctrl.signalerChangementDEtage();// Cabine en 4
		ctrl.signalerChangementDEtage();// Cabine en 3
		assertEquals(3, ctrl.getPosition());
		dernierAffichage();

		ctrl.demander(new Demande(3, Sens.MONTEE));
		ctrl.demander(new Demande(3, Sens.DESCENTE));
		ctrl.demander(new Demande(3, Sens.INDEFINI));
		assertEquals("appel 3^" + lineSeparator + "appel 3v" + lineSeparator
				+ "appel 3-" + lineSeparator, dernierAffichage());
	}
}
