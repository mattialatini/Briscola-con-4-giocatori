package it.edu.marconipontedera.ServerBriscola;

// Import delle classi necessarie per la rete e per scrivere nei socket
import java.net.ServerSocket;
import java.net.Socket;
import java.io.PrintWriter;

// Classe principale del server della Briscola
// Il server aspetta 4 giocatori e poi fa partire la partita
public class ServerBriscola {

    // Porta su cui il server rimane in ascolto
    private static final int Port = 1234;

    // Array che contiene i gestori dei client (uno per ogni giocatore)
    // Indici da 0 a 3
    private static ClientHandler[] clienthandlers = new ClientHandler[4];

    // Array che contiene gli oggetti Giocatore collegati ai client
    private static Giocatore[] giocatori = new Giocatore[4];

    // Oggetto che rappresenta la partita in corso
    private static Partita partita;

    // Conta quanti giocatori hanno premuto il pulsante "Sono pronto"
    private static int Pronti = 0;

    // Conta quanti client si sono collegati al server
    private static int Connessi = 0;

    // Metodo principale che avvia il server
    public static void main(String[] args) {
        try {
            // Creazione del ServerSocket sulla porta indicata
            ServerSocket serverSocket = new ServerSocket(Port);

            // Messaggio stampato sul terminale del server
            System.out.println("Server in ascolto sulla porta " + Port);

            // Creazione della partita
            partita = new Partita();

            // Ciclo che aspetta la connessione di 4 giocatori
            while (Connessi < 4) {

                // Il server rimane in attesa finché un client non si connette
                Socket socket = serverSocket.accept();

                // Stream di uscita per mandare messaggi al client
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                // Creiamo un giocatore temporaneo
                // Il nome vero verrà poi inviato dal client
                giocatori[Connessi] = new Giocatore("G" + Connessi, "G" + Connessi, out);

                // Creiamo il gestore del client
                ClientHandler clienthandler = new ClientHandler(socket, Connessi, giocatori[Connessi]);

                // Salviamo il gestore nell'array
                clienthandlers[Connessi] = clienthandler;

                // Avviamo il thread che gestirà la comunicazione con questo client
                new Thread(clienthandler, "Client-" + Connessi).start();

                // Messaggio nel terminale del server che indica chi si è connesso
                System.out.println("Giocatore " + Connessi + " connesso (" + (Connessi + 1) + "/4)");

                // Incrementiamo il numero di giocatori connessi
                Connessi++;
            }

            // Messaggio quando tutti i giocatori sono collegati
            System.out.println("Tutti connessi. In attesa che siano pronti...");

        } catch (Exception e) {
            // Se succede un errore viene stampato nello stack trace
            e.printStackTrace();
        }
    }

    // Metodo chiamato quando un giocatore preme "Sono pronto"
    // Quando tutti e 4 sono pronti la partita può iniziare
    public static synchronized void VerificaInizioPartita() {

        // Aumenta il numero di giocatori pronti
        Pronti++;

        // Stampa quanti giocatori sono pronti
        System.out.println("Pronti: " + Pronti + "/4");

        // Se tutti e 4 sono pronti
        if (Pronti == 4) {

            // Viene creata una nuova partita
            partita = new Partita();

            // La partita viene avviata
            partita.iniziaPartita();
        }
    }

    // Metodi getter usati da altre classi del server

    // Restituisce l'array dei giocatori
    public static Giocatore[] getGiocatori() {
        return giocatori;
    }

    // Restituisce l'array dei gestori dei client
    public static ClientHandler[] getClienthandlers() {
        return clienthandlers;
    }

    // Restituisce la partita corrente
    public static Partita getPartita() { 
        return partita;
    }

    // Restituisce i ClientHandler della Squadra A
    // Squadra A = giocatori 0 e 2
    public static ClientHandler[] getTeamA() { 
        return new ClientHandler[]{clienthandlers[0], clienthandlers[2]};
    }

    // Restituisce i ClientHandler della Squadra B
    // Squadra B = giocatori 1 e 3
    public static ClientHandler[] getTeamB() { 
        return new ClientHandler[]{clienthandlers[1], clienthandlers[3]};
    }
}