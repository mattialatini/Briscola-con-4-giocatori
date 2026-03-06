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

// Schermata di attesa: il giocatore aspetta che tutti siano pronti
public class AttesaController implements ClientUI {

    // Finestra principale dell'applicazione
    private final Stage  stage;

    // Oggetto client che gestisce la comunicazione con il server
    private final Client client;

    // Messaggio di stato mostrato all'utente
    private Label  Stato;

    // Bottone che permette al giocatore di segnalare che è pronto
    private Button BtnPronto;

    // Costruttore della classe
    public AttesaController(Stage stage, Client client) {
        this.stage = stage;
        this.client = client;
    }

    // Mostra questa schermata nella finestra (chiamato dal thread di ascolto)
    private void Mostra() {

        // Platform.runLater serve per aggiornare la GUI nel thread grafico di JavaFX
        Platform.runLater(() -> stage.setScene(CreaScena()));
    }

    // Costruisce la scena grafica
    private Scene CreaScena() {

        // Titolo della schermata
        Text Titolo = new Text("Connesso! In attesa degli altri giocatori…");

        // Applica lo stile CSS
        Titolo.getStyleClass().add("attesa-title");

        // Card con le informazioni del giocatore
        HBox Card = new HBox(20, CardGiocatore(client.getGiocatore()));

        // Allinea al centro
        Card.setAlignment(Pos.CENTER);

        // Messaggio informativo per il giocatore
        Stato = new Label("Premi il bottone quando sei pronto a iniziare.");

        // Applica stile CSS
        Stato.getStyleClass().add("attesa-testo");

        // Bottone per segnalare che il giocatore è pronto
        BtnPronto = new Button("Sono pronto!");

        // Applica stile CSS
        BtnPronto.getStyleClass().add("btn-pronto");

        // Azione eseguita quando si preme il bottone
        BtnPronto.setOnAction(e -> {

            // Disabilita il bottone per evitare di premere più volte
            BtnPronto.setDisable(true);

            // Cambia il messaggio di stato
            Stato.setText("In attesa degli altri giocatori…");

            // Informa il server che questo giocatore è pronto
            client.InviaGiocatorePronto();
        });

        // Contenitore verticale con tutti gli elementi della schermata
        VBox panel = new VBox(20, Titolo, Card, Stato, BtnPronto);

        // Allineamento al centro
        panel.setAlignment(Pos.CENTER);

        // Applica stile CSS
        panel.getStyleClass().add("attesa-panel");

        // Larghezza massima del pannello
        panel.setMaxWidth(500);

        // Contenitore principale
        StackPane Root = new StackPane(panel);

        // Applica stile CSS
        Root.getStyleClass().add("attesa-root");

        // Margine interno
        Root.setPadding(new Insets(50));

        // Creazione della scena con dimensioni della finestra
        Scene scene = new Scene(Root, 580, 400);

        // Carica il file CSS per lo stile grafico
        scene.getStylesheets().add(getClass().getResource("/resources/GUIBriscola.css").toExternalForm());

        return scene;
    }

    // Crea il riquadro con nome, squadra e codice del giocatore
    private VBox CardGiocatore(Giocatore giocatore) {

        // Nome del giocatore
        Label nome = new Label(giocatore.getNomeGiocatore());

        // Nome della squadra
        Label squadra = new Label(giocatore.getNomeSquadra());

        // Variabile per il codice giocatore
        String CodiceGiocatore;

        // Controlla se il codice giocatore esiste
        if (giocatore.getCodiceGiocatore() != null) {
            CodiceGiocatore = giocatore.getCodiceGiocatore();
        } else {
            CodiceGiocatore = "—";
        }

        // Etichetta che mostra il codice
        Label codice  = new Label("Codice: " + CodiceGiocatore);

        // Aggiunge le classi CSS per lo stile
        nome.getStyleClass().add("player-name-label");
        squadra.getStyleClass().add("player-squad-label");
        codice.getStyleClass().add("player-code-label");

        // Card verticale con tutte le informazioni
        VBox card = new VBox(6, nome, squadra, codice);

        // Classe CSS della card
        card.getStyleClass().add("player-card");

        // Larghezza minima della card
        card.setMinWidth(150);

        return card;
    }

    // ClientUI

    @Override
    public void OnConnessioneCompletata(Giocatore giocatore) {

        // Mostra la schermata di attesa appena la connessione è completata
        Mostra();
    }

    @Override
    public void OnInizioPartita(String SemeBriscola, String CodiceGiocatoreIniziale) {

        // Passiamo alla schermata di gioco
        Platform.runLater(() -> {

            // Crea il controller della partita
            PartitaController partitacontroller = new PartitaController(stage, client);

            // Imposta la nuova interfaccia grafica nel client
            client.setUI(partitacontroller);

            // Mostra la schermata di gioco
            partitacontroller.Mostra();

            // Avvia la partita passando il seme e il primo giocatore
            partitacontroller.OnInizioPartita(SemeBriscola, CodiceGiocatoreIniziale);
        });
    }

    // Metodi dell'interfaccia ClientUI non utilizzati in questa schermata

    @Override 
    public void OnNuovoTurno(String CodiceGiocatore, boolean MioTurno) {}

    @Override 
    public void AggiornaMano(Giocatore giocatore) {}

    @Override 
    public void AggiornaTavolo(Partita partita) {}

    @Override 
    public void OnPresaEffettuata(String CodiceGiocatore, String ValoreSeme, int Punteggio, 
            Partita partita) {}

    @Override 
    public void OnFineSmazzata(int PunteggioA, int PunteggioB) {}

    @Override 
    public void OnRisultati(String nomeSquadra) {}

    @Override
    public void OnErrore(String Messaggio) {

        // Mostra il messaggio di errore nella GUI
        Platform.runLater(() -> {

            // Se la label esiste aggiorna il testo
            if (Stato != null)
                Stato.setText("ERRORE: " + Messaggio);
        });
    }
}