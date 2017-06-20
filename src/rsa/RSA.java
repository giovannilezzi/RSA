package rsa;

import utility.FileManager;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Classe che Implementa l'algoritmo RSA
 *
 *@author Giovanni
 */
public class RSA {
    /**
     * Costruttore
     */
    public RSA() {}

    /**
     * Prende una stringa e lo converte in intero
     * @param text
     * @return Biginteger
     */
    private BigInteger stringToCipher(String text)
    {
        return new BigInteger(text.getBytes());
    }

    /**
     * Prende un Intero codificato e lo converte in testo
     * @param text testo codificato in interi
     * @return il testo decodificato in stringa
     */
    private String cipherToString(BigInteger text)
    {
        return new String(text.toByteArray());
    }

    /**
     * Cripta il messaggio da inviare leggendo le chiavi da file
     * @param messaggioDaInviare stringa che rappresenta il messaggio da inviare
     * @param nomefile stringa che rappresenta il nome del file da cui leggere le chiavi
     * @return il messaggio criptato
     * @throws IOException
     */
    public BigInteger scriviEcripta (String messaggioDaInviare, String nomefile) throws IOException {

        FileManager fileManager = new FileManager();
        ArrayList<String> chiavipubbliche;
        chiavipubbliche = fileManager.readFromFile(nomefile);
        BigInteger N = new BigInteger(chiavipubbliche.get(0));
        BigInteger E = new BigInteger(chiavipubbliche.get(1));
        return this.encrypt(this.stringToCipher(messaggioDaInviare), E, N);

    }

    /**
     * Cripta il messaggio
     * @param message messaggio da criptare
     * @param e chiave pubblica
     * @param n cgave pubblica
     * @return il messaggio criptato
     */
    public BigInteger encrypt(BigInteger message, BigInteger e, BigInteger n) {
        return message.modPow(e, n);
    }

    /**
     * Decripta il messaggio
     * @param message messaggio da decriptare
     * @param d chiave pubblica
     * @param n chiave pubblica
     * @return messaggio decriptato
     */
    public String decrypt(BigInteger message, BigInteger d, BigInteger n) {

        return this.cipherToString(message.modPow(d,n));
    }

}
