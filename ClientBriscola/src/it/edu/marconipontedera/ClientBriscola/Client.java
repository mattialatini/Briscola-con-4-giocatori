package it.edu.marconipontedera.ClientBriscola;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

// Client: collega un giocatore al server e gestisce i messaggi
public class Client implements Runnable {

    // Porta usata per la connessione al server
    private static final int Port = 1234;

    private String Host;          // indirizzo del server
    private Socket socket;        // socket di connessione
    private BufferedReader in;    // legge i messaggi dal server
    private PrintWriter out;      // invia messaggi al server

    private Giocatore giocatore;  // il giocatore associato a questo client
    private Partita partita;      // stato attuale della partita
    private ClientUI UI;          // interfaccia grafica del client

    private boolean MioTurno = false; // true se è il turno del nostro giocatore

    // Costruttore del client
    public Client(String NomeGiocatore, String NomeSquadra, String Host) {

        // se l'host è valido lo usa, altrimenti usa localhost
        if (Host != null && !Host.isEmpty()) {
            this.Host = Host;
        } else {
            this.Host = "localhost";
        }

        // crea il giocatore con nome e squadra
        this.giocatore = new Giocatore(null, NomeGiocatore, NomeSquadra);

        // crea l'oggetto partita che contiene lo stato del gioco
        this.partita   = new Partita();
    }

    // Imposta la schermata grafica
    public void setUI(ClientUI UI) {
        this.UI = UI;
    }

    // Si connette al server e avvia l'ascolto dei messaggi
    public void Connetti() throws Exception {

        // crea la connessione socket
        socket = new Socket(Host, Port);

        // crea i flussi di input e output
        in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        // avvia il thread che ascolta i messaggi del server
        new Thread(this, "AscoltaMessaggi").start();

        // invia al server la richiesta di connessione
        invia(MessageHeader.RichiestaConnessione(
                giocatore.getNomeGiocatore(),
                giocatore.getNomeSquadra()));
    }

    // Thread: ascolta continuamente i messaggi in arrivo dal server
    @Override
    public void run() {
        try {
            String Riga;

            // continua a leggere finché il server manda messaggi
            while ((Riga = in.readLine()) != null) {
                Gestione(Riga); // gestisce il messaggio ricevuto
            }

        } catch (Exception e) {
            // se la connessione si chiude
            System.out.println("Connessione chiusa");
        }
    }

    // Dice al server che il giocatore è pronto a iniziare
    public void InviaGiocatorePronto() {

        // invia messaggio di giocatore pronto
        invia(MessageHeader.GiocatorePronto(
                giocatore.getCodiceGiocatore()));
    }

    // Manda al server la carta scelta dal giocatore
    public void InviaGiocaCarta(Carta carta) {

        // invia la carta giocata
        invia(MessageHeader.GiocaCarta(
                giocatore.getCodiceGiocatore(),
                carta.toString()));
    }

    // Invia un messaggio al server
    private void invia(MessageHeader Message) {

        // se il canale di uscita esiste invia il messaggio
        if (out != null)
            out.println(Message.toString());
    }

    // Legge il tipo di messaggio e chiama il metodo giusto
    private void Gestione(String Riga) {

        // converte la stringa ricevuta in un oggetto messaggio
        MessageHeader Message = MessageHeader.ParseMessage(Riga);

        // controlla il tipo di messaggio tramite ID
        switch (Message.getIDMessaggio()) {

            case "19":
                OnConfermaConnessione(Message.getCampi());
                break; // connessione accettata
            case "10":
                OnEsitoConnessione(Message.getCampi());
                break; // connessione rifiutata
            case "04":
                OnInizioPartita(Message.getCampi());
                break; // partita inizia
            case "64":
                OnCarteDistribuite(Message.getCampi());
                break; // riceviamo le carte
            case "34":
                OnNuovoTurno(Message.getCampi());
                break; // cambio turno
            case "67":
                OnCartaAccettata(Message.getCampi());
                break; // carta giocata sul tavolo
            case "14":
                OnEsitoGiocata(Message.getCampi());
                break; // mossa non valida
            case "92":
                OnPresaEffettuata(Message.getCampi());
                break; // qualcuno ha preso
            case "52":
                OnCartaPescata(Message.getCampi());
                break; // carta pescata dal mazzo
            case "12": 
                OnFineSmazzata(Message.getCampi());
                break; // fine del giro di carte
            case "56":
                OnRisultati(Message.getCampi());
                break; // fine partita
        }
    }

