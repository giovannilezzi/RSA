package RSA;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
/**
 * Created by root on 17/06/17.
 */
public class AttaccoFattorizzazione {

    public AttaccoFattorizzazione() {}

    public ArrayList<BigInteger> calcolaPrivateKeyClient(String nomeFile) throws IOException {
        FileManager fileManager = new FileManager();
        ArrayList<String> chiaviPubblicheClient;
        chiaviPubblicheClient = fileManager.readFromFile(nomeFile);
        BigInteger N = new BigInteger(chiaviPubblicheClient.get(0));
        BigInteger E = new BigInteger(chiaviPubblicheClient.get(1));
        double n = N.doubleValue();
        double e = E.doubleValue();
        double radice = Math.sqrt(n);
        radice = Math.ceil(radice);
        if (radice % 2 == 0) {
            radice = radice + 1;
        }
        Integer radice1 = (int) radice;
        BigInteger rad = new BigInteger(radice1.toString());
        System.out.println("prima");
        while (N.mod(rad) != BigInteger.ZERO && rad.compareTo(BigInteger.ZERO) >= 0) {
            rad = rad.subtract(new BigInteger("2"));
        }
        System.out.println("dopo");
        ArrayList<BigInteger> chiaviTrovate = new ArrayList<BigInteger>();
        BigInteger p = rad;
        BigInteger q = N.divide(p);
        BigInteger fi_n = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        BigInteger d = E.modInverse(fi_n);
        chiaviTrovate.add(p);
        chiaviTrovate.add(q);
        chiaviTrovate.add(d);
        return chiaviTrovate;
    }


    public static void main(String[] args) throws IOException {
        AttaccoFattorizzazione attaccoFattorizzazione = new AttaccoFattorizzazione();
        ArrayList<BigInteger> chiaviTrovate;
        System.out.println("ciao");
        chiaviTrovate = attaccoFattorizzazione.calcolaPrivateKeyClient("ChiaviPubblicheClient");
        System.out.println("Questp è P:" + chiaviTrovate.get(0));//restituisce P
        System.out.println("Questo è D:" + chiaviTrovate.get(2));
    }

}
