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

// Prima schermata del gioco:
// qui il giocatore inserisce il nome, la squadra e l'indirizzo del server
public class LoginController {

    // Stage principale della finestra
    private final Stage stage;

    // Costruttore: riceve lo stage principale
    public LoginController(Stage stage) {
        this.stage = stage;
    }

    // Metodo che costruisce e restituisce la scena grafica del login
    public Scene CreaScena() {

        // Titolo principale della schermata
        Text Titolo = new Text("La Briscola");
        Titolo.getStyleClass().add("login-title"); // stile CSS

        // Sottotitolo sotto il titolo
        Text Sottotitolo = new Text("4 giocatori a coppie");
        Sottotitolo.getStyleClass().add("login-subtitle"); // stile CSS

        // Etichetta della sezione giocatore
        Label LblGiocatore = label("Il tuo Giocatore");

        // Campo dove il giocatore inserisce il proprio nome
        TextField CampoNome = Campo("es. Mario");

        // Campo dove il giocatore inserisce la squadra
        TextField CampoSquadra = Campo("es. Squadra A");

        // Etichetta della sezione server
        Label LblServer = label("Server");

        // Campo dove si inserisce l'indirizzo del server
        TextField CampoHost = Campo("localhost");

        // Messaggio di errore o informazione (inizialmente vuoto)
        Label Errore = new Label("");
        Errore.getStyleClass().add("error-label");

        // Bottone per connettersi al server
        Button BtnConnetti = new Button("CONNETTITI");
        BtnConnetti.getStyleClass().add("btn-primary");

        // Azione eseguita quando si preme il bottone
        BtnConnetti.setOnAction(e -> {

            // Controlla se i campi nome o squadra sono vuoti
            if (CampoNome.getText().isBlank() || CampoSquadra.getText().isBlank()) {
                Errore.setText("Compila tutti i campi.");
                return;
            }

            // Messaggio temporaneo durante la connessione
            Errore.setText("Connessione in corso...");
            BtnConnetti.setDisable(true); // disabilita il bottone

            // Crea il client con i dati inseriti
            Client client = new Client(
                CampoNome.getText().trim(),
                CampoSquadra.getText().trim(),
                CampoHost.getText().trim()
            );

            // Crea la schermata di attesa
            AttesaController Attesa = new AttesaController(stage, client);

            // Imposta la schermata di attesa come interfaccia grafica del client
            client.setUI(Attesa);

            // La connessione viene fatta in un thread separato
            // per non bloccare l'interfaccia grafica
            new Thread(() -> {
                try {

                    // Prova a connettersi al server
                    client.Connetti();

                } catch (Exception ex) {

                    // Se succede un errore aggiorna la GUI
                    Platform.runLater(() -> {
                        Errore.setText("ERRORE: " + ex.getMessage());
                        BtnConnetti.setDisable(false); // riattiva il bottone
                    });
                }
            }).start();
        });

        // Layout principale verticale con tutti gli elementi
        VBox panel = new VBox(10,

            // Titolo e sottotitolo
            new VBox(4, Titolo, Sottotitolo),

            // Sezione giocatore
            LblGiocatore,
            Riga(CampoNome, "Nome:", CampoSquadra, "Squadra:"),

            // Separatore grafico
            new Separator(),

            // Sezione server
            LblServer,
            Riga(CampoHost, "Host:", null, null),

            // Messaggio di errore
            Errore,

            // Bottone connessione
            BtnConnetti
        );

        // Allineamento del pannello
        panel.setAlignment(Pos.CENTER_LEFT);

        // Applica stile CSS
        panel.getStyleClass().add("login-panel");

        // Larghezza massima del pannello
        panel.setMaxWidth(460);

        // Root della scena
        StackPane Root = new StackPane(panel);
        Root.getStyleClass().add("login-root");
        Root.setPadding(new Insets(40)); // spazio interno

        // Creazione della scena
        Scene scene = new Scene(Root, 580, 420);

        // Collegamento del file CSS
        scene.getStylesheets().add(getClass().getResource("/resources/GUIBriscola.css").toExternalForm());

        // Restituisce la scena
        return scene;
    }

    // Metodo che crea una riga con etichetta + campo di testo
    // opzionalmente può avere una seconda coppia etichetta + campo
    private HBox Riga(TextField f1, String l1, TextField f2, String l2) {

        // Prima etichetta + campo
        HBox box = new HBox(8, etichetta(l1), f1);

        // Se esiste un secondo campo lo aggiunge
        if (f2 != null) box.getChildren().addAll(etichetta(l2), f2);

        // Allineamento
        box.setAlignment(Pos.CENTER_LEFT);

        return box;
    }

    // Metodo che crea un campo di testo con testo suggerito
    private TextField Campo(String prompt) {

        TextField tf = new TextField();

        // testo suggerito nel campo
        tf.setPromptText(prompt);

        // applica stile CSS
        tf.getStyleClass().add("input-field");

        return tf;
    }

    // Metodo che crea una label di sezione (titolo sezione)
    private Label label(String testo) {

        Label l = new Label(testo);

        // stile CSS
        l.getStyleClass().add("section-label");

        return l;
    }

    // Metodo che crea una piccola etichetta vicino a un campo
    private Label etichetta(String testo) {

        Label l = new Label(testo);

        // stile CSS
        l.getStyleClass().add("field-label");

        return l;
    }
}