package it.edu.marconipontedera.ServerBriscola;
// Indica il package (cartella logica) in cui si trova questa classe

import java.util.Random;

public class Mazzo {
    // Crea un array di carte con una lunghezza di 40
    private Carta[] carte = new Carta[40];

    // Costruttore della classe Mazzo
    public Mazzo() {
        // Inizializza tutte le carte con il seme e il valore
        carte[0]  = new Carta("Denari",  "Asso",    11);
        carte[1]  = new Carta("Denari",  "Tre",     10);
        carte[2]  = new Carta("Denari",  "Re",       4);
        carte[3]  = new Carta("Denari",  "Cavallo",  3);
        carte[4]  = new Carta("Denari",  "Fante",    2);
        carte[5]  = new Carta("Denari",  "7",        0);
        carte[6]  = new Carta("Denari",  "6",        0);
        carte[7]  = new Carta("Denari",  "5",        0);
        carte[8]  = new Carta("Denari",  "4",        0);
        carte[9]  = new Carta("Denari",  "2",        0);
        carte[10] = new Carta("Coppe",   "Asso",    11);
        carte[11] = new Carta("Coppe",   "Tre",     10);
        carte[12] = new Carta("Coppe",   "Re",       4);
        carte[13] = new Carta("Coppe",   "Cavallo",  3);
        carte[14] = new Carta("Coppe",   "Fante",    2);
        carte[15] = new Carta("Coppe",   "7",        0);
        carte[16] = new Carta("Coppe",   "6",        0);
        carte[17] = new Carta("Coppe",   "5",        0);
        carte[18] = new Carta("Coppe",   "4",        0);
        carte[19] = new Carta("Coppe",   "2",        0);
        carte[20] = new Carta("Spade",   "Asso",    11);
        carte[21] = new Carta("Spade",   "Tre",     10);
        carte[22] = new Carta("Spade",   "Re",       4);
        carte[23] = new Carta("Spade",   "Cavallo",  3);
        carte[24] = new Carta("Spade",   "Fante",    2);
        carte[25] = new Carta("Spade",   "7",        0);
        carte[26] = new Carta("Spade",   "6",        0);
        carte[27] = new Carta("Spade",   "5",        0);
        carte[28] = new Carta("Spade",   "4",        0);
        carte[29] = new Carta("Spade",   "2",        0);
        carte[30] = new Carta("Bastoni", "Asso",    11);
        carte[31] = new Carta("Bastoni", "Tre",     10);
        carte[32] = new Carta("Bastoni", "Re",       4);
        carte[33] = new Carta("Bastoni", "Cavallo",  3);
        carte[34] = new Carta("Bastoni", "Fante",    2);
        carte[35] = new Carta("Bastoni", "7",        0);
        carte[36] = new Carta("Bastoni", "6",        0);
        carte[37] = new Carta("Bastoni", "5",        0);
        carte[38] = new Carta("Bastoni", "4",        0);
        carte[39] = new Carta("Bastoni", "2",        0);
    }

    // Metodo per mescolare le carte nel mazzo
    public void MescolaCarte() {
        Random random = new Random(); // Crea un oggetto random per mescolare le carte
        // Algoritmo di mescolamento
        for (int i = carte.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1); // Genera un numero casuale
            Carta temp = carte[i]; // Scambia la carta corrente con quella casuale
            carte[i] = carte[j];
            carte[j] = temp;
        }
    }

    // Metodo per pescare la prima carta disponibile nel mazzo
    public Carta PescaCarta() {
        for (int i = 0; i < carte.length; i++) { 
            if (carte[i] != null) { // Se la carta non è nulla, la Pesca
                Carta carta = carte[i]; // Memorizza la carta
                carte[i] = null; // Rimuove la carta dal mazzo
                return carta; // Restituisce la carta pescata
            }
        }
        return null; // Se non ci sono più carte, restituisce null
    }

    // Restituisce la carta briscola, cioè l'ultima carta rimasta nel mazzo
    public Carta getBriscola() {
        for (int i = carte.length - 1; i >= 0; i--) { // Scorre il mazzo dal fondo
            if (carte[i] != null) return carte[i]; // Restituisce l'ultima carta non nulla
        }
        return null; // Se non ci sono carte, restituisce null
    }

    // Restituisce solo il seme della carta briscola
    public String getSemeBriscola() {
        Carta carta = getBriscola(); // Prende la carta briscola
        if (carta != null) {
            return carta.getSeme(); // Restituisce il seme della carta briscola
        }
        return null; // Se non c'è briscola, restituisce null
    }

    // Restituisce la dimensione del mazzo (quante carte sono ancora nel mazzo)
    public int getSize() {
        int count = 0; // Contatore per le carte nel mazzo
        for (int i = 0; i < carte.length; i++) {
            if (carte[i] != null) count++; // Incrementa il contatore per ogni carta non nulla
        }
        return count; // Restituisce il numero di carte nel mazzo
    }

    // Controlla se il mazzo è vuoto
    public boolean isEmpty() {
        return getSize() == 0; // Se la dimensione è 0, il mazzo è vuoto
    }
}