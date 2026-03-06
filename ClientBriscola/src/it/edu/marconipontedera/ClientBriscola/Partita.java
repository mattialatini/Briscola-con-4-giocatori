package it.edu.marconipontedera.ClientBriscola;

// Classe che rappresenta lo stato della partita dal punto di vista del client
// I dati vengono aggiornati tramite i messaggi ricevuti dal server
public class Partita {

    // Seme della briscola (es: cuori, denari, ecc.)
    private String SemeBriscola;

    // Codice del giocatore che deve giocare il turno attuale
    private String CodiceGiocatoreIniziale;

    // Creazione delle due squadre della partita
    private Squadra SquadraA = new Squadra("Squadra A");
    private Squadra SquadraB = new Squadra("Squadra B");

    // Array che contiene le carte giocate sul tavolo nella mano corrente (massimo 4)
    private String[] CarteTavolo = new String[4];

    // Array che contiene i codici dei giocatori che hanno giocato le carte sul tavolo
    private String[] GiocatoriTavolo = new String[4];

    // Contatore del numero di carte giocate nella mano corrente
    private int CarteGiocate = 0;

    // Metodo chiamato all'inizio della partita
    // Salva il Seme di briscola e il primo giocatore che deve giocare (messaggio 04 del server)
    public void ImpostaInizio(String Seme, String CodiceGiocatoreIniziale) {
        this.SemeBriscola = Seme;       // imposta il Seme della briscola
        this.CodiceGiocatoreIniziale  = CodiceGiocatoreIniziale; // imposta il primo giocatore
        CarteGiocate = 0;               // azzera il numero di carte sul tavolo
    }

    // Metodo che aggiorna il giocatore che deve giocare il turno (messaggio 34 del server)
    public void ImpostaTurno(String CodiceGiocatoreIniziale) { 
        this.CodiceGiocatoreIniziale = CodiceGiocatoreIniziale; 
    }

    // Metodo che aggiunge una carta giocata sul tavolo (messaggio 67 del server)
    public void AggiungiCartaTavolo(String CodiceGiocatore, String valoreSeme) {

        // Controlla che non siano già state giocate 4 carte
        if (CarteGiocate < 4) {

            // Salva la carta giocata (valore + seme)
            CarteTavolo[CarteGiocate] = valoreSeme;

            // Salva il CodiceGiocatore del giocatore che ha giocato la carta
            GiocatoriTavolo[CarteGiocate] = CodiceGiocatore;

            // Incrementa il numero di carte giocate
            CarteGiocate++;
        }
    }

    // Metodo chiamato quando una squadra prende la mano (messaggio 92 del server)
    // Serve per pulire il tavolo e prepararlo alla mano successiva
    public void RegistraPresa() {

        // Reinizializza l'array delle carte sul tavolo
        CarteTavolo    = new String[4];

        // Reinizializza l'array dei giocatori che hanno giocato
        GiocatoriTavolo = new String[4];

        // Azzera il contatore delle carte giocate
        CarteGiocate   = 0;
    }

    // Metodo che aggiorna i punteggi delle due squadre (messaggi 92 e 12 del server)
    public void AggiornaPunteggi(int PunteggioA, int PunteggioB) {

        // Imposta il punteggio della squadra A
        SquadraA.setPunteggio(PunteggioA);

        // Imposta il punteggio della squadra B
        SquadraB.setPunteggio(PunteggioB);
    }

    // Metodo che restituisce il seme della briscola
    public String getSemeBriscola() { 
        return SemeBriscola;
    }

    // Metodo che restituisce il codice del giocatore che deve giocare
    public String getCodiceGiocatoreIniziale() { 
        return CodiceGiocatoreIniziale;
    }

    // Restituisce l'oggetto squadra A
    public Squadra  getSquadraA() {
        return SquadraA;
    }

    // Restituisce l'oggetto squadra B
    public Squadra  getSquadraB() { 
        return SquadraB;
    }

    // Restituisce l'array delle carte sul tavolo
    public String[] getCarteTavolo() { 
        return CarteTavolo;
    }

    // Restituisce l'array dei giocatori che hanno giocato le carte
    public String[] getGiocatoriTavolo() { 
        return GiocatoriTavolo; 
    }

    // Restituisce quante carte sono state giocate nel turno corrente
    public int getCarteGiocate() {
        return CarteGiocate;
    }
}