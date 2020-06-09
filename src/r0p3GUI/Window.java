package r0p3GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
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
import javax.swing.JTextField;
import javax.swing.JTree;
// import javax.swing.tree.DefaultMutableTreeNode;

import java.util.regex.*;

import r0p3.Directory;
import r0p3.FileData;
import r0p3.SystemFile;

public class Window extends JFrame {

    // porque sino se me queja
    private static final long serialVersionUID = 1L;

    public static final String AUTHOR_NAME  = "Rodrigo Pereira";
    public static final String PROJECT_NAME = "FileSysStat";
    public static final String PROJECT_VERS = "v1.0.2-dcbcac85e1";
    public static final String SOURCE_CODE  = "https://github.com/elr0p3/FileSysStat";
    
    private JFileChooser chooser;
    private JMenuBar menubar;
    private JMenu file, window, filter, help;
    private JMenuItem reverse, treeB, tableB, graphB,
            extensionB, percentageB, numberB, sizeB, unfilterB,
            export, about;
    private JTree fileSysTree;
    // private DefaultMutableTreeNode root;

    private JTable table;
    private TableFileData tdata;
    private Tree ttable;

    private JLabel[] partition_name;
    private JProgressBar[] partition_sizeBars;

    private String pathToStart;

	private Map<String, FileData> fileData;
    private Directory fs_dir;
    private Map<String, Long> total;
    private Map<String, Long> used;


    /**
     * Window constructor
     * */
    public Window () {
        this(PROJECT_NAME);
    }

