package it.edu.marconipontedera.ClientBriscola.GUI;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

// Classe che rappresenta una carta piccola mostrata sul tavolo durante una mano.
// Questa carta non è cliccabile, serve solo per mostrare cosa è stato giocato.
public class CartaTavoloView extends VBox {

    // Costruttore della carta sul tavolo.
    // valoreSeme contiene informazioni nel formato "Valore;Seme" (es: "Asso;Cuori")
    // CodiceGiocatore indica il giocatore che ha giocato la carta
    public CartaTavoloView(String valoreSeme, String CodiceGiocatore) {

        // Aggiunge la classe CSS per lo stile della carta sul tavolo
        getStyleClass().add("carta-tavolo");

        // Imposta la dimensione della carta
        setPrefSize(62, 86);

        // Allinea il contenuto al centro
        setAlignment(Pos.CENTER);

        // Spazio tra gli elementi interni
        setSpacing(2);

        // Variabili per Valore e seme della carta.
        // Se qualcosa va storto verranno mostrati "?"
        String Valore = "?";
        String Seme = "?";

        // Controlla che la stringa non sia nulla e che contenga il separatore ";"
        if (valoreSeme != null && valoreSeme.contains(";")) {

            // Divide la stringa in due parti: Valore e seme
            String[] Parti = valoreSeme.split(";");

            // Prima parte = Valore della carta
            Valore = Parti[0];

            // Seconda parte = seme della carta
            Seme = Parti[1];
        }

        // Testo che mostra il Valore della carta (es: Asso, 7, Re...)
        Text tValore = new Text(Valore);

        // Imposta dimensione e grassetto del testo
        tValore.setStyle("-fx-font-size: 13px; -fx-font-weight: bold;");

        // Applica il colore del seme usando un metodo della classe CartaView
        tValore.getStyleClass().add(CartaView.ColoreSeme(Seme));

        // Testo che mostra il Simbolo del seme (D, C, B, S ecc.)
        Text tSimbolo = new Text(CartaView.Simbolo(Seme));

        // Imposta la dimensione del Simbolo del seme
        tSimbolo.setStyle("-fx-font-size: 20px;");

        // Nome del giocatore abbreviato che verrà mostrato sotto la carta
        String NomeBreve;

        // Controlla che il codice giocatore non sia null
        if (CodiceGiocatore != null) {

            // Prende solo i primi 6 caratteri del nome del giocatore
            // (se è più corto prende tutta la stringa)
            NomeBreve = CodiceGiocatore.substring(0, Math.min(6, CodiceGiocatore.length()));
        } else {

            // Se il codice giocatore è null lascia vuoto
            NomeBreve = "";
        }

        // Testo che mostra il nome abbreviato del giocatore
        Text tGioc = new Text(NomeBreve);

        // Imposta dimensione e colore del testo del giocatore
        tGioc.setStyle("-fx-font-size: 9px; -fx-fill: #888;");

        // Aggiunge tutti gli elementi grafici alla carta
        // ordine: Valore, Simbolo del seme, nome del giocatore
        getChildren().addAll(tValore, tSimbolo, tGioc);
    }
}