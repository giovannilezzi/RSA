package algoritmoTestPrimalita;

import java.math.BigInteger;
import java.util.Random;

/**
 * Questa classe rappresenta l'implementazione per verificare la primalità di un numero intero mediante l'applicazione
 * dell'algoritmo di Fermat. - Design Pattern Strategy
 *
 * @author Giovanni
 */

public class AlgoritmoTestPrimalitàFermatStrategy implements IAlgoritmoTestPrimalitaStrategy {
    /**
     * Testa la primalità del numero intero dispari n, per diverse volte.
     *
     * @param n     Numero intero da testare.
     * @param times Numero di volte in cui effettuare il test.
     * @return
     */
    @Override
    public boolean testaPrimalitaIntero(BigInteger n, int times) {
        if (n.equals(BigInteger.ONE))
            return false;

        BigInteger a;
        Random rand = new Random();

        for (int i = 0; i < times; i++) {
            a = new BigInteger(n.bitLength() - 1, rand);
            a = a.modPow(n.subtract(BigInteger.ONE), n);

            if (!a.equals(BigInteger.ONE))
                return false;
        }

        return true;
    }
}
