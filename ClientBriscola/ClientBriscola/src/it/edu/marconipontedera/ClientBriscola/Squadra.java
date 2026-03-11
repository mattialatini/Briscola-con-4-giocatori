package it.edu.marconipontedera.ClientBriscola; 
// Indica il package (cartella logica) in cui si trova questa classe

// Tiene il Punteggio di una squadra (aggiornato dai messaggi del server)
public class Squadra { 
// Definizione della classe Squadra, che rappresenta una squadra nel gioco

    private String NomeSquadra; 
    // Variabile privata che memorizza il NomeSquadra della squadra

    private int Punteggio = 0; 
    // Variabile privata che memorizza il Punteggio della squadra, inizialmente 0

    public Squadra(String NomeSquadra) {
        this.NomeSquadra = NomeSquadra; 
    } 
    // Costruttore della classe: quando si crea una Squadra si passa il NomeSquadra
    // Il NomeSquadra ricevuto viene salvato nella variabile NomeSquadra della squadra

    public void setPunteggio(int Punteggio) {
        this.Punteggio = Punteggio; 
    } 
    // Metodo che permette di modificare il Punteggio della squadra

    public int  getPunteggio() { 
        return Punteggio; 
    } 
    // Metodo che restituisce il Punteggio attuale della squadra

    public String getNomeSquadra() { 
        return NomeSquadra; 
    } 
    // Metodo che restituisce il NomeSquadra della squadra
}