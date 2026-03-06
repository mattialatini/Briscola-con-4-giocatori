package it.edu.marconipontedera.ClientBriscola; 
// Definisce il package in cui si trova questa classe

import it.edu.marconipontedera.ClientBriscola.GUI.LoginController; 
// Importa la classe LoginController che gestisce la schermata di login

import javafx.application.Application; 
// Importa la classe Application di JavaFX, necessaria per creare applicazioni grafiche

import javafx.stage.Stage; 
// Importa la classe Stage che rappresenta la finestra principale dell'applicazione

// Punto di avvio dell'applicazione JavaFX
public class ClientBriscola extends Application {

    @Override
    // Metodo chiamato automaticamente quando l'applicazione JavaFX parte
    public void start(Stage stage) {

        stage.setTitle("Briscola a Coppie"); 
        // Imposta il titolo della finestra

        stage.setResizable(false); 
        // Impedisce all'utente di ridimensionare la finestra

        // Apre la schermata di login
        stage.setScene(new LoginController(stage).CreaScena()); 
        // Crea la scena usando il LoginController e la imposta nella finestra

        stage.show(); 
        // Mostra la finestra sullo schermo
    }

    public static void main(String[] args) { 
        // Metodo main: punto di avvio del programma Java
        launch(args); 
        // Avvia l'applicazione JavaFX
    }
}