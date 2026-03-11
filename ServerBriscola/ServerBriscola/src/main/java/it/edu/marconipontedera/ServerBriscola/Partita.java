package it.edu.marconipontedera.ServerBriscola;
// Indica il package (cartella logica) in cui si trova questa classe

// Gestisce una partita di Briscola a coppie (4 giocatori, 2 squadre).

public class Partita {

    private Squadra squadraA; // Riferimento alla squadra A
    private Squadra squadraB; // Riferimento alla squadra B
    private Mazzo mazzo; // Mazzo di carte
    private String SemeBriscola; // Seme della briscola (ad esempio: "Denari")
    private int Turno; // Indica il turno corrente
    private Carta[] carteMano = new Carta[4]; // Le carte giocate nella mano corrente
    private int[] OrdineGiocatori = new int[4]; // Ordine dei giocatori che hanno giocato
    private int CarteGiocate = 0; // Conta le carte giocate nella mano
    private int PunteggioA = 0; // Punteggio della squadra A
    private int PunteggioB = 0; // Punteggio della squadra B
    private boolean PartitaTerminata = false; // Flag che indica se la partita è terminata

    // Restituisce se la partita è terminata
    private synchronized boolean isPartitaTerminata() {
        return PartitaTerminata;
    }
    
    // Imposta lo stato di terminazione della partita
    private synchronized void setPartitaTerminata(boolean PartitaTerminata) {
        this.PartitaTerminata = PartitaTerminata;
    }

    // Costruttore della partita
    public Partita() {
        mazzo = new Mazzo(); // Crea un nuovo mazzo
        squadraA = new Squadra("Squadra A"); // Crea la squadra A
        squadraB = new Squadra("Squadra B"); // Crea la squadra B
    }

    // Inizia una nuova partita
    public void IniziaPartita() {
        mazzo.MescolaCarte(); // Mescola il mazzo

        Giocatore[] giocatori = ServerBriscola.getGiocatori(); // Ottiene l'elenco dei giocatori

        // Assegna i giocatori alle squadre
        squadraA.setGiocatore1(giocatori[0]);
        squadraA.setGiocatore2(giocatori[2]);
        squadraB.setGiocatore1(giocatori[1]);
        squadraB.setGiocatore2(giocatori[3]);

        // Imposta la squadra di ciascun giocatore
        giocatori[0].setSquadra(squadraA);
        giocatori[2].setSquadra(squadraA);
        giocatori[1].setSquadra(squadraB);
        giocatori[3].setSquadra(squadraB);

        // Distribuisce 3 carte a ciascun giocatore (12 carte totali)
        for (int i = 0; i < 4; i++) {
            Carta[] mano = new Carta[3];
            for (int j = 0; j < 3; j++) mano[j] = mazzo.PescaCarta(); // Pesca le carte dal mazzo
            giocatori[i].setCarte(mano); // Assegna le carte al giocatore
        }

        // La briscola è l'utlima carta del mazzo
        Carta cartaBriscola = mazzo.getBriscola();
        SemeBriscola = cartaBriscola.getSeme(); // Imposta il seme della briscola

        // Invia ai giocatori le loro carte e la briscola
        for (int i = 0; i < 4; i++) {
            Carta[] mano = giocatori[i].getCarte();
            MessageHeader Message = MessageHeader.InizioPartita(
                    cartaBriscola.toString(),  // Valore e seme della briscola (es. "Asso,Denari")
                    mano[0].toString(),         // Prima carta del giocatore
                    mano[1].toString(),         // Seconda carta del giocatore
                    mano[2].toString(),         // Terza carta del giocatore
                    giocatori[0].getCodiceGiocatore()); // Codice del primo giocatore
            giocatori[i].getOut().println(Message.toString()); // Invia il messaggio al giocatore
        }

        Turno = 0; // Imposta il turno iniziale
        ComunicaTurno(); // Comunica a tutti il turno corrente
    }

    // Comunica il turno corrente al prossimo giocatore
    private void ComunicaTurno() {
        String CodiceGiocatore = ServerBriscola.getGiocatori()[Turno].getCodiceGiocatore();
        InviaATutti(MessageHeader.NuovoTurno(CodiceGiocatore)); // Invia il messaggio a tutti
    }

