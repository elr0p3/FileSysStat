package r0p3GUI;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class LoadScreen extends JFrame implements Runnable {

    static final long serialVersionUID = 0l;
    JPanel panel;
    JLabel label;
    ImageIcon loading;

    public LoadScreen () {
        panel = new JPanel();
        label = new JLabel();
        loading = new ImageIcon(System.getProperty("user.dir") + "/gif/loading.gif");
    }

    public void createAndShowGUI () {
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
        label.setIcon(loading);
        add(label, BorderLayout.CENTER);
		
        pack();
		setVisible(true);
    }

    public void close () {
        setVisible(false);
    }

    public void run() {
        createAndShowGUI();
    }

    // public static void main (String[] argv) {
        // LoadScreen lds = new LoadScreen();

           // javax.swing.SwingUtilities.invokeLater(new Runnable() {
            // public void run() {
                // lds.createAndShowGUI();
            // }
        // });

    // }

}