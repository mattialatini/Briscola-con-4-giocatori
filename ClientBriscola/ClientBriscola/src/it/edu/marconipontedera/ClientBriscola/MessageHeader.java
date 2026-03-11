package it.edu.marconipontedera.ClientBriscola;
// Indica il package (cartella logica) in cui si trova questa classe

// Classe che rappresenta l'intestazione di un messaggio del protocollo.
// I messaggi hanno formato: "ID;campo1;campo2;..."
public class MessageHeader {

    // ID del messaggio (identifica il tipo di messaggio)
    private String   IDMessaggio;

    // Array che contiene i Campi del messaggio
    private String[] Campi;

    // Costruttore della classe
    // Riceve l'ID del messaggio e i Campi associati
    public MessageHeader(String IDMessaggio, String[] Campi) {
        this.IDMessaggio    = IDMessaggio;      // assegna l'IDMessaggio ricevuto alla variabile 
                                                //della classe
        this.Campi = Campi;   // assegna i Campi ricevuti alla variabile della classe
    }

    // Metodo che restituisce l'ID del messaggio
    public String getIDMessaggio() { 
        return IDMessaggio; 
    }

    // Metodo che restituisce i Campi del messaggio
    public String[] getCampi() { 
        return Campi; 
    }

    // Trasforma il messaggio in una stringa da inviare tramite socket
    // Il formato finale sarà: ID;campo1;campo2;...
    @Override
    public String toString() {

        // Inizializza la stringa con l'ID del messaggio
        String Risultato = IDMessaggio;

        // Aggiunge tutti i Campi separati da ";"
        for (int i = 0; i < Campi.length; i++) {
            Risultato = Risultato + ";" + Campi[i];
        }

        // Restituisce la stringa completa
        return Risultato;
    }

    // Metodo statico che trasforma una stringa ricevuta
    // in un oggetto MessageHeader
    public static MessageHeader ParseMessage(String Message) {

        // Divide la stringa usando ";" come separatore
        String[] Parti = Message.split(";");

        // La prima parte è l'ID del messaggio
        String IDMessaggio = Parti[0];

        // Crea un array per i Campi (tutti gli elementi tranne il primo)
        String[] Campi = new String[Parti.length - 1];

        // Copia i Campi dall'array "Parti" al nuovo array "Campi"
        for (int i = 1; i < Parti.length; i++) Campi[i - 1] = Parti[i];

        // Restituisce un nuovo oggetto MessageHeader
        return new MessageHeader(IDMessaggio, Campi);
    }


    // ID 79: richiesta di connessione al server
    // Invia il NomeGiocatore del giocatore e il NomeSquadra
    public static MessageHeader RichiestaConnessione(String NomeGiocatore, String NomeSquadra) {
        String[] Campi = {NomeGiocatore, NomeSquadra};
        return new MessageHeader("79", Campi);
    }

    // ID 09: indica che il giocatore è pronto a giocare
    // Invia il CodiceGiocatore del giocatore
    public static MessageHeader GiocatorePronto(String CodiceGiocatore) {
        String[] Campi = {CodiceGiocatore};
        return new MessageHeader("09", Campi);
    }

    // ID 63: messaggio per giocare una carta
    // Invia il CodiceGiocatore del giocatore e la carta (formato "Valore,Seme")
    public static MessageHeader GiocaCarta(String CodiceGiocatore, String ValoreSeme) {
        String[] Campi = {CodiceGiocatore, ValoreSeme};
        return new MessageHeader("63", Campi);
    }
}