package it.edu.marconipontedera.ClientBriscola;
// Indica il package (cartella logica) in cui si trova questa classe

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

// Client: collega un giocatore al server e gestisce i messaggi.
public class Client implements Runnable {

    private static final int Port = 1234;  // Porta su cui il client si connette al server

    private String Host;  // Indirizzo del server
    private Socket socket;  // Socket per la connessione con il server
    private BufferedReader in;  // Lettore per ricevere dati dal server
    private PrintWriter out;  // Scrittore per inviare dati al server

    private Giocatore giocatore;  // Oggetto per rappresentare il giocatore
    private Partita partita;  // Oggetto per gestire lo stato della partita
    private ClientUI UI;  // Oggetto per aggiornare la UI del client

    private boolean MioTurno = false;  // Flag per sapere se è il turno del giocatore

    // Costruttore per inizializzare il client con i dati del giocatore e dell'host
    public Client(String NomeGiocatore, String NomeSquadra, String Host) {
        if (Host != null && !Host.isEmpty()) {
            this.Host = Host;  // Se viene passato un host, usalo
        } else {
            this.Host = "localhost";  // Altrimenti, usa localhost
        }
        this.giocatore = new Giocatore(null, NomeGiocatore, NomeSquadra);  // Crea il giocatore
        this.partita   = new Partita();  // Crea la partita
    }

    // Metodo per impostare l'interfaccia utente (UI)
    public synchronized void setUI(ClientUI UI) {
        this.UI = UI;
    }

