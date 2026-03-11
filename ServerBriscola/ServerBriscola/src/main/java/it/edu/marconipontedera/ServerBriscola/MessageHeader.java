package it.edu.marconipontedera.ServerBriscola;
// Indica il package (cartella logica) in cui si trova questa classe

public class MessageHeader {

    private String IDMessaggio;  // ID del messaggio
    private String[] Campi;      // Array che contiene i vari campi del messaggio

    // Costruttore per inizializzare l'ID del messaggio e i campi
    public MessageHeader(String IDMessaggio, String[] Campi) {
        this.IDMessaggio = IDMessaggio;
        this.Campi = Campi;
    }

    // Getter per l'ID del messaggio
    public String getIDMessaggio() { 
        return IDMessaggio; 
    }

    // Getter per i campi del messaggio
    public String[] getCampi() { 
        return Campi; 
    }

    // Setter per l'ID del messaggio
    public void setIDMessaggio(String IDMessaggio) { 
        this.IDMessaggio = IDMessaggio; 
    }

    // Setter per i campi del messaggio
    public void setCampi(String[] Campi) { 
        this.Campi = Campi; 
    }

    // Metodo per formattare il messaggio in una stringa separata da punto e virgola
    public String Formattazione() {
        String Message = IDMessaggio;  // Inizializza il messaggio con l'IDMessaggio
        for (int i = 0; i < Campi.length; i++) {  // Aggiunge ogni campo separato da ";"
            Message += ";" + Campi[i];
        }
        return Message;  // Restituisce il messaggio formattato
    }

    // Metodo statico per analizzare un messaggio formattato
    public static MessageHeader ParseMessage(String Message) {
        String[] Parti = Message.split(";");  // Divide la stringa del messaggio usando ";" come 
                                              //separatore
        String IDMessaggio = Parti[0];  // Il primo elemento è l'ID del messaggio
        String[] Campi = new String[Parti.length - 1];  // Crea un array per i campi
        for (int i = 1; i < Parti.length; i++) {  // Riempie l'array dei campi con gli altri elementi
            Campi[i - 1] = Parti[i];
        }
        return new MessageHeader(IDMessaggio, Campi);  // Crea e restituisce un oggetto MessageHeader
    }

    // Metodo statico per creare un messaggio di conferma di connessione
    // ID 19: Conferma connessione
    public static MessageHeader ConfermaConnessione(String CodiceGiocatore, String CodiceSquadra) {
        String[] Campi = {CodiceGiocatore, CodiceSquadra};  // Crea un array di campi con il codice giocatore 
                                                            // e il codice squadra
        return new MessageHeader("19", Campi);  // Restituisce un nuovo messaggio con ID 19
    }

    // Metodo statico per creare un messaggio per l'inizio partita
    // ID 04: Inizio partita
    // Campi: CartaBriscola, Carta1, Carta2, Carta3, CodiceGiocatore
    public static MessageHeader InizioPartita(String CartaBriscola, String ValoreSeme1, 
            String ValoreSeme2, String ValoreSeme3, String CodiceGiocatore) {
        String[] Campi = {CartaBriscola, ValoreSeme1, ValoreSeme2, ValoreSeme3, CodiceGiocatore};
        // Crea un array con i vari parametri
        return new MessageHeader("04", Campi);  // Restituisce un nuovo messaggio con ID 04
    }

    // Metodo statico per creare un messaggio di giocatore pronto
    // ID 09: Giocatore pronto
    public static MessageHeader GiocatorePronto(String CodiceGiocatore) {
        String[] Campi = {CodiceGiocatore};  // Crea un array con il codice giocatore
        return new MessageHeader("09", Campi);  // Restituisce un nuovo messaggio con ID 09
    }

    // Metodo statico per creare un messaggio di carta accettata
    // ID 67: Carta accettata
    public static MessageHeader CartaAccettata(String codiceGiocatore, String valoreSeme) {
        String[] Campi = {codiceGiocatore, valoreSeme};  // Crea un array con il codice giocatore e 
                                                         // il valore del seme
        return new MessageHeader("67", Campi);  // Restituisce un nuovo messaggio con ID 67
    }

    // Metodo statico per creare un messaggio di esito giocata
    // ID 14: Esito giocata (mossa non valida)
    public static MessageHeader EsitoGiocata(String Esito) {
        String[] Campi = {Esito};  // Crea un array con il codice dell'esito
        return new MessageHeader("14", Campi);  // Restituisce un nuovo messaggio con ID 14
    }

    // Metodo statico per creare un messaggio di presa effettuata
    // ID 92: Presa effettuata
    public static MessageHeader PresaEffettuata(String CodiceGiocatore, String PunteggioA, String PunteggioB) {
        String[] Campi = {CodiceGiocatore, PunteggioA, PunteggioB};  // Crea un array con il codice giocatore e i punteggi
        return new MessageHeader("92", Campi);  // Restituisce un nuovo messaggio con ID 92
    }

    // Metodo statico per creare un messaggio di carta pescata
    // ID 52: Carta pescata
    public static MessageHeader CartaPescata(String CodiceGiocatore, String ValoreSeme) {
        String[] Campi = {CodiceGiocatore, ValoreSeme};  // Crea un array con il codice giocatore e il valore del seme
        return new MessageHeader("52", Campi);  // Restituisce un nuovo messaggio con ID 52
    }

    // Metodo statico per creare un messaggio di nuovo turno
    // ID 34: Nuovo turno
    public static MessageHeader NuovoTurno(String CodiceGiocatore) {
        String[] Campi = {CodiceGiocatore};  // Crea un array con il codice giocatore
        return new MessageHeader("34", Campi);  // Restituisce un nuovo messaggio con ID 34
    }

    // Metodo statico per creare un messaggio di fine smazzata
    // ID 12: Fine smazzata
    public static MessageHeader FineSmazzata(int PunteggioA, int PunteggioB) {
        String[] Campi = {String.valueOf(PunteggioA), String.valueOf(PunteggioB)};  // Crea un array con i punteggi A e B
        return new MessageHeader("12", Campi);  // Restituisce un nuovo messaggio con ID 12
    }

    // Metodo statico per creare un messaggio di risultati finali
    // ID 56: Risultati finali (vincitore)
    public static MessageHeader Risultati(String NomeSquadra) {
        String[] Campi = {NomeSquadra};  // Crea un array con il nome della squadra vincitrice
        return new MessageHeader("56", Campi);  // Restituisce un nuovo messaggio con ID 56
    }

    // Metodo statico per creare un messaggio di partita interrotta
    // ID 99: Partita interrotta (un giocatore si è disconnesso)
    public static MessageHeader PartitaInterrotta(String Esito) {
        String[] Campi = {Esito};  // Crea un array con il motivo della disconnessione
        return new MessageHeader("99", Campi);  // Restituisce un nuovo messaggio con ID 99
    }

    // Metodo toString che restituisce la rappresentazione del messaggio formattato
    @Override
    public String toString() {
        return Formattazione();  // Restituisce il messaggio formattato
    }
}