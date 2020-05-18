import java.awt.BorderLayout;
import java.awt.Component;
//  www .ja va  2s  .  c  o m
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;

public class Main2 extends JFrame {

  private JTree tree;

  public Main2() {
    DefaultMutableTreeNode AA1 = new DefaultMutableTreeNode("AA1");
    DefaultMutableTreeNode AA2 = new DefaultMutableTreeNode("AA2");
    DefaultMutableTreeNode A = new DefaultMutableTreeNode("A");
    A.add(AA1);
    A.add(AA2);
    DefaultMutableTreeNode BB1 = new DefaultMutableTreeNode("BB1");
    DefaultMutableTreeNode BB2 = new DefaultMutableTreeNode("BB2");
    DefaultMutableTreeNode B = new DefaultMutableTreeNode("B");
    B.add(BB1);
    B.add(BB2);
    DefaultMutableTreeNode CC1 = new DefaultMutableTreeNode("CC1");
    DefaultMutableTreeNode CC2 = new DefaultMutableTreeNode("CC2");
    DefaultMutableTreeNode C = new DefaultMutableTreeNode("C");
    C.add(CC1);
    C.add(CC2);
    DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
    root.add(A);
    root.add(B);
    root.add(C);
    tree = new JTree(root);
    tree.setCellRenderer(new MyTableInTreeCellRenderer());
    tree.setRowHeight(0);
    JScrollPane jsp = new JScrollPane(tree);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    add(jsp, BorderLayout.CENTER);
    pack();
  }

  public static void main(String[] args) throws Exception {

    new Main2().setVisible(true);

  }
}

class MyTableInTreeCellRenderer extends JPanel implements TreeCellRenderer {

  private JTable table;

  public MyTableInTreeCellRenderer() {
    super(new BorderLayout());
    table = new JTable();
    JScrollPane scrollPane = new JScrollPane(table);
    add(scrollPane);
  }

  public Component getTreeCellRendererComponent(JTree tree, Object value,
      boolean selected, boolean expanded, boolean leaf, int row,
      boolean hasFocus) {
    final String v = (String) ((DefaultMutableTreeNode) value).getUserObject();
    table.setModel(new DefaultTableModel() {
      @Override
      public int getRowCount() {
        return 2;
      }
      @Override
      public int getColumnCount() {
        return 2;
      }
      @Override
      public Object getValueAt(int row, int column) {
        return v + ":" + row + ":" + column;
      }
    });
    table.setPreferredScrollableViewportSize(table.getPreferredSize());
    return this;
  }
}
