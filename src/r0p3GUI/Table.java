package r0p3GUI;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.io.IOException;
import java.util.Map;
import java.awt.GridBagConstraints;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import r0p3.Directory;
import r0p3.FileData;

public class Table extends JFrame {

    // porque sino se me queja
    private static final long serialVersionUID = 1L;
    
    private JFileChooser chooser;
    private JMenuBar menubar;
    private JMenu file;
    // private JMenuItem nuevoPedido;
    private JTree fileSysTree;
    private DefaultMutableTreeNode root;
    private TFileData tdata;
    private TreeTable ttable;

    private String pathToStart;

	private Map<String, FileData> fileData;
    private Directory fs_dir;

    public Table (String title) {
        super(title);
        chooser = new JFileChooser();
        menubar = new JMenuBar();
        file = new JMenu("File");
        tdata = new TFileData();
    }


    /**
     *  Method to select a directory to start scanning
     *
     *  @return     the path where it start scanning
     * */
    public String selectDirectory () {
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle("Directory Chooser");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);

        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            System.out.println("getCurrentDirectory(): " + chooser.getCurrentDirectory());
            System.out.println("getSelectedFile() : " + chooser.getSelectedFile());
            pathToStart = chooser.getSelectedFile().toString();
            return pathToStart;
        }
        System.out.println("No Selection ");
        return null;
    }


    /**
     *  Positions the items in the gui
     * */
    public void createAndShowGUI (Map<String, FileData> fd, Directory dir)
            throws IOException {
        fileData = fd;
        fs_dir = dir;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// setSize(new Dimension(640, 480));
		
        add(setMenu(), BorderLayout.NORTH);
        add(addComponentsToPane(), BorderLayout.CENTER);
		
        pack();
		setVisible(true);
    }

    public JMenuBar setMenu () {
        menubar.add(file);

        return menubar;
    }

    public JPanel addComponentsToPane () throws IOException {
        JPanel panel = new JPanel();
        panel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        panel.setLayout(new GridBagLayout());
	    GridBagConstraints gbc = new GridBagConstraints();
        // gbc.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gbc.fill = GridBagConstraints.BOTH;

        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JScrollPane(setFileSystemTree()), gbc);

        // gbc.weightx = 1.0;
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(new JScrollPane(new JTable(setFileDataTable())), gbc);

        // gbc.fill = GridBagConstraints.HORIZONTAL;
        // gbc.gridx = 0;
        // gbc.gridy = 1;
        // panel.add(, gbc);

        return panel;
    }
    
    public JTree setFileSystemTree () throws IOException {
        // root = new DefaultMutableTreeNode(pathToStart);
        // DefaultMutableTreeNode otro = new DefaultMutableTreeNode("Maricon");
        // root.add(otro);
        ttable = new TreeTable(fs_dir);
        fileSysTree = new JTree(ttable.setTree());

        return fileSysTree;
    }

    public TFileData setFileDataTable () {
        tdata.setElements(fileData);
        return tdata;
    }

}
