package rsa;

import utility.FileManager;
import utility.UtilityIntegerNumber;
import algoritmoTestPrimalita.AlgoritmoTestPrimalitaMillerRabinStrategy;
import algoritmoTestPrimalita.IAlgoritmoTestPrimalitaStrategy;
import java.io.*;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Classe che genera le chiavi pubbliche e private
 *
 *@author Giovanni
 */
public class KeyGenerator {
    /***
     * Accuratezza
     */
    private static int _accuracy = 100;

    /**
     * Limite superiore alla ricerca dei numeri primi, utilizzati per testare
     * che un generico numero intero, non sia un multiplo di essi. Si utilizza a monte
     * del test di Miller-Rabin per effettuare una scrematura sui numeri interi proposti.
     */
    private static BigInteger _upperBoundRicercaPrimi = new BigInteger("256");


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

    private BigInteger getD(){
        BigInteger numberStart_d = new BigInteger(10, new SecureRandom());
        BigInteger d = getFirstPrimeNumberAfterNumber(numberStart_d, _accuracy);
        return d;
    }

    private BigInteger getEWiener(BigInteger d, BigInteger fi_n){
         return  d.modInverse(fi_n);
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

    /**
     * Calcola N=P*Q
     * @param p
     * @param q
     * @return
     */

    private BigInteger getn(BigInteger p, BigInteger q) {
        return p.multiply(q);

    }

    /**
     * Restituisce le chiavi private
     * @param numerocifre
     * @return
     */
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

    /**
     * Calcola e salva le chiavi pubbliche su un file esterno
     *
     * @param chiaviPrivate
     * @param numerocifre
     * @param nomeFile
     * @return
     * @throws IOException
     */
    public ArrayList<BigInteger> computeAndSavePublicKeyOnFile(ArrayList<BigInteger> chiaviPrivate, int numerocifre, String nomeFile) throws IOException {
        // genero le chiavi pubbliche del client
        ArrayList<BigInteger> chiavipubbliche = new ArrayList<>();
        //genero e aggiungo N all'array
        chiavipubbliche.add(this.getn(chiaviPrivate.get(0), chiaviPrivate.get(1)));
        BigInteger phi = this.getPhi(chiaviPrivate.get(0), chiaviPrivate.get(1));
        //genero e aggiungo E all'array
        BigInteger D = this.getD();
        //BigInteger E = this.getE(phi, numerocifre);
        BigInteger E = this.getEWiener(D, phi);
        chiavipubbliche.add(E);
        // genero e aggiungo D nell'array
       // BigInteger D = this.extEuclid(E, phi)[1];
        chiavipubbliche.add(D);
        FileManager fileManager = new FileManager();
        fileManager.saveOnFile(chiavipubbliche, nomeFile);
        return chiavipubbliche;
    }

    /**
     * Metodo per ottenere il primo 'numero primo' dopo un numero intero dato.
     *
     * @param number   Numero dal quale avviare la ricerca.
     * @param accuracy Precisione nella valutazione di primalità.
     */
    public static BigInteger getFirstPrimeNumberAfterNumber(BigInteger number, int accuracy) {
        // Numero primo da restituire
        BigInteger primeNumber = null;
        // Booleano che rappresenta la condizione di uscita.
        boolean trovato = false;
        // Strategia
        IAlgoritmoTestPrimalitaStrategy algoritmoTestPrimalitaStrategy = new AlgoritmoTestPrimalitaMillerRabinStrategy();
        //IAlgoritmoTestPrimalitaStrategy algoritmoTestPrimalitaStrategy = new AlgoritmoTestPrimalitàFermatStrategy();
//		System.out.println("Numero di partenza: " + number);

        // Carico la lista dei numeri primi precedenti a number.
        List<BigInteger> listaNumeriPrimiPrecedentiNumber = UtilityIntegerNumber.getListaPrimiPrecedentiNumber(_upperBoundRicercaPrimi, _accuracy);
        // Ciclo finch� non trovo il numero primo.
        while (!trovato) {
            // Effettuo il test e salvo l'esito in trovato
            trovato = algoritmoTestPrimalitaStrategy.testaPrimalitaIntero(number, accuracy);
            // Se l'esito del test � positivo assegno il valore di number a primeNumber.
            if (trovato == true) {
                primeNumber = number;
            } else {
				/*
				 * Ricavo il numero intero, successivo a number, buon candidato ad essere un numero primo.
				 */
                number = KeyGenerator.nextIntegerNotDivisibleBySeveralPrime(number, listaNumeriPrimiPrecedentiNumber);
            }
        }
//		System.out.println("Prime number: " + primeNumber);

        return primeNumber;
    }

    /**
     * Metodo per ottenere un numero intero successivo a quello dato, non divisibile dalla lista
     * dei numeri primi precedenti all'attributo _upperBoundRicercaPrimi, quindi si ottiene un buon
     * candidato ad essere un numero primo.
     *
     * @param number     Numero dal quale si parte per effettuare il test.
     * @param listaPrimi Lista dei primi sulla quale effettuare il test.
     * @return Numero intero non divisibile dalla lista dei numeri primi precedenti di _upperBoundRicercaPrimi.
     */
    private static BigInteger nextIntegerNotDivisibleBySeveralPrime(BigInteger number, List<BigInteger> listaPrimi) {
        // BigInteger da restituire
        BigInteger integerNotDivisibleBySeveralPrime = null;
        // Condizione di uscita dal ciclo
        boolean trovato = false;
        // Incremento number di 1, per passare ad effettuare il testing dal primo numero successivo a quello dato.
        number = number.add(BigInteger.ONE);
        // Ciclo finche non trovo un buon numero
        while (!trovato) {
			/*
			 * Condizione per verificare se il numero da testare in questa iterazione del
			 * ciclo � o meno multiplo della lista dei primi.
			 */
            boolean multiplo = false;
            // Ciclo per verificare che il numero nell'iterazione corrente non sia multiplo di un numero primo.
            for (Iterator<BigInteger> iterator = listaPrimi.iterator(); iterator.hasNext(); ) {
                // Generico elemento della lista dei numeri primi
                BigInteger primeNumber = (BigInteger) iterator.next();
                // Verifico che number non sia multiplo di primeNumber
                if (UtilityIntegerNumber.A_multiplo_B(number, primeNumber) == true) {
                    multiplo = true;
                }
            }
			/*
			 * Se il numero appena testato � un multiplo di uno dei primi, incremento
			 * di uno e al prossimo ciclo testo nuovamente.
			 */
            if (multiplo == true) {
                number = number.add(BigInteger.ONE);
            } else {
				/*
				 * Se il numero appena testato non era multiplo di nessuno dei primi allora
				 * � un buon candidato e usciamo dal ciclo.
				 */
                trovato = true;
                // Assegno il valore del numero trovato alla variabile da fornire in output.
                integerNotDivisibleBySeveralPrime = new BigInteger(number.toString());
            }
        }
        return integerNotDivisibleBySeveralPrime;
    }
}