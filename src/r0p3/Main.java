package r0p3;

import java.io.IOException;

public class Main {

    public static void main (String [] args) throws IOException {

        SystemFile fs = new SystemFile();
        fs.travelDirectories("./");
        System.out.println();
        fs.printFileSystem();

        System.out.println("\n");
        System.out.println("Total space: " + fs.getTotalSpace() + "B");
        System.out.println("Used space:  " + fs.getUsedSpace() + "B");
        System.out.println("Dir num:     " + fs.getNumDirectories());
        System.out.println("File num:    " + fs.getNumFiles());
    }
        
}
