package utility;

import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Questa classe serve per leggere e scrivere da e su file
 *
 * @author Giovanni
1 */

public class FileManager{
    /**
     * Costruttore
     */
    public FileManager() {}

    /**
     * Salva le chiavi Pubbliche su file
     * @param chiavi chiavi Pubbliche da salvare
     * @param nomefilie ome del file su cui salvare
     * @throws IOException
     */
    public void saveOnFile(ArrayList<BigInteger> chiavi, String nomefilie) throws IOException {
        File file = new File(nomefilie + ".bin");
        FileWriter fw = new FileWriter(file);
        for(int i =0; i<chiavi.size(); i++){
            fw.write(chiavi.get(i) + "\r\n");
            fw.flush();
        }
        fw.close();
    }

    /**
     * Legge le chiavi Pubbliche da file
     * @param nomefile nome del file da cui leggere
     * @return Ritorna l'Array di chiavi
     * @throws IOException
     */
    public ArrayList<String> readFromFile(String nomefile) throws IOException {
        File file = new File( nomefile + ".bin");
        FileReader fr = new FileReader(file);
        BufferedReader reader = new BufferedReader(fr);
        ArrayList<String> righe = new ArrayList<>();
        for(int i = 0; i < 3; i++){
            righe.add(reader.readLine());
        }
        return righe;
    }
}
