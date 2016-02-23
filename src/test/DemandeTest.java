/**
 * Classes de test des classes du projet ascenseur.
 * @author Lucile Torres-Gerardin
 */
package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import outils.Demande;
import outils.ExceptionCabineArretee;
import outils.Sens;

/**
 * Classe de test unitaire de la classe {@link outils.Demande}.
 */
public class DemandeTest {
	
	private Demande d1, d2, d3, d4;

	
	@Before
	public void setUp()  {
		d1 = new Demande();
		d2 = new Demande(4, Sens.MONTEE);
		d3 = new Demande(3, Sens.DESCENTE);
		d4 = new Demande(1, Sens.INDEFINI);
	}

	@After
	public void tearDown() {
		d1 = d2 = d3 = d4 = null;
	}

	/**
	 * Methode de test de {@link outils.Demande#Demande()}.
	 */
	@Test
	public void testDemande1() {
		assertSame( 0, d1.etage());
		assertTrue( d1.estIndefini());
	}
	
	/**
	 * Methode de test de {@link outils.Demande#Demande(int, outils.Sens)}.
	 */
	@Test
	public void testDemandeIntSens1() {
		assertSame( 4, d2.etage());
		assertTrue( d2.enMontee());
		assertSame( 3, d3.etage());
		assertTrue( d3.enDescente());
		assertSame( 1, d4.etage());
		assertTrue( d4.estIndefini());
	}
	
	/**
	 * Methode de test de {@link outils.Demande#etage()}.
	 */
	@Test
	public void testEtage() {
		assertSame( 3, d3.etage());
	}

	/**
	 * Methode de test de {@link outils.Demande#passeEtageSuivant()}.
	 * Cas normaux.
	 */
	@Test
	public void testPasseEtageSuivant1() throws ExceptionCabineArretee {
		d2.passeEtageSuivant();
		assertSame( 5, d2.etage());
		d3.passeEtageSuivant();
		assertSame( 2, d3.etage());
	}
	
	/**
	 * Methode de test de {@link outils.Demande#passeEtageSuivant()}.
	 * Cas exceptionnel.
	 */
	@Test(expected=ExceptionCabineArretee.class)
	public void testPasseEtageSuivant2() throws ExceptionCabineArretee {
		d4.passeEtageSuivant();
	}

	/**
	 * Methode obsolete de test de la mehode obsolete {@link outils.Demande#sens()}.
	 * 
	 * @deprecated remplacee par {@link #testEnMontee()}, {@link #testEnDEscente()} et
	 * {@link #testEstIndefini()}
	 */
	@Test @Deprecated
	public void testSens() {
		assertEquals( Sens.MONTEE, d2.sens());
		assertEquals( Sens.DESCENTE, d3.sens());
		assertEquals( Sens.INDEFINI, d4.sens());
	}
	
	/**
	 * Methode de test de {@link outils.Demande#enMontee()}.
	 */
	@Test
	public void testEnMontee() {
		assertTrue( d2.enMontee());
		assertFalse( d3.enMontee());
		assertFalse( d4.enMontee());
	}

	/**
	 * Methode de test de {@link outils.Demande#enDescente()}.
	 */
	@Test
	public void testEnDescente() {
		assertTrue( d3.enDescente());
		assertFalse( d2.enDescente());
		assertFalse( d4.enDescente());
	}

	/**
	 * Methode de test de {@link outils.Demande#estIndefini()}.
	 */
	@Test
	public void testEstIndefini() {
		assertTrue( d4.estIndefini());
		assertFalse( d2.estIndefini());
		assertFalse( d3.estIndefini());
	}

	/**
	 * Methode de test de {@link outils.Demande#changeSens(outils.Sens)}.
	 */
	@Test
	public void testChangeSens() {
		d3.changeSens(Sens.DESCENTE);
		assertTrue( d3.enDescente());
		d3.changeSens(Sens.MONTEE);
		assertTrue( d3.enMontee());
		d3.changeSens(Sens.INDEFINI);
		assertTrue( d3.estIndefini());
	}
	/**
	 * Methode de test de {@link outils.Demande#toString()}.
	 */
	@Test
	public void testToString() {
		assertEquals( d2.toString(), "4^");
		assertEquals( d3.toString(), "3v");
		assertEquals( d4.toString(), "1-");
	}

	/**
	 * Methode de test de {@link outils.Demande#equals(java.lang.Object)}.
	 */
	@Test
	public void testEqualsObject() {
		assertFalse( d2.equals( null));
		assertFalse( d2.equals( new Integer(4)));
		assertTrue( d2.equals( new Demande(4, Sens.MONTEE)));
		assertFalse( d2.equals( new Demande(4, Sens.INDEFINI)));
		assertFalse( d2.equals( new Demande(4, Sens.DESCENTE)));
		assertFalse( d2.equals( new Demande(3, Sens.MONTEE)));
		assertTrue( d1.equals( new Demande(0, Sens.INDEFINI)));
	}
}