    // Metodo per connettersi al server
    public void Connetti() throws Exception {
        socket = new Socket(Host, Port);  // Crea una connessione al server
        in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));  // Crea il lettore
        out = new PrintWriter(socket.getOutputStream(), true);  // Crea lo scrittore
        Thread AscoltaMessaggi = new Thread(this); // crea un nuovo thread usando l'oggetto corrente
        AscoltaMessaggi.setName("AscoltaMessaggi"); // assegna un nome al thread (opzionale)
        AscoltaMessaggi.start(); // avvia il thread
        Invia(MessageHeader.RichiestaConnessione(giocatore.getNomeGiocatore(), giocatore.getNomeSquadra()));
        // Invia richiesta di connessione al server
    }

    private boolean PartitaTerminata = false;  // Flag per sapere se la partita è terminata

    // Metodo che ascolta i messaggi in arrivo dal server
    @Override
    public void run() {
        try {
            String Riga;
            while ((Riga = in.readLine()) != null) {  // Legge una riga dal server
                GestioneMessaggi(Riga);  // Gestisce il messaggio ricevuto
            }
        } catch (Exception e) {
            System.out.println("Connessione chiusa: " + e.getMessage());  // Gestisce la chiusura della connessione
        } finally {
            // Notifica la UI solo se la partita non è già stata terminata normalmente
            if (!PartitaTerminata && UI != null) {
                UI.OnPartitaInterrotta("Connessione persa con il server.");
                // Avvisa l'utente che la partita è stata interrotta
            }
        }
    }

    // Metodo per inviare il messaggio che il giocatore è pronto
    public void InviaGiocatorePronto() {
        Invia(MessageHeader.GiocatorePronto(giocatore.getCodiceGiocatore()));  // Invia il messaggio di giocatore pronto
    }

    // Metodo per inviare il messaggio di giocata di una carta
    public void InviaGiocaCarta(Carta carta) {
        Invia(MessageHeader.GiocaCarta(giocatore.getCodiceGiocatore(), carta.toString()));  // Invia la carta giocata
    }

    // Metodo per inviare un messaggio al server
    private synchronized void Invia(MessageHeader Message) {
        if (out != null) out.println(Message.toString());  // Invia il messaggio se il writer è disponibile
    }

    // Metodo per gestire i messaggi ricevuti dal server
    private void GestioneMessaggi(String Riga) {
        MessageHeader Message = MessageHeader.ParseMessage(Riga);  // Parsing del messaggio ricevuto

        switch (Message.getIDMessaggio()) {  // Gestisce il messaggio in base al suo ID
            case "19":
                OnConfermaConnessione(Message.getCampi());
                break;
            case "10":
                OnEsitoConnessione(Message.getCampi());
                break;
            case "04":
                OnInizioPartita(Message.getCampi());
                break;
            case "64":
                OnCarteDistribuite(Message.getCampi());
                break;
            case "34":
                OnNuovoTurno(Message.getCampi());
                break;
            case "67":
                OnCartaAccettata(Message.getCampi());
                break;
            case "14":
                OnEsitoGiocata(Message.getCampi());
                break;
            case "92":
                OnPresaEffettuata(Message.getCampi());
                break;
            case "52":
                OnCartaPescata(Message.getCampi());
                break;
            case "12":
                OnFineSmazzata(Message.getCampi());
                break;
            case "56":
                OnRisultati(Message.getCampi());
                break;
            case "99":
                OnPartitaInterrotta(Message.getCampi());
                break;
        }
    }

    // Metodo che gestisce la conferma di connessione ricevuta
    private void OnConfermaConnessione(String[] Campi) {
        giocatore.setCodiceGiocatore(Campi[0]);  // Imposta il codice del giocatore
        if (UI != null) UI.OnConnessioneCompletata(giocatore);  // Aggiorna la UI
    }

    // Metodo che gestisce l'esito della connessione
    private void OnEsitoConnessione(String[] Campi) {
        if (!"0".equals(Campi[0]) && UI != null)
            UI.OnErrore("Connessione rifiutata.");  // Notifica un errore nella connessione
    }

    // Metodo che gestisce l'inizio della partita
    private void OnInizioPartita(String[] Campi) {
        // Estrae i dati relativi all'inizio della partita
        String CartaBriscola   = Campi[0];  // la carta di briscola
        String GiocatoreIniziale  = Campi[4];

        partita.ImpostaInizio(CartaBriscola, GiocatoreIniziale);  // Imposta l'inizio della partita
        partita.setCartaBriscola(CartaBriscola);  // Imposta la carta di briscola

        // Imposta le carte del giocatore
        giocatore.setCarte(Carta.FromString(Campi[1]), Carta.FromString(Campi[2]), 
                Carta.FromString(Campi[3]));

        if (UI != null) UI.OnInizioPartita(CartaBriscola, GiocatoreIniziale);  // Aggiorna la UI
        if (UI != null) UI.AggiornaMano(giocatore);  // Aggiorna le carte del giocatore nella UI
    }

    // Metodo che gestisce la distribuzione delle carte
    private void OnCarteDistribuite(String[] Campi) {
        // Imposta le carte del giocatore
        giocatore.setCarte(Carta.FromString(Campi[0]), Carta.FromString(Campi[1]), 
                Carta.FromString(Campi[2]));
        if (UI != null) UI.AggiornaMano(giocatore);  // Aggiorna le carte nella UI
    }

    // Metodo che gestisce l'inizio di un nuovo turno
    private void OnNuovoTurno(String[] Campi) {
        partita.ImpostaTurno(Campi[0]);  // Imposta il nuovo turno
        MioTurno = Campi[0].equals(giocatore.getCodiceGiocatore());  // Verifica se è il turno 
                                                                    //del giocatore
        if (UI != null) UI.OnNuovoTurno(Campi[0], MioTurno);  // Aggiorna la UI
    }

    // Metodo che gestisce l'accettazione di una carta
    private void OnCartaAccettata(String[] Campi) {
        partita.AggiungiCartaTavolo(Campi[0], Campi[1]);  // Aggiunge la carta al tavolo
        if (Campi[0].equals(giocatore.getCodiceGiocatore()))  // Rimuove la carta giocata dal giocatore
            giocatore.RimuoviCarta(Campi[1]);
        if (UI != null) UI.AggiornaTavolo(partita);  // Aggiorna il tavolo nella UI
    }

    // Metodo che gestisce l'esito di una giocata
    private void OnEsitoGiocata(String[] Campi) {
        if (!"0".equals(Campi[0]) && UI != null)
            UI.OnErrore("Mossa non valida. Riprova.");  // Notifica un errore nella giocata
    }

    // Metodo che gestisce l'esito di una presa effettuata
    private void OnPresaEffettuata(String[] Campi) {
        int PunteggioA = Integer.parseInt(Campi[1]);
        int PunteggioB = Integer.parseInt(Campi[2]);
        partita.AggiornaPunteggi(PunteggioA, PunteggioB);  // Aggiorna i punteggi
        partita.RegistraPresa();  // Registra la presa effettuata
        if (UI != null)
            UI.OnPresaEffettuata(Campi[0], String.valueOf(PunteggioA + PunteggioB), PunteggioA, partita);
            // Aggiorna la UI con l'esito della presa
    }

    // Metodo che gestisce la pesca di una carta
    private void OnCartaPescata(String[] Campi) {
        if (Campi[0].equals(giocatore.getCodiceGiocatore()))  // Se è il giocatore che pesca la carta
            giocatore.AggiungiCarta(Carta.FromString(Campi[1]));  // Aggiunge la carta alla mano
        if (UI != null) UI.AggiornaMano(giocatore);  // Aggiorna le carte nella UI
    }

    // Metodo che gestisce la fine di una smazzata
    private void OnFineSmazzata(String[] Campi) {
        partita.AggiornaPunteggi(Integer.parseInt(Campi[0]), Integer.parseInt(Campi[1]));  // Aggiorna i punteggi
        if (UI != null)
            UI.OnFineSmazzata(partita.getSquadraA().getPunteggio(), partita.getSquadraB().getPunteggio());
            // Aggiorna la UI con i punteggi finali
    }

    // Metodo che gestisce i risultati finali
    private void OnRisultati(String[] Campi) {
        PartitaTerminata = true;  // Imposta che la partita è terminata
        if (UI != null) UI.OnRisultati(Campi[0]);  // Mostra i risultati finali nella UI
    }

    // Metodo che gestisce l'interruzione della partita
    private void OnPartitaInterrotta(String[] Campi) {
        PartitaTerminata = true;  // Imposta che la partita è stata interrotta
        String Esito;
        if (Campi.length > 0) {
            Esito = Campi[0];  // Motivo dell'interruzione
        } else {
            Esito = "Un giocatore si è disconnesso.";  // Motivo di default
        }
        if (UI != null) UI.OnPartitaInterrotta(Esito);  // Notifica l'interruzione nella UI
    }

    // Metodi getter per ottenere informazioni sul giocatore e la partita
    public Giocatore getGiocatore() {
        return giocatore;
    }
    public Partita getPartita() {
        return partita;
    }
    public boolean isMioTurno() {
        return MioTurno;
    }
}