    /**
     * Window constructor
     *
     * @param title the title of the window
     * */
    public Window (String title) {
        super(title);
        chooser = new JFileChooser();
        menubar = new JMenuBar();
        file   = new JMenu("File");
        // file.setFont(new Font("Sf Mono", Font.PLAIN, 15));
        window = new JMenu("Window");
        filter = new JMenu("Filter");
        help   = new JMenu("Help");
        tableB  = new JMenuItem("Table");
        treeB   = new JMenuItem("Tree");
        graphB  = new JMenuItem("Graph");
        reverse = new JMenuItem("Reverse");
        extensionB  = new JMenuItem("Extension");
        percentageB = new JMenuItem("Percentage");
        numberB     = new JMenuItem("Number");
        sizeB       = new JMenuItem("Size");
        unfilterB   = new JMenuItem("Unfilter");
        export = new JMenuItem("Export");
        about = new JMenuItem("About");
        tdata = new TableFileData();
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
        String default_name = "Table_Data.txt";
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
                String aux = file.getPath();
                aux = aux.substring(0, aux.length() - 1);
                aux += default_name;
                file = new File(aux);
            }
            if (file.isDirectory())
                file = new File(file.getPath() + "/" + default_name);
            else if (file.exists()) {
                System.err.println("ERROR! The file already exists");
                return null;
            }
            if (!file.createNewFile())
                System.err.println("ERROR! Cannot create the file");
            System.out.println("PATH -> " + file.getPath());
            return file.getPath();
        }
        System.out.println("No Selection ");
        return null;
    }

    /**
     *  Positions the items in the gui
     *
     *  @param sf   SystemFile object
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

    /**
     * Set buttons on the top bar
     *
     * @param total total data
     * @param used  used data
     * */
    private JMenuBar setMenu (Map<String, Long> total, Map<String, Long> used) 
            throws IOException {
        menubar.add(file);
        file.add(export);
        
        menubar.add(window);
        window.add(tableB);
        window.add(treeB);
        window.add(graphB);
        window.add(reverse);
        
        menubar.add(filter);
        filter.add(extensionB);
        filter.add(percentageB);
        filter.add(numberB);
        filter.add(sizeB);
        filter.add(unfilterB);
        
        menubar.add(help);
        help.add(about);

        file.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        export.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        window.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        tableB.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        treeB.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        graphB.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        reverse.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        filter.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        extensionB.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        percentageB.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        numberB.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        sizeB.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        unfilterB.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        help.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        about.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));


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

        extensionB.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
                getExtensionFilter();
			}
		});

        percentageB.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
                getPercentageFilter();
			}
		});

        numberB.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
                getNumberFilter();
			}
		});

        sizeB.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
                getSizeFilter();
			}
		});

        unfilterB.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
                unfilterRows();
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

        about.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
                displayInfo();
			}
		});

        return menubar;
    }

    /**
     * Creates a new window containing the table data
     * */
    private void tableWindow () {
        JDialog frameWindow = new JDialog();

        JTable new_table = new JTable(setFileDataTable());
        frameWindow.add(new JScrollPane(new_table));
        frameWindow.pack();
		frameWindow.setVisible(true);
    }

    /**
     * Creates a new window containing the file system tree
     * */
    private void treeWindow () throws IOException {
        JDialog frameWindow = new JDialog();

        frameWindow.add(new JScrollPane(setFileSystemTree()));
        frameWindow.pack();
		frameWindow.setVisible(true);
    }

    /**
     * Creates a new window containing the graphs size
     *
     * @param total total data
     * @param used  used data
     * */
    private void graphWindow (Map<String, Long> total, Map<String, Long> used) {
        JDialog frameWindow = new JDialog();

        frameWindow.add(graphZone(total, used));
        frameWindow.pack();
		frameWindow.setVisible(true);
    }

    /**
     * Display information about the program
     * */
    private void displayInfo () {
        JDialog frameWindow = new JDialog();
        frameWindow.setTitle("About FileSysStat");
        frameWindow.setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel[] l_format = new JLabel[4];
        for (int i = 0; i < 4; i++)
            l_format[i] = new JLabel("                                     ");
        JTextField project_name = new JTextField(Window.PROJECT_NAME);
        JTextField project_vers = new JTextField(Window.PROJECT_VERS);
        JTextField author       = new JTextField(Window.AUTHOR_NAME);

        // JLabel hyperlink = new JLabel("Visit the source code in GitHub", SwingConstants.CENTER);
        JTextField hyperlink = new JTextField("Visit the source code in GitHub");
        hyperlink.setForeground(Color.BLUE.darker());
        hyperlink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        hyperlink.addMouseListener(new MouseAdapter() {
 
            @Override
            public void mouseClicked(MouseEvent e) {
                // the user clicks on the label
                try {
                    Desktop.getDesktop().browse(new URI(Window.SOURCE_CODE));
                } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
 
            @Override
            public void mouseEntered(MouseEvent e) {
                // the mouse has entered the label
            }
 
            @Override
            public void mouseExited(MouseEvent e) {
                // the mouse has exited the label
            }
        });


        // Format text
        project_name.setFont(new Font("SansSerif", Font.BOLD, 20));
        project_name.setHorizontalAlignment(JTextField.CENTER);
        project_vers.setFont(new Font("SansSerif", Font.BOLD, 10));
        project_vers.setHorizontalAlignment(JTextField.CENTER);
        author.setFont(new Font("SansSerif", Font.PLAIN, 12));
        author.setHorizontalAlignment(JTextField.CENTER);
        hyperlink.setFont(new Font("SansSerif", Font.CENTER_BASELINE, 14));
        hyperlink.setHorizontalAlignment(JTextField.CENTER);
        
        // Not edit the text
        project_name.setEditable(false);
        project_vers.setEditable(false);
        author.setEditable(false);
        hyperlink.setEditable(false);

        // Same as JLabel
        project_name.setBackground(null);
        project_vers.setBackground(null);
        author.setBackground(null);
        hyperlink.setBackground(null);
        
        // Remove the border
        project_name.setBorder(null);
        project_vers.setBorder(null);
        author.setBorder(null);
        hyperlink.setBorder(null);

        // Add to panel
        panel.add(l_format[0]);
        panel.add(project_name);
        panel.add(project_vers);
        panel.add(l_format[1]);
        panel.add(author);
        panel.add(l_format[2]);
        panel.add(hyperlink);
        panel.add(l_format[3]);


        frameWindow.add(panel);
        frameWindow.pack();
		frameWindow.setVisible(true);
    }

    /**
     * Creates the graphs showing the partitions storage
     *
     * @param total total data
     * @param used  used data
     * @return      a JPanel containing all the graphs
     * */
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

    /**
     * Add all the components to the Main Pane
     *
     * @return  a JPanel containing the tree and the table
     * */
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
    
    /**
     * Set the File System Tree
     *
     * @return  JTree file system
     * */
    private JTree setFileSystemTree () throws IOException {
        // root = new DefaultMutableTreeNode(pathToStart);
        // DefaultMutableTreeNode otro = new DefaultMutableTreeNode("Maricon");
        // root.add(otro);
        ttable = new Tree(fs_dir);
        fileSysTree = new JTree(ttable.setTree());

        return fileSysTree;
    }

    /**
     * Set the Table containing all the file data
     *
     * @return  a TableFileData object
     * */
    private TableFileData setFileDataTable () {
        tdata.setElements(fileData);
        return tdata;
    }

    /**
     * Exports file data to a file, which will be create
     * */
    private void exportData () throws IOException {
        String path = createFile(); 
        if (path != null) {
            FileWriter file = new FileWriter(path, true);
            for (FileData f : tdata.getTableData()) {
                file.append("EXTENSION:" + f.getExtension() + "\n");
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

    /**
     * Filter by extension button
     * */
    private void getExtensionFilter () {
        JDialog frameWindow = new JDialog();
        frameWindow.setTitle("Extension Filter");
        frameWindow.setResizable(false);
        JPanel panel = new JPanel(new FlowLayout());
        JLabel label = new JLabel("Introduce the file extension: ");
        JTextField text = new JTextField();
        text.setPreferredSize(new Dimension(300, 20));
        JButton button = new JButton("Accept");

        panel.add(label);
        panel.add(text);
        panel.add(button);

        button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
                filterRows(text.getText());
		        frameWindow.setVisible(false);
			}
		});

        frameWindow.add(panel);
        frameWindow.pack();
		frameWindow.setVisible(true);
    }

    /**
     * Filter by extension helper
     * */
    private void filterRows (String data) {

        if (data != null) {
            tdata.filterExtension(data);
            table.updateUI();
        } else {
            System.err.println("ERROR! Unknown type");
        }
    }

    /**
     * Filter by percentage button
     * */
    private void getPercentageFilter () {
        JDialog frameWindow = new JDialog();
        frameWindow.setTitle("Percentage Filter");
        frameWindow.setResizable(false);
        JPanel panel = new JPanel(new GridLayout(2, 1));
        JLabel label = new JLabel("Introduce the percentage: ");
        JTextField text = new JTextField();
        // text.setPreferredSize(new Dimension(300, 20));
        JButton buttonU = new JButton("Upper");
        JButton buttonL = new JButton("Lower");

        panel.add(label);
        panel.add(text);
        panel.add(buttonU);
        panel.add(buttonL);

        buttonU.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
                Pattern pattern = Pattern.compile("([0-9]*[.])?[0-9]+");
                // Por algun motivo este patron hace match con '3.' o '3%'
                // entre otras cadenas absurdas, pero llevo demasiado tiempo
                // aqui, asi que paso
                Matcher matcher = pattern.matcher(text.getText());

                if (matcher.find()) {
                    filterRows(TableFileData.PERCENTAGE, text.getText(), TableFileData.UP);
		            frameWindow.setVisible(false);
                } else {
                    frameWindow.setTitle("ERROR! Invalid input");
                    System.err.println("ERROR! Invalid input");
                }
			}
		});

        buttonL.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
                Pattern pattern = Pattern.compile("([0-9]*[.])?[0-9]+");
                // Por algun motivo este patron hace match con '3.' o '3%'
                // entre otras cadenas absurdas, pero llevo demasiado tiempo
                // aqui, asi que paso
                Matcher matcher = pattern.matcher(text.getText());

                if (matcher.find()) {
                    filterRows(TableFileData.PERCENTAGE, text.getText(), TableFileData.DOWN);
		            frameWindow.setVisible(false);
                } else {
                    frameWindow.setTitle("ERROR! Invalid input");
                    System.err.println("ERROR! Invalid input");
                }
			}
		});

        frameWindow.add(panel);
        frameWindow.pack();
		frameWindow.setVisible(true);
    }

    /**
     * Filter by number of files button
     * */
    private void getNumberFilter () {
        JDialog frameWindow = new JDialog();
        frameWindow.setTitle("File Number Filter");
        frameWindow.setResizable(false);
        JPanel panel = new JPanel(new GridLayout(2, 1));
        JLabel label = new JLabel("Introduce the file number: ");
        JTextField text = new JTextField();
        JButton buttonU = new JButton("Upper");
        JButton buttonL = new JButton("Lower");

        panel.add(label);
        panel.add(text);
        panel.add(buttonU);
        panel.add(buttonL);

        buttonU.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
                Pattern pattern = Pattern.compile("[0-9]+");
                Matcher matcher = pattern.matcher(text.getText());

                if (matcher.find()) {
                    filterRows(TableFileData.NUMBER, text.getText(), TableFileData.UP);
		            frameWindow.setVisible(false);
                } else {
                    frameWindow.setTitle("ERROR! Invalid input");
                    System.err.println("ERROR! Invalid input");
                }
			}
		});

        buttonL.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
                Pattern pattern = Pattern.compile("[0-9]+");
                Matcher matcher = pattern.matcher(text.getText());

                if (matcher.find()) {
                    filterRows(TableFileData.NUMBER, text.getText(), TableFileData.DOWN);
		            frameWindow.setVisible(false);
                } else {
                    frameWindow.setTitle("ERROR! Invalid input");
                    System.err.println("ERROR! Invalid input");
                }
			}
		});

        frameWindow.add(panel);
        frameWindow.pack();
		frameWindow.setVisible(true);
    }

    /**
     * Filter by file size button
     * */
    private void getSizeFilter () {
        JDialog frameWindow = new JDialog();
        frameWindow.setTitle("File Size Filter");
        frameWindow.setResizable(false);
        JPanel panel = new JPanel(new GridLayout(2, 1));
        JLabel label = new JLabel("Introduce the file size (in Bytes): ");
        JTextField text = new JTextField();
        JButton buttonU = new JButton("Upper");
        JButton buttonL = new JButton("Lower");

        panel.add(label);
        panel.add(text);
        panel.add(buttonU);
        panel.add(buttonL);

        buttonU.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
                Pattern pattern = Pattern.compile("[0-9]+");
                Matcher matcher = pattern.matcher(text.getText());

                if (matcher.find()) {
                    filterRows(TableFileData.SIZE, text.getText(), TableFileData.UP);
		            frameWindow.setVisible(false);
                } else {
                    frameWindow.setTitle("ERROR! Invalid input");
                    System.err.println("ERROR! Invalid input");
                }
			}
		});

        buttonL.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
                Pattern pattern = Pattern.compile("[0-9]+");
                Matcher matcher = pattern.matcher(text.getText());

                if (matcher.find()) {
                    filterRows(TableFileData.SIZE, text.getText(), TableFileData.DOWN);
		            frameWindow.setVisible(false);
                } else {
                    frameWindow.setTitle("ERROR! Invalid input");
                    System.err.println("ERROR! Invalid input");
                }
			}
		});

        frameWindow.add(panel);
        frameWindow.pack();
		frameWindow.setVisible(true);
    }

    /**
     * Select which type of filter
     * */
    private void filterRows (String type, String data, String order) {
        Pattern pattern = Pattern.compile("[a-zA-Z]");
        Matcher matcher = pattern.matcher(data);

        if (matcher.find()) {
            System.err.println("ERROR! Invalid input");
            return;
        }
        
        if (type.equals(TableFileData.PERCENTAGE)) {
            if (order.equals(TableFileData.UP)) {
                tdata.filterPercentage(Float.parseFloat(data), TableFileData.UP);
                table.updateUI();
            } else if (order.equals(TableFileData.DOWN)){
                tdata.filterPercentage(Float.parseFloat(data), TableFileData.DOWN);
                table.updateUI();
            } else
                System.err.println("ERROR! Unknown type");
            
        } else if (type.equals(TableFileData.NUMBER)) {
            if (order.equals(TableFileData.UP)) {
                tdata.filterNumber(Long.valueOf(data), TableFileData.UP);
                table.updateUI();
            } else if (order.equals(TableFileData.DOWN)){
                tdata.filterNumber(Long.valueOf(data), TableFileData.DOWN);
                table.updateUI();
            } else
                System.err.println("ERROR! Unknown type");
            
        } else if (type.equals(TableFileData.SIZE)) {
            if (order.equals(TableFileData.UP)) {
                tdata.filterSize(Long.valueOf(data), TableFileData.UP);
                table.updateUI();
            } else if (order.equals(TableFileData.DOWN)){
                tdata.filterSize(Long.valueOf(data), TableFileData.DOWN);
                table.updateUI();
            } else
                System.err.println("ERROR! Unknown type");
            
        } else {
            System.err.println("ERROR! Unknown type");
        }
    }

    /**
     * Unfilter the Table, and update the GUI
     * */
    private void unfilterRows () {
        tdata.unfilter();
        table.updateUI();
    }

}
