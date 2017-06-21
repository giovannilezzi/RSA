package algoritmoAttacco;

import java.math.BigInteger;
import java.util.ArrayList;

/** Questa interfaccia Implementa l'attacco
 *
 * @author Giovanni
 */

public interface IAlgoritmoAttaccoStrategy {

    /**
     *
     * @param key lista di stringhe che sono le chiavi pubbliche
     */
    public void attack(ArrayList<String> key);

    /**
     * Questa classe stampa il risultato
     */
    public void printResult();
}
