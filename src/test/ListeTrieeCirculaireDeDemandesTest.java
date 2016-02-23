/**
 * Classes de test des classes du projet ascenseur.
 */
package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import outils.Demande;
import outils.Sens;
import commande.IListeTrieeCirculaire;
import commande.ListeTrieeCirculaireDeDemandes;

/**
 * Classe de test de la classe {@link commande.ListeTrieeCirculaireDeDemandes}.
 * 
 * @author L. Torres
 *
 */
public class ListeTrieeCirculaireDeDemandesTest {

	private IListeTrieeCirculaire<Demande> suite1, suite2;
	
	@Before
	public void setUp() {
		suite1 = new ListeTrieeCirculaireDeDemandes( 10);
		suite2 = new ListeTrieeCirculaireDeDemandes( 10);
		suite1.inserer( new Demande(8, Sens.MONTEE));
		suite1.inserer( new Demande(2, Sens.MONTEE));
		suite1.inserer( new Demande(8, Sens.DESCENTE));
		suite1.inserer( new Demande(4, Sens.DESCENTE));
		assertEquals( suite1.toString(), "[2^,8^,8v,4v]");
		assertEquals( suite2.toString(), "[]");
	}

	@After
	public void tearDown() throws Exception {
		suite1 = null;
		suite2 = null;
	}

	/**
	 * Methode de test de {@link commande.ListeTrieeCirculaireDeDemandes#ListeTrieeCirculaireDeDemandes()}.
	 */
	@Test
	public void testListeTrieeCirculaireDeDemandesDeDeplacement() {
		assertTrue( suite2.estVide());
		assertNotNull( suite1);
	}

	/**
	 * Methode de test de {@link commande.ListeTrieeCirculaireDeDemandes#vider()}.
	 */
	@Test
	public void testVider() {
		assertFalse( suite1.estVide());
		suite1.vider();
		assertTrue( suite1.estVide());
	}

	/**
	 * Methode de test de {@link commande.ListeTrieeCirculaireDeDemandes#estVide()}.
	 * @throws ExceptionDemandeInvalide 
	 */
	@Test
	public void testEstVide() {
		assertFalse( suite1.estVide());
		assertTrue( suite2.estVide());
		suite2.inserer( new Demande( 1, Sens.DESCENTE));
		assertFalse( suite2.estVide());
	}
	
	/**
	 * Methode de test de {@link commande.ListeTrieeCirculaireDeDemandes#taille()}.
	 */
	@Test
	public void testTaille() {
		assertSame( 4,suite1.taille());
		assertSame( 0, suite2.taille());
	}


	/**
	 * Methode de test de {@link commande.ListeTrieeCirculaireDeDemandes#inserer(outils.Demande)}.
	 * Cas normaux.
	 */
	@Test
	public void testInserer1() {
		suite1.inserer( new Demande(9, Sens.DESCENTE));
		assertEquals( suite1.toString(), "[2^,8^,9v,8v,4v]");
		suite1.inserer( new Demande(1, Sens.DESCENTE));
		assertEquals( suite1.toString(), "[2^,8^,9v,8v,4v,1v]");
		suite1.inserer( new Demande(4, Sens.MONTEE));
		assertEquals( suite1.toString(), "[2^,4^,8^,9v,8v,4v,1v]");
		suite1.inserer( new Demande(3, Sens.DESCENTE));
		assertEquals( suite1.toString(), "[2^,4^,8^,9v,8v,4v,3v,1v]");
		suite1.inserer( new Demande(0, Sens.MONTEE));
		assertEquals( suite1.toString(), "[0^,2^,4^,8^,9v,8v,4v,3v,1v]");
		
		suite1.inserer( new Demande(8, Sens.DESCENTE));
		assertEquals( suite1.toString(), "[0^,2^,4^,8^,9v,8v,4v,3v,1v]");
		suite1.inserer( new Demande(9, Sens.DESCENTE));
		assertEquals( suite1.toString(), "[0^,2^,4^,8^,9v,8v,4v,3v,1v]");
		suite1.inserer( new Demande(1, Sens.DESCENTE));
		assertEquals( suite1.toString(), "[0^,2^,4^,8^,9v,8v,4v,3v,1v]");
		suite1.inserer( new Demande(0, Sens.MONTEE));
		assertEquals( suite1.toString(), "[0^,2^,4^,8^,9v,8v,4v,3v,1v]");
		suite1.inserer( new Demande(2, Sens.MONTEE));
		assertEquals( suite1.toString(), "[0^,2^,4^,8^,9v,8v,4v,3v,1v]");
		suite1.inserer( new Demande(8, Sens.MONTEE));
		assertEquals( suite1.toString(), "[0^,2^,4^,8^,9v,8v,4v,3v,1v]");
	}
	
