package it.edu.marconipontedera.ClientBriscola;
// Indica il package (cartella logica) in cui si trova questa classe

// Interfaccia che definisce i metodi che devono essere implementati dalla UI del client
public interface ClientUI {
    
    // Chiamato quando la connessione è stata completata
    void OnConnessioneCompletata(Giocatore giocatore);
    
    // Chiamato quando una nuova partita inizia, con il seme di briscola e il codice del giocatore iniziale
    void OnInizioPartita(String SemeBriscola, String CodiceGiocatoreIniziale);
    
    // Chiamato all'inizio di un nuovo turno, con il codice del giocatore e se è il suo turno
    void OnNuovoTurno(String CodiceGiocatore, boolean MioTurno);
    
    // Chiamato per aggiornare la mano del giocatore
    void AggiornaMano(Giocatore giocatore);
    
    // Chiamato per aggiornare lo stato del tavolo durante la partita
    void AggiornaTavolo(Partita partita);
    
    // Chiamato quando un giocatore ha effettuato una presa, con il punteggio e la partita aggiornata
    void OnPresaEffettuata(String CodiceGiocatore, String ValoreSeme, int Punteggio, Partita partita);
    
    // Chiamato quando una smazzata è terminata, con il punteggio finale di entrambe le squadre
    void OnFineSmazzata(int PunteggioA, int PunteggioB);
    
    // Chiamato per mostrare i risultati finali della partita
    void OnRisultati(String NomeSquadra);
    
    // Chiamato in caso di errore, con un messaggio di errore
    void OnErrore(String Messaggio);

    // chiamato quando la partita viene interrotta (disconnessione di un giocatore)
    // Chiamato quando la partita è interrotta, con il motivo dell'interruzione
    void OnPartitaInterrotta(String Esito);
}