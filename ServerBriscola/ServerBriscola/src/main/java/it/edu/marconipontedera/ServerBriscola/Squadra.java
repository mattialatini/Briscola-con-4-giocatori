package it.edu.marconipontedera.ServerBriscola;

public class Squadra {
    private Giocatore Giocatore1;  // Primo giocatore della squadra
    private Giocatore Giocatore2;  // Secondo giocatore della squadra
    private String NomeSquadra;    // Nome della squadra

    // Costruttore della squadra
    public Squadra(String Nome) {
        this.NomeSquadra = Nome;
        this.Giocatore1 = null;  // Inizialmente senza giocatori
        this.Giocatore2 = null;  // Inizialmente senza giocatori
    }

    // Setter e getter per i giocatori della squadra
    public Giocatore getGiocatore1() {
        return Giocatore1;
    }

    public void setGiocatore1(Giocatore Giocatore1) {
        this.Giocatore1 = Giocatore1;
    }

    public Giocatore getGiocatore2() {
        return Giocatore2;
    }

    public void setGiocatore2(Giocatore Giocatore2) {
        this.Giocatore2 = Giocatore2;
    }

    // Getter per il Nome della squadra
    public String getNomeSquadra() {
        return NomeSquadra;
    }

    public void setNomeSquadra(String NomeSquadra) {
        this.NomeSquadra = NomeSquadra;
    }

    // Calcola il Punteggio complessivo della squadra
    public int getPunteggioSquadra() {
        int PunteggioSquadra = 0;
        if (Giocatore1 != null) {
            PunteggioSquadra += Giocatore1.getPunteggioGiocatore();
        }
        if (Giocatore2 != null) {
            PunteggioSquadra += Giocatore2.getPunteggioGiocatore();
        }
        return PunteggioSquadra;
    }

    // Verifica se la squadra ha due giocatori
    public boolean isFull() {
        return Giocatore1 != null && Giocatore2 != null;
    }

    // Aggiungi carte alla squadra
    public void AggiungiCarte(Carta[] carte) {
        // Aggiungi le carte al Punteggio della squadra
        int Punteggio = 0;
        for (int i = 0; i < carte.length; i++) {
            Punteggio += carte[i].getPunteggio();  // Aggiungi i punti di ogni carta
        }
        // Aggiungi il Punteggio alla squadra
        if (Giocatore1 != null) {
            Giocatore1.AggiungiPunteggioGiocatore(Punteggio);
        }
        if (Giocatore2 != null) {
            Giocatore2.AggiungiPunteggioGiocatore(Punteggio);
        }
    }
}