    // Il server ha confermato la connessione: salviamo il codice assegnato
    private void OnConfermaConnessione(String[] Campi) {

        // salva il codice giocatore assegnato dal server
        giocatore.setCodiceGiocatore(Campi[0]);

        // aggiorna la UI
        if (UI != null)
            UI.OnConnessioneCompletata(giocatore);
    }

    // Il server ha rifiutato la connessione
    private void OnEsitoConnessione(String[] Campi) {

        // se il codice non è 0 significa errore
        if (!"0".equals(Campi[0]) && UI != null)
            UI.OnErrore("Connessione rifiutata.");
    }

    // La partita sta per iniziare: salviamo briscola e primo turno
    private void OnInizioPartita(String[] Campi) {

        // salva le informazioni di inizio partita
        partita.ImpostaInizio(Campi[0], Campi[1]);

        // aggiorna la UI
        if (UI != null)
            UI.OnInizioPartita(Campi[0], Campi[1]);
    }

    // Riceviamo 3 carte dal server
    private void OnCarteDistribuite(String[] Campi) {

        // assegna le tre carte al giocatore
        giocatore.setCarte(
                Carta.fromString(Campi[0]),
                Carta.fromString(Campi[1]),
                Carta.fromString(Campi[2]));

        // aggiorna la mano nella UI
        if (UI != null)
            UI.AggiornaMano(giocatore);
    }

    // Il server comunica chi deve giocare adesso
    private void OnNuovoTurno(String[] Campi) {

        // salva il nuovo turno nella partita
        partita.ImpostaTurno(Campi[0]);

        // controlla se il turno è il nostro
        MioTurno = Campi[0].equals(giocatore.getCodiceGiocatore());

        // aggiorna la UI
        if (UI != null)
            UI.OnNuovoTurno(Campi[0], MioTurno);
    }

    // Una carta è stata giocata sul tavolo (da noi o da un avversario)
    private void OnCartaAccettata(String[] Campi) {

        // aggiunge la carta al tavolo
        partita.AggiungiCartaTavolo(Campi[0], Campi[1]);

        // se la carta è nostra la rimuove dalla mano
        if (Campi[0].equals(giocatore.getCodiceGiocatore()))
            giocatore.RimuoviCarta(Campi[1]);

        // aggiorna la UI del tavolo
        if (UI != null)
            UI.AggiornaTavolo(partita);
    }

    // La nostra mossa non era valida
    private void OnEsitoGiocata(String[] Campi) {

        // se il codice non è 0 significa errore
        if (!"0".equals(Campi[0]) && UI != null)
            UI.OnErrore("Mossa non valida. Riprova.");
    }

    // Qualcuno ha preso la mano: aggiorniamo i punteggi
    private void OnPresaEffettuata(String[] Campi) {

        // legge i punti delle squadre
        int PunteggioA = Integer.parseInt(Campi[1]);
        int PunteggioB = Integer.parseInt(Campi[2]);

        // aggiorna i punteggi nella partita
        partita.AggiornaPunteggi(PunteggioA, PunteggioB);

        // registra la presa
        partita.RegistraPresa();

        // aggiorna la UI
        if (UI != null)
            UI.OnPresaEffettuata(
                    Campi[0],
                    String.valueOf(PunteggioA + PunteggioB),
                    PunteggioA,
                    partita);
    }

    // Abbiamo pescato una carta nuova dal mazzo
    private void OnCartaPescata(String[] Campi) {

        // se la carta pescata è nostra la aggiungiamo alla mano
        if (Campi[0].equals(giocatore.getCodiceGiocatore()))
            giocatore.AggiungiCarta(Carta.fromString(Campi[1]));

        // aggiorna la mano nella UI
        if (UI != null)
            UI.AggiornaMano(giocatore);
    }

    // Finito il giro di 40 carte: mostriamo i punteggi finali
    private void OnFineSmazzata(String[] Campi) {

        // aggiorna i punteggi finali
        partita.AggiornaPunteggi(
                Integer.parseInt(Campi[0]),
                Integer.parseInt(Campi[1]));

        // mostra i punteggi nella UI
        if (UI != null)
            UI.OnFineSmazzata(
                    partita.getSquadraA().getPunteggio(),
                    partita.getSquadraB().getPunteggio());
    }

    // Fine partita: mostriamo il nome della squadra vincitrice
    private void OnRisultati(String[] Campi) {

        // mostra il risultato finale
        if (UI != null)
            UI.OnRisultati(Campi[0]);
    }

    // Getter

    // restituisce il giocatore
    public Giocatore getGiocatore() { 
        return giocatore; 
    }

    // restituisce lo stato della partita
    public Partita   getPartita() {
        return partita;   
    }

    // restituisce se è il nostro turno
    public boolean   isMioTurno() { 
        return MioTurno;  
    }
}