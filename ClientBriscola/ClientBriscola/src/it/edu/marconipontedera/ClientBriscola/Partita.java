package it.edu.marconipontedera.ClientBriscola;
// Indica il package (cartella logica) in cui si trova questa classe

// Classe che rappresenta una partita di Briscola
public class Partita {
    private String CartaBriscola; // carta completa della briscola (es. "Asso,Denari")
    private String CodiceGiocatoreIniziale; // codice del giocatore che inizia il turno
    private Squadra squadraA = new Squadra("Squadra A"); // istanza della squadra A
    private Squadra squadraB = new Squadra("Squadra B"); // istanza della squadra B
    private String[] CarteTavolo    = new String[4]; // array che contiene le carte giocate sul tavolo
    private String[] GiocatoriTavolo = new String[4]; // array che contiene i giocatori che hanno giocato 
                                                      //le carte
    private int CarteGiocate; // numero di carte giocate nel turno corrente

    // Imposta l'inizio della partita con seme briscola e giocatore iniziale
    public synchronized void ImpostaInizio(String CartaBriscola, String CodiceGiocatoreIniziale) {
        this.CartaBriscola = CartaBriscola;
        this.CodiceGiocatoreIniziale = CodiceGiocatoreIniziale;
        CarteGiocate = 0; // resetta il contatore delle carte giocate
    }

    // Imposta quale giocatore inizia il turno
    public synchronized void ImpostaTurno(String CodiceGiocatoreIniziale) {
        this.CodiceGiocatoreIniziale = CodiceGiocatoreIniziale;
    }

    // Aggiunge una carta al tavolo e registra il giocatore che l'ha giocata
    public synchronized void AggiungiCartaTavolo(String CodiceGiocatore, String valoreSeme) {
        if (CarteGiocate < 4) { // si assicura che non vengano giocate più di 4 carte
            CarteTavolo[CarteGiocate] = valoreSeme;
            GiocatoriTavolo[CarteGiocate] = CodiceGiocatore;
            CarteGiocate++; // incrementa il contatore delle carte giocate
        }
    }

    // Resetta il tavolo dopo una presa
    public synchronized void RegistraPresa() {
        CarteTavolo = new String[4]; // resetta le carte sul tavolo
        GiocatoriTavolo = new String[4]; // resetta i giocatori sul tavolo
        CarteGiocate = 0; // resetta il contatore delle carte giocate
    }

    // Aggiorna i punteggi delle due squadre
    public synchronized void AggiornaPunteggi(int PunteggioA, int PunteggioB) {
        squadraA.setPunteggio(PunteggioA);
        squadraB.setPunteggio(PunteggioB);
    }

    // Metodi getter e setter per accedere e modificare gli attributi privati
    public synchronized String getCartaBriscola() {
        return CartaBriscola;
    }
    
    public synchronized void setCartaBriscola(String CartaBriscola) {
        this.CartaBriscola = CartaBriscola;
    }
    public String getCodiceGiocatoreIniziale() {
        return CodiceGiocatoreIniziale;
    }
    public synchronized Squadra getSquadraA() {
        return squadraA;
    }
    public synchronized Squadra getSquadraB() {
        return squadraB;
    }
    public synchronized String[] getCarteTavolo() {
        return CarteTavolo;
    }
    public synchronized String[] getGiocatoriTavolo() {
        return GiocatoriTavolo;
    }
    public synchronized int getCarteGiocate() {
        return CarteGiocate;
    }
}
