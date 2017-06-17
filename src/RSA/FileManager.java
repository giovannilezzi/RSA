package RSA;

import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Created by root on 17/06/17.
 */

public class FileManager{

    public FileManager() {
    }

    public void saveOnFile(ArrayList<BigInteger> chiavi, String nomefilie) throws IOException {
        File file = new File(nomefilie + ".bin");
        FileWriter fw = new FileWriter(file);
        for(int i =0; i<chiavi.size(); i++){
            fw.write(chiavi.get(i) + "\r\n");
            fw.flush();
        }
        fw.close();
    }

    public ArrayList<String> readFromFile(String nomefile) throws IOException {
        File file = new File(nomefile + ".bin");
        FileReader fr = new FileReader(file);
        BufferedReader reader = new BufferedReader(fr);
        ArrayList<String> righe = new ArrayList<>();
        for(int i = 0; i < 3; i++){
            righe.add(reader.readLine());
        }
        return righe;
    }
}
