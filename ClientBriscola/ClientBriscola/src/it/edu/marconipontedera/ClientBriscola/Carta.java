package it.edu.marconipontedera.ClientBriscola; 
// Indica il package (cartella logica) in cui si trova questa classe

// Rappresenta una carta da gioco sul client
public class Carta {

    private String Seme;   
    // Variabile che memorizza il Seme della carta (es. "Denari", "Coppe", "Spade", "Bastoni")

    private String Valore; 
    // Variabile che memorizza il Valore della carta (es. "Asso", "Tre", "Re")

    public Carta(String Seme, String Valore) {
        // Costruttore della classe Carta: crea una carta con Seme e Valore

        this.Seme   = Seme;   
        // Assegna il Seme ricevuto alla variabile della classe

        this.Valore = Valore; 
        // Assegna il Valore ricevuto alla variabile della classe
    }

    // Costruisce una Carta dalla stringa "Valore,Seme" usata nei messaggi
    public static Carta FromString(String ValoreSeme) {
        // Metodo statico che crea una Carta partendo da una stringa ricevuta nei messaggi

        String[] parti = ValoreSeme.split(","); 
        // Divide la stringa in due parti usando "," come separatore

        if (parti.length == 2) return new Carta(parti[1], parti[0]); 
        // Se la stringa è corretta (due parti), crea una nuova Carta
        // parti[0] = Valore
        // parti[1] = Seme

        return new Carta("?", ValoreSeme); 
        // Se il formato è sbagliato, crea una carta con Seme sconosciuto
    }

    public String getSeme() { 
        return Seme; 
    }
    // Metodo getter che restituisce il Seme della carta

    public String getValore(){ 
        return Valore; 
    }
    // Metodo getter che restituisce il Valore della carta

    // Formato usato nei messaggi: "Valore,Seme"
    @Override
    public String toString() {
        // Metodo che converte la carta in stringa

        return Valore + "," + Seme;
        // Restituisce la carta nel formato "Valore,Seme"
        // Esempio: "Asso,Denari"
    }
}