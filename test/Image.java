
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class Image {
	
	public static void main (String [] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				createShowGUI();
			}
		});
	}

	public static void createShowGUI () {
		JFrame frame = new JFrame("Hello World!");
		frame.setSize(1920/2, 1080/2);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//frame.getContentPane().setBackground(new Color(80, 80, 80));
		//frame.setBackground(Color.pink);
		
		
		setPanels(frame);

		//insideGUI(frame);
		
		
		frame.pack();
		frame.setVisible(true);
	}
	
	
	public static void setPanels (JFrame frame) {
		JPanel panelSuperior = new JPanel();
		frame.add(panelSuperior, BorderLayout.NORTH);
		panelSuperior.setLayout(new FlowLayout());
		panelSuperior.setBackground(new Color(255, 255, 255));
		
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JPanel panelPrincipal = new JPanel();
		frame.add(panelPrincipal, BorderLayout.CENTER);
		//panelPrincipal.setLayout(new GridLayout(3, 3));
		panelPrincipal.setBackground(new Color(123, 123, 123));
		
		JLabel lblImage = new JLabel("");
		lblImage.setIcon(new ImageIcon("../gif/java.jpg"));
		//lblImage.setPreferredSize(new Dimension(100, 100));
		panelPrincipal.add(lblImage, 1, 0);
		
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JPanel panelInferior = new JPanel();
		frame.add(panelInferior, BorderLayout.SOUTH);
		GridLayout flayout = new GridLayout(2, 3);
		panelInferior.setLayout(flayout);
		flayout.setHgap(20);
		panelInferior.setBackground(new Color(0, 0, 0));
		
		JLabel text1 = new JLabel("PAPOPEPO");
		panelInferior.add(text1);
		text1.setForeground(Color.white);
		JLabel text2 = new JLabel("NO SE QUE PONER");
		panelInferior.add(text2);
		text2.setForeground(Color.white);
		JLabel text3 = new JLabel("");
		panelInferior.add(text3);
		String opt[] = {"14", "69", "420", "666"};
	    JComboBox opciones = new JComboBox(opt);
	    panelInferior.add(opciones);
		JButton button = new JButton("BOTON INUTIL");
		panelInferior.add(button, 1, 2);
	}
	
	public static void insideGUI (JFrame frame) {
		
		JButton button = new JButton("BOTON INUTIL");
		frame.getContentPane().add(button, 0, 0);
		button.setPreferredSize(new Dimension(50, 50));
		JButton button2 = new JButton("BOTON INUTIL 2");
		frame.getContentPane().add(button2, 0, 1);
		JButton button3 = new JButton("BOTON INUTIL 3");
		frame.getContentPane().add(button3, 1, 0);
		JButton button4 = new JButton("BOTON INUTIL 4");
		frame.getContentPane().add(button4, 1, 1);
		
		JLabel label = new JLabel("Hello World!");
		frame.getContentPane().add(label, 2, 1);
		
		
	}
	
}
