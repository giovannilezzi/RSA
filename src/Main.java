import utility.FileManager;
import algoritmoAttacco.IAlgoritmoAttaccoStrategy;
import algoritmoAttacco.AlgoritmoAttaccoWienerStrategy;

import java.io.*;
import java.util.ArrayList;

/**
 * Main
 * @author Giovanni
 *
 */
public class Main {

    public static void main(String[] args) throws IOException {
        FileManager fileManager = new FileManager();
        ArrayList<String> chiavipubbliche = fileManager.readFromFile("ChiaviPubblicheClient");
        IAlgoritmoAttaccoStrategy Mallory = new AlgoritmoAttaccoWienerStrategy();
        Mallory.attack(chiavipubbliche);
        Mallory.printResult();
    }



}