	/**
	 * Methode de test de {@link commande.ListeTrieeCirculaireDeDemande#inserer(outils.Demande)}.
	 * Cas exceptionnel.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testInserer2() {
		suite1.inserer( new Demande(4, Sens.INDEFINI));
	}
	
	/**
	 * Methode de test de {@link commande.ListeTrieeCirculaireDeDemande#inserer(outils.Demande)}.
	 * Cas exceptionnel.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testInserer3() {
		suite1.inserer( new Demande(0, Sens.DESCENTE));
	}

	/**
	 * Methode de test de {@link commande.ListeTrieeCirculaireDeDemande#inserer(outils.Demande)}.
	 * Cas exceptionnel.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testInserer4() {
		suite1.inserer( new Demande(9, Sens.MONTEE));
	}

	/**
	 * Methode de test de {@link commande.ListeTrieeCirculaireDeDemande#inserer(outils.Demande)}.
	 * Cas exceptionnel.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testInserer5() {
		suite1.inserer( new Demande(-3, Sens.MONTEE));
	}

	/**
	 * Methode de test de {@link commande.ListeTrieeCirculaireDeDemande#inserer(outils.Demande)}.
	 * Cas exceptionnel.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testInserer6() {
		suite1.inserer( new Demande(-1, Sens.DESCENTE));
	}

	/**
	 * Methode de test de {@link commande.ListeTrieeCirculaireDeDemande#inserer(outils.Demande)}.
	 * Cas exceptionnel.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testInserer7() {
		suite1.inserer( new Demande(-8, Sens.INDEFINI));
	}

	/**
	 * Methode de test de {@link commande.ListeTrieeCirculaireDeDemande#inserer(outils.Demande)}.
	 * Cas exceptionnel.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testInserer8() {
		suite1.inserer( new Demande(10, Sens.DESCENTE));
	}
	
	/**
	 * Methode de test de {@link commande.ListeTrieeCirculaireDeDemande#inserer(outils.Demande)}.
	 * Cas exceptionnel.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testInserer9() {
		suite1.inserer( new Demande(11, Sens.MONTEE));
	}

	/**
	 * Methode de test de {@link commande.ListeTrieeCirculaireDeDemande#inserer(outils.Demande)}.
	 * Cas exceptionnel.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testInserer10() {
		suite1.inserer( new Demande(12, Sens.INDEFINI));
	}
	
	/**
	 * Methode de test {@link commande.ListeTrieeCirculaireDeDemandes#supprimer(outils.Demande)}.
	 * Cas normaux.
	 */
	@Test
	public void testSupprimer1()  {
		suite1.supprimer( new Demande(2, Sens.MONTEE));
		assertEquals( suite1.toString(), "[8^,8v,4v]");
		suite1.supprimer( new Demande(8, Sens.DESCENTE));
		assertEquals( suite1.toString(), "[8^,4v]");
		suite1.supprimer( new Demande(8, Sens.MONTEE));
		assertEquals( suite1.toString(), "[4v]");
		suite1.supprimer( new Demande(4, Sens.DESCENTE));
		assertTrue( suite1.estVide());
	}
	
