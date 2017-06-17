package algoritmoWiener;

import java.math.BigInteger;

/**
 * Created by root on 17/06/17.
 */
public class WienerResult {
    public BigInteger d;
    public BigInteger p;
    public BigInteger q;
    public boolean success;

    public WienerResult() {
        this.success = false;
    }

    WienerResult(BigInteger d, BigInteger p, BigInteger q, boolean success) {
        this.d = d;
        this.p = p;
        this.q = q;
        this.success = success;
    }
}