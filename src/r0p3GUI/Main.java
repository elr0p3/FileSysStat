package r0p3GUI;

import java.io.IOException;

import r0p3.SystemFile;

public class Main {

    public static void main (String [] argv) throws IOException {
        Table tbl = new Table("File System Stats");
        SystemFile fs = new SystemFile();
        LoadScreen lds = new LoadScreen(); 
		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                String path = tbl.selectDirectory();

                if (path != null) {
                    try {

                        lds.createAndShowGUI();
                        fs.travelDirectories(path, false);
                        // lds.close();
            	        
                        tbl.createAndShowGUI(fs);

                    } catch (IOException e)  {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

}
