package r0p3GUI;

import java.io.IOException;
import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;

import r0p3.Directory;

public class TreeTable {

    private Directory root;
    private DefaultMutableTreeNode start;

    private String[] columnNames = {PERCNT, SIZE, ITEMS, FILES, SUB_DIRS, SYM_LINK};
    public static final String PERCNT = "Percentage";
    public static final String SIZE = "Size";
    public static final String ITEMS = "Items";
    public static final String FILES = "Files";
    public static final String SUB_DIRS = "SubDir";
    public static final String SYM_LINK = "SymLink";

    public TreeTable (Directory fs_dir) throws IOException {
        root = fs_dir;
        start = new DefaultMutableTreeNode(fs_dir.getPath().toRealPath().toString());
    }

    public DefaultMutableTreeNode setTree () {
        completeTree(start, root);
        return start;
    }

    private void completeTree (DefaultMutableTreeNode actual, Directory dir) {
         
        for (Map.Entry<String, Long> entry : dir.getFilesContent().entrySet()) {
            DefaultMutableTreeNode new_node =
                new DefaultMutableTreeNode(entry.getKey());
            actual.add(new_node);            
        }

        for (String name : dir.getSymLinkContent()) {
            DefaultMutableTreeNode new_node =
                new DefaultMutableTreeNode(name);
            actual.add(new_node);
        }

        for (Directory new_dir : dir.getDirsContent()){
            DefaultMutableTreeNode new_node =
                new DefaultMutableTreeNode(new_dir.getPath().getFileName().toString());
            actual.add(new_node);            
            completeTree(new_node, new_dir);
        }
    }

}
