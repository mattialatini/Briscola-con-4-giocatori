package it.edu.marconipontedera.ClientBriscola.GUI;

import it.edu.marconipontedera.ClientBriscola.Client;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Prima schermata: il giocatore inserisce nome, squadra e indirizzo del server.
 */
public class LoginController {

    private final Stage stage; // La finestra principale (Stage) della GUI

    // Costruttore, inizializza il controller con lo Stage
    public LoginController(Stage stage) {
        this.stage = stage;
    }

    // Metodo che crea la scena della schermata di login
    public Scene CreaScena() {

        // Creazione dei titoli di testo
        Text Titolo = new Text("La Briscola");
        Titolo.getStyleClass().add("login-title"); // Aggiungi uno stile per il titolo

        Text Sottotitolo1 = new Text("4 giocatori a coppie");
        Sottotitolo1.getStyleClass().add("login-subtitle"); // Aggiungi uno stile per il sottotitolo
        
        Text Sottotitolo2 = new Text("Creato da Falzetta, Latini, Senzacqua");
        Sottotitolo2.getStyleClass().add("login-subtitle"); // Aggiungi uno stile per il sottotitolo

        // Etichette e campi di input per il nome del giocatore, la squadra e l'host del server
        Label LblGiocatore = Label("Il tuo Giocatore");
        TextField CampoNome    = Campo("es. Mario");
        TextField CampoSquadra = Campo("es. SquadraA");

        Label LblServer = Label("Server");
        TextField CampoHost = Campo("localhost");

        // Etichetta per eventuali errori
        Label Errore = new Label("");
        Errore.getStyleClass().add("error-label");

        // Bottone di connessione
        Button BtnConnetti = new Button("CONNETTITI");
        BtnConnetti.getStyleClass().add("btn-primary");

        // Gestione del clic sul bottone "Connetti"
        BtnConnetti.setOnAction(e -> {
            // Se uno dei campi è vuoto, mostra un errore
            if (CampoNome.getText().isBlank() || CampoSquadra.getText().isBlank()) {
                Errore.setText("Compila tutti i campi.");
                return;
            }
            Errore.setText("Connessione in corso...");
            BtnConnetti.setDisable(true); // Disabilita il pulsante mentre ci si connette

            // Crea un client con i dati inseriti
            Client client = new Client(
                CampoNome.getText().trim(),
                CampoSquadra.getText().trim(),
                CampoHost.getText().trim());

            // Crea una schermata di attesa
            AttesaController Attesa = new AttesaController(stage, client);
            client.setUI(Attesa);

            // Crea un nuovo thread per la connessione
            new Thread(() -> {
                try {
                    client.Connetti(); // Prova a connettersi al server
                } catch (Exception ex) {
                    Platform.runLater(() -> {
                        // Se c'è un errore, aggiorna l'interfaccia utente con il messaggio di errore
                        Errore.setText("ERRORE: " + ex.getMessage());
                        BtnConnetti.setDisable(false); // Riabilita il pulsante
                    });
                }
            }).start();
        });

        // Creazione del pannello verticale che contiene gli elementi della scena
        VBox panel = new VBox(14,
            new VBox(4, Titolo, Sottotitolo1, Sottotitolo2), // Titolo e sottotitolo
            LblGiocatore, // Etichetta per il nome del giocatore
            Riga(CampoNome, "Nome:", CampoSquadra, "Squadra:"), // Riga per il nome e la squadra
            new Separator(), // Separatore orizzontale
            LblServer, // Etichetta per l'host del server
            Riga(CampoHost, "Host:", null, null), // Riga per l'host
            Errore, // Etichetta per gli errori
            BtnConnetti); // Bottone per connettersi

        panel.setAlignment(Pos.CENTER_LEFT); // Allinea gli elementi a sinistra
        panel.getStyleClass().add("login-panel"); // Aggiungi uno stile al pannello
        panel.setMaxWidth(520); // Imposta la larghezza massima del pannello

        // Crea un StackPane come root per la scena
        StackPane Root = new StackPane(panel);
        Root.getStyleClass().add("login-root"); // Aggiungi uno stile per il root
        Root.setPadding(new Insets(40)); // Imposta un padding intorno al root

        // Crea la scena con il root e applica il foglio di stile
        Scene scene = new Scene(Root);
        scene.getStylesheets().add(
            getClass().getResource("/resources/GUIBriscola.css").toExternalForm());

        // Imposta la finestra come massimizzata
        stage.setMaximized(true);
        return scene; // Restituisci la scena
    }

    // Metodo che crea una riga con un campo di testo e la relativa etichetta
    private HBox Riga(TextField f1, String l1, TextField f2, String l2) {
        HBox box = new HBox(10, Etichetta(l1), f1); // Aggiungi il campo di testo con l'etichetta
        if (f2 != null) box.getChildren().addAll(Etichetta(l2), f2); // Se esiste un secondo campo, aggiungilo
        box.setAlignment(Pos.CENTER_LEFT); // Allinea gli elementi a sinistra
        return box;
    }

    // Metodo per creare un campo di testo con un prompt
    private TextField Campo(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt); // Imposta il testo di esempio
        tf.getStyleClass().add("input-field"); // Aggiungi uno stile al campo di testo
        return tf;
    }

    // Metodo per creare un'etichetta con uno stile
    private Label Label(String testo) {
        Label l = new Label(testo);
        l.getStyleClass().add("section-label"); // Aggiungi uno stile all'etichetta
        return l;
    }

    // Metodo per creare un'etichetta per il campo di testo
    private Label Etichetta(String testo) {
        Label l = new Label(testo);
        l.getStyleClass().add("field-label"); // Aggiungi uno stile all'etichetta
        return l;
    }
}