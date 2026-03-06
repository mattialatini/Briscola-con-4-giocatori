package it.edu.marconipontedera.ServerBriscola;

// Import delle classi necessarie per gestire input/output e connessione di rete
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

// ClientHandler: classe che gestisce la comunicazione con un giocatore collegato al server
public class ClientHandler implements Runnable {

    // Socket che rappresenta la connessione tra server e client
    private Socket socket;

    // Oggetto per leggere i messaggi che arrivano dal client
    private BufferedReader in;

    // Oggetto per inviare messaggi al client
    private PrintWriter out;

    // Oggetto che rappresenta il giocatore collegato
    private Giocatore giocatore;

    // Indice del giocatore (da 0 a 3 nella partita)
    private int IDGiocatore;

    // Costruttore della classe: inizializza socket, IDGiocatore e giocatore
    public ClientHandler(Socket socket, int IDGiocatore, Giocatore giocatore) {
        this.socket = socket;
        this.IDGiocatore = IDGiocatore;
        this.giocatore = giocatore;
    }

    // Metodo eseguito quando il thread parte
    @Override
    public void run() {
        try {
            // Creiamo il lettore per ricevere messaggi dal client
            in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Creiamo il writer per inviare messaggi al client
            out = new PrintWriter(socket.getOutputStream(), true);

            // Variabile che conterrà i messaggi ricevuti
            String Messaggio;

            // Continua a leggere messaggi finché il client rimane connesso
            while ((Messaggio = in.readLine()) != null) {

                // Ogni Messaggio ricevuto viene elaborato
                ElaboraMessaggio(Messaggio);
            }

        } catch (Exception e) {

            // Se c'è un errore probabilmente il client si è disconnesso
            System.out.println("Giocatore " + IDGiocatore + " disconnesso.");

        } finally {
            try {
                // Chiudiamo la connessione
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Metodo che analizza il Messaggio ricevuto e decide cosa fare
    private void ElaboraMessaggio(String messaggio) {

        // Converte la stringa ricevuta in un oggetto MessageHeader
        MessageHeader Message = MessageHeader.parseMessage(messaggio);

        // In base al tipo di Messaggio esegue un'azione diversa
        switch (Message.getIDMessaggio()) {

            // 79 = richiesta di connessione al server
            case "79":
                richiestaConnessione(Message.getCampi());
                break;

            // 09 = il giocatore ha premuto "sono pronto"
            case "09":
                giocatorePronto(Message.getCampi());
                break;

            // 63 = il giocatore gioca una carta
            case "63":
                giocaCarta(Message.getCampi());
                break;

            // Se il codice Messaggio non è riconosciuto
            default:
                System.out.println("Messaggio sconosciuto: " + messaggio);
        }
    }

    // Metodo chiamato quando il client chiede di connettersi
    private void richiestaConnessione(String[] Campi) {

        // Campi[0] contiene il nome del giocatore
        String NomeGiocatore = Campi[0];

        // Campi[1] contiene la squadra del giocatore
        String NomeSquadra = Campi[1];

        // Salviamo il codice del giocatore
        giocatore.setCodiceGiocatore(NomeGiocatore);

        // Salviamo il nome del giocatore
        giocatore.setNomeGiocatore(NomeGiocatore);

        // Stampiamo nel server chi si è connesso
        System.out.println("Giocatore " + IDGiocatore + ": " + NomeGiocatore + " [" + NomeSquadra + "]");

        // Inviamo al client il Messaggio di conferma connessione
        out.println(MessageHeader.ConfermaConnessione(NomeGiocatore, NomeSquadra).toString());
    }

    // Metodo chiamato quando il giocatore preme "sono pronto"
    private void giocatorePronto(String[] Campi) {

        // Stampiamo nel server che il giocatore è pronto
        System.out.println("Pronto: " + giocatore.getCodiceGiocatore());

        // Controlliamo se tutti i giocatori sono pronti per iniziare la partita
        ServerBriscola.VerificaInizioPartita();
    }

    // Metodo chiamato quando il giocatore gioca una carta
    private void giocaCarta(String[] Campi) {

        // Campi[0] = codice giocatore
        // Campi[1] = carta giocata
        String ValoreSeme = Campi[1];

        // Stampiamo nel server quale carta è stata giocata
        System.out.println(giocatore.getCodiceGiocatore() + " gioca: " + ValoreSeme);

        // Otteniamo la partita attuale dal server
        Partita partita = ServerBriscola.getPartita();

        // Se la partita esiste, facciamo giocare la carta
        if (partita != null)
            partita.GiocaCarta(IDGiocatore, ValoreSeme);
    }

    // Metodo getter per ottenere il writer (serve per inviare messaggi al client)
    public PrintWriter getOut() {
        return out;
    }

    // Metodo getter per ottenere il giocatore associato a questo client
    public Giocatore getGiocatore() {
        return giocatore;
    }

    // Metodo getter per ottenere l'IDGiocatore del giocatore
    public int getId() {
        return IDGiocatore;
    }
}