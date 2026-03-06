package it.edu.marconipontedera.ServerBriscola;

public class MessageHeader {
    
    private String IDMessaggio;  // ID del messaggio (ad esempio "19" per conferma connessione)
    private String[] Campi;   // Array per memorizzare i campi del messaggio

    // Costruttore che accetta l'ID del messaggio e i campi associati
    public MessageHeader(String IDMessaggio, String[] Campi) {
        this.IDMessaggio = IDMessaggio;  // Assegna l'ID del messaggio
        this.Campi = Campi;        // Assegna i campi del messaggio
    }

    // Metodo per ottenere l'ID del messaggio
    public String getIDMessaggio() {
        return IDMessaggio;  // Restituisce l'ID del messaggio
    }

    // Metodo per ottenere i campi del messaggio
    public String[] getCampi() {
        return Campi;  // Restituisce l'array dei campi del messaggio
    }

    // Metodo per impostare l'ID del messaggio
    public void setIDMessaggio(String IDMessaggio) {
        this.IDMessaggio = IDMessaggio;  // Imposta l'ID del messaggio
    }

    // Metodo per impostare i campi del messaggio
    public void setCampi(String[] Campi) {
        this.Campi = Campi;  // Imposta i campi del messaggio
    }

    // Metodo per costruire il messaggio come stringa formattata
    public String Formattazione() {
        String Message = IDMessaggio;  // Inizializza il messaggio con l'ID del messaggio

        // Usa un ciclo per concatenare i campi separati da ";"
        for (int i = 0; i < Campi.length; i++) {
            Message += ";" + Campi[i];  // Aggiungi ciascun campo separato da ";"
        }

        return Message;  // Ritorna la stringa finale del messaggio
    }

    // Metodo per elaborare il messaggio e dividerlo nei vari campi
    public static MessageHeader parseMessage(String message) {
        String[] parts = message.split(";");  // Divide la stringa in base al punto e virgola
        String IDMessaggio = parts[0];  // Il primo campo è sempre l'ID del messaggio
        String[] Campi = new String[parts.length - 1];  // Crea un array per i campi rimanenti

        // Usa un ciclo per copiare i campi nell'array
        for (int i = 1; i < parts.length; i++) {
            Campi[i - 1] = parts[i];  // Copia ogni campo dopo l'ID
        }

        return new MessageHeader(IDMessaggio, Campi);  // Crea un nuovo oggetto MessageHeader con i dati analizzati
    }

    // Metodo per la creazione di un messaggio di tipo "Conferma connessione"
    public static MessageHeader ConfermaConnessione(String codiceGiocatore, String codiceSquadra) {
        String[] Campi = {codiceGiocatore, codiceSquadra};  // Crea un array di campi con i dati
        return new MessageHeader("19", Campi);  // ID 19 per conferma connessione
    }

    // Metodo per la creazione di un messaggio di tipo "Inizio partita"
    public static MessageHeader InizioPartita(String SemeBriscola, String ValoreSeme1, String ValoreSeme2, String ValoreSeme3, String CodiceGiocatore) {
    	String[] Campi = {SemeBriscola, ValoreSeme1, ValoreSeme2, ValoreSeme3, CodiceGiocatore};
        return new MessageHeader("04", Campi);
    }

    // Metodo per la creazione di un messaggio "Giocatore pronto"
    public static MessageHeader GiocatorePronto(String codiceGiocatore) {
        String[] Campi = {codiceGiocatore};  // Crea il campo con il codice del giocatore
        return new MessageHeader("09", Campi);  // ID 09 per giocatore pronto
    }
    
    // Metodo per creare il messaggio "Carta Accettata"
    public static MessageHeader CartaAccettata(String codiceGiocatore, String valoreSeme) {
        String[] Campi = {codiceGiocatore, valoreSeme};  // Crea un array con il codice del giocatore e il valore della carta
        return new MessageHeader("14", Campi);  // ID 14 per messaggio di carta accettata
    }
    
    // Metodo per creare il messaggio "Presa Effettuata"
    public static MessageHeader PresaEffettuata(String codiceGiocatore, String valoreSeme, String Punteggio) {
        String[] Campi = {codiceGiocatore, valoreSeme, Punteggio};  // Crea un array con il codice del giocatore, il valore della carta e il Punteggio
        return new MessageHeader("92", Campi);  // ID 92 per presa effettuata
    }

    // Metodo per ottenere una stringa di rappresentazione
    @Override
    public String toString() {
        return Formattazione();  // Restituisce la rappresentazione come stringa del messaggio
    }
    
    public static MessageHeader FineSmazzata(int PunteggioA, int PunteggioB) {
    	String[] Campi = {String.valueOf(PunteggioA), String.valueOf(PunteggioB)};
        return new MessageHeader("12", Campi);
    }
    
    public static MessageHeader CartaPescata(String CodiceGiocatore, String ValoreSeme) {
    	String[] Campi = {CodiceGiocatore, ValoreSeme};
        return new MessageHeader("52", Campi);
    }
    
    public static MessageHeader NuovoTurno(String CodiceGiocatore) {
    	String[] Campi = {CodiceGiocatore};
        return new MessageHeader("34", Campi);
    }
}