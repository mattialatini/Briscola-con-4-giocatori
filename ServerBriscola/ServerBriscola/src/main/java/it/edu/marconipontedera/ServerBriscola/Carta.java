package it.edu.marconipontedera.ServerBriscola;
// Indica il package (cartella logica) in cui si trova questa classe

public class Carta {
    private String Seme;  // Seme della carta (es. Denari, Coppe, ecc.)
    private String Valore; // Il Valore della carta (es. Asso, Tre, ecc.)
    private int Punteggio; // Punteggio associato alla carta (es. Asso = 11 Punti, Tre = 10 Punti, ecc.)

    // Costruttore per inizializzare una carta con Seme, Valore e Punteggio
    public Carta(String Seme, String Valore, int Punteggio) {
        this.Seme = Seme;
        this.Valore = Valore;
        this.Punteggio = Punteggio;
    }

    // Metodi getter e setter per accedere e modificare gli attributi
    public String getSeme() {
        return Seme;
    }

    public void setSeme(String Seme) {
        this.Seme = Seme;
    }

    public String getValore() {
        return Valore;
    }

    public void setValore(String Valore) {
        this.Valore = Valore;
    }

    public int getPunteggio() {
        return Punteggio;
    }

    public void setPunteggio(int Punteggio) {
        this.Punteggio = Punteggio;
    }
    
    @Override
    public String toString() {
        return Valore + "," + Seme;
    }
}