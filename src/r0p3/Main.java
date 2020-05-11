package r0p3;

import java.io.IOException;

public class Main {

    public static void main (String [] args) throws IOException {

        SystemFile fs = new SystemFile();
        fs.travelDirectories("/", false);
        System.out.println();
        // fs.printFileSystem();

        System.out.println();
        fs.printFileData();
    }
        
}
