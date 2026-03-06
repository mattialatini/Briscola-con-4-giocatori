package it.edu.marconipontedera.ClientBriscola.GUI;

// Import della classe Carta che rappresenta una carta da gioco
import it.edu.marconipontedera.ClientBriscola.Carta;

// Import per gestire l'allineamento dei componenti
import javafx.geometry.Pos;

// Import del bottone JavaFX
import javafx.scene.control.Button;

// Import del layout verticale
import javafx.scene.layout.VBox;

// Import del testo JavaFX
import javafx.scene.text.Text;

// Classe che rappresenta graficamente una carta cliccabile
public class CartaView extends Button {

    // Oggetto carta associato a questo bottone
    private final Carta carta;

    // Costruttore che riceve una carta da rappresentare
    public CartaView(Carta carta) {

        // Salva la carta
        this.carta = carta;

        // Aggiunge la classe CSS per lo stile della carta
        getStyleClass().add("carta-btn");

        // Imposta la dimensione del bottone
        setPrefSize(80, 110);

        // Crea il Simbolo del seme (D, C, B, S)
        Text simbolo = new Text(Simbolo(carta.getSeme()));

        // Imposta la dimensione del Simbolo
        simbolo.setStyle("-fx-font-size: 28px;");

        // Testo che mostra il valore della carta (es. Asso, Re...)
        Text valore = new Text(carta.getValore());

        // Stile del valore (dimensione e grassetto)
        valore.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Aggiunge la classe CSS per colorare il testo in base al seme
        valore.getStyleClass().add(ColoreSeme(carta.getSeme()));

        // Testo che mostra il nome del seme (Denari, Coppe...)
        Text seme = new Text(carta.getSeme());

        // Imposta la dimensione del testo del seme
        seme.setStyle("-fx-font-size: 10px;");

        // Aggiunge la classe CSS per il colore del seme
        seme.getStyleClass().add(ColoreSeme(carta.getSeme()));

        // Crea un contenitore verticale con valore, Simbolo e seme
        VBox box = new VBox(2, valore, simbolo, seme);

        // Allinea i contenuti al centro
        box.setAlignment(Pos.CENTER);

        // Impedisce al mouse di interagire con i singoli elementi
        // così il click funziona solo sul bottone
        box.setMouseTransparent(true);

        // Imposta il contenuto grafico del bottone
        setGraphic(box);

        // Rimuove il testo del bottone
        setText(null);
    }

    // Metodo per ottenere la carta associata al bottone
    public Carta getCarta() { 
        return carta; 
    }

    // Metodo che restituisce il Simbolo del Seme
    public static String Simbolo(String Seme) {

        // Controlla il Seme della carta
        switch (Seme) {

            // Denari
            case "Denari":
                return "D";

            // Coppe
            case "Coppe":
                return "C";

            // Bastoni
            case "Bastoni":
                return "B";

            // Spade
            case "Spade":
                return "S";

            // Caso di sicurezza se il Seme non esiste
            default:
                return "?";
        }
    }

    // Metodo che restituisce la classe CSS per colorare il Seme
    public static String ColoreSeme(String Seme) {

        // Controlla il Seme
        switch (Seme) {

            // Classe CSS per Denari
            case "Denari":
                return "seme-denari";

            // Classe CSS per Coppe
            case "Coppe":
                return "seme-coppe";

            // Classe CSS per Bastoni
            case "Bastoni":
                return "seme-bastoni";

            // Classe CSS per Spade (default)
            default:
                return "seme-spade";
        }
    }
}