package RSA;


import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;
/**
 * Created by root on 17/06/17.
 */
public class KeyGenerator {


    /**
     * 3. calcola Phi(n) (funzione toziente di eulero)
     * Phi(n) = (p-1)(q-1)
     * BigInteger è un oggetto e posso usare i suoi metodi per le operazioni
     */
    private BigInteger getPhi(BigInteger p, BigInteger q) {
        BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
        return phi;
    }

    /**
     * Genera un numero primo Random
     */
    private BigInteger getKey(int numerocifre) {
        Random randomInteger = new Random();
        BigInteger largePrime = BigInteger.probablePrime(numerocifre, randomInteger);
        return largePrime;
    }


    /**
     * implementazione dell'algoritmo di euclide
     * Note: Uses BigInteger operations
     */
    private BigInteger gcd(BigInteger a, BigInteger b) {
        if (b.equals(BigInteger.ZERO)) {
            return a;
        } else {
            return gcd(b, a.mod(b));
        }
    }


    /**
     * Calcola d con l'algoritmo di euclide esteso
     */
    private BigInteger[] extEuclid(BigInteger e, BigInteger phi) {
        if (phi.equals(BigInteger.ZERO)) return new BigInteger[]{
                e, BigInteger.ONE, BigInteger.ZERO
        }; // { a, 1, 0 }
        BigInteger[] vals = extEuclid(phi, e.mod(phi));
        BigInteger d = vals[0];
        BigInteger p = vals[2];
        BigInteger q = vals[1].subtract(e.divide(phi).multiply(vals[2]));
        return new BigInteger[]{
                d, p, q
        };
    }


    /**
     * genera E trova una Phi tale che siano coprimi (gcd = 1)
     */
    private BigInteger getE(BigInteger phi, int numerocifre) {
        Random rand = new Random();
        BigInteger e;
        do {
            e = new BigInteger(numerocifre, rand);
            while (e.min(phi).equals(phi)) {
                e = new BigInteger(numerocifre, rand);
            }
        } while (!gcd(e, phi).equals(BigInteger.ONE));
        return e;
    }

    private BigInteger getn(BigInteger p, BigInteger q) {
        return p.multiply(q);

    }

    public ArrayList<BigInteger> getprivatesKey(int numerocifre) {
        //genero le chiavi private del client
        ArrayList<BigInteger> chiaviprivate = new ArrayList<>();
        // genero e aggiungo P all'array
        BigInteger P = this.getKey(numerocifre);
        chiaviprivate.add(P);
        // genero e aggiungo Q all'array
        BigInteger Q = this.getKey(numerocifre);
        chiaviprivate.add(Q);
        return chiaviprivate;
    }

    public ArrayList<BigInteger> computeAndSavePublicKeyOnFile(ArrayList<BigInteger> chiaviPrivate, int numerocifre, String nomeFile) throws IOException {
        // genero le chiavi pubbliche del client
        ArrayList<BigInteger> chiavipubbliche = new ArrayList<>();
        //genero e aggiungo N all'array
        chiavipubbliche.add(this.getn(chiaviPrivate.get(0), chiaviPrivate.get(1)));
        BigInteger phi = this.getPhi(chiaviPrivate.get(0), chiaviPrivate.get(1));
        //genero e aggiungo E all'array
        BigInteger E = this.getE(phi, numerocifre);
        chiavipubbliche.add(E);
        // genero e aggiungo D nell'array
        BigInteger D = this.extEuclid(E, phi)[1];
        chiavipubbliche.add(D);
        FileManager fileManager = new FileManager();
        fileManager.saveOnFile(chiavipubbliche, nomeFile);
        return chiavipubbliche;
    }
}