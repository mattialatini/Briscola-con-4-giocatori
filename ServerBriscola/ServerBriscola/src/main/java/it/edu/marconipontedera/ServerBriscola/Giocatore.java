package it.edu.marconipontedera.ServerBriscola;
// Indica il package (cartella logica) in cui si trova questa classe

import java.io.PrintWriter;

public class Giocatore {
    // Variabili d'istanza della classe Giocatore
    private String CodiceGiocatore;  // Codice univoco per il giocatore
    private String NomeGiocatore;    // Nome del giocatore
    private String NomeSquadra;      // Nome della squadra a cui appartiene il giocatore
    private int PunteggioGiocatore; // Punteggio del giocatore
    private Squadra squadra;        // Oggetto di tipo Squadra che rappresenta la squadra del giocatore
    private Carta[] carte;          // Array di carte in mano al giocatore
    private PrintWriter out;        // Oggetto PrintWriter per la comunicazione con il giocatore (es. invio messaggi)

    // Costruttore che inizializza un giocatore con un CodiceGiocatore, un Nome e un oggetto 
    // PrintWriter
    public Giocatore(String CodiceGiocatore, String Nome, PrintWriter out) {
        this.CodiceGiocatore = CodiceGiocatore; // Inizializza il codice del giocatore
        this.NomeGiocatore = Nome;              // Inizializza il nome del giocatore
        this.NomeSquadra = "";                  // Nome squadra vuoto inizialmente
        this.PunteggioGiocatore = 0;            // Punteggio iniziale del giocatore
        this.squadra = null;                    // Squadra inizialmente nulla
        this.carte = new Carta[3];              // Mano del giocatore con 3 carte iniziali
        this.out = out;                         // Oggetto PrintWriter per la comunicazione con il giocatore
    }

    // Metodo per ottenere le carte del giocatore
    public Carta[] getCarte() {
        return carte;
    }

    // Metodo per impostare le carte del giocatore
    public synchronized void setCarte(Carta[] carte) {
        this.carte = carte;
    }

    // Metodo per ottenere l'oggetto PrintWriter
    public PrintWriter getOut() {
        return out; 
    }

    // Metodo per aggiungere punti al punteggio del giocatore
    public void AggiungiPunteggioGiocatore(int PunteggioGiocatore) {
        this.PunteggioGiocatore += PunteggioGiocatore;
    }

    // Metodo per ottenere il punteggio del giocatore
    public int getPunteggioGiocatore() {
        return PunteggioGiocatore;
    }

    // Metodo per ottenere il codice del giocatore
    public String getCodiceGiocatore() {
        return CodiceGiocatore;
    }

    // Metodo per impostare il codice del giocatore
    public void setCodiceGiocatore(String CodiceGiocatore) {
        this.CodiceGiocatore = CodiceGiocatore;
    }

    // Metodo per ottenere il nome del giocatore
    public String getNomeGiocatore() {
        return NomeGiocatore;
    }

    // Metodo per impostare il nome del giocatore
    public void setNomeGiocatore(String NomeGiocatore) {
        this.NomeGiocatore = NomeGiocatore;
    }

    // Metodo per ottenere il nome della squadra del giocatore
    public String getNomeSquadra() {
        return NomeSquadra;
    }

    // Metodo per impostare il nome della squadra del giocatore
    public void setNomeSquadra(String NomeSquadra) {
        this.NomeSquadra = NomeSquadra;
    }

    // Metodo per impostare la squadra del giocatore
    public void setSquadra(Squadra squadra) {
        this.squadra = squadra;
    }

    // Metodo per ottenere la squadra del giocatore
    public Squadra getSquadra() {
        return squadra; 
    }

    // Metodo che rimuove una carta dalla mano del giocatore e la restituisce
    public synchronized Carta GiocaCarta(String ValoreSeme) {
        for (int i = 0; i < carte.length; i++) { // Ciclo attraverso tutte le carte
            if (carte[i] != null && carte[i].toString().equals(ValoreSeme)) { 
            // Trova la carta con il valore del seme specificato
                Carta carta = carte[i];  // Salva la carta da giocare
                carte[i] = null;     // Rimuove la carta dalla mano
                return carta;            // Restituisce la carta
            }
        }
        return null;  // Se non trova la carta, restituisce null
    }

    // Metodo che aggiunge una carta alla mano del giocatore
    public synchronized void AggiungiCarta(Carta carta) {
        for (int i = 0; i < carte.length; i++) { // Ciclo attraverso tutte le carte
            if (carte[i] == null) {  // Se la posizione è vuota
                carte[i] = carta;     // Aggiunge la carta nella mano del giocatore
                return;               // Esce dal metodo
            }
        }
    }
}