	/**
	 * Methode de test {@link commande.ListeTrieeCirculaireDeDemandes#supprimer(outils.Demande)}.
	 * Cas Exceptionnel.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testSupprimer2() {
		suite1.supprimer( new Demande(3, Sens.MONTEE));
	}

	/**
	 * Methode de test {@link commande.ListeTrieeCirculaireDeDemandes#supprimer(outils.Demande)}.
	 * Cas Exceptionnel.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testSupprimer3() {
		suite1.supprimer( new Demande(3, Sens.DESCENTE));
	}

	/**
	 * Methode de test {@link commande.ListeTrieeCirculaireDeDemandes#supprimer(outils.Demande)}.
	 * Cas Exceptionnel.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testSupprimer4() {
		suite2.supprimer( new Demande(3, Sens.MONTEE));
	}

	/**
	 * Methode de test {@link commande.ListeTrieeCirculaireDeDemandes#supprimer(outils.Demande)}.
	 * Cas Exceptionnel.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testSupprimer5() {
		suite1.supprimer( new Demande(8, Sens.INDEFINI));
	}

	/**
	 * Methode de test de {@link commande.ListeTrieeCirculaireDeDemandes#suivantDe(outils.Demande)}.
	 */
	@Test
	public void testSuivantDe() {
		assertNull( suite2.suivantDe(new Demande(3,Sens.MONTEE)));
		
		suite2.inserer( new Demande( 3, Sens.MONTEE));
		assertEquals( suite2.toString(), "[3^]");
		assertEquals( new Demande( 3, Sens.MONTEE), 
				suite2.suivantDe(new Demande(1, Sens.MONTEE)));
		assertEquals( new Demande( 3, Sens.MONTEE), 
				suite2.suivantDe(new Demande( 4, Sens.MONTEE)));
		assertEquals( new Demande( 3, Sens.MONTEE), 
				suite2.suivantDe(new Demande(1, Sens.DESCENTE)));
		assertEquals( new Demande( 3, Sens.MONTEE), 
				suite2.suivantDe(new Demande( 4, Sens.DESCENTE)));

		suite2.inserer( new Demande( 0, Sens.MONTEE));
		assertEquals( suite2.toString(), "[0^,3^]");
		assertEquals( new Demande( 3, Sens.MONTEE), 
				suite2.suivantDe(new Demande(1, Sens.MONTEE)));
		assertEquals( new Demande( 0, Sens.MONTEE), 
				suite2.suivantDe(new Demande( 4, Sens.MONTEE)));
		assertEquals( new Demande( 0, Sens.MONTEE), 
				suite2.suivantDe(new Demande(1, Sens.DESCENTE)));
		assertEquals( new Demande( 0, Sens.MONTEE), 
				suite2.suivantDe(new Demande( 4, Sens.DESCENTE)));

		suite2.supprimer( new Demande( 3, Sens.MONTEE));
		suite2.supprimer( new Demande( 0, Sens.MONTEE));
		suite2.inserer( new Demande( 2, Sens.DESCENTE));
		suite2.inserer( new Demande( 4, Sens.DESCENTE));
		assertEquals( suite2.toString(), "[4v,2v]");
		assertEquals( new Demande( 4, Sens.DESCENTE), 
				suite2.suivantDe(new Demande(1, Sens.MONTEE)));
		assertEquals( new Demande( 4, Sens.DESCENTE), 
				suite2.suivantDe(new Demande( 5, Sens.MONTEE)));
		assertEquals( new Demande( 2, Sens.DESCENTE), 
				suite2.suivantDe(new Demande(3, Sens.DESCENTE)));
		assertEquals( new Demande( 4, Sens.DESCENTE), 
				suite2.suivantDe(new Demande( 1, Sens.DESCENTE)));
		
		assertEquals( suite1.toString(), "[2^,8^,8v,4v]");
		assertEquals( new Demande(2,Sens.MONTEE), 
				suite1.suivantDe(new Demande(0,Sens.MONTEE)));
		assertEquals( new Demande(2,Sens.MONTEE),
				suite1.suivantDe(new Demande(2,Sens.MONTEE)));
		assertEquals( new Demande(8,Sens.MONTEE),
				suite1.suivantDe(new Demande(3,Sens.MONTEE)));
		assertEquals( new Demande(8,Sens.MONTEE), 
				suite1.suivantDe(new Demande(7,Sens.MONTEE)));
		assertEquals( new Demande(8,Sens.MONTEE),
				suite1.suivantDe(new Demande(8,Sens.MONTEE)));
		assertEquals( new Demande(4,Sens.DESCENTE),
				suite1.suivantDe(new Demande(7,Sens.DESCENTE)));
		assertEquals( new Demande(4,Sens.DESCENTE), 
				suite1.suivantDe(new Demande(4,Sens.DESCENTE)));
		assertEquals( new Demande(2,Sens.MONTEE),
				suite1.suivantDe(new Demande(3,Sens.DESCENTE)));
	}
}
