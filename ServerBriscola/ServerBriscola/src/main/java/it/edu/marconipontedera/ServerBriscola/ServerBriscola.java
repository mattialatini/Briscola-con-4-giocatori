package it.edu.marconipontedera.ServerBriscola;
// Indica il package (cartella logica) in cui si trova questa classe

import java.net.ServerSocket;
import java.net.Socket;
import java.io.PrintWriter;

// Server principale della Briscola a coppie.
// Gestisce la connessione dei client, l'inizio della partita e la gestione delle disconnessioni.
public class ServerBriscola {

    private static final int Port = 1234; // Porta su cui il server ascolta

    // Oggetto lock dedicato per l'attesa del main thread
    private static final Object Lock = new Object();

    // Array per gestire i client e i giocatori
    private static ClientHandler[] ClientHandlers = new ClientHandler[4];
    private static Giocatore[] giocatori = new Giocatore[4];
    private static Partita partita = null;

    // Variabili per tenere traccia dei giocatori connessi e dei loro stati
    private static int Connessi = 0;
    private static boolean[] Pronti = new boolean[4]; // Stato dei giocatori (pronto o no)
    private static int CPronti = 0; // Conta i giocatori pronti
    private static boolean PartitaAvviata = false; // Flag per verificare se la partita è iniziata

    public static void main(String[] args) {
        try {
            // Crea un ServerSocket per ascoltare le connessioni in arrivo sulla porta 1234
            ServerSocket serverSocket = new ServerSocket(Port);
            System.out.println("Server Briscola in ascolto sulla porta " + Port);

            // Accetta le connessioni dei giocatori finché non sono tutti connessi
            while (Connessi < 4) {
                // Aspetta che un client si connetta
                Socket socket = serverSocket.accept();
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                // Crea un nuovo giocatore per ogni client connesso
                giocatori[Connessi] = new Giocatore("G" + Connessi, "G" + Connessi, out);
                ClientHandler clienthandler = new ClientHandler(socket, Connessi, giocatori[Connessi]);
                ClientHandlers[Connessi] = clienthandler;

                // Avvia un nuovo thread per gestire il client
                Thread t = new Thread(clienthandler, "Client-" + Connessi);
                t.start();
                System.out.println("Giocatore " + Connessi + " connesso (" + (Connessi + 1) + "/4)");
                Connessi++; // Incrementa il contatore dei giocatori connessi
            }

            // Una volta che tutti e 4 i giocatori sono connessi, aspetta che si dichiarino pronti
            System.out.println("Tutti e 4 i giocatori connessi. In attesa del 'Sono pronto'...");

            // Il main thread rimane in attesa passiva utilizzando il lock
            synchronized (Lock) {
                Lock.wait(); // Attende finché non arriva il segnale di inizio partita
            }

        } catch (Exception e) {
            e.printStackTrace(); // Gestisce eventuali eccezioni
        }
    }

    // Chiamato quando un giocatore preme "Sono pronto".
    // Quando tutti i giocatori sono pronti, la partita inizia.
    
    public static synchronized void VerificaInizioPartita(int IDGiocatore) {
        // Se la partita è già iniziata, non fa nulla
        if (PartitaAvviata)
            return;

        // Se il giocatore non è ancora segnato come pronto, lo marca come pronto
        if (!Pronti[IDGiocatore]) {
            Pronti[IDGiocatore] = true;
            CPronti++; // Incrementa il numero di giocatori pronti
        }

        // Stampa il numero di giocatori pronti
        System.out.println("Pronti: " + CPronti + "/4");

        // Quando tutti i giocatori sono pronti, inizia la partita
        if (CPronti == 4) {
            PartitaAvviata = true;
            partita = new Partita();
            partita.IniziaPartita(); // Inizia la partita
        }
    }

    // Chiamato quando un giocatore si disconnette.
    // Se la partita è in corso, la partita viene interrotta.
    
    public static synchronized void GiocatoreDisconnesso(int IDGiocatore) {
        String NomeGiocatore;
        if (giocatori[IDGiocatore] != null) {
            NomeGiocatore = giocatori[IDGiocatore].getNomeGiocatore(); // Ottiene il nome del giocatore
        } else {
            NomeGiocatore = "Giocatore " + IDGiocatore; // Se il giocatore è null, usa un nome generico
        }

        // Stampa la disconnessione del giocatore
        System.out.println("Disconnessione: " + NomeGiocatore + " (IDGiocatore=" + IDGiocatore + ")");

        // Se la partita è in corso, la interrompe
        if (partita != null) {
            partita.InterrompiPartita(NomeGiocatore);
        } else if (PartitaAvviata == false) {
            // Se la partita non è ancora iniziata, rimuove il giocatore
            ClientHandlers[IDGiocatore] = null;
            giocatori[IDGiocatore] = null;
        }
    }

    // Getter statici

    public static synchronized Giocatore[] getGiocatori(){
        return giocatori;
    } // Ritorna l'array dei giocatori
    public static synchronized ClientHandler[] getClientHandlers() {
        return ClientHandlers;
    } // Ritorna l'array dei clienthandler
    public static synchronized Partita getPartita() {
        return partita;
    } // Ritorna la partita in corso

    // Ritorna i client del Team A
    public static ClientHandler[] getTeamA() {
        return new ClientHandler[]{ ClientHandlers[0], ClientHandlers[2] };
    }

    // Ritorna i client del Team B
    public static ClientHandler[] getTeamB() {
        return new ClientHandler[]{ ClientHandlers[1], ClientHandlers[3] };
    }
}
