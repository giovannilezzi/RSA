package rsa;

import Utility.FileManager;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Created by root on 17/06/17.
 */
public class RSA {

    public RSA() {}

    /**
     * Prende una stringa e lo converte in intero
     * Returns BigInteger
     */
    private BigInteger stringToCipher(String text)
    {
        return new BigInteger(text.getBytes());
    }

    /**
     * Prende un Intero codificato e lo converte in testo
     *	returns a String
     */
    private String cipherToString(BigInteger text)
    {
        return new String(text.toByteArray());
    }

    public BigInteger scriviEcripta (String messaggioDaInviare, String nomefile) throws IOException {

        FileManager fileManager = new FileManager();
        ArrayList<String> chiavipubbliche;
        chiavipubbliche = fileManager.readFromFile(nomefile);
        BigInteger N = new BigInteger(chiavipubbliche.get(0));
        BigInteger E = new BigInteger(chiavipubbliche.get(1));
        return this.encrypt(this.stringToCipher(messaggioDaInviare), E, N);

    }

    public BigInteger encrypt(BigInteger message, BigInteger e, BigInteger n) {
        return message.modPow(e, n);
    }

    public String decrypt(BigInteger message, BigInteger d, BigInteger n) {

        return this.cipherToString(message.modPow(d,n));
    }

}
