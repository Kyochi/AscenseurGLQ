package test;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import outils.Controleur;
import outils.Demande;
import outils.ICabine;
import outils.IIUG;
import outils.Sens;

public class ControleurTest {

	private static OutputStream sortie; // flux de redirection de la sortie
	private static PrintStream original; // flux de sortie initial
	private static String lineSeparator; // saut de ligne
	private static int indiceAffichage; // variable utilisee par la methode
	private static String signalPal = "signal de franchissement de palier";// dernierAffichage
	private static String arretPEtg = "arreter prochain étage";
	private static String monter = "monter";
	private static String desc = "descendre";
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		original = System.out;
		sortie = new ByteArrayOutputStream();
		System.setOut(new PrintStream(sortie));
		lineSeparator = System.lineSeparator();
		indiceAffichage = 0;
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		System.setOut(original);
		original = null;
		sortie = null;
	}

	/*
	 * Renvoie le texte qui vient d'etre affiche sur la sortie depuis le dernier
	 * appel de la methode
	 */
	protected String dernierAffichage() {
		String resultat = sortie.toString().substring(indiceAffichage);
		indiceAffichage = sortie.toString().length();
		return resultat;
	}
	
	private Controleur ctrl;
	
	private class DoublureDeCabine implements ICabine { 
		@Override public void monter() { 
			System.out.println("monter");
		}

		@Override
		public void descendre() {
			// TODO Auto-generated method stub
			System.out.println("descendre");
		}

		@Override
		public void arreterProchainNiveau() {
			// TODO Auto-generated method stub
			System.out.println("arreter prochain étage");
		}

		@Override
		public void arreter() {
			// TODO Auto-generated method stub
			System.out.println("arret d'urgence");
		}  
	} 
	private class DoublureDIUG implements IIUG {

		@Override
		public void allumerBouton(Demande d) {
			// TODO Auto-generated method stub
			System.out.println("allumer bouton " + d);
		}

		@Override
		public void eteindreBouton(Demande d) {
			// TODO Auto-generated method stub
			System.out.println("eteindre bouton " + d);
		}

		@Override
		public void ajouterMessage(String s) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void changerPosition(int i) {
			// TODO Auto-generated method stub
			
		} 
		
	}
	@Before
	public void setUp()  {
		ctrl = Controleur.getInstance(11,2,new DoublureDIUG(), new DoublureDeCabine());
	}
	
	/**
	 * Utilisateur au RdC (1), Ascenseur au 2, va en 4 
	 */
	@Test
	public void cas1() {
		assertSame(2,ctrl.getPosition());
		ctrl.demander(new Demande(1, Sens.MONTEE));
		ctrl.signalerChangementIDEtage();
		assertSame(1,new Demande(1, Sens.MONTEE).etage());
		assertSame(true,new Demande(1, Sens.MONTEE).enMontee());
		assertSame(1,ctrl.getPosition());
		assertSame(true,ctrl.getListeDemande().estVide());
		ctrl.demander(new Demande(4, Sens.INDEFINI));
		ctrl.signalerChangementIDEtage();
		ctrl.signalerChangementIDEtage();
		ctrl.signalerChangementIDEtage();
		assertSame(true,ctrl.getListeDemande().estVide());
		assertEquals(
				"appel 1^" + lineSeparator + 
				"allumer bouton 1^" + lineSeparator + 
				desc + lineSeparator + 
				arretPEtg + lineSeparator + 
				signalPal + lineSeparator + 
				"eteindre bouton 1^" + lineSeparator + 
				"appel 4-" + lineSeparator + 
				"allumer bouton 4-" + lineSeparator + 
				monter + lineSeparator + 
				signalPal + lineSeparator + 
				signalPal + lineSeparator + 
				arretPEtg + lineSeparator + 
				signalPal + lineSeparator + 
				"eteindre bouton 4^" + lineSeparator
				,dernierAffichage()
		);
	}

	/**
	 * Les appels de l'ascenseur dans le même sens que celui de la cabine en cours de déplacement 
	 */
	@Test
	public void cas3() {
		//test interédiaire pour amener la cabine là ou les prochain test indique
		//ou elledervait être
		ctrl.demander(new Demande(10, Sens.INDEFINI));
		ctrl.signalerChangementIDEtage();//5
		ctrl.signalerChangementIDEtage();//6
		ctrl.signalerChangementIDEtage();//7
		ctrl.signalerChangementIDEtage();//8
		ctrl.signalerChangementIDEtage();//9
		ctrl.signalerChangementIDEtage();//10
		assertSame(10,ctrl.getPosition());
		dernierAffichage();
		
		//La cabine est en 10
		ctrl.demander(new Demande(10,Sens.DESCENTE));
		ctrl.demander(new Demande(2,Sens.INDEFINI));
		ctrl.signalerChangementIDEtage();//cabine 9
		ctrl.signalerChangementIDEtage();//cabine 8
		ctrl.signalerChangementIDEtage();//cabine 7
		ctrl.signalerChangementIDEtage();//cabine 6
		ctrl.demander(new Demande(4,Sens.DESCENTE));
		ctrl.signalerChangementIDEtage();//cabine 5
		ctrl.signalerChangementIDEtage();//cabine 4
		ctrl.demander(new Demande(2,Sens.INDEFINI));
		ctrl.signalerChangementIDEtage();//cabine 3
		ctrl.signalerChangementIDEtage();//cabine 2
		assertEquals(
				"appel 10v"+ lineSeparator + 
				"appel 2-"+ lineSeparator + 
				"allumer bouton 2-"+ lineSeparator + 
				desc+ lineSeparator + 
				signalPal+ lineSeparator + 
				signalPal+ lineSeparator + 
				signalPal+ lineSeparator + 
				signalPal+ lineSeparator + 
				"appel 4v"+ lineSeparator + 
				"allumer bouton 4v"+ lineSeparator + 
				signalPal+ lineSeparator + 
				arretPEtg+ lineSeparator + 
				signalPal+ lineSeparator + 
				"eteindre bouton 4v"+ lineSeparator + 
				"appel 2-"+ lineSeparator + 
				signalPal+ lineSeparator + 
				arretPEtg+ lineSeparator + 
				signalPal+ lineSeparator + 
				"eteindre bouton 2v"+ lineSeparator 
				,dernierAffichage()
			);		
	}

	/**
	 * Cabine en 6 monte en 10 et redescend en 7
	 */
	@Test
	public void cas4() {
		//Cabine est en 2
		ctrl.demander(new Demande(6, Sens.INDEFINI));
		ctrl.signalerChangementIDEtage();//Cabine en 3
		ctrl.signalerChangementIDEtage();//Cabine en 4
		ctrl.signalerChangementIDEtage();//Cabine en 5
		ctrl.signalerChangementIDEtage();//Cabine en 6
		//On s'assure que la cabine est bien en 6
		assertSame(6,ctrl.getPosition());
		dernierAffichage();
		
		ctrl.demander(new Demande(10, Sens.DESCENTE));
		ctrl.signalerChangementIDEtage();//Cabine en 7
		ctrl.signalerChangementIDEtage();//Cabine en 8
		ctrl.signalerChangementIDEtage();//Cabine en 9
		ctrl.signalerChangementIDEtage();//Cabine en 10
		ctrl.demander(new Demande(9, Sens.INDEFINI));
		ctrl.demander(new Demande(8, Sens.INDEFINI));
		ctrl.demander(new Demande(7, Sens.INDEFINI));
		ctrl.signalerChangementIDEtage();//Cabine en 9
		ctrl.signalerChangementIDEtage();//Cabine en 8
		ctrl.signalerChangementIDEtage();//Cabine en 7
		assertEquals(
			"appel 10v" + lineSeparator+
			"allumer bouton 10v" + lineSeparator+
			monter + lineSeparator+
			signalPal + lineSeparator+
			signalPal + lineSeparator+
			signalPal + lineSeparator+
			arretPEtg + lineSeparator+
			signalPal + lineSeparator+
			"eteindre bouton 10v" + lineSeparator+
			"appel 9-" + lineSeparator+
			"allumer bouton 9-" + lineSeparator+
			desc + lineSeparator+
			"appel 8-" + lineSeparator+
			"allumer bouton 8-" + lineSeparator+
			"appel 7-" + lineSeparator+
			"allumer bouton 7-" + lineSeparator+
			arretPEtg + lineSeparator+
			signalPal + lineSeparator+
			"eteindre bouton 9v" + lineSeparator+
			arretPEtg + lineSeparator+
			signalPal + lineSeparator+
			"eteindre bouton 8v" + lineSeparator+
			arretPEtg + lineSeparator+
			signalPal + lineSeparator+
			"eteindre bouton 7v" + lineSeparator
			,dernierAffichage()
		);
	}

	/**
	 * deux appels à partir du même palier (palier 2) (cabine en 4)
	 */
	@Test
	public void cas5(){
		//cabine en 7
		ctrl.demander(new Demande(4, Sens.INDEFINI));
		ctrl.signalerChangementIDEtage();//Cabine en 6
		ctrl.signalerChangementIDEtage();//Cabine en 5
		ctrl.signalerChangementIDEtage();//Cabine en 4
		assertSame(4,ctrl.getPosition());
		dernierAffichage();
		
		ctrl.demander(new Demande(2, Sens.MONTEE));
		ctrl.demander(new Demande(2, Sens.MONTEE));
		ctrl.signalerChangementIDEtage();//Cabine en 3
		ctrl.signalerChangementIDEtage();//Cabine en 2
		assertEquals(
				"appel 2^"+ lineSeparator + 
				"allumer bouton 2^"+ lineSeparator + 
				desc+ lineSeparator + 
				"appel 2^"+ lineSeparator + 
				signalPal+ lineSeparator + 
				arretPEtg+ lineSeparator + 
				signalPal+ lineSeparator + 
				"eteindre bouton 2^"+ lineSeparator 
				,dernierAffichage());
	}

	/**
	 * deux appels pour le même étage(cabine en 5)
	 */
	@Test
	public void cas6(){
		//cabine en 2
		ctrl.demander(new Demande(5, Sens.INDEFINI));
		ctrl.signalerChangementIDEtage();//Cabine en 3
		ctrl.signalerChangementIDEtage();//Cabine en 4
		ctrl.signalerChangementIDEtage();//Cabine en 5
		assertSame(5,ctrl.getPosition());
		dernierAffichage();
		
		ctrl.demander(new Demande(2, Sens.MONTEE));
		ctrl.demander(new Demande(3, Sens.MONTEE));
		ctrl.signalerChangementIDEtage();
		ctrl.signalerChangementIDEtage();
		ctrl.signalerChangementIDEtage();
		ctrl.demander(new Demande(4, Sens.INDEFINI));
		ctrl.signalerChangementIDEtage();//Cabine en 3
		ctrl.demander(new Demande(4, Sens.INDEFINI));
		ctrl.signalerChangementIDEtage();//Cabine en 4
		assertEquals(
				"appel 2^" + lineSeparator + 
				"allumer bouton 2^" + lineSeparator + 
				desc + lineSeparator + 
				"appel 3^" + lineSeparator + 
				"allumer bouton 3^" + lineSeparator + 
				signalPal + lineSeparator + 
				signalPal + lineSeparator + 
				arretPEtg + lineSeparator + 
				signalPal + lineSeparator + 
				"eteindre bouton 2^" + lineSeparator + 
				"appel 4-" + lineSeparator + 
				"allumer bouton 4-" + lineSeparator + 
				monter + lineSeparator + 
				arretPEtg + lineSeparator + 
				signalPal + lineSeparator + 
				"eteindre bouton 3^" + lineSeparator + 
				"appel 4-" + lineSeparator + 
				arretPEtg + lineSeparator + 
				signalPal + lineSeparator + 
				"eteindre bouton 4^" + lineSeparator 
				,dernierAffichage());
		
	}

	/**
	 * les arrêts d'urgence
	 */
	@Test
	public void cas7(){
		//Cabine actuellement en 4
		//Cabine en 3
		ctrl.demander(new Demande(3, Sens.INDEFINI));
		ctrl.signalerChangementIDEtage();//Cabine en 3
		assertSame(3,ctrl.getPosition());
		dernierAffichage();
		
		ctrl.demander(new Demande(2, Sens.MONTEE));
		ctrl.signalerChangementIDEtage();
		ctrl.demander(new Demande(4, Sens.INDEFINI));
		ctrl.signalerChangementIDEtage();
		ctrl.demander(new Demande(5, Sens.MONTEE));
		ctrl.arretDUrgence();
		assertTrue(ctrl.getListeDemande().estVide());
		assertEquals(
				"appel 2^" + lineSeparator +
				"allumer bouton 2^" + lineSeparator +
				desc + lineSeparator +
				arretPEtg + lineSeparator +
				signalPal + lineSeparator +
				"eteindre bouton 2^" + lineSeparator +
				"appel 4-" + lineSeparator +
				"allumer bouton 4-" + lineSeparator +
				monter + lineSeparator +
				signalPal + lineSeparator +
				"appel 5^" + lineSeparator +
				"allumer bouton 5^" + lineSeparator +
				"arret d'urgence" + lineSeparator +
				"eteindre bouton 5^" + lineSeparator +
				"eteindre bouton 4^" + lineSeparator 
				,dernierAffichage());
	}
	
	/**
	 * la reprise après arrêt d'urgence
	 */
	@Test
	public void cas8(){
		//Cabine actuellement en 3
		//Cabine en 3
		assertSame(3,ctrl.getPosition());
		ctrl.demander(new Demande(2, Sens.MONTEE));
		ctrl.signalerChangementIDEtage();
		ctrl.demander(new Demande(4, Sens.INDEFINI));
		ctrl.signalerChangementIDEtage();
		ctrl.arretDUrgence();
		ctrl.demander(new Demande(4, Sens.INDEFINI));
		ctrl.signalerChangementIDEtage();
		assertEquals(
				"appel 2^" + lineSeparator +
				"allumer bouton 2^" + lineSeparator +
				desc + lineSeparator +
				arretPEtg + lineSeparator +
				signalPal + lineSeparator +
				"eteindre bouton 2^" + lineSeparator +
				"appel 4-" + lineSeparator +
				"allumer bouton 4-" + lineSeparator +
				monter + lineSeparator +
				signalPal + lineSeparator +
				"arret d'urgence" + lineSeparator +
				"eteindre bouton 4^" + lineSeparator +
				"appel 4-" + lineSeparator +
				"allumer bouton 4-" + lineSeparator +
				monter + lineSeparator +
				arretPEtg + lineSeparator +
				signalPal + lineSeparator +
				"eteindre bouton 4^" + lineSeparator 
				,dernierAffichage());
	}
	
	/**
	 * les appels de l'ascenseur après un arrêt prolongé
	 */
	@Test
	public void cas9(){
		//Cabine actuellement en 4
		//Cabine en 0
		ctrl.demander(new Demande(0, Sens.INDEFINI));
		ctrl.signalerChangementIDEtage();//Cabine en 3
		ctrl.signalerChangementIDEtage();//Cabine en 2
		ctrl.signalerChangementIDEtage();//Cabine en 1
		ctrl.signalerChangementIDEtage();//Cabine en 0
		assertSame(0,ctrl.getPosition());
		dernierAffichage();
		
		ctrl.demander(new Demande(2, Sens.MONTEE));
		ctrl.signalerChangementIDEtage(); //Cabine en 1
		ctrl.signalerChangementIDEtage(); //Cabine en 2
		ctrl.demander(new Demande(3, Sens.INDEFINI));
		ctrl.signalerChangementIDEtage(); //Cabine en 3
		
		assertEquals(
				"appel 2^" + lineSeparator +
				"allumer bouton 2^" + lineSeparator +
				monter + lineSeparator +
				signalPal + lineSeparator +
				arretPEtg + lineSeparator +
				signalPal + lineSeparator +
				"eteindre bouton 2^" + lineSeparator +
				"appel 3-" + lineSeparator +
				"allumer bouton 3-" + lineSeparator +
				monter + lineSeparator +
				arretPEtg + lineSeparator +
				signalPal + lineSeparator +
				"eteindre bouton 3^" + lineSeparator 
				,dernierAffichage());
		
		//Cabine actuellement en 3
		//Cabine en 2
		ctrl.demander(new Demande(2, Sens.INDEFINI));
		ctrl.signalerChangementIDEtage();//Cabine en 2
		assertSame(2,ctrl.getPosition());
		dernierAffichage();
		
		ctrl.demander(new Demande(1, Sens.DESCENTE));
		ctrl.signalerChangementIDEtage();//Cabine en 1
		ctrl.demander(new Demande(0, Sens.INDEFINI));
		ctrl.signalerChangementIDEtage();//Cabine en 0
		
		assertEquals(
				"appel 1v" + lineSeparator +
				"allumer bouton 1v" + lineSeparator +
				desc + lineSeparator +
				arretPEtg + lineSeparator +
				signalPal + lineSeparator +
				"eteindre bouton 1v" + lineSeparator +
				"appel 0-" + lineSeparator +
				"allumer bouton 0-" + lineSeparator +
				desc + lineSeparator +
				arretPEtg + lineSeparator +
				signalPal + lineSeparator +
				"eteindre bouton 0^" + lineSeparator 
				,dernierAffichage());
		
		//Cabine actuellement en 0
		//Cabine en 1
		ctrl.demander(new Demande(1, Sens.INDEFINI));
		ctrl.signalerChangementIDEtage();//Cabine en 1
		assertSame(1,ctrl.getPosition());
		dernierAffichage();
		
		ctrl.demander(new Demande(3, Sens.DESCENTE));
		ctrl.signalerChangementIDEtage();//Cabine en 2
		ctrl.signalerChangementIDEtage();//Cabine en 3
		ctrl.demander(new Demande(2, Sens.INDEFINI));
		ctrl.signalerChangementIDEtage();//Cabine en 2
		
		assertEquals(
				"appel 3v" + lineSeparator +
				"allumer bouton 3v" + lineSeparator +
				monter + lineSeparator +
				signalPal + lineSeparator +
				arretPEtg + lineSeparator +
				signalPal + lineSeparator +
				"eteindre bouton 3v" + lineSeparator +
				"appel 2-" + lineSeparator +
				"allumer bouton 2-" + lineSeparator +
				desc + lineSeparator +
				arretPEtg + lineSeparator +
				signalPal + lineSeparator +
				"eteindre bouton 2v" + lineSeparator 
				,dernierAffichage());
		
		//Cabine actuellement en 2
		//Cabine en 3
		ctrl.demander(new Demande(3, Sens.INDEFINI));
		ctrl.signalerChangementIDEtage();//Cabine en 3
		assertSame(3,ctrl.getPosition());
		dernierAffichage();
		
		ctrl.demander(new Demande(2, Sens.MONTEE));
		ctrl.signalerChangementIDEtage();//Cabine en 2
		ctrl.demander(new Demande(3, Sens.INDEFINI));
		ctrl.signalerChangementIDEtage();//Cabine en 3
		
		assertEquals(
				"appel 2^" + lineSeparator +
				"allumer bouton 2^" + lineSeparator +
				desc + lineSeparator +
				arretPEtg + lineSeparator +
				signalPal + lineSeparator +
				"eteindre bouton 2^" + lineSeparator +
				"appel 3-" + lineSeparator +
				"allumer bouton 3-" + lineSeparator +
				monter + lineSeparator +
				arretPEtg + lineSeparator +
				signalPal + lineSeparator +
				"eteindre bouton 3^" + lineSeparator 
				,dernierAffichage());
		
		//Cabine actuellement en 3
		//Cabine en 2
		ctrl.demander(new Demande(2, Sens.INDEFINI));
		ctrl.signalerChangementIDEtage();//Cabine en 2
		assertSame(2,ctrl.getPosition());
		dernierAffichage();
		
		ctrl.demander(new Demande(4, Sens.INDEFINI));
		ctrl.signalerChangementIDEtage();//Cabine en 3
		ctrl.signalerChangementIDEtage();//Cabine en 4
		
		assertEquals(
				"appel 4-" + lineSeparator +
				"allumer bouton 4-" + lineSeparator +
				monter + lineSeparator +
				signalPal + lineSeparator +
				arretPEtg + lineSeparator +
				signalPal + lineSeparator +
				"eteindre bouton 4^" + lineSeparator 
				,dernierAffichage());
		
		
		//les appels de l'ascenseur qui sont satisfaits sans changement de sens, ou avec un changement de
	 	//sens, ou avec deux changements de sens de la cabine
		//Cabine actuellement en 4
		//Cabine en 0
		ctrl.demander(new Demande(0, Sens.INDEFINI));
		ctrl.signalerChangementIDEtage();//Cabine en 3
		ctrl.signalerChangementIDEtage();//Cabine en 2
		ctrl.signalerChangementIDEtage();//Cabine en 1
		ctrl.signalerChangementIDEtage();//Cabine en 0
		assertSame(0,ctrl.getPosition());
		dernierAffichage();
		
		ctrl.demander(new Demande(4, Sens.DESCENTE));
		ctrl.signalerChangementIDEtage();//Cabine en 1
		ctrl.demander(new Demande(5, Sens.DESCENTE));
		ctrl.signalerChangementIDEtage();//Cabine en 2
		ctrl.signalerChangementIDEtage();//Cabine en 3
		ctrl.signalerChangementIDEtage();//Cabine en 4
		ctrl.signalerChangementIDEtage();//Cabine en 5
		ctrl.demander(new Demande(3, Sens.INDEFINI));
		ctrl.signalerChangementIDEtage();//Cabine en 4
		ctrl.signalerChangementIDEtage();//Cabine en 3
		
		assertEquals(
				"appel 4v" + lineSeparator + 
				"allumer bouton 4v" + lineSeparator + 
				monter + lineSeparator + 
				signalPal + lineSeparator + 
				"appel 5v" + lineSeparator + 
				"allumer bouton 5v" + lineSeparator + 
				signalPal + lineSeparator + 
				signalPal + lineSeparator + 
				signalPal + lineSeparator + 
				arretPEtg + lineSeparator + 
				signalPal + lineSeparator + 
				"eteindre bouton 5v" + lineSeparator + 
				"appel 3-" + lineSeparator + 
				"allumer bouton 3-" + lineSeparator + 
				desc + lineSeparator + 
				arretPEtg + lineSeparator + 
				signalPal + lineSeparator + 
				"eteindre bouton 4v" + lineSeparator + 
				arretPEtg + lineSeparator + 
				signalPal + lineSeparator + 
				"eteindre bouton 3v" + lineSeparator 
				,dernierAffichage());
		
		//un appel pour un étage en cours de service
		
		//Cabine actuellement en 5
		//Cabine en 3
		ctrl.demander(new Demande(3, Sens.INDEFINI));
		ctrl.signalerChangementIDEtage();//Cabine en 4
		ctrl.signalerChangementIDEtage();//Cabine en 3
		assertSame(3,ctrl.getPosition());
		dernierAffichage();
		
		ctrl.demander(new Demande(3, Sens.MONTEE));
		ctrl.demander(new Demande(3, Sens.DESCENTE));
		ctrl.demander(new Demande(3, Sens.INDEFINI));
		assertEquals(
				"appel 3^" + lineSeparator +
				"appel 3v" + lineSeparator +
				"appel 3-" + lineSeparator 
				,dernierAffichage());
	}
}
