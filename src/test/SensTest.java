/**
 * Classes de test des classes du projet ascenseur.
 */
package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import outils.Sens;

/**
 * Classe de test unitaire de l'enumeration {@link outils.Sens}.
 * 
 * @author L. Torres
 *
 */
public class SensTest {
	/**
	 * Methode de test de  {@link ascenseur.outils.Sens}.
	 */
	@Test
	public void test() {
		assertEquals( 3, Sens.values().length);
		for( Sens s : Sens.values()) {
			switch(s) {
			case DESCENTE : assertEquals( s.toString(), "v"); break;
			case INDEFINI : assertEquals( s.toString(), "-"); break;
			case MONTEE : assertEquals( s.toString(), "^"); break;
			default : fail( "Code inatteignable");
			}
		}
	}
}
