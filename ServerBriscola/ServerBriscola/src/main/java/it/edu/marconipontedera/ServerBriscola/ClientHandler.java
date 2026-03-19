package it.edu.marconipontedera.ServerBriscola;
// Indica il package (cartella logica) in cui si trova questa classe

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

// ClientHandler: gestisce la comunicazione con un giocatore collegato al server.

public class ClientHandler implements Runnable {

    private Socket socket; // Socket di connessione con il client
    private BufferedReader in; // Lettura dei messaggi in ingresso dal client
    private PrintWriter out; // Scrittura dei messaggi in uscita verso il client
    private Giocatore giocatore; // Riferimento al giocatore associato a questo client
    private int IDGiocatore; // ID del giocatore
    private boolean connesso = true; // Stato della connessione con il client

    // Metodo che ritorna lo stato di connessione
    public synchronized boolean isConnesso() {
        return connesso;
    }

    // Metodo privato per settare lo stato della connessione
    private synchronized void setConnesso(boolean Connesso) {
        connesso = Connesso;
    }

    // Costruttore che inizializza il socket, l'ID del giocatore e l'oggetto Giocatore
    public ClientHandler(Socket socket, int IDGiocatore, Giocatore giocatore) {
        this.socket = socket;
        this.IDGiocatore = IDGiocatore;
        this.giocatore = giocatore;
    }

    // Metodo che esegue il ciclo di lettura dei messaggi dal client
    @Override
    public void run() {
        try {
            // Inizializzo gli stream di comunicazione
            in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            String Messaggio;
            // Legge i messaggi dal client finché la connessione è aperta
            while ((Messaggio = in.readLine()) != null) {
                ElaboraMessaggio(Messaggio); // Elabora ogni messaggio ricevuto
            }

        } catch (Exception e) {
            // Se c'è un errore durante la lettura o il client si disconnette
            System.out.println("Giocatore " + IDGiocatore + " disconnesso: " + e.getMessage());
        } finally {
            // Alla fine della comunicazione, setto la connessione come chiusa
            setConnesso(false);
            try {
                socket.close();
            } catch (Exception ignored) {} // Chiudo il socket
            ServerBriscola.GiocatoreDisconnesso(IDGiocatore); // Notifico il server che il giocatore è disconnesso
        }
    }

    // Metodo per elaborare il messaggio ricevuto
    private void ElaboraMessaggio(String Messaggio) {
        MessageHeader Message = MessageHeader.ParseMessage(Messaggio); // Parsing del messaggio

        // Gestione dei vari messaggi in base all'ID
        switch (Message.getIDMessaggio()) {
            case "79":
                RichiestaConnessione(Message.getCampi()); // Gestisce la richiesta di connessione
                break;
            case "09":
                GiocatorePronto(Message.getCampi()); // Gestisce quando il giocatore è pronto
                break;
            case "63":
                GiocaCarta(Message.getCampi()); // Gestisce il giocatore che gioca una carta
                break;
            default:
                System.out.println("Messaggio sconosciuto: " + Messaggio); // Messaggio non riconosciuto
        }
    }

    // Metodo per gestire la richiesta di connessione del giocatore
    private void RichiestaConnessione(String[] Campi) {
        String NomeGiocatore = Campi[0]; // Nome del giocatore
        String NomeSquadra; // Nome della squadra
        if (Campi.length > 1) {
            NomeSquadra = Campi[1]; // Nome della squadra (se presente)
        } else {
            NomeSquadra = "Sconosciuta"; // Se non presente, la squadra è sconosciuta
        }

        // Aggiorno il nome del giocatore e della squadra
        giocatore.setNomeGiocatore(NomeGiocatore);
        giocatore.setNomeSquadra(NomeSquadra);

        System.out.println("Connesso: " + NomeGiocatore + " [" + NomeSquadra + "] CodiceGiocatore=" 
                + giocatore.getCodiceGiocatore());

        // Invio al client la conferma della connessione con il codice del giocatore
        out.println(MessageHeader.ConfermaConnessione(giocatore.getCodiceGiocatore(), 
                NomeSquadra).toString());
    }

    // Metodo che gestisce la situazione in cui il giocatore si è dichiarato pronto
    private void GiocatorePronto(String[] Campi) {
        System.out.println("Pronto: " + giocatore.getCodiceGiocatore());
        ServerBriscola.VerificaInizioPartita(IDGiocatore); // Verifica se la partita può iniziare
    }

    // Metodo che gestisce il giocatore che gioca una carta
    private void GiocaCarta(String[] Campi) {
        if (Campi.length < 2) return; // Se non ci sono abbastanza dati, esco

        String ValoreSeme = Campi[1]; // Valore e seme della carta giocata
        System.out.println(giocatore.getCodiceGiocatore() + " gioca: " + ValoreSeme);

        // Recupera la partita corrente dal server
        Partita partita = ServerBriscola.getPartita();
        if (partita != null)
            partita.GiocaCarta(IDGiocatore, ValoreSeme); // Fa giocare la carta nella partita
    }

    // Getter per l'oggetto PrintWriter (per inviare messaggi al client)
    public synchronized PrintWriter getOut() {
        return out;
    }

    // Getter per l'oggetto Giocatore
    public Giocatore getGiocatore() {
        return giocatore;
    }

    // Getter per l'ID del giocatore
    public int getIDGiocatore() {
        return IDGiocatore;
    }

    // Invia un messaggio al client
    public synchronized void invia(MessageHeader Message) {
        if (out != null && isConnesso()) {
            out.println(Message.toString()); // Invia il messaggio al client
        }
    }
}
