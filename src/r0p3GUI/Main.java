package r0p3GUI;

import java.io.IOException;

import r0p3.SystemFile;

public class Main {

    public static void main (String [] argv) throws IOException {
        Window tbl = new Window("FileSysStat");
        SystemFile fs = new SystemFile();
        LoadScreen lds = new LoadScreen("FileSysStat LOADING..."); 
        Thread thrd = new Thread(lds);  // No es necesario, pero pruebo multithreading

        String path = tbl.selectDirectory();
		
        System.out.println(Thread.currentThread().getName() +
                " -> " + Thread.currentThread().isAlive());
        if (path != null) {

	        javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    System.out.println(thrd.getName() + " -> " + thrd.isAlive());
                    thrd.start();
                    System.out.println(thrd.getName() + " -> " + thrd.isAlive());
                }
            });

            fs.travelDirectories(path, false);
            lds.close();    
	        
            javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    try {
                        tbl.createAndShowGUI(fs);
                    } catch (IOException e)  {
                        e.printStackTrace();
                    } 
                }
            });
        }
    }

}
