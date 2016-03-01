package outils;

public interface IIUG {
	void demander(Demande d);
	void arretDUrgence();
	void allumerBouton(Demande d);
	void eteindreBouton(Demande d);
	void ajouterMessage(String s);
	void changerPosition(int i);
}
