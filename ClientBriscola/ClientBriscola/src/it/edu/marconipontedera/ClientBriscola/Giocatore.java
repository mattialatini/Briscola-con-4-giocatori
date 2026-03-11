package it.edu.marconipontedera.ClientBriscola;
// Indica il package (cartella logica) in cui si trova questa classe

// Classe che rappresenta un giocatore visto dal client
public class Giocatore {

    private String  CodiceGiocatore;   // CodiceGiocatore identificativo del giocatore assegnato 
                                       //dal server dopo la connessione
    private String  NomeGiocatore;     // NomeGiocatore del giocatore
    private String  NomeSquadra;  // NomeSquadra a cui appartiene il giocatore
    private Carta[] carte = new Carta[3]; // array che contiene le 3 carte in mano

    // Costruttore della classe Giocatore
    // Serve per creare un nuovo giocatore con CodiceGiocatore, NomeGiocatore e NomeSquadra
    public Giocatore(String CodiceGiocatore, String NomeGiocatore, String NomeSquadra) {
        this.CodiceGiocatore = CodiceGiocatore;   // assegna il CodiceGiocatore al giocatore
        this.NomeGiocatore = NomeGiocatore;     // assegna il NomeGiocatore al giocatore
        this.NomeSquadra = NomeSquadra;  // assegna il NomeSquadra al giocatore
    }

    // Metodo che imposta le 3 carte iniziali distribuite dal server
    public void setCarte(Carta carta1, Carta carta2, Carta carta3) {
        carte[0] = carta1; // inserisce la prima carta
        carte[1] = carta2; // inserisce la seconda carta
        carte[2] = carta3; // inserisce la terza carta
    }

    // Metodo che rimuove dalla mano una carta giocata
    // Viene chiamato dopo la conferma del server
    public void RimuoviCarta(String valoreSeme) {

        // scorre tutte le carte della mano
        for (int i = 0; i < carte.length; i++) {

            // controlla se la carta esiste e se il suo valore coincide con quello passato
            if (carte[i] != null && carte[i].toString().equals(valoreSeme)) {

                carte[i] = null; // elimina la carta dalla mano

                return; // termina il metodo
            }
        }
    }

    // Metodo che aggiunge una carta pescata dal mazzo
    // La inserisce nel primo spazio libero dell'array
    public void AggiungiCarta(Carta carta) {

        // scorre tutte le posizioni dell'array
        for (int i = 0; i < carte.length; i++) {

            // se trova uno slot vuoto inserisce la carta
            if (carte[i] == null) { 
                carte[i] = carta; 
                return; // termina il metodo dopo aver inserito la carta
            }
        }
    }

    // Restituisce il CodiceGiocatore del giocatore
    public String getCodiceGiocatore(){ 
        return CodiceGiocatore; 
    }

    // Imposta/modifica il CodiceGiocatore del giocatore
    public void setCodiceGiocatore(String CodiceGiocatore) { 
        this.CodiceGiocatore = CodiceGiocatore; 
    }

    // Restituisce il NomeGiocatore del giocatore
    public String getNomeGiocatore() { 
        return NomeGiocatore; 
    }

    // Restituisce il NomeSquadra del giocatore
    public String getNomeSquadra() { 
        return NomeSquadra; 
    }

    // Restituisce l'array delle carte in mano
    public Carta[] getCarte() { 
        return carte; 
    }
}