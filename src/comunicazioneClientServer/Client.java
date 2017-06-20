package comunicazioneClientServer;

import rsa.RSA;
import rsa.KeyGenerator;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Questa classe Implementa un Client che comunicherà con un Server
 * attraverso una comunicazione cifrata dall'RSA
 *
 * @author Giovanni
 */

public class Client {

    private String ip;
    private int port;
    Socket socket;
    Scanner	socketIn;
    PrintWriter socketOut;
    Scanner in;

    /**
     * Costruttore
     *
     * @param ip
     * @param port
     */
    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    /**
     * Metodo che inizializza la comunicazione da parte del Client
     *
     * @throws IOException
     */
    public void startClient() throws IOException { //eseguo il metodo attraverso IOException poiche necessito di un controllo dei dati in input ed output
        socket = new Socket(ip, port); //inizializzo un socket con ip del destinatario e porta da utilizzare
        System.out.println("Connessione con il server stabilita" + '\n' + "Per terminare la partita scrivere: 'Esci'" + '\n');
        socketIn = new	Scanner(socket.getInputStream()); //inizializzo uno scanner per poi stampare il flusso di dati proveniente dal server attarverso il socket
        socketOut = new PrintWriter(socket.getOutputStream()); //inizializzzo un printwriter per inserire nel socket il flusso di dati da inviare al server


    }

    /**
     * Metodo che comunica effettivamente con il Server
     *
     * @param D chiave privata del Client per decriptare il messaggio arrivato dal Server
     * @param N chiave pubblica del Server per decriptare il messaggio arrivato dal Server
     * @throws IOException
     */
    public void comunica(BigInteger D, BigInteger N) throws IOException {
        try {
            String inputLine; //stringa per l'input da tastiera
            in = new Scanner(System.in); //inizializzo uno scanner per acquisire l'input da tastiera
            RSA rsa = new RSA();
            while (true) {
                boolean ripeti;
                do{
                    ripeti = true;
                    //invio dati al comunicazioneClientServer.Server
                    System.out.println("Messaggio da inviare al comunicazioneClientServer.Server:");
                    inputLine = in.nextLine();
                    BigInteger messaggioCodificato = rsa.scriviEcripta(inputLine, "ChiaviPubblicheServer");
                    System.out.println("Messaggio da inviare al comunicazioneClientServer.Server CODIFICATO:" + messaggioCodificato);
                    socketOut.println(messaggioCodificato); //stampo nel socket la stringa da voler inviare
                    socketOut.flush();	//svuota il flusso di dati prendi su socketOut
                    //ricezione dati dal comunicazioneClientServer.Server
                    BigInteger messaggioCriptato = new BigInteger(socketIn.nextLine());
                    System.out.println("Messaggio ricevuto dal comunicazioneClientServer.Client CRIPTATO:" + messaggioCriptato );
                    System.out.println("Messaggio ricevuto dal comunicazioneClientServer.Client DECRIPTATO:" + rsa.decrypt(messaggioCriptato, D, N));
                }while(ripeti); //cinvio il contenuto del socket la servericla solo quando si esegue il codice di default e viene richiesto all'utente di reinserire la propria scelta
            }
        }
        catch(NoSuchElementException e) { //si esegue come conseguenza alla risposta che riceveremo dopo aver inviato "Esci" o "esci" al server
            System.out.println("Connessione chiusa");
        }
        finally {
            in.close();
        }
    }

    /**
     * chiude la comunicazione
     *
     * @throws IOException
     */
    public void close() throws IOException {
        socketIn.close(); //chiusura del scanner per l'acquisizionde dei flussi in entrata
        socketOut.close(); //chiusura del printwriter per l'invio dei flussi in uscita
        socket.close();
    }

    public static void main(String[] args) throws Exception {
        Client client = new Client("127.0.0.1", 1337);
        int numerocifre = 1024;//fino a 13 funziona
        client.startClient();
        KeyGenerator keyGenerator = new KeyGenerator();
        ArrayList<BigInteger> chiaviPrivate = keyGenerator.getprivatesKey(numerocifre);//restituisce P e Q in ordine
        System.out.println("Questo è P:" + chiaviPrivate.get(0));
        System.out.println("Questo è Q:" + chiaviPrivate.get(1));
        ArrayList<BigInteger> chiaviPubbliche = keyGenerator.computeAndSavePublicKeyOnFile(chiaviPrivate, numerocifre, "ChiaviPubblicheClient");// restituisce N,E,D in ordine
        client.comunica(chiaviPubbliche.get(2), chiaviPubbliche.get(0));
        client.close();
    }

}