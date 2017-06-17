package algoritmoWiener;

import java.math.*;
import java.util.ArrayList;

/**
 * Created by root on 17/06/17.
 */


public class AlgoritmoAttaccoWienerStrategy implements IAlgoritmoAttaccoStrategy{

    public final static BigInteger zero = BigInteger.ZERO;
    public final static BigInteger one = BigInteger.ONE;
    public final static BigInteger two = new BigInteger("2");
    public final static BigInteger three = new BigInteger("3");
    private WienerResult _result;

    public AlgoritmoAttaccoWienerStrategy() {
        _result = new WienerResult();
    }
    @Override
    public void attack(ArrayList<String> key) {
        BigInteger temp, x, y;
        BigInteger q_0, q_i, d_i, d_i_1, d_i_2, n_i, n_i_1, n_i_2, n_1, n_0, d_1, d_0;
        BigInteger guess_d, guess_k, guess_dg, phi_n;
        BigInteger p_plus_q_div_2, p_minus_q_div_2;

        BigInteger n = new BigInteger(key.get(0));
        BigInteger e = new BigInteger(key.get(1));

        x = e;
        y = n;

        // i = 0
        // q[0] = [e/n]
        // n[0] = q[0]
        // d[0] = 1
        q_0 = x.divide(y);
        n_0 = q_0;
        d_0 = BigInteger.ONE;

        temp = x;
        x = y;
        y = temp.add((y.multiply(q_0)).negate());

        // i = 1
        // q[1] = [n / (e - n * [e/n])]
        // n[1] = q[0] * q[1] + 1
        // d[1] = q[1]
        q_i = x.divide(y);
        n_1 = q_0.multiply(q_i).add(BigInteger.ONE);
        n_i = n_1;
        d_1 = q_i;
        d_i = d_1;

        // i = 2
        d_i_2 = d_0;
        d_i_1 = d_1;
        d_i = d_i_1;

        n_i_2 = n_0;
        n_i_1 = n_1;
        n_i = n_i_1;

        int i = 1;
        while(x.add((y.multiply(q_i)).negate()).signum() != 0) {

            i++;

            temp = x;
            x = y;
            y = temp.add((y.multiply(q_i)).negate());

            q_i = x.divide(y);

            // d[i] = q[i] * d[i-1] + d[i-2]
            // n[i] = q[i] * n[i-1] + n[i-2]
            d_i = q_i.multiply(d_i_1).add(d_i_2);
            n_i = q_i.multiply(n_i_1).add(n_i_2);

            d_i_2 = d_i_1;
            d_i_1 = d_i;
            n_i_2 = n_i_1;
            n_i_1 = n_i;

            if(i % 2 == 0){
                // k / dg = <q[0],...,q[i-1],q[i]+1>
                // k = (q[i] + 1) * n[i-1] + n[i-2]
                guess_k = n_i_1.add(n_i_2);
                // dg = (q[i] + 1) * d[i-1] + d[i-2]
                guess_dg = d_i_1.add(d_i_2);
            } else {
                // k / dg = <q[0],...,q[i-1],q[i]>
                // k = q[i] * n[i-1] + n[i-2]
                guess_k = n_i_1;
                // dg = q[i] * d[i-1] + d[i-2]
                guess_dg = d_i_1;
            }

            // phi(n) = (edg) / k
            phi_n = e.multiply(guess_dg).divide(guess_k);

            // (p+q)/2 = (pq - (p-1)*(q-1) + 1)/2
            p_plus_q_div_2 = n.subtract(phi_n).add(one).divide(two);

            if(isSquare(p_plus_q_div_2.pow(2).subtract(n))){
                // ((p-q)/2)^2 = ((p+q)/2)^2 - pq
                p_minus_q_div_2 = sqrt(p_plus_q_div_2.pow(2).subtract(n));
                // d = (dg / g) = dg / (edg mod k)
                guess_d = (guess_dg.divide(e.multiply(guess_dg).mod(guess_k)));
                // (p+q)/2 = (pq - (p-1)*(q-1) + 1)/2
                BigInteger guess_p = p_plus_q_div_2.add(p_minus_q_div_2);
                // q = (p+q)/2 - (p-q)/2
                BigInteger guess_q = p_plus_q_div_2.subtract(p_minus_q_div_2);
                this._result = new WienerResult(guess_d, guess_p, guess_q, true);
            }
        }
    }

    public void printResult() {
        if(_result.success) {
            System.out.println("p = " + _result.p.toString());
            System.out.println("q = " + _result.q.toString());
            System.out.println("d = " + _result.d.toString());
        } else {
            System.out.println("Unable to get the private key!");
        }
    }

    public static boolean isSquare(BigInteger n) {
        if(sqrt(n).pow(2).subtract(n).compareTo(zero) != 0)
            return false;
        else return true;
    }

    public static BigInteger sqrt(BigInteger n) {
        BigInteger a = one;
        BigInteger b = new BigInteger(n.shiftRight(5).add(new BigInteger("8")).toString());
        while(b.compareTo(a) >= 0) {
            BigInteger mid = new BigInteger(a.add(b).shiftRight(1).toString());
            if(mid.multiply(mid).compareTo(n) > 0)
                b = mid.subtract(one);
            else a = mid.add(one);
        }
        return a.subtract(one);
    }

}

