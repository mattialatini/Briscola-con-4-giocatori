package it.edu.marconipontedera.ClientBriscola;
// Indica il package (cartella logica) in cui si trova questa classe

// Importa il controller per la schermata di login
import it.edu.marconipontedera.ClientBriscola.GUI.LoginController;
// Importa la libreria per l'applicazione JavaFX
import javafx.application.Application;
// Importa la classe Stage di JavaFX, che rappresenta la finestra principale
import javafx.stage.Stage;

public class ClientBriscola extends Application {

    @Override
    public void start(Stage stage) {
        // Imposta il titolo della finestra
        stage.setTitle("Briscola a Coppie");
        // Imposta la finestra come ridimensionabile
        stage.setResizable(true);

        // Crea la scena di login e la imposta come scena principale
        stage.setScene(new LoginController(stage).CreaScena());
        // Mostra la finestra
        stage.show();
    }

    public static void main(String[] args) {
        // Avvia l'applicazione JavaFX
        launch(args);
    }
}