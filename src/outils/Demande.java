package outils;

public class Demande {
	private Integer numEtage;
	private Sens sens;
	public Demande() {
		numEtage = null;
	}
	public Demande(int etage, Sens montee) {
		numEtage = etage;
		sens = montee;
	}
	public int etage() { return numEtage; }
<<<<<<< HEAD

	// PLS
    .--.                                               
   / _(c\   .-.     __                                    
  | / /  '-;   \'-'`  `\______                            
  \_\/'/ __/ )  /  )   |      \--,                        
  | \`""`__-/ .'--/   /--------\  \                       
   \\`  ///-\/   /   /---;-.    '-'                       
jgs                (________\  \ 
=======
>>>>>>> 221fb0300b702fde8076be9672874f4429f42120
	public boolean estIndefini() {
		return (this.numEtage == null ? true : false);
	}
	public boolean enMontee() {
		return (Sens.MONTEE == sens) ? true : false;
	}
	public boolean enDescente() {
		return (Sens.DESCENTE == sens) ? true : false;
	}
}
