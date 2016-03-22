/**
 * Classes et interfaces de la partie commande de l'ascenseur. 
 * @author Lucile Torres-Gerardin
 *
 */
package commande;

import outils.Demande;

/**
 * L'interface IControleur décrit un controleur du deplacement de la cabine en reponse
 * aux demandes de deplacement generees par l'IUG. 
 * @author Lucile Torres-Gerardin
 * @see Controleur
 *
 */
public interface IControleur {
	/**
	 * Methode appelee par l'IUG a chaque demande de deplacement d'un utilisateur.
	 * @param d demande de deplacement
	 */
    public void demander( Demande d);
    
    /**
     * Methode appelee par l'IUG a chaque clic sur le bouton arret urgence.
     * Un appel declenche l'arret d'urgence, le suivant l'annule.
     */
    public void arretUrgence();
    
    /**
     * Methode appeleee par (les capteurs de) la cabine pour signaler le franchissement
     * d'un palier. Aucune indication du numero de palier franchi n'est fournie. Le 
     * controleur a la charge de calculer la position courante de la cabine.
     */
    public void signalerChangementDEtage();
    
    public void exit(); // fin de l'application
}
