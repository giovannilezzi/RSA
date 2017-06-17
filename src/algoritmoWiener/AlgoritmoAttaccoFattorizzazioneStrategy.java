package algoritmoWiener;

import Utility.FileManager;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Created by root on 17/06/17.
 */
public class AlgoritmoAttaccoFattorizzazioneStrategy implements IAlgoritmoAttaccoStrategy {

    private ArrayList<BigInteger> chiaviTrovate;
    private boolean trovate = false;

    @Override
    public void attack(ArrayList<String> key){
        BigInteger N = new BigInteger(key.get(0));
        BigInteger E = new BigInteger(key.get(1));
        double n = N.doubleValue();
        double e = E.doubleValue();
        double radice = Math.sqrt(n);
        radice = Math.ceil(radice);
        if (radice % 2 == 0) {
            radice = radice + 1;
        }
        Integer radice1 = (int) radice;
        BigInteger rad = new BigInteger(radice1.toString());
        while (N.mod(rad) != BigInteger.ZERO && rad.compareTo(BigInteger.ZERO) >= 0) {
            rad = rad.subtract(new BigInteger("2"));
        }
        chiaviTrovate = new ArrayList<>();
        BigInteger p = rad;
        BigInteger q = N.divide(p);
        BigInteger fi_n = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        BigInteger d = E.modInverse(fi_n);
        chiaviTrovate.add(p);
        chiaviTrovate.add(q);
        chiaviTrovate.add(d);
        if(chiaviTrovate != null) trovate = true;
        else trovate = false;
    }

    public void printResult() {
        if(trovate) {
            System.out.println("p = " + chiaviTrovate.get(0));
            System.out.println("q = " + chiaviTrovate.get(1));
            System.out.println("d = " + chiaviTrovate.get(2));
        } else {
            System.out.println("Unable to get the private key!");
        }
    }


    public static void main(String[] args) throws IOException {
        FileManager fileManager = new FileManager();
        ArrayList<String> chiaviPubblicheClient = fileManager.readFromFile("ChiaviPubblicheClient");
        AlgoritmoAttaccoFattorizzazioneStrategy attaccoFattorizzazione = new AlgoritmoAttaccoFattorizzazioneStrategy();
        attaccoFattorizzazione.attack(chiaviPubblicheClient);
        attaccoFattorizzazione.printResult();
    }

}
