package r0p3GUI;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import r0p3.Directory;
import r0p3.FileData;
import r0p3.SystemFile;

public class Table extends JFrame {

    // porque sino se me queja
    private static final long serialVersionUID = 1L;
    
    private JFileChooser chooser;
    private JMenuBar menubar;
    private JMenu file;
    private JMenu window;
    private JMenuItem reverse, treeB, tableB, graphB, export;
    private JTree fileSysTree;
    private DefaultMutableTreeNode root;

    private JTable table;
    private TFileData tdata;
    private TreeTable ttable;

    private JLabel[] partition_name;
    private JProgressBar[] partition_sizeBars;

    private String pathToStart;

	private Map<String, FileData> fileData;
    private Directory fs_dir;
    private Map<String, Long> total;
    private Map<String, Long> used;


    public Table (String title) {
        super(title);
        chooser = new JFileChooser();
        menubar = new JMenuBar();
        file = new JMenu("File");
        window= new JMenu("Window");
        tableB = new JMenuItem("Table");
        treeB = new JMenuItem("Tree");
        graphB = new JMenuItem("Graph");
        reverse = new JMenuItem("Reverse");
        export = new JMenuItem("Export");
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
            System.out.println("getSelectedFile()    : " + chooser.getSelectedFile());
            pathToStart = chooser.getSelectedFile().toString();
            return pathToStart;
        }
        System.out.println("No Selection ");
        return null;
    }

    /**
     *  Method to select a file to store data
     *  
     *  @return     the path where it start scanning
     * */
     private String createFile () throws IOException {
        File file = null;
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle("Directory Chooser");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);

        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            System.out.println("getCurrentDirectory(): " + chooser.getCurrentDirectory());
            System.out.println("getSelectedFile()    : " + chooser.getSelectedFile());
            // file = new File(chooser.getSelectedFile() + "/saved_data.txt");
            file = chooser.getSelectedFile();
            if (file.getName().equals(".")) {
                System.err.println("ERROR! Wrong file name");
                return null;
            }
            if (!file.createNewFile())
                System.err.println("ERROR! Cannot create the file");
            System.out.println("PATH -> " + file.getPath().toString());
            return file.getPath().toString();
        }
        System.out.println("No Selection ");
        return null;
    }

    /**
     *  Positions the items in the gui
     * */
    public void createAndShowGUI (SystemFile sf) throws IOException {
        fileData = sf.getFileData();
        fs_dir = sf.getDirTree();
        total = sf.getPartitionSize();
        used = sf.getPartitionUsed();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
        add(setMenu(total, used), BorderLayout.NORTH);
        add(addComponentsToPane(), BorderLayout.CENTER);
        add(graphZone(total, used), BorderLayout.SOUTH);
		
        pack();
		setVisible(true);
    }

    private JMenuBar setMenu (Map<String, Long> total, Map<String, Long> used) 
            throws IOException {
        menubar.add(file);
        menubar.add(window);
        window.add(tableB);
        window.add(treeB);
        window.add(graphB);
        window.add(reverse);
        menubar.add(export);

        tableB.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
                tableWindow();
			}
		});

        treeB.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
                try {
                    treeWindow();
                } catch (IOException e) {
                    e.printStackTrace();
                }
			}
		});


        graphB.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
                graphWindow(total, used);
			}
		});

        reverse.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
                tdata.reverseElements();
                table.updateUI();
			}
		});

        export.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
                try {
                    exportData();
                } catch (IOException e) {
                    e.printStackTrace();
                }
			}
		});

        return menubar;
    }

    private void tableWindow () {
        JDialog frameWindow = new JDialog();

        JTable new_table = new JTable(setFileDataTable());
        frameWindow.add(new JScrollPane(new_table));
        frameWindow.pack();
		frameWindow.setVisible(true);
    }

    private void treeWindow () throws IOException {
        JDialog frameWindow = new JDialog();

        frameWindow.add(new JScrollPane(setFileSystemTree()));
        frameWindow.pack();
		frameWindow.setVisible(true);
    }


    private void graphWindow (Map<String, Long> total, Map<String, Long> used) {
        JDialog frameWindow = new JDialog();

        frameWindow.add(graphZone(total, used));
        frameWindow.pack();
		frameWindow.setVisible(true);
    }

    private JPanel graphZone (Map<String, Long> total, Map<String, Long> used) {
        Integer sz = total.size();
        int i = 0;
		JPanel panel = new JPanel(new GridLayout(sz, 1));
        
        partition_name = new JLabel[sz];
        for (Map.Entry<String, Long> entry : total.entrySet()) {
            partition_name[i] = new JLabel(entry.getKey());
            i++;
        }

        i = 0;

        partition_sizeBars = new JProgressBar[sz];
        for (Map.Entry<String, Long> entry : total.entrySet()) {
            partition_sizeBars[i] = new JProgressBar(0, 100);
            Long use = used.get(entry.getKey());
            partition_sizeBars[i].setValue((int)(use * 100 / entry.getValue()));
            partition_sizeBars[i].setStringPainted(true);
            i++;
        }

        for (i = 0; i < sz; i++) {
            panel.add(partition_name[i]);
            panel.add(partition_sizeBars[i]);
        }

        return panel;
    }

    private JPanel addComponentsToPane () throws IOException {
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
        table = new JTable(setFileDataTable());
        panel.add(new JScrollPane(table), gbc);

        // gbc.fill = GridBagConstraints.HORIZONTAL;
        // gbc.gridx = 0;
        // gbc.gridy = 1;
        // panel.add(, gbc);

        return panel;
    }
    
    private JTree setFileSystemTree () throws IOException {
        // root = new DefaultMutableTreeNode(pathToStart);
        // DefaultMutableTreeNode otro = new DefaultMutableTreeNode("Maricon");
        // root.add(otro);
        ttable = new TreeTable(fs_dir);
        fileSysTree = new JTree(ttable.setTree());

        return fileSysTree;
    }

    private TFileData setFileDataTable () {
        tdata.setElements(fileData);
        return tdata;
    }

    private void exportData () throws IOException {
        String path = createFile(); 
        if (path != null) {
            FileWriter file = new FileWriter(path, true);
            for (FileData f : tdata.getTableData()) {
                file.append("EXTENTION:" + f.getExtention() + "\n");
                // file.append("PERCENTAGE:" + String.format("%.16f", f.getPercentage()) + "\n");
                file.append("PERCENTAGE:" + f.getPercentage().toString() + "\n");
                file.append("NUMBER:" + f.getNumberOfFiles().toString() + "\n");
                file.append("SIZE:" + f.getSize().toString() + "\n");
                file.append("\n");
            }
            file.close();
        } else {
            System.err.println("ERROR! Cannot access the dir");
        }
    }

}