    // Gestisce la giocata di una carta
    public synchronized void GiocaCarta(int CodiceGiocatore, String ValoreSeme) {
        if (isPartitaTerminata()) return; // Se la partita è finita, non fa nulla

        Giocatore giocatore = ServerBriscola.getGiocatori()[CodiceGiocatore];
        Carta carta = giocatore.GiocaCarta(ValoreSeme); // Il giocatore gioca una carta
        if (carta == null) return; // Se la carta è nulla, non fa nulla

        carteMano[CarteGiocate] = carta; // Aggiunge la carta alla mano corrente
        OrdineGiocatori[CarteGiocate] = CodiceGiocatore; // Salva l'ordine del giocatore
        CarteGiocate++; // Incrementa il numero di carte giocate

        // Invia il messaggio che la carta è stata accettata
        InviaATutti(MessageHeader.CartaAccettata(giocatore.getCodiceGiocatore(), ValoreSeme));

        // Se tutte e 4 le carte sono state giocate, risolve la mano
        if (CarteGiocate == 4) {
            RisolviMano();
        } else {
            Turno = (Turno + 1) % 4; // Passa al prossimo giocatore
            ComunicaTurno(); // Comunica il turno al prossimo giocatore
        }
    }

    // Risolve la mano corrente, determinando il vincitore
    private void RisolviMano() {
        int Vincitore = OrdineGiocatori[0]; // Inizialmente il vincitore è il primo giocatore
        Carta cartaVincente = carteMano[0]; // La prima carta è quella vincente inizialmente
        String SemeAttacco = cartaVincente.getSeme(); // Seme della carta vincente

        // Confronta le altre carte per determinare la carta vincente
        for (int i = 1; i < 4; i++) {
            if (Batte(carteMano[i], cartaVincente, SemeAttacco)) {
                cartaVincente = carteMano[i];
                Vincitore = OrdineGiocatori[i];
            }
        }

        // Calcola il punteggio della mano
        int PunteggioMano = 0;
        for (int i = 0; i < carteMano.length; i++) {
            PunteggioMano += carteMano[i].getPunteggio(); // Somma i punti delle carte
        }

        // Aggiorna il punteggio della squadra vincitrice
        Giocatore vincitore = ServerBriscola.getGiocatori()[Vincitore];
        if (vincitore.getSquadra() == squadraA) PunteggioA += PunteggioMano;
        else PunteggioB += PunteggioMano;

        // Invia il risultato della mano
        InviaATutti(MessageHeader.PresaEffettuata(
                vincitore.getCodiceGiocatore(),
                String.valueOf(PunteggioA),
                String.valueOf(PunteggioB)));

        PescaCarte(Vincitore); // Pesca nuove carte
        ResetMano(); // Resetta la mano corrente

        // Se il mazzo è vuoto e i giocatori non hanno carte, termina la smazzata
        if (mazzo.isEmpty() && !GiocatoriHannoCarte()) {
            FineSmazzata();
        } else {
            Turno = Vincitore; // Imposta il nuovo turno
            ComunicaTurno(); // Comunica il turno al prossimo giocatore
        }
    }

    // Fa pescare una carta ai giocatori
    private void PescaCarte(int Vincitore) {
        for (int i = 0; i < 4; i++) {
            int IDGiocatore = (Vincitore + i) % 4;
            if (!mazzo.isEmpty()) {
                Carta carta = mazzo.PescaCarta(); // Pesca una carta dal mazzo
                ServerBriscola.getGiocatori()[IDGiocatore].AggiungiCarta(carta); // Aggiungi la carta al giocatore
                ServerBriscola.getGiocatori()[IDGiocatore].getOut().println(
                        MessageHeader.CartaPescata(
                                ServerBriscola.getGiocatori()[IDGiocatore].getCodiceGiocatore(),
                                carta.toString()).toString()); // Invia la carta pescata al giocatore
            }
        }
    }

    // Resetta la mano corrente
    private void ResetMano() {
        carteMano = new Carta[4]; // Resetta le carte giocate
        OrdineGiocatori = new int[4]; // Resetta l'ordine dei giocatori
        CarteGiocate = 0; // Resetta il conteggio delle carte giocate
    }

