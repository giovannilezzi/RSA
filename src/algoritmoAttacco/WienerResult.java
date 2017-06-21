package algoritmoAttacco;

import java.math.BigInteger;

/**
 * Questa classe implementa il risultato dell'attacco di Wiener
 *
 * @author Giovanni
 */
public class WienerResult {
    public BigInteger d;
    public BigInteger p;
    public BigInteger q;
    public boolean success;

    /**
     * Costruttore
     */
    public WienerResult() {
        this.success = false;
    }

    /**
     * Metodo che serve per il risultato dell'attacco di Wiener
     *
     * @param d chiave privata
     * @param p chiave privata
     * @param q chiave privata
     * @param success
     */
    WienerResult(BigInteger d, BigInteger p, BigInteger q, boolean success) {
        this.d = d;
        this.p = p;
        this.q = q;
        this.success = success;
    }
}