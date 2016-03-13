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
import outils.Sens;

public class ControleurTest {

	private static OutputStream sortie; // flux de redirection de la sortie
	private static PrintStream original; // flux de sortie initial
	private static String lineSeparator; // saut de ligne
	private static int indiceAffichage; // variable utilisee par la methode
	private static String signalPal = "signal de franchissement de palier";// dernierAffichage
	private static String arretPEtg = "arreter prochain �tage";
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
	
	@Before
	public void setUp()  {
		ctrl = Controleur.getInstance(11,2);
	}
	
	/**
	 * Utilisateur au RdC (1), Ascenseur au 2, va en 4 
	 */
	@Test
	public void test1() {
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
	 * Les appels de l'ascenseur dans le m�me sens que celui de la cabine en cours de d�placement 
	 */
	@Test
	public void test3() {
		//test inter�diaire pour amener la cabine l� ou les prochain test indique
		//ou elledervait �tre
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
	public void test4() {
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
	 * deux appels � partir du m�me palier (palier 2) (cabine en 4)
	 */
	@Test
	public void test5(){
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
	 * deux appels pour le m�me �tage(cabine en 5)
	 */
	@Test
	public void test6(){
		//cabine en 2
		ctrl.demander(new Demande(5, Sens.INDEFINI));
		ctrl.signalerChangementIDEtage();//Cabine en 3
		ctrl.signalerChangementIDEtage();//Cabine en 4
		ctrl.signalerChangementIDEtage();//Cabine en 5
		assertSame(5,ctrl.getPosition());
		dernierAffichage();
		
		ctrl.demander(new Demande(2, Sens.MONTEE));
		ctrl.demander(new Demande(3, Sens.MONTEE));
		ctrl.signalerChangementIDEtage();//Cabine en 4
		ctrl.signalerChangementIDEtage();//Cabine en 3
		ctrl.signalerChangementIDEtage();//Cabine en 2
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
}
