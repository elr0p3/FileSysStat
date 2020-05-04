package test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.io.File;

public class Test {


    public static void main (String [] args) throws IOException {

        
        FileSystem fileSystem = FileSystems.getDefault();

        System.out.printf("%40s | %15s | %20s | %20s \n", "", "TYPE", "TOTAL SPACE", "FREE SPACE");
        System.out.println("-------------------------------------------------"
                + "----------------------------------------------------------");
        
        Iterable<FileStore> iterable = fileSystem.getFileStores();
        iterable.forEach(s -> {
            try {
                System.out.printf("%40s | %15s | %20s | %20s \n", s, s.type(), (s.getTotalSpace() / 1073741824f) + " GB", (s.getUsableSpace() / 1073741824f) + "GB");
            } catch (IOException e) {
               e.printStackTrace();
            }
        });

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        for (Path root : FileSystems.getDefault().getRootDirectories()) {

            System.out.print("\n\n" + root + ": ");
            try {
                FileStore store = Files.getFileStore(root);
                System.out.println("available =" + store.getUsableSpace() / (1024 * 1024 * 1204f)
                                + "\ntotal =" + store.getTotalSpace() / (1024 * 1024 * 1204f)
                                + "\nunalocated = " + store.getUnallocatedSpace() / (1024 * 1024 * 1204f));
            } catch (IOException e) {
                System.out.println("error querying space: " + e.toString());
            }
        }

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        File file = new File("/");
        System.out.println("\n\nTotal Space = " + file.getTotalSpace() / (1024 * 1024 * 1024f) + " GB");
        System.out.println("Free Space = " + file.getFreeSpace() / (1024 * 1024 * 1024f) + " GB");

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        Path r = Paths.get("/");
        FileStore f = Files.getFileStore(r);
        System.out.println("\nTOTAL -> " + f.getTotalSpace() / 1073741824f);

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        
   		Iterable<Path> roots = FileSystems.getDefault().getRootDirectories();
        for (Path p : roots) {
            FileStore fs = Files.getFileStore(p);
            System.out.println("\nTOTAL2 -> " + fs.getTotalSpace() / 1073741824f);
        }


    }

}
