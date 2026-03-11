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

/**
 * Controller della schermata principale di gioco.
 * Gestisce la visualizzazione della mano, del tavolo, della briscola, dei punteggi e dei log.
 */
public class PartitaController implements ClientUI {

    private final Stage  stage;    // Finestra principale
    private final Client client;   // Riferimento al client

    // Etichette per informazioni principali
    private Label LblBriscola, LblTurno, LblPunteggi;
    private CartaTavoloView BriscolaCardView; // Visualizzazione della carta briscola sul tavolo
    private HBox    Tavolo;                  // Contenitore delle carte sul tavolo
    private HBox    Mano;                    // Contenitore della mano del giocatore
    private VBox    PanelGiocatore;          // Pannello della mano del giocatore
    private TextArea Log;                     // Area di log delle azioni
    private StackPane Root;                   // Contenitore principale

    public PartitaController(Stage stage, Client client) {
        this.stage  = stage;
        this.client = client;
    }

    // Mostra la schermata di gioco
    public void Mostra() {
        Platform.runLater(() -> {
            stage.setScene(CreaScena());
            stage.setMaximized(true); // Massimizza la finestra
        });
    }

    // Crea la scena principale con layout e componenti
    private Scene CreaScena() {

        // Header
        LblBriscola = new Label("Briscola: —");          // Etichetta briscola
        LblBriscola.getStyleClass().add("briscola-label");

        LblTurno = new Label("Turno: —");               // Etichetta turno
        LblTurno.getStyleClass().add("turno-label");

        LblPunteggi = new Label("A: 0  |  B: 0");      // Etichetta punteggi
        LblPunteggi.getStyleClass().add("punteggio-label");

        // Spazi flessibili per layout
        Region sp1 = new Region(); HBox.setHgrow(sp1, Priority.ALWAYS);
        Region sp2 = new Region(); HBox.setHgrow(sp2, Priority.ALWAYS);

        // Contenitore header
        HBox header = new HBox(16, LblBriscola, sp1, LblTurno, sp2, LblPunteggi);
        header.getStyleClass().add("header-bar");
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 16, 0, 16));

        // Tavolo
        Tavolo = new HBox(12);
        Tavolo.setAlignment(Pos.CENTER);

        // Carta briscola separata sul lato
        Label briscolaLabel = new Label("Briscola");
        briscolaLabel.setStyle("-fx-text-fill:#e8c87b; -fx-font-size:11px; -fx-font-weight:bold;");
        BriscolaCardView = new CartaTavoloView(null, null);
        BriscolaCardView.setRotate(90);
        BriscolaCardView.setOpacity(0.85);
        VBox briscolaBox = new VBox(4, briscolaLabel, BriscolaCardView);
        briscolaBox.setAlignment(Pos.CENTER);

        Label tavoloTitolo = new Label("Tavolo");      // Titolo tavolo
        tavoloTitolo.getStyleClass().add("tavolo-title");

        // Layout tavolo + briscola
        HBox tavoloContent = new HBox(18, Tavolo, briscolaBox);
        tavoloContent.setAlignment(Pos.CENTER);
        HBox.setHgrow(Tavolo, Priority.ALWAYS);

        VBox TavoloWrap = new VBox(8, tavoloTitolo, tavoloContent);
        TavoloWrap.setAlignment(Pos.CENTER);
        TavoloWrap.getStyleClass().add("tavolo-area");
        VBox.setVgrow(TavoloWrap, Priority.ALWAYS);
        HBox.setHgrow(TavoloWrap, Priority.ALWAYS);

        // Log
        Log = new TextArea();
        Log.setEditable(false);           // Non modificabile
        Log.setWrapText(true);            // A capo automatico
        Log.setPrefWidth(280);
        Log.setMinWidth(220);
        Log.getStyleClass().add("log-area");
        VBox.setVgrow(Log, Priority.ALWAYS);

        Label logTitolo = new Label("Log");
        logTitolo.getStyleClass().add("log-title");

        VBox logPanel = new VBox(4, logTitolo, Log);
        logPanel.setPadding(new Insets(8));
        VBox.setVgrow(Log, Priority.ALWAYS);

        // Centro
        HBox centro = new HBox(10, TavoloWrap, logPanel);
        centro.setAlignment(Pos.CENTER);
        centro.setPadding(new Insets(10, 14, 4, 14));
        VBox.setVgrow(centro, Priority.ALWAYS);

        // Mano giocatore
        Label NomeLabel = new Label(
            client.getGiocatore().getNomeGiocatore()
            + "  [" + client.getGiocatore().getNomeSquadra() + "]");
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

        Root = new StackPane(Layout);      // Contenitore principale
        Root.getStyleClass().add("partita-root");

        Scene scene = new Scene(Root);
        scene.getStylesheets().add(
            getClass().getResource("/resources/GUIBriscola.css").toExternalForm());
        return scene;
    }

    // Aggiorna le carte nella mano del giocatore
    private void AggiornaCarte(Giocatore giocatore, HBox Box, boolean Abilitata) {
        Box.getChildren().clear();
        Carta[] ListaCarte = giocatore.getCarte();
        for (int i = 0; i < ListaCarte.length; i++) {
            Carta carta = ListaCarte[i];
            if (carta == null) continue;
            CartaView cv = new CartaView(carta);
            cv.setDisable(!Abilitata);
            if (Abilitata) {
                final Carta cartaFinale = carta;
                cv.setOnAction(e -> {
                    for (int j = 0; j < Box.getChildren().size(); j++) {
                        Box.getChildren().get(j).setDisable(true);
                    }
                    client.InviaGiocaCarta(cartaFinale);   // Invia carta giocata al server
                    Scrivi("GIOCA: " + giocatore.getNomeGiocatore() + " → " + cartaFinale);
                });
            }
            Box.getChildren().add(cv);
        }
        // Se non ci sono carte mostra un simbolo
        if (Box.getChildren().isEmpty()) {
            Box.getChildren().add(
                new Label("—") {{ getStyleClass().add("empty-hand-label"); }});
        }
    }

    // Aggiorna le carte sul tavolo
    private void AggiornaTavoloCarte(Partita partita) {
        Tavolo.getChildren().clear();
        for (int i = 0; i < partita.getCarteGiocate(); i++) {
            CartaTavoloView ctv = new CartaTavoloView(
                partita.getCarteTavolo()[i],
                partita.getGiocatoriTavolo()[i]);
            ctv.setOpacity(0);           // Effetto fade in
            Tavolo.getChildren().add(ctv);
            FadeTransition ft = new FadeTransition(Duration.millis(300), ctv);
            ft.setToValue(1); ft.play();
        }
    }

    // Evidenzia il turno del giocatore
    private void EvidenziaTurno(boolean MioTurno) {
        PanelGiocatore.getStyleClass().remove("turno-attivo");
        if (MioTurno) PanelGiocatore.getStyleClass().add("turno-attivo");
    }

    // Scrive un messaggio nel log
    private void Scrivi(String Riga) {
        Platform.runLater(() -> Log.appendText(Riga + "\n"));
    }

    // Mostra un popup temporaneo
    private void Popup(String Titolo, String Testo) {
        Platform.runLater(() -> {
            Text t1 = new Text(Titolo);
            t1.getStyleClass().add("notifica-titolo");
            Label t2 = new Label(Testo);
            t2.getStyleClass().add("notifica-testo");
            t2.setMaxWidth(400);
            t2.setWrapText(true);
            VBox box = new VBox(12, t1, t2);
            box.setAlignment(Pos.CENTER);
            box.getStyleClass().add("notifica-box");
            box.setMaxWidth(460);
            StackPane overlay = new StackPane(box);
            overlay.getStyleClass().add("notifica-overlay");
            Root.getChildren().add(overlay);
            PauseTransition pt = new PauseTransition(Duration.seconds(4));
            pt.setOnFinished(e -> Root.getChildren().remove(overlay)); // Scompare dopo 4 secondi
            pt.play();
        });
    }

    // ClientUI

    @Override public void OnConnessioneCompletata(Giocatore giocatore) {}

    // Inizio della partita
    @Override
    public void OnInizioPartita(String CartaBriscola, String CodiceGiocatoreIniziale) {
        String displayBriscola;
        if (CartaBriscola.contains(";")) {
            displayBriscola = CartaBriscola.replace(";", " di "); // Formatta carta
        } else {
            displayBriscola = CartaBriscola;
        }

        Platform.runLater(() -> {
            LblBriscola.setText("Briscola: " + displayBriscola);
            LblTurno.setText("Turno: " + CodiceGiocatoreIniziale);
            Scrivi("PARTITA INIZIATA — Briscola: " + displayBriscola);

            // Aggiorna la carta briscola sul tavolo
            if (BriscolaCardView != null) {
                VBox parent = (VBox) BriscolaCardView.getParent();
                int idx = parent.getChildren().indexOf(BriscolaCardView);
                CartaTavoloView nuova = new CartaTavoloView(CartaBriscola, null);
                nuova.setRotate(90);
                nuova.setOpacity(0.85);
                parent.getChildren().set(idx, nuova);
                BriscolaCardView = nuova;
            }
        });
        Popup("Partita iniziata!", "Briscola: " + displayBriscola
              + "\nPrimo turno: " + CodiceGiocatoreIniziale);
    }

    // Aggiorna la mano del giocatore
    @Override
    public void AggiornaMano(Giocatore giocatore) {
        Platform.runLater(() -> AggiornaCarte(giocatore, Mano, false));
    }

    // Aggiorna il tavolo di gioco
    @Override
    public void AggiornaTavolo(Partita partita) {
        Platform.runLater(() -> AggiornaTavoloCarte(partita));
    }

    // Nuovo turno
    @Override
    public void OnNuovoTurno(String CodiceGiocatore, boolean MioTurno) {
        Platform.runLater(() -> {
            LblTurno.setText("Turno: " + CodiceGiocatore);
            EvidenziaTurno(MioTurno);
            Scrivi("Turno di: " + CodiceGiocatore);
            AggiornaCarte(client.getGiocatore(), Mano, MioTurno);
            if (MioTurno)
                Scrivi("Tocca a te, " + client.getGiocatore().getNomeGiocatore() + "!");
        });
    }

    // Aggiorna punteggi dopo una presa
    @Override
    public void OnPresaEffettuata(String CodiceGiocatore, String ValoreSeme, int Punteggio, Partita partita) {
        Platform.runLater(() -> {
            LblPunteggi.setText("A: " + partita.getSquadraA().getPunteggio()
                              + "  |  B: " + partita.getSquadraB().getPunteggio());
            Tavolo.getChildren().clear(); // Svuota tavolo
            Scrivi("Presa di " + CodiceGiocatore + " [" + Punteggio + " pt totali squadra]");
        });
    }

    // Fine di una smazzata
    @Override
    public void OnFineSmazzata(int PunteggioA, int PunteggioB) {
        Platform.runLater(() -> {
            LblPunteggi.setText("A: " + PunteggioA + "  |  B: " + PunteggioB);
            Scrivi("FINE SMAZZATA  A:" + PunteggioA + "  B:" + PunteggioB);
        });
    }

    // Mostra il vincitore
    @Override
    public void OnRisultati(String NomeSquadra) {
        Scrivi("VINCITORE: " + NomeSquadra);
        Platform.runLater(() ->
            new FinePartitaController(stage, NomeSquadra,
                client.getPartita().getSquadraA().getPunteggio(),
                client.getPartita().getSquadraB().getPunteggio()).MostraScena()
        );
    }

    // Mostra un messaggio di errore
    @Override
    public void OnErrore(String Messaggio) {
        Scrivi("ERRORE: " + Messaggio);
        Popup("Errore", Messaggio);
    }

    // Partita interrotta
    @Override
    public void OnPartitaInterrotta(String Esito) {
        Platform.runLater(() -> {
            // Disabilita tutte le carte
            for (int i = 0; i < Mano.getChildren().size(); i++) {
                Mano.getChildren().get(i).setDisable(true);
            }
            Scrivi("⚠ PARTITA INTERROTTA: " + Esito);

            // Mostra popup permanente
            Text t1 = new Text("Partita Interrotta");
            t1.getStyleClass().add("notifica-titolo");
            Label t2 = new Label(Esito);
            t2.getStyleClass().add("notifica-testo");
            t2.setWrapText(true);
            t2.setMaxWidth(400);

            Button BtnChiudi = new Button("Chiudi");
            BtnChiudi.getStyleClass().add("btn-primary");

            VBox Box = new VBox(14, t1, t2, BtnChiudi);
            Box.setAlignment(Pos.CENTER);
            Box.getStyleClass().add("notifica-box");
            Box.setMaxWidth(460);

            StackPane Overlay = new StackPane(Box);
            Overlay.getStyleClass().add("notifica-overlay");
            Root.getChildren().add(Overlay);

            BtnChiudi.setOnAction(e -> stage.close()); // Chiude la finestra
        });
    }
}