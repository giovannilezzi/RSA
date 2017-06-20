package algoritmoTestPrimalita;

import java.math.BigInteger;

/**
 * Questa interfaccia contiene l'algoritmo per verificare la primalità di un numero primo - Design Pattern Strategy
 *
 * @author Giovanni
 */
public interface IAlgoritmoTestPrimalitaStrategy {
    /**
     * Testa la primalità del numero intero dispari n, per diverse volte.
     *
     * @param n     Numero intero da testare.
     * @param times Numero di volte in cui effettuare il test.
     * @return True se il numero è probabilmente primo. False se è composto.
     */
    public boolean testaPrimalitaIntero(BigInteger n, int times);
}
