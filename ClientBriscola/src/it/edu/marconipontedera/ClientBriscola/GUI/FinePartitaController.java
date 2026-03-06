package it.edu.marconipontedera.ClientBriscola.GUI;

// Import delle classi JavaFX usate per layout, controlli e grafica
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

// Classe che gestisce la schermata finale della partita
// Mostra il vincitore e i punteggi delle due squadre
public class FinePartitaController {

    // Finestra principale dell'applicazione
    private final Stage  stage;

    // Nome della squadra vincitrice
    private final String NomeSquadra;

    // Punteggi delle due squadre
    private final int PunteggioA;
    private final int PunteggioB;

    // Costruttore della classe
    // Riceve la finestra principale, la squadra vincitrice e i punteggi
    public FinePartitaController(Stage stage, String NomeSquadra, int PunteggioA, int PunteggioB) {
        this.stage    = stage;
        this.NomeSquadra = NomeSquadra;
        this.PunteggioA = PunteggioA; 
        this.PunteggioB = PunteggioB;
    }

    // Metodo pubblico che Mostra la schermata finale
    public void Mostra() { 
        stage.setScene(CreaScena()); // imposta la nuova scena nello stage
    }

    // Metodo privato che costruisce tutta la scena grafica
    private Scene CreaScena() {

        // Testo che rappresenta il trofeo (decorativo)
        Text trofeo = new Text("***"); 
        trofeo.setStyle("-fx-font-size:60px; -fx-fill: #c8a84b;"); // stile del trofeo

        // Titolo principale della schermata
        Text titolo = new Text("FINE PARTITA");
        titolo.getStyleClass().add("fine-titolo"); // classe CSS per lo stile

        // Label che Mostra quale squadra ha vinto
        Label vincLabel = new Label("Ha vinto: " + NomeSquadra);
        vincLabel.getStyleClass().add("fine-vincitore"); // stile CSS

        // Label con il punteggio della squadra A
        Label ptA = new Label("Squadra A:  " + PunteggioA + " punti");
        ptA.getStyleClass().addAll("fine-punteggio", "fine-sqA");

        // Label con il punteggio della squadra B
        Label ptB = new Label("Squadra B:  " + PunteggioB + " punti");
        ptB.getStyleClass().addAll("fine-punteggio", "fine-sqB");

        // VBox che contiene i due punteggi
        VBox punteggiBox = new VBox(8, ptA, ptB); // 8 = spazio tra gli elementi
        punteggiBox.getStyleClass().add("fine-punteggio-box");
        punteggiBox.setAlignment(Pos.CENTER_LEFT); // allineamento a sinistra

        // Pulsante per chiudere la finestra
        Button btnChiudi = new Button("Chiudi");
        btnChiudi.getStyleClass().add("btn-ricomincia"); // stile CSS

        // Azione del bottone: chiude la finestra quando premuto
        btnChiudi.setOnAction(e -> stage.close());

        // Pannello principale verticale che contiene tutti gli elementi
        VBox panel = new VBox(18, trofeo, titolo, vincLabel, punteggiBox, btnChiudi);
        panel.setAlignment(Pos.CENTER); // elementi centrati
        panel.getStyleClass().add("fine-panel"); // classe CSS
        panel.setMaxWidth(440); // larghezza massima del pannello

        // StackPane usato come contenitore principale
        StackPane root = new StackPane(panel);
        root.getStyleClass().add("fine-root"); // stile CSS
        root.setPadding(new Insets(60)); // spazio interno dai bordi

        // Creazione della scena con dimensioni della finestra
        Scene scene = new Scene(root, 680, 520);

        // Caricamento del file CSS per lo stile grafico
        scene.getStylesheets().add(
            getClass().getResource("/resources/GUIBriscola.css").toExternalForm());

        // Ritorna la scena creata
        return scene;
    }
}