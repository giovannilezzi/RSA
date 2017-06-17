package RSA;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by root on 17/06/17.
 */

public class Server {

    ServerSocket serverSocket;
    private int	port;
    Socket socket;
    Scanner	socketIn;
    PrintWriter	socketOut;
    Scanner	in;

    public Server(int port) {
        this.port =	port;
    }

    public void startServer() throws IOException {
        serverSocket = new ServerSocket(port); //inizializzo un un serversocket per permettere al client di instaurare una connessione
        System.out.println("RSA.Server socket pronto sulla porta: " + port);
        socket = serverSocket.accept(); //creo un socket aspettando la connessione di un client alla porta del server
        System.out.println("Connessione RSA.Client Ricevuta");
        socketIn = new Scanner(socket.getInputStream()); //inizializzo uno scanner per acquisire i flussi di dati in entrata nel socket
        socketOut = new PrintWriter(socket.getOutputStream());
    }

    public void comunica(BigInteger D, BigInteger N) throws IOException {
        in = new Scanner(System.in); //inizializzo uno scanner per acquisire l'input da tastiera
        String inputLine; //stringa per l'input da tastiera
        RSA rsa = new RSA();
        while (true) {
            //ricezione dati dal RSA.Client
            BigInteger line = new BigInteger(socketIn.nextLine());
            System.out.println("Messaggio ricevuto dal RSA.Client CRIPTATO:" + line );
            String messaggioDecriptato = rsa.decrypt(line, D, N);
            System.out.println("Messaggio ricevuto dal RSA.Client DECRIPTATO: " + messaggioDecriptato);
            if (messaggioDecriptato.equals("Esci") || messaggioDecriptato.equals("esci")) { //se ricevo una stringa "Esci" o "esci" esco dal ciclo
                break;
            } else {
                //invio dati al RSA.Client
                System.out.println("Messaggio da inviare al RSA.Client:");
                inputLine = in.nextLine();
                BigInteger messagioCriptato = rsa.scriviEcripta(inputLine, "ChiaviPubblicheClient");
                System.out.println("Messaggio da inviare al RSA.Client Criptato:" + messagioCriptato);
                socketOut.println(messagioCriptato);
                socketOut.flush();
            }
        }
    }

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
        ArrayList<BigInteger> chiaviprivate = keyGenerator.getprivatesKey(1024);//restituisce P e Q in ordine
        ArrayList<BigInteger> chiavipubbliche = keyGenerator.computeAndSavePublicKeyOnFile(chiaviprivate, numerocifre, "ChiaviPubblicheServer"); // restituisce N,E,D in ordine
        server.comunica(chiavipubbliche.get(2), chiavipubbliche.get(0));
        server.close();
    }
}

