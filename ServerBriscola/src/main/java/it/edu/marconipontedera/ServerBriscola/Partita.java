package it.edu.marconipontedera.ServerBriscola;

/*
 * Classe che gestisce una partita di Briscola a coppie
 * 4 giocatori totali divisi in 2 squadre
 */
public class Partita {

    // squadra composta da 2 giocatori
    private Squadra squadraA;

    // squadra composta da 2 giocatori
    private Squadra squadraB;

    // mazzo di carte della partita
    private Mazzo mazzo;

    // seme della briscola
    private String SemeBriscola;

    // indice del giocatore di turno (0-3)
    private int Turno;

    // carte giocate nella mano corrente
    private Carta[] carteMano = new Carta[4];

    // ordine dei giocatori che hanno giocato
    private int[] OrdineGiocatori = new int[4];

    // numero di carte giocate nella mano corrente
    private int CarteGiocate = 0;

    // punteggio della squadra A
    private int PunteggioA = 0;

    // punteggio della squadra B
    private int PunteggioB = 0;

    /*
     * Costruttore della partita
     * inizializza mazzo e squadre
     */
    public Partita() {

        // crea il mazzo
        mazzo = new Mazzo();

        // crea le squadre
        squadraA = new Squadra("Squadra A");
        squadraB = new Squadra("Squadra B");
    }

    /*
     * Metodo che avvia la partita
     */
    public void iniziaPartita() {

        // mescola il mazzo
        mazzo.mescola();

        // prende il seme della briscola
        SemeBriscola = mazzo.getSemeBriscola();

        // prende i giocatori dal server
        Giocatore[] giocatori = ServerBriscola.getGiocatori();

        // assegna i giocatori alla squadra A
        squadraA.setGiocatore1(giocatori[0]);
        squadraA.setGiocatore2(giocatori[2]);

        // assegna i giocatori alla squadra B
        squadraB.setGiocatore1(giocatori[1]);
        squadraB.setGiocatore2(giocatori[3]);

        // salva la squadra dentro ogni giocatore
        giocatori[0].setSquadra(squadraA);
        giocatori[2].setSquadra(squadraA);

        giocatori[1].setSquadra(squadraB);
        giocatori[3].setSquadra(squadraB);

        // ciclo per distribuire le carte ai 4 giocatori
        for (int i = 0; i < 4; i++) {

            // crea la mano del giocatore (3 carte)
            Carta[] mano = new Carta[3];

            // pesca 3 carte dal mazzo
            for (int j = 0; j < 3; j++) {

                mano[j] = mazzo.pesca();

            }

            // assegna le carte al giocatore
            giocatori[i].setCarte(mano);

            // crea il messaggio di inizio partita
            MessageHeader Message = MessageHeader.InizioPartita(
                    SemeBriscola,
                    mano[0].toString(),
                    mano[1].toString(),
                    mano[2].toString(),
                    giocatori[0].getCodiceGiocatore()
            );

            // invia il messaggio al client
            giocatori[i].getOut().println(Message.toString());
        }

        // il primo turno è del giocatore 0
        Turno = 0;

        // comunica il turno a tutti
        ComunicaTurno();
    }

    /*
     * comunica a tutti quale giocatore deve giocare
     */
    private void ComunicaTurno() {

        // prende il codice del giocatore di turno
        String CodiceGiocatoreIniziale = ServerBriscola.getGiocatori()[Turno].getCodiceGiocatore();

        // invia il messaggio a tutti
        InviaATutti(MessageHeader.NuovoTurno(CodiceGiocatoreIniziale));
    }

    /*
     * metodo chiamato quando un giocatore gioca una carta
     */
    public synchronized void GiocaCarta(int CodiceGiocatore, String ValoreSeme) {

        // controlla se è davvero il turno del giocatore
        if (CodiceGiocatore != Turno)
            return;

        // prende il giocatore
        Giocatore giocatore = ServerBriscola.getGiocatori()[CodiceGiocatore];

        // rimuove la carta dalla mano del giocatore
        Carta carta = giocatore.GiocaCarta(ValoreSeme);

        // se la carta non esiste esce
        if (carta == null)
            return;

        // salva la carta nella mano corrente
        carteMano[CarteGiocate] = carta;

        // salva chi ha giocato la carta
        OrdineGiocatori[CarteGiocate] = CodiceGiocatore;

        // aumenta il numero di carte giocate
        CarteGiocate++;

        // avvisa tutti che la carta è stata giocata
        InviaATutti(MessageHeader.CartaAccettata(giocatore.getCodiceGiocatore(), ValoreSeme));

        // se hanno giocato tutti i giocatori
        if (CarteGiocate == 4) {

            // risolve la mano
            RisolviMano();

        } else {

            // passa il turno al prossimo giocatore
            Turno = (Turno + 1) % 4;

            // comunica il nuovo turno
            ComunicaTurno();
        }
    }

