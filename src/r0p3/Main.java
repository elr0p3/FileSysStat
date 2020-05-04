package r0p3;

import java.io.IOException;

public class Main {

    public static void main (String [] args) throws IOException {

        FileSystem fs = new FileSystem();
        fs.travelDirectories("./");

    }

}
