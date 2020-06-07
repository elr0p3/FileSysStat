package r0p3GUI;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class LoadScreen extends JFrame implements Runnable {

    static final long serialVersionUID = 0l;
    private JLabel label;
    private ImageIcon loading;

    /**
     * LoadScreen constructor
     *
     * @param name  the name of the window
     * */
    public LoadScreen (String name) {
        super(name);
        label = new JLabel();
        loading = new ImageIcon(System.getProperty("user.dir") + "/gif/loading.gif");
    }

    /**
     * Create and show the GUI
     * */
    public void createAndShowGUI () {
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
        label.setIcon(loading);
        add(label, BorderLayout.CENTER);
		
        pack();
		setVisible(true);
    }
    
    /**
     * Close the GUI
     * */
    public void close () {
        setVisible(false);
    }

    /**
     * Run the thread to create and show the GUI
     * */
    @Override
    public void run() {
        createAndShowGUI();
    }

}