    /*
     * calcola chi ha vinto la mano
     */
    private void RisolviMano() {

        // il Giocatore iniziale è il primo giocatore
        int GiocatoreIniziale = OrdineGiocatori[0];

        // carta vincente iniziale
        Carta carta = carteMano[0];

        // seme della prima carta giocata
        String SemeAttacco = carta.getSeme();

        // confronta tutte le altre carte
        for (int i = 1; i < 4; i++) {

            if (Batte(carteMano[i], carta, SemeAttacco)) {

                carta = carteMano[i];
                GiocatoreIniziale = OrdineGiocatori[i];

            }
        }

        // calcola i punti della mano
        int PunteggioMano = 0;

        for (int i = 0; i < carteMano.length; i++) {

            PunteggioMano += carteMano[i].getPunteggio();

        }

        // prende il giocatore vincitore
        Giocatore giocatore = ServerBriscola.getGiocatori()[GiocatoreIniziale];

        // assegna i punti alla squadra
        if (giocatore.getSquadra() == squadraA)
            PunteggioA += PunteggioMano;
        else
            PunteggioB += PunteggioMano;

        // invia il messaggio della presa
        InviaATutti(MessageHeader.PresaEffettuata(
                giocatore.getCodiceGiocatore(),
                String.valueOf(PunteggioA),
                String.valueOf(PunteggioB)));

        // pesca nuove carte
        PescaCarte(GiocatoreIniziale);

        // reset della mano
        ResetMano();

        // controlla se la partita è finita
        if (mazzo.isEmpty() && !GiocatoriHannoCarte())
            FineSmazzata();
        else {

            // il vincitore inizia la prossima mano
            Turno = GiocatoreIniziale;

            ComunicaTurno();
        }
    }

    /*
     * pesca nuove carte dopo la presa
     */
    private void PescaCarte(int IDGiocatore) {

        // ciclo sui giocatori
        for (int i = 0; i < 4; i++) {

            int id = (IDGiocatore + i) % 4;

            // se il mazzo non è vuoto
            if (!mazzo.isEmpty()) {

                // pesca una carta
                Carta carta = mazzo.pesca();

                // aggiunge la carta al giocatore
                ServerBriscola.getGiocatori()[id].aggiungiCarta(carta);

                // invia la nuova carta al client
                ServerBriscola.getGiocatori()[id].getOut().println(MessageHeader.CartaPescata(ServerBriscola.getGiocatori()[id].getCodiceGiocatore(),
                                carta.toString()
                        ).toString()
                );
            }
        }
    }

    /*
     * reset delle variabili della mano
     */
    private void ResetMano() {

        carteMano = new Carta[4];
        OrdineGiocatori = new int[4];
        CarteGiocate = 0;
    }

    /*
     * controlla se i giocatori hanno ancora carte
     */
    private boolean GiocatoriHannoCarte() {

        Giocatore[] giocatori = ServerBriscola.getGiocatori();

        for (int i = 0; i < giocatori.length; i++) {

            Carta[] carte = giocatori[i].getCarte();

            for (int j = 0; j < carte.length; j++) {

                if (carte[j] != null)
                    return true;

            }
        }

        return false;
    }

    /*
     * controlla quale carta vince
     */
    private boolean Batte(Carta Sfidante, Carta Attuale, String SemeAttacco) {

        boolean SBriscola = Sfidante.getSeme().equals(SemeBriscola);
        boolean ABriscola = Attuale.getSeme().equals(SemeBriscola);

        if (SBriscola && !ABriscola) return true;
        if (!SBriscola && ABriscola) return false;

        if (SBriscola)
            return gerarchia(Sfidante) > gerarchia(Attuale);

        boolean SAttacco = Sfidante.getSeme().equals(SemeAttacco);
        boolean AAttacco = Attuale.getSeme().equals(SemeAttacco);

        if (SAttacco && !AAttacco) return true;
        if (!SAttacco && AAttacco) return false;

        if (SAttacco)
            return gerarchia(Sfidante) > gerarchia(Attuale);

        return false;
    }

    /*
     * gerarchia delle carte nella briscola
     */
    private int gerarchia(Carta carta) {

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
        return 0;
    }

    /*
     * fine partita
     */
    private void FineSmazzata() {

        InviaATutti(MessageHeader.FineSmazzata(PunteggioA, PunteggioB));
    }

    /*
     * invia un messaggio a tutti i giocatori
     */
    private void InviaATutti(MessageHeader Message) {

        for (int i = 0; i < ServerBriscola.getTeamA().length; i++) {

            ClientHandler clienthandler = ServerBriscola.getTeamA()[i];

            if (clienthandler != null)
                clienthandler.getOut().println(Message.toString());
        }

        for (int i = 0; i < ServerBriscola.getTeamB().length; i++) {

            ClientHandler clienthandler = ServerBriscola.getTeamB()[i];

            if (clienthandler != null)
                clienthandler.getOut().println(Message.toString());
        }
    }
}