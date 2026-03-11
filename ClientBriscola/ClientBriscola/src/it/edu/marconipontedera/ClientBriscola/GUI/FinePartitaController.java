package it.edu.marconipontedera.ClientBriscola.GUI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

// Questa classe gestisce la schermata di fine partita del gioco Briscola
public class FinePartitaController {

    // Dichiarazione delle variabili principali della classe
    private final Stage  stage;       // La finestra principale dove mostrare la scena
    private final String NomeSquadra; // Nome della squadra vincente
    private final int    PunteggioA;  // Punteggio della squadra A
    private final int    PunteggioB;  // Punteggio della squadra B

    // Costruttore della classe, inizializza le variabili con i valori passati
    public FinePartitaController(Stage stage, String NomeSquadra, int PunteggioA, int PunteggioB) {
        this.stage       = stage;
        this.NomeSquadra = NomeSquadra;
        this.PunteggioA  = PunteggioA;
        this.PunteggioB  = PunteggioB;
    }

    // Metodo per mostrare la scena di fine partita
    public void MostraScena() { 
        stage.setScene(CreaScena()); // Imposta la scena creata nel metodo CreaScena
    }

    // Metodo privato che costruisce e restituisce la scena completa
    private Scene CreaScena() {
        // Icona del trofeo
        Text trofeo = new Text("🏆");
        trofeo.setStyle("-fx-font-size:72px;"); // Imposta la dimensione del trofeo

        // Titolo della schermata
        Text titolo = new Text("FINE PARTITA");
        titolo.getStyleClass().add("fine-titolo"); // Aggiunge una classe CSS per lo stile

        // Label che mostra la squadra vincente
        Label vincLabel = new Label("Ha vinto: " + NomeSquadra);
        vincLabel.getStyleClass().add("fine-vincitore"); // Stile CSS per il vincitore

        // Label che mostra il punteggio della squadra A
        Label ptA = new Label("Squadra A:  " + PunteggioA + " punti");
        ptA.getStyleClass().addAll("fine-punteggio", "fine-sqA"); // Stile CSS multiplo

        // Label che mostra il punteggio della squadra B
        Label ptB = new Label("Squadra B:  " + PunteggioB + " punti");
        ptB.getStyleClass().addAll("fine-punteggio", "fine-sqB"); // Stile CSS multiplo

        // Box verticale per contenere i punteggi delle squadre
        VBox punteggiBox = new VBox(8, ptA, ptB); // 8 = spazio tra i componenti
        punteggiBox.getStyleClass().add("fine-punteggio-box"); // Stile CSS
        punteggiBox.setAlignment(Pos.CENTER); // Centra i contenuti

        // Bottone per chiudere la finestra
        Button btnChiudi = new Button("Chiudi");
        btnChiudi.getStyleClass().add("btn-primary"); // Stile CSS per il bottone
        btnChiudi.setOnAction(e -> stage.close()); // Azione: chiude la finestra quando cliccato

        // Pannello principale verticale con tutti i componenti
        VBox panel = new VBox(20, trofeo, titolo, vincLabel, punteggiBox, btnChiudi); // 20 = spazio tra componenti
        panel.setAlignment(Pos.CENTER); // Centra tutti i componenti
        panel.getStyleClass().add("fine-panel"); // Stile CSS del pannello
        panel.setMaxWidth(500); // Imposta larghezza massima del pannello

        // Root della scena, contenitore principale
        StackPane root = new StackPane(panel);
        root.getStyleClass().add("fine-root"); // Stile CSS del root
        root.setPadding(new Insets(60)); // Padding interno di 60px

        // Creazione della scena vera e propria
        Scene scene = new Scene(root); // Crea la scena con il root
        scene.getStylesheets().add(
            getClass().getResource("/resources/GUIBriscola.css").toExternalForm()); // Aggiunge il file CSS
        stage.setMaximized(true); // Massimizza la finestra
        return scene; // Restituisce la scena pronta
    }
}