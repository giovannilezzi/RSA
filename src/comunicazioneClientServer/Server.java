package comunicazioneClientServer;

import rsa.KeyGenerator;
import rsa.RSA;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Questa classe Implementa un Server che comunicherà con un Client
 * attraverso una comunicazione cifrata dall'RSA
 *
 * @author Giovanni
 */
public class Server {

    ServerSocket serverSocket;
    private int	port;
    Socket socket;
    Scanner	socketIn;
    PrintWriter	socketOut;
    Scanner	in;

    /**
     * Costruttore
     *
     * @param port
     */
    public Server(int port) {
        this.port =	port;
    }

    /**
     * Metodo che inizializza la comunicazione da parte del Client
     *
     * @throws IOException
     */
    public void startServer() throws IOException {
        serverSocket = new ServerSocket(port); //inizializzo un un serversocket per permettere al client di instaurare una connessione
        System.out.println("comunicazioneClientServer.Server socket pronto sulla porta: " + port);
        socket = serverSocket.accept(); //creo un socket aspettando la connessione di un client alla porta del server
        System.out.println("Connessione comunicazioneClientServer.Client Ricevuta");
        socketIn = new Scanner(socket.getInputStream()); //inizializzo uno scanner per acquisire i flussi di dati in entrata nel socket
        socketOut = new PrintWriter(socket.getOutputStream());
    }

    /**
     * Metodo che comunica effettivamente con il Server
     *
     * @param D chiave privata del Server per decriptare il messaggio arrivato dal Server
     * @param N chiave pubblica del Client per decriptare il messaggio arrivato dal Server
     * @throws IOException
     */

    public void comunica(BigInteger D, BigInteger N) throws IOException {
        in = new Scanner(System.in); //inizializzo uno scanner per acquisire l'input da tastiera
        String inputLine; //stringa per l'input da tastiera
        RSA rsa = new RSA();
        while (true) {
            //ricezione dati dal comunicazioneClientServer.Client
            BigInteger line = new BigInteger(socketIn.nextLine());
            System.out.println("Messaggio ricevuto dal comunicazioneClientServer.Client CRIPTATO:" + line );
            String messaggioDecriptato = rsa.decrypt(line, D, N);
            System.out.println("Messaggio ricevuto dal comunicazioneClientServer.Client DECRIPTATO: " + messaggioDecriptato);
            if (messaggioDecriptato.equals("Esci") || messaggioDecriptato.equals("esci")) { //se ricevo una stringa "Esci" o "esci" esco dal ciclo
                break;
            } else {
                //invio dati al comunicazioneClientServer.Client
                System.out.println("Messaggio da inviare al comunicazioneClientServer.Client:");
                inputLine = in.nextLine();
                BigInteger messagioCriptato = rsa.scriviEcripta(inputLine, "ChiaviPubblicheClient");
                System.out.println("Messaggio da inviare al comunicazioneClientServer.Client Criptato:" + messagioCriptato);
                socketOut.println(messagioCriptato);
                socketOut.flush();
            }
        }
    }

    /**
     * chiude la comunicazione
     *
     * @throws IOException
     */
    public void close() throws IOException {
        System.out.println("Chiusura socket");
        socketIn.close();
        socketOut.close();
        socket.close();
        serverSocket.close();
    }

    public static void main(String[] args) throws Exception {
        Server server = new Server(1337);
        int numerocifre = 1024;
        server.startServer();
        KeyGenerator keyGenerator = new KeyGenerator();
        ArrayList<BigInteger> chiaviprivate = keyGenerator.getprivatesKey(numerocifre);//restituisce P e Q in ordine
        ArrayList<BigInteger> chiavipubbliche = keyGenerator.computeAndSavePublicKeyOnFile(chiaviprivate, numerocifre, "ChiaviPubblicheServer"); // restituisce N,E,D in ordine
        server.comunica(chiavipubbliche.get(2), chiavipubbliche.get(0));
        server.close();

    }
}