/**
 * Classes et interfaces de la partie commande de l'ascenseurr. 
 * @author Lucile Torres-Gerardin
 *
 */
package commande;

/**
 * Une IListeCirculaire represente une liste circulaire triee. 
 * @author Lucile Torres-Gerardin
 *
 */
public interface IListeTrieeCirculaire<E> {		
	/** 
	 * Renvoie la taille de la liste circulaire triee.
	 * @return la taille de la liste.
	 */
	int taille();

	/** 
	 * Teste si la liste circulaire triee est vide.
	 * @return true si la liste est vide, false sinon.
	 */
	boolean estVide();
	
	/**
	 * Vide la liste triee circulaire.
	 */
	void vider();
	
	/**
	 * Teste si la liste circulaire triee contient un element.
	 * @param e l'element pour lequel on teste la presence.
	 * @return true si l'element e est contenu par la
	 * liste, false sinon.
	 */
	boolean contient(E e);

	/**
	 * Insere un element dans la liste circulaire triee si l'element
	 * n'est pas deja contenu par la liste.
	 * @param e l'element a inserer
	 * @throws IllegalArgumentException si l'element est deja dans la liste 
	 */
	void inserer(E e);
	
	
	/**
	 * Supprime un element de la liste circulaire triee.
	 * @param e l'element a supprimer
	 * @throws IllegalArgumentException si l'element n'est pas 
	 * dans la liste.
	 */
	void supprimer(E e);
	
	/**
	 * Recherche l'element suivant dans la liste triee circulaire,
	 * de l'element donne en parametre. L'element donne en parametre 
	 * n'est pas necessairement present dans la liste.
	 * @param  l'element courant (present ou non dans la liste)
	 * @returns l'element suivant de l'element courant dans la liste
	 * ou null si la liste est vide.
	 * 
	 */
	E suivantDe( E courant);	
}
