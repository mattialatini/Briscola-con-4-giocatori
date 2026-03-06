package it.edu.marconipontedera.ClientBriscola;

// Interfaccia che la schermata grafica deve implementare per ricevere eventi dal client
// Un'interfaccia serve a definire dei metodi che altre classi dovranno implementare
public interface ClientUI {

    // Metodo chiamato quando il nostro giocatore si è collegato con successo al server
    // Viene passato l'oggetto Giocatore che rappresenta il giocatore locale
    void OnConnessioneCompletata(Giocatore giocatore);

    // Metodo chiamato quando la partita inizia
    // SemeBriscola = il seme della briscola (es. Cuori, Denari, ecc.)
    // CodiceGiocatoreIniziale = il codice del giocatore che inizia il turno
    void OnInizioPartita(String SemeBriscola, String CodiceGiocatoreIniziale);

    // Metodo chiamato ogni volta che cambia il turno
    // CodiceGiocatore = codice del giocatore a cui tocca giocare
    // MioTurno = true se è il turno del nostro giocatore, false se è il turno di un altro
    void OnNuovoTurno(String CodiceGiocatore, boolean MioTurno);

    // Metodo chiamato quando cambiano le carte nella mano del giocatore
    // Succede dopo la distribuzione iniziale o dopo aver pescato una carta
    // Viene passato il giocatore con la mano aggiornata
    void AggiornaMano(Giocatore giocatore);

    // Metodo chiamato quando una carta viene giocata sul tavolo
    // L'oggetto Partita contiene lo stato aggiornato della partita
    void AggiornaTavolo(Partita partita);

    // Metodo chiamato quando un giocatore prende la mano (vince il turno)
    // CodiceGiocatore = codice del giocatore che ha preso la mano
    // ValoreSeme = valore e seme della carta vincente
    // Punteggio = punteggi ottenuti con la presa
    // partita = stato aggiornato della partita
    void OnPresaEffettuata(String CodiceGiocatore, String ValoreSeme, int Punteggio, Partita partita);

    // Metodo chiamato alla fine di una smazzata (quando finiscono le carte)
    // PunteggioA = punteggio della squadra A
    // PunteggioB = punteggio della squadra B
    void OnFineSmazzata(int PunteggioA, int PunteggioB);

    // Metodo chiamato quando la partita termina
    // NomeSquadra = nome della squadra vincitrice
    void OnRisultati(String nomeSquadra);

    // Metodo chiamato quando si verifica un errore nel client o nella comunicazione
    // Messaggio = descrizione dell'errore
    void OnErrore(String Messaggio);
}