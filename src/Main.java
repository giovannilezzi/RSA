/**
 * Created by root on 17/06/17.
 */

import Utility.FileManager;
import algoritmoWiener.IAlgoritmoAttaccoStrategy;
import algoritmoWiener.AlgoritmoAttaccoWienerStrategy;

import java.io.*;
import java.util.ArrayList;

public class Main {





    public static void main(String[] args) throws IOException {
        FileManager fileManager = new FileManager();
        ArrayList<String> chiavipubbliche = fileManager.readFromFile("ChiaviPubblicheClient");
        IAlgoritmoAttaccoStrategy Mallory = new AlgoritmoAttaccoWienerStrategy();
        Mallory.attack(chiavipubbliche);
        Mallory.printResult();
    }



}