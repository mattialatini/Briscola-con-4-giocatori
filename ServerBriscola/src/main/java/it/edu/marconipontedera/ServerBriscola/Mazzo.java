package it.edu.marconipontedera.ServerBriscola;
import java.util.Random;

public class Mazzo {
    private Carta[] carte = new Carta[40]; // Un array di 40 carte

    // Costruttore che inizializza il mazzo con tutte le carte
    public Mazzo() {
        // Creazione delle carte
        carte[0] = new Carta("Denari", "Asso", 11);
        carte[1] = new Carta("Denari", "Tre", 10);
        carte[2] = new Carta("Denari", "Re", 4);
        carte[3] = new Carta("Denari", "Cavallo", 3);
        carte[4] = new Carta("Denari", "Fante", 2);
        carte[5] = new Carta("Denari", "7", 0);
        carte[6] = new Carta("Denari", "6", 0);
        carte[7] = new Carta("Denari", "5", 0);
        carte[8] = new Carta("Denari", "4", 0);
        carte[9] = new Carta("Denari", "2", 0);
        carte[10] = new Carta("Coppe", "Asso", 11);
        carte[11] = new Carta("Coppe", "Tre", 10);
        carte[12] = new Carta("Coppe", "Re", 4);
        carte[13] = new Carta("Coppe", "Cavallo", 3);
        carte[14] = new Carta("Coppe", "Fante", 2);
        carte[15] = new Carta("Coppe", "7", 0);
        carte[16] = new Carta("Coppe", "6", 0);
        carte[17] = new Carta("Coppe", "5", 0);
        carte[18] = new Carta("Coppe", "4", 0);
        carte[19] = new Carta("Coppe", "2", 0);
        carte[20] = new Carta("Spade", "Asso", 11);
        carte[21] = new Carta("Spade", "Tre", 10);
        carte[22] = new Carta("Spade", "Re", 4);
        carte[23] = new Carta("Spade", "Cavallo", 3);
        carte[24] = new Carta("Spade", "Fante", 2);
        carte[25] = new Carta("Spade", "7", 0);
        carte[26] = new Carta("Spade", "6", 0);
        carte[27] = new Carta("Spade", "5", 0);
        carte[28] = new Carta("Spade", "4", 0);
        carte[29] = new Carta("Spade", "2", 0);
        carte[30] = new Carta("Bastoni", "Asso", 11);
        carte[31] = new Carta("Bastoni", "Tre", 10);
        carte[32] = new Carta("Bastoni", "Re", 4);
        carte[33] = new Carta("Bastoni", "Cavallo", 3);
        carte[34] = new Carta("Bastoni", "Fante", 2);
        carte[35] = new Carta("Bastoni", "7", 0);
        carte[36] = new Carta("Bastoni", "6", 0);
        carte[37] = new Carta("Bastoni", "5", 0);
        carte[38] = new Carta("Bastoni", "4", 0);
        carte[39] = new Carta("Bastoni", "2", 0);
        
    }

    // Metodo per mescolare il mazzo
    public void mescola() {
        for (int i = 0; i < carte.length; i++) {
            Random random = new Random(); // Creiamo un oggetto Random per generare numeri casuali
            int j = random.nextInt(40); // Indice casuale tra 0 e 39
            // Scambia le carte tra le posizioni i e j
            Carta temp = carte[i];
            carte[i] = carte[j];
            carte[j] = temp;
        }
    }

    // Metodo per pescare una carta dal mazzo (restituisce e rimuove la prima carta)
    public Carta pesca() {
        Carta carta = carte[0]; // Preleva la carta in cima al mazzo
        // Spostiamo tutte le carte a sinistra
        for (int i = 0; i < carte.length - 1; i++) {
            carte[i] = carte[i + 1];
        }
        carte[carte.length - 1] = null; // La carta rimossa è ora null
        return carta;
    }
    
    // Metodo per ottenere il seme di briscola
    public String getSemeBriscola() {
        return carte[0].getSeme();  // Prende il seme dalla prima carta
    }

    public int getSize() {
        int count = 0;
        for (int i = 0; i < carte.length; i++) {
            if (carte[i] != null) {
                count++; // Conta le carte che non sono null
            }
        }
        return count;
    }
    
    public boolean isEmpty() {
        return getSize() == 0;
    }
}