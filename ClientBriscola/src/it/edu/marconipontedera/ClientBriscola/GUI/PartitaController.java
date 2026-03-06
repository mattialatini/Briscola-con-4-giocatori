package it.edu.marconipontedera.ClientBriscola.GUI;

import it.edu.marconipontedera.ClientBriscola.*;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

// Schermata principale di gioco: mostra tavolo, mano del giocatore e log
public class PartitaController implements ClientUI {

    private final Stage  stage;
    private final Client client;

    // Etichette header
    private Label LblBriscola, LblTurno, LblPunteggi;

    // Contenitori grafici
    private HBox    Tavolo;         // carte sul tavolo
    private HBox    Mano;           // carte in mano al giocatore
    private VBox    PanelGiocatore; // pannello del giocatore (si evidenzia quando è il suo turno)
    private TextArea Log;           // registro degli eventi
    private StackPane Root;         // contenitore principale (serve per i popup)

    public PartitaController(Stage stage, Client client) {
        this.stage  = stage;
        this.client = client;
    }

    // Mostra la schermata di gioco
    public void Mostra() {
        Platform.runLater(() -> {
            stage.setScene(CreaScena());
            stage.setWidth(1000);
            stage.setHeight(600);
            stage.centerOnScreen();
        });
    }

    // Costruisce l'intera interfaccia di gioco
    private Scene CreaScena() {

        // Header
        LblBriscola = new Label("Briscola: —");
        LblBriscola.getStyleClass().add("briscola-label");

        LblTurno = new Label("Turno: —");
        LblTurno.getStyleClass().add("turno-label");

        LblPunteggi = new Label("A: 0  |  B: 0");
        LblPunteggi.getStyleClass().add("punteggio-label");

        Region sp1 = new Region(); HBox.setHgrow(sp1, Priority.ALWAYS);
        Region sp2 = new Region(); HBox.setHgrow(sp2, Priority.ALWAYS);

        HBox header = new HBox(16, LblBriscola, sp1, LblTurno, sp2, LblPunteggi);
        header.getStyleClass().add("header-bar");
        header.setAlignment(Pos.CENTER_LEFT);

        // Tavolo
        Tavolo = new HBox(12);
        Tavolo.setAlignment(Pos.CENTER);

        VBox TavoloWrap = new VBox(6,
            new Label("Tavolo") {{ getStyleClass().add("tavolo-title"); }},
            Tavolo);
        TavoloWrap.setAlignment(Pos.CENTER);
        TavoloWrap.getStyleClass().add("tavolo-area");
        TavoloWrap.setPrefSize(420, 200);

        // Log
        Log = new TextArea();
        Log.setEditable(false);
        Log.setWrapText(true);
        Log.setPrefWidth(230);
        Log.getStyleClass().add("log-area");
        VBox.setVgrow(Log, Priority.ALWAYS);

        VBox logPanel = new VBox(4,
            new Label("Log") {{ setStyle("-fx-text-fill:#c8a84b88; -fx-font-size:11px;"); }},
            Log);
        logPanel.setPadding(new Insets(8));

        HBox centro = new HBox(10, TavoloWrap, logPanel);
        centro.setAlignment(Pos.CENTER);
        centro.setPadding(new Insets(10, 14, 4, 14));
        HBox.setHgrow(logPanel, Priority.ALWAYS);

        // Mano del giocatore
        Label NomeLabel = new Label(
            client.getGiocatore().getNomeGiocatore() + "  [" + client.getGiocatore().getNomeSquadra() + "]");
        NomeLabel.getStyleClass().add("player-panel-name");

        Mano = new HBox(10);
        Mano.setAlignment(Pos.CENTER_LEFT);

        PanelGiocatore = new VBox(6, NomeLabel, Mano);
        PanelGiocatore.getStyleClass().add("player-panel");
        PanelGiocatore.setPadding(new Insets(10, 14, 10, 14));

        VBox Mani = new VBox(8, PanelGiocatore);
        Mani.setPadding(new Insets(4, 14, 12, 14));

        // Layout principale
        VBox Layout = new VBox(0, header, centro, Mani);
        VBox.setVgrow(centro, Priority.ALWAYS);

        Root = new StackPane(Layout);
        Root.getStyleClass().add("partita-root");

        Scene scene = new Scene(Root, 1000, 600);
        scene.getStylesheets().add(getClass().getResource("/resources/GUIBriscola.css").toExternalForm());
        return scene;
    }

    // Mostra le carte del giocatore. Se abilitata=true il giocatore può cliccarle
    private void AggiornaCarte(Giocatore giocatore, HBox box, boolean Abilitata) {
        box.getChildren().clear();
        Carta[] ListaCarte = giocatore.getCarte();
        for (int i = 0; i < ListaCarte.length; i++) {
            Carta carta = ListaCarte[i];
            if (carta == null) continue;
            CartaView cartaview = new CartaView(carta);
            cartaview.setDisable(!Abilitata);
            if (Abilitata) {
                cartaview.setOnAction(e -> {
                    // Disabilita tutte le carte per evitare doppi click
                    for (int j = 0; j < box.getChildren().size(); j++) {
                        box.getChildren().get(j).setDisable(true);
                    }
                    client.InviaGiocaCarta(carta);
                    Scrivi("GIOCA: " + giocatore.getNomeGiocatore() + " → " + carta);
                });
            }
            box.getChildren().add(cartaview);
        }
        // Se non ci sono carte mostriamo un trattino
        if (box.getChildren().isEmpty())
            box.getChildren().add(new Label("—") {{ setStyle("-fx-text-fill:#666;"); }});
    }

