package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import outils.Controleur;
import outils.Demande;
import outils.Sens;

public class ControleurTest {
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
		
	}

}
