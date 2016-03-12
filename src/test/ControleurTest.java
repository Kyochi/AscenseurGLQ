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
	
	private Demande d1,d2;
	private Controleur ctrl;
	
	@Before
	public void setUp()  {
		d1 = new Demande(1, Sens.MONTEE);
		d2 = new Demande(4, Sens.INDEFINI);
		ctrl = Controleur.getInstance(10,2);
	}
	@Test
	public void test1() {
		assertSame(2,ctrl.getPosition());
		ctrl.demander(d1);
		assertSame(1,d1.etage());
		assertSame(true,d1.enMontee());
		assertSame(1,ctrl.getPosition());
		assertSame(true,ctrl.getListeDemande().estVide());
		ctrl.demander(d2);
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
}