    // Verifica se i giocatori hanno ancora carte da giocare
    private boolean GiocatoriHannoCarte() {
        Giocatore[] giocatori = ServerBriscola.getGiocatori();
        for (int i = 0; i < giocatori.length; i++) {
            Carta[] carte = giocatori[i].getCarte();
            for (int j = 0; j < carte.length; j++) {
                if (carte[j] != null) return true; // Se trova una carta, ritorna true
            }
        }
        return false; // Se nessun giocatore ha carte, ritorna false
    }

    // Termina la smazzata e invia i punteggi finali
    private void FineSmazzata() {
        setPartitaTerminata(true); // Imposta la partita come terminata

        // Invia i punteggi finali a tutti i giocatori
        InviaATutti(MessageHeader.FineSmazzata(PunteggioA, PunteggioB));

        // Determina la squadra vincitrice
        String NomeSquadra;
        if (PunteggioA > PunteggioB) NomeSquadra = squadraA.getNomeSquadra();
        else if (PunteggioB > PunteggioA) NomeSquadra = squadraB.getNomeSquadra();
        else NomeSquadra = "Pareggio";

        // Invia i risultati a tutti i giocatori
        InviaATutti(MessageHeader.Risultati(NomeSquadra));

        // Stampa i risultati della partita
        System.out.println("FINE PARTITA: " + NomeSquadra + "  A:" + PunteggioA + " B:" + PunteggioB);
    }

    // Interrompe la partita perché un giocatore si è disconnesso.
    // Notifica tutti i client rimasti connessi.
     
    public synchronized void InterrompiPartita(String NomeGiocatore) {
        if (isPartitaTerminata()) return; // Se la partita è già finita, non fa nulla
        setPartitaTerminata(true); // Imposta la partita come terminata

        String Esito = NomeGiocatore + " si è disconnesso. La partita è terminata."; // Motivo della fine
        System.out.println("PARTITA INTERROTTA: " + Esito); // Stampa il motivo
        InviaATutti(MessageHeader.PartitaInterrotta(Esito)); // Invia a tutti il messaggio di interruzione
    }

    // Verifica se una carta "batte" un'altra carta
    private boolean Batte(Carta Sfidante, Carta Attuale, String SemeAttacco) {
        boolean SBriscola = Sfidante.getSeme().equals(SemeBriscola); // Verifica se la carta sfidante è briscola
        boolean ABriscola = Attuale.getSeme().equals(SemeBriscola); // Verifica se la carta attuale è briscola

        // Se una delle carte è briscola, vince
        if (SBriscola && !ABriscola) return true;
        if (!SBriscola && ABriscola) return false;
        if (SBriscola) return Gerarchia(Sfidante) > Gerarchia(Attuale);

        boolean SAttacco = Sfidante.getSeme().equals(SemeAttacco); // Verifica se la carta sfidante è del seme d'attacco
        boolean AAttacco = Attuale.getSeme().equals(SemeAttacco); // Verifica se la carta attuale è del seme d'attacco

        // Se una delle carte è del seme d'attacco, vince
        if (SAttacco && !AAttacco) return true;
        if (!SAttacco && AAttacco) return false;
        if (SAttacco) return Gerarchia(Sfidante) > Gerarchia(Attuale);

        return false;
    }

    // Restituisce il valore gerarchico di una carta
    private int Gerarchia(Carta carta) {
        switch (carta.getValore()) {
            case "Asso":
                return 10;
            case "Tre":
                return 9;
            case "Re":
                return 8;
            case "Cavallo":
                return 7;
            case "Fante":
                return 6;
            case "7":
                return 5;
            case "6":
                return 4;
            case "5":
                return 3;
            case "4":
                return 2;
            case "2":
                return 1;
        }
        return 0; // Nel caso in cui non ci sia corrispondenza, ritorna 0
    }

    // Invia un messaggio a tutti i client
    private void InviaATutti(MessageHeader Message) {
        ClientHandler[] clienthandlers = ServerBriscola.getClientHandlers();
        for (int i = 0; i < clienthandlers.length; i++) {
            if (clienthandlers[i] != null && clienthandlers[i].getOut() != null) {
                clienthandlers[i].getOut().println(Message.toString()); // Invia il messaggio al client
            }
        }
    }
}