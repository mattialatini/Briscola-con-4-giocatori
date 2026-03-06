package it.edu.marconipontedera.ServerBriscola;

import java.io.PrintWriter;

public class Giocatore {
    private String CodiceGiocatore;  // Codice univoco del giocatore
    private String NomeGiocatore;             // NomeGiocatore del giocatore
    private int PunteggioGiocatore;  // Punteggio del giocatore
    private Squadra squadra;         // La squadra a cui appartiene
    private Carta[] carte;           // Carte che il giocatore ha in mano (3 carte)
    private PrintWriter out;         // Flusso di output per inviare dati al client

    // Costruttore per inizializzare il giocatore
    public Giocatore(String CodiceGiocatore, String Nome, PrintWriter out) {
        this.CodiceGiocatore = CodiceGiocatore;
        this.NomeGiocatore = Nome;
        this.PunteggioGiocatore = 0;
        this.squadra = null;
        this.carte = new Carta[3]; // Ogni giocatore ha 3 carte
        this.out = out;
    }

    // Getter e setter per le carte
    public Carta[] getCarte() {
        return carte;
    }

    public void setCarte(Carta[] carte) {
        this.carte = carte;  // Imposta le carte del giocatore
    }

    // Getter per il flusso di output
    public PrintWriter getOut() {
        return out;  // Restituisce il flusso di output del client
    }

    // Aggiungi punti al giocatore
    public void aggiungiPunteggioGiocatore(int PunteggioGiocatore) {
        this.PunteggioGiocatore += PunteggioGiocatore;  // Aggiungi punti al PunteggioGiocatore
    }

    // Getter per il punteggio
    public int getPunteggioGiocatore() {
        return PunteggioGiocatore;
    }

    // Getter per il codice del giocatore
    public String getCodiceGiocatore() {
        return CodiceGiocatore;
    }

    // Getter per il NomeGiocatore del giocatore
    public String getNomeGiocatore() {
        return NomeGiocatore;
    }

    // Imposta la squadra del giocatore
    public void setSquadra(Squadra squadra) {
        this.squadra = squadra;
    }

    // Restituisce la squadra del giocatore
    public Squadra getSquadra() {
        return squadra;
    }
    
    // Rimuove una carta dalla mano del giocatore e la restituisce
    public Carta GiocaCarta(String ValoreSeme) {
        for (int i = 0; i < carte.length; i++) {
            if (carte[i] != null && carte[i].toString().equals(ValoreSeme)) {
                Carta carta = carte[i];
                carte[i] = null;
                return carta;
            }
        }
        return null;
    }
    
    // Aggiunge una carta pescata in uno slot libero
    public void aggiungiCarta(Carta carta) {
        for (int i = 0; i < carte.length; i++) {
            if (carte[i] == null) {
                carte[i] = carta;
                return;
            }
        }
    }
    
    // Metodo per impostare il codice del giocatore
    public void setCodiceGiocatore(String CodiceGiocatore) {
        // Assegna il valore passato come parametro alla variabile CodiceGiocatore
        this.CodiceGiocatore = CodiceGiocatore;
    }

    // Metodo per impostare il nome del giocatore
    public void setNomeGiocatore(String NomeGiocatore) {
        // Assegna il valore passato come parametro alla variabile NomeGiocatore
        this.NomeGiocatore = NomeGiocatore;
    }
}