package r0p3GUI;

import java.io.IOException;

import r0p3.SystemFile;

public class Main {

    public static void main (String [] argv) throws IOException {
        Table tbl = new Table("File System Stats");
        SystemFile fs = new SystemFile();
        LoadScreen lds = new LoadScreen(); 
        Thread thrd = new Thread(lds);

        String path = tbl.selectDirectory();
		
        if (path != null) {
	        javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    thrd.start();
                }
            });
            try {
                fs.travelDirectories(path, false);
                lds.close();
                thrd.join();
                
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
	        javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    if (path != null) {
                        try {
                            tbl.createAndShowGUI(fs);

                        } catch (IOException e)  {
                            e.printStackTrace();
                        } 
                    }
                }
            });
        }
    }

}
