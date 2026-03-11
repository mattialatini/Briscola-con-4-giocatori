// Importazione delle librerie necessarie per il funzionamento dell'interfaccia grafica
package it.edu.marconipontedera.ClientBriscola.GUI;

import it.edu.marconipontedera.ClientBriscola.*;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AttesaController implements ClientUI {

    // Dichiarazione delle variabili di classe
    private final Stage  stage; // La finestra principale dell'applicazione
    private final Client client; // Oggetto client che gestisce la connessione
    private Label  Stato; // Etichetta per mostrare lo stato
    private Button BtnPronto; // Bottone per dichiarare la prontezza

    // Costruttore per inizializzare la finestra e il client
    public AttesaController(Stage stage, Client client) {
        this.stage  = stage; // Imposta la finestra principale
        this.client = client; // Imposta il client
    }

    // Metodo per mostrare la scena
    private void Mostra() {
        Platform.runLater(() -> stage.setScene(CreaScena())); // Cambia la scena in modo sicuro per il thread grafico
    }

    // Metodo per creare la scena principale
    private Scene CreaScena() {
        // Titolo che informa l'utente che è connesso e sta aspettando gli altri giocatori
        Text Titolo = new Text("Connesso! In attesa degli altri giocatori…");
        Titolo.getStyleClass().add("attesa-title"); // Aggiunge una classe CSS per lo stile

        // Crea una casella per visualizzare le informazioni sul giocatore
        HBox Card = new HBox(20, CardGiocatore(client.getGiocatore()));
        Card.setAlignment(Pos.CENTER); // Allinea la casella al centro

        // Etichetta che descrive lo stato attuale
        Stato = new Label("Premi il bottone quando sei pronto a iniziare.");
        Stato.getStyleClass().add("attesa-testo"); // Aggiunge uno stile CSS

        // Bottone per dichiarare che il giocatore è pronto
        BtnPronto = new Button("Sono pronto!");
        BtnPronto.getStyleClass().add("btn-pronto"); // Aggiunge uno stile CSS al bottone
        BtnPronto.setOnAction(e -> {
            // Azione che viene eseguita quando il bottone viene premuto
            BtnPronto.setDisable(true); // Disabilita il bottone
            Stato.setText("In attesa degli altri giocatori…"); // Cambia il testo dello stato
            client.InviaGiocatorePronto(); // Invia il segnale al server che il giocatore è pronto
        });

        // Pannello verticale che contiene il titolo, la card del giocatore, lo stato e il bottone
        VBox panel = new VBox(20, Titolo, Card, Stato, BtnPronto);
        panel.setAlignment(Pos.CENTER); // Allinea gli elementi al centro
        panel.getStyleClass().add("attesa-panel"); // Aggiunge uno stile CSS al pannello
        panel.setMaxWidth(500); // Imposta una larghezza massima

        // Contenitore principale della scena
        StackPane Root = new StackPane(panel);
        Root.getStyleClass().add("attesa-root"); // Aggiunge uno stile CSS al contenitore
        Root.setPadding(new Insets(50)); // Aggiunge del padding intorno al contenuto

        // Crea la scena e applica il file CSS
        Scene scene = new Scene(Root);
        scene.getStylesheets().add(
            getClass().getResource("/resources/GUIBriscola.css").toExternalForm()); // Aggiunge il foglio di stile
        return scene; // Ritorna la scena creata
    }

    // Metodo per creare la card con le informazioni del giocatore
    private VBox CardGiocatore(Giocatore giocatore) {
        // Etichette con il nome del giocatore, il nome della squadra e il codice del giocatore
        Label NomeGiocatore = new Label(giocatore.getNomeGiocatore());
        Label NomeSquadra = new Label(giocatore.getNomeSquadra());
        String CodiceGiocatore;
        if (giocatore.getCodiceGiocatore() != null) {
            CodiceGiocatore = giocatore.getCodiceGiocatore(); // Se il codice è presente, lo usa
        } else {
            CodiceGiocatore = "—"; // Se il codice non è presente, mostra un trattino
        }
        Label codice  = new Label("Codice: " + CodiceGiocatore);

        // Aggiunge classi CSS per lo stile
        NomeGiocatore.getStyleClass().add("player-name-label");
        NomeSquadra.getStyleClass().add("player-squad-label");
        codice.getStyleClass().add("player-code-label");

        // Crea una VBox con le etichette del giocatore
        VBox card = new VBox(6, NomeGiocatore, NomeSquadra, codice);
        card.getStyleClass().add("player-card"); // Aggiunge uno stile CSS alla card
        card.setMinWidth(150); // Imposta una larghezza minima per la card
        return card; // Ritorna la card
    }

    // Metodo che viene chiamato quando la connessione è completata
    @Override
    public void OnConnessioneCompletata(Giocatore giocatore) { 
        Mostra(); // Mostra la scena di attesa
    }

    // Metodo che viene chiamato quando la partita inizia
    @Override
    public void OnInizioPartita(String SemeBriscola, String CodiceGiocatoreIniziale) {
        Platform.runLater(() -> {
            // Crea e mostra la scena per la partita
            PartitaController pc = new PartitaController(stage, client);
            client.setUI(pc); // Imposta l'interfaccia utente della partita
            pc.Mostra(); // Mostra la scena della partita
            pc.OnInizioPartita(SemeBriscola, CodiceGiocatoreIniziale); // Avvia la partita con i dati iniziali
        });
    }

    // Metodi non implementati che potrebbero essere usati in altre parti del gioco
    @Override public void OnNuovoTurno(String CodiceGiocatore, boolean MioTurno) {}
    @Override public void AggiornaMano(Giocatore giocatore) {}
    @Override public void AggiornaTavolo(Partita partita) {}
    @Override public void OnPresaEffettuata(String CodiceGiocatore, String ValoreSeme, 
            int Punteggio, Partita partita) {}
    @Override public void OnFineSmazzata(int PunteggioA, int PunteggioB) {}
    @Override public void OnRisultati(String NomeSquadra) {}

    // Metodo che gestisce gli errori
    @Override
    public void OnErrore(String Messaggio) {
        Platform.runLater(() -> {
            // Mostra il messaggio di errore sullo schermo
            if (Stato != null) Stato.setText("ERRORE: " + Messaggio);
        });
    }

    // Metodo che gestisce la fine della partita in caso di interruzione
    @Override
    public void OnPartitaInterrotta(String Esito) {
        Platform.runLater(() -> {
            // Mostra il messaggio di interruzione della partita
            if (Stato != null) Stato.setText("PARTITA INTERROTTA: " + Esito);
            if (BtnPronto != null) BtnPronto.setDisable(true); // Disabilita il bottone "Sono pronto"
        });
    }
}