    // Aggiorna le carte visibili sul tavolo
    private void AggiornaTavoloCarte(Partita partita) {
        Tavolo.getChildren().clear();
        for (int i = 0; i < partita.getCarteGiocate(); i++) {
            CartaTavoloView cartatavoloview = new CartaTavoloView(partita.getCarteTavolo()[i], 
                    partita.getGiocatoriTavolo()[i]);
            cartatavoloview.setOpacity(0);
            Tavolo.getChildren().add(cartatavoloview);
            // Animazione di comparsa
            FadeTransition ft = new FadeTransition(Duration.millis(300), cartatavoloview);
            ft.setToValue(1);
            ft.play();
        }
    }

    // Evidenzia il pannello del giocatore quando è il suo turno
    private void EvidenziaTurno(boolean MioTurno) {
        PanelGiocatore.getStyleClass().remove("turno-attivo");
        if (MioTurno) PanelGiocatore.getStyleClass().add("turno-attivo");
    }

    // Aggiunge una riga al log
    private void Scrivi(String Riga) {
        Platform.runLater(() -> Log.appendText(Riga + "\n"));
    }

    // Mostra un messaggio temporaneo al centro dello schermo (scompare dopo 3 secondi)
    private void Popup(String Titolo, String Riga) {
        Platform.runLater(() -> {
            Text t1 = new Text(Titolo);
            t1.getStyleClass().add("notifica-titolo");
            Label t2 = new Label(Riga);
            t2.getStyleClass().add("notifica-testo");
            t2.setMaxWidth(360);
            VBox box = new VBox(12, t1, t2);
            box.setAlignment(Pos.CENTER);
            box.getStyleClass().add("notifica-box");
            box.setMaxWidth(420);
            StackPane overlay = new StackPane(box);
            overlay.getStyleClass().add("notifica-overlay");
            Root.getChildren().add(overlay);
            PauseTransition pt = new PauseTransition(Duration.seconds(3));
            pt.setOnFinished(e -> Root.getChildren().remove(overlay));
            pt.play();
        });
    }

    // ClientUI

    @Override 
    public void OnConnessioneCompletata(Giocatore giocatore) {}

    @Override
    public void OnInizioPartita(String SemeBriscola, String CodiceGiocatoreIniziale) {
        Platform.runLater(() -> {
            LblBriscola.setText("Briscola: " + SemeBriscola);
            LblTurno.setText("Turno: " + CodiceGiocatoreIniziale);
            Scrivi("PARTITA INIZIATA — Briscola: " + SemeBriscola);
        });
        Popup("Partita iniziata!", "Briscola: " + SemeBriscola + "\nPrimo turno: " + CodiceGiocatoreIniziale);
    }

    @Override
    public void AggiornaMano(Giocatore giocatore) {
        // Mostriamo le carte ma non ancora cliccabili (le abiliterà OnNuovoTurno)
        Platform.runLater(() -> AggiornaCarte(giocatore, Mano, false));
    }

    @Override
    public void AggiornaTavolo(Partita partita) {
        Platform.runLater(() -> AggiornaTavoloCarte(partita));
    }

    @Override
    public void OnNuovoTurno(String CodiceGiocatore, boolean MioTurno) {
        Platform.runLater(() -> {
            LblTurno.setText("Turno: " + CodiceGiocatore);
            EvidenziaTurno(MioTurno);
            Scrivi("Turno di: " + CodiceGiocatore);
            // Abilitiamo le carte solo se tocca a noi
            AggiornaCarte(client.getGiocatore(), Mano, MioTurno);
            if (MioTurno)
                Scrivi("Tocca a te, " + client.getGiocatore().getNomeGiocatore() + "!");
        });
    }

    @Override
    public void OnPresaEffettuata(String CodiceGiocatore, String ValoreSeme, int Punteggio, Partita partita) {
        Platform.runLater(() -> {
            LblPunteggi.setText("A: " + partita.getSquadraA().getPunteggio() + "  ;  B: " 
                    + partita.getSquadraB().getPunteggio());
            Tavolo.getChildren().clear();
            Scrivi("Presa di " + CodiceGiocatore + " [" + Punteggio + " pt]");
        });
    }

    @Override
    public void OnFineSmazzata(int PunteggioA, int PunteggioB) {
        Platform.runLater(() -> {
            LblPunteggi.setText("A: " + PunteggioA + "  ;  B: " + PunteggioB);
            Scrivi("FINE SMAZZATA  A:" + PunteggioA + "  B:" + PunteggioB);
        });
    }

    @Override
    public void OnRisultati(String NomeSquadra) {
        Scrivi("VINCITORE: " + NomeSquadra);
        Platform.runLater(() ->
            new FinePartitaController(stage, NomeSquadra,
                client.getPartita().getSquadraA().getPunteggio(),
                client.getPartita().getSquadraB().getPunteggio()).Mostra()
        );
    }

    @Override
    public void OnErrore(String Messaggio) {
        Scrivi("ERRORE: " + Messaggio);
        Popup("Errore", Messaggio);
    }
}
