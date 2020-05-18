/*
 * The contents of this file are subject to the Sapient Public License
 * Version 1.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * http://carbon.sf.net/License.html.
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 *
 * The Original Code is The Carbon Component Framework.
 *
 * The Initial Developer of the Original Code is Sapient Corporation
 *
 * Copyright (C) 2003 Sapient Corporation. All Rights Reserved.
 */


/*
 * @(#)JTreeTable.java    1.2 98/10/27
 *
 * Copyright 1997, 1998 by Sun Microsystems, Inc.,
 * 901 San Antonio Road, Palo Alto, California, 94303, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Sun Microsystems, Inc. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Sun.
 */

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 * This example shows how to create a simple JTreeTable component,
 * by using a JTree as a renderer (and editor) for the cells in a
 * particular column in the JTable.
 *
 * @version 1.2 10/27/98
 *
 * @author Philip Milne
 * @author Scott Violet
 */
public class JTreeTable extends JTable {
    /** A subclass of JTree. */
    protected TreeTableCellRenderer tree;

    public JTreeTable(TreeTableModel treeTableModel) {
    super();

    // Create the tree. It will be used as a renderer and editor.
    tree = new TreeTableCellRenderer(treeTableModel);

    // Install a tableModel representing the visible rows in the tree.
    super.setModel(new TreeTableModelAdapter(treeTableModel, tree));

    // Force the JTable and JTree to share their row selection models.
    ListToTreeSelectionModelWrapper selectionWrapper = new
                            ListToTreeSelectionModelWrapper();
    tree.setSelectionModel(selectionWrapper);
    setSelectionModel(selectionWrapper.getListSelectionModel());

    // Install the tree editor renderer and editor.
    setDefaultRenderer(TreeTableModel.class, tree);
    setDefaultEditor(TreeTableModel.class, new TreeTableCellEditor());

    // No grid.
    setShowGrid(false);

    // No intercell spacing
    setIntercellSpacing(new Dimension(0, 0));

    // And update the height of the trees row to match that of
    // the table.
    if (tree.getRowHeight() < 1) {
        // Metal looks better like this.
        setRowHeight(18);
    }
    }

    /**
     * Overridden to message super and forward the method to the tree.
     * Since the tree is not actually in the component hieachy it will
     * never receive this unless we forward it in this manner.
     */
    public void updateUI() {
    super.updateUI();
    if(tree != null) {
        tree.updateUI();
    }
    // Use the tree's default foreground and background colors in the
    // table.
        LookAndFeel.installColorsAndFont(this, "Tree.background",
                                         "Tree.foreground", "Tree.font");
    }

    /* Workaround for BasicTableUI anomaly. Make sure the UI never tries to
     * paint the editor. The UI currently uses different techniques to
     * paint the renderers and editors and overriding setBounds() below
     * is not the right thing to do for an editor. Returning -1 for the
     * editing row in this case, ensures the editor is never painted.
     */
    public int getEditingRow() {
        return (getColumnClass(editingColumn) == TreeTableModel.class) ? -1 :
            editingRow;
    }

    /**
     * Overridden to pass the new rowHeight to the tree.
     */
    public void setRowHeight(int rowHeight) {
        super.setRowHeight(rowHeight);
    if (tree != null && tree.getRowHeight() != rowHeight) {
            tree.setRowHeight(getRowHeight());
    }
    }

    /**
     * Returns the tree that is being shared between the model.
     */
    public JTree getTree() {
    return tree;
    }

    /**
     * A TreeCellRenderer that displays a JTree.
     */
    public class TreeTableCellRenderer extends JTree implements
             TableCellRenderer {
    /** Last table/tree row asked to renderer. */
    protected int visibleRow;

    public TreeTableCellRenderer(TreeModel model) {
        super(model);
    }

    /**
     * updateUI is overridden to set the colors of the Tree's renderer
     * to match that of the table.
     */
    public void updateUI() {
        super.updateUI();
        // Make the tree's cell renderer use the table's cell selection
        // colors.
        TreeCellRenderer tcr = getCellRenderer();
        if (tcr instanceof DefaultTreeCellRenderer) {
        DefaultTreeCellRenderer dtcr = ((DefaultTreeCellRenderer)tcr);
        // For 1.1 uncomment this, 1.2 has a bug that will cause an
        // exception to be thrown if the border selection color is
        // null.
        // dtcr.setBorderSelectionColor(null);
        dtcr.setTextSelectionColor(UIManager.getColor
                       ("Table.selectionForeground"));
        dtcr.setBackgroundSelectionColor(UIManager.getColor
                        ("Table.selectionBackground"));
        }
    }

    /**
     * Sets the row height of the tree, and forwards the row height to
     * the table.
     */
    public void setRowHeight(int rowHeight) {
        if (rowHeight > 0) {
        super.setRowHeight(rowHeight);
        if (JTreeTable.this != null &&
            JTreeTable.this.getRowHeight() != rowHeight) {
            JTreeTable.this.setRowHeight(getRowHeight());
        }
        }
    }

    /**
     * This is overridden to set the height to match that of the JTable.
     */
    public void setBounds(int x, int y, int w, int h) {
        super.setBounds(x, 0, w, JTreeTable.this.getHeight());
    }

    /**
     * Sublcassed to translate the graphics such that the last visible
     * row will be drawn at 0,0.
     */
    public void paint(Graphics g) {
        g.translate(0, -visibleRow * getRowHeight());
        super.paint(g);
    }

    /**
     * TreeCellRenderer method. Overridden to update the visible row.
     */
    public Component getTableCellRendererComponent(JTable table,
                               Object value,
                               boolean isSelected,
                               boolean hasFocus,
                               int row, int column) {
        if(isSelected)
        setBackground(table.getSelectionBackground());
        else
        setBackground(table.getBackground());

        visibleRow = row;
        return this;
    }
    }


    /**
     * TreeTableCellEditor implementation. Component returned is the
     * JTree.
     */
     class TreeTableCellEditor extends AbstractCellEditor implements
             TableCellEditor {
    public Component getTableCellEditorComponent(JTable table,
                             Object value,
                             boolean isSelected,
                             int r, int c) {
        return tree;
    }

    /**
     * Overridden to return false, and if the event is a mouse event
     * it is forwarded to the tree.<p>
     * The behavior for this is debatable, and should really be offered
     * as a property. By returning false, all keyboard actions are
     * implemented in terms of the table. By returning true, the
     * tree would get a chance to do something with the keyboard
     * events. For the most part this is ok. But for certain keys,
     * such as left/right, the tree will expand/collapse where as
     * the table focus should really move to a different column. Page
     * up/down should also be implemented in terms of the table.
     * By returning false this also has the added benefit that clicking
     * outside of the bounds of the tree node, but still in the tree
     * column will select the row, whereas if this returned true
     * that wouldn't be the case.
     * <p>By returning false we are also enforcing the policy that
     * the tree will never be editable (at least by a key sequence).
     */
    public boolean isCellEditable(EventObject e) {
        if (e instanceof MouseEvent) {
        for (int counter = getColumnCount() - 1; counter >= 0;
             counter--) {
            if (getColumnClass(counter) == TreeTableModel.class) {
            MouseEvent me = (MouseEvent)e;
            MouseEvent newME = new MouseEvent(tree, me.getID(),
                   me.getWhen(), me.getModifiers(),
                   me.getX() - getCellRect(0, counter, true).x,
                   me.getY(), me.getClickCount(),
                                   me.isPopupTrigger());
            tree.dispatchEvent(newME);
            break;
            }
        }
        }
        return false;
    }

    public Object getCellEditorValue() {
      // TODO Auto-generated method stub
      return null;
    }
    }


    /**
     * ListToTreeSelectionModelWrapper extends DefaultTreeSelectionModel
     * to listen for changes in the ListSelectionModel it maintains. Once
     * a change in the ListSelectionModel happens, the paths are updated
     * in the DefaultTreeSelectionModel.
     */
    class ListToTreeSelectionModelWrapper extends DefaultTreeSelectionModel {
    /** Set to true when we are updating the ListSelectionModel. */
    protected boolean         updatingListSelectionModel;

    public ListToTreeSelectionModelWrapper() {
        super();
        getListSelectionModel().addListSelectionListener
                                (createListSelectionListener());
    }

    /**
     * Returns the list selection model. ListToTreeSelectionModelWrapper
     * listens for changes to this model and updates the selected paths
     * accordingly.
     */
    ListSelectionModel getListSelectionModel() {
        return listSelectionModel;
    }

    /**
     * This is overridden to set <code>updatingListSelectionModel</code>
     * and message super. This is the only place DefaultTreeSelectionModel
     * alters the ListSelectionModel.
     */
    public void resetRowSelection() {
        if(!updatingListSelectionModel) {
        updatingListSelectionModel = true;
        try {
            super.resetRowSelection();
        }
        finally {
            updatingListSelectionModel = false;
        }
        }
        // Notice how we don't message super if
        // updatingListSelectionModel is true. If
        // updatingListSelectionModel is true, it implies the
        // ListSelectionModel has already been updated and the
        // paths are the only thing that needs to be updated.
    }

    /**
     * Creates and returns an instance of ListSelectionHandler.
     */
    protected ListSelectionListener createListSelectionListener() {
        return new ListSelectionHandler();
    }

    /**
     * If <code>updatingListSelectionModel</code> is false, this will
     * reset the selected paths from the selected rows in the list
     * selection model.
     */
    protected void updateSelectedPathsFromSelectedRows() {
        if(!updatingListSelectionModel) {
        updatingListSelectionModel = true;
        try {
            // This is way expensive, ListSelectionModel needs an
            // enumerator for iterating.
            int        min = listSelectionModel.getMinSelectionIndex();
            int        max = listSelectionModel.getMaxSelectionIndex();

            clearSelection();
            if(min != -1 && max != -1) {
            for(int counter = min; counter <= max; counter++) {
                if(listSelectionModel.isSelectedIndex(counter)) {
                TreePath     selPath = tree.getPathForRow
                                            (counter);

                if(selPath != null) {
                    addSelectionPath(selPath);
                }
                }
            }
            }
        }
        finally {
            updatingListSelectionModel = false;
        }
        }
    }

    /**
     * Class responsible for calling updateSelectedPathsFromSelectedRows
     * when the selection of the list changse.
     */
    class ListSelectionHandler implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent e) {
        updateSelectedPathsFromSelectedRows();
        }
    }
    }
}

/*
 * The contents of this file are subject to the Sapient Public License
 * Version 1.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * http://carbon.sf.net/License.html.
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 *
 * The Original Code is The Carbon Component Framework.
 *
 * The Initial Developer of the Original Code is Sapient Corporation
 *
 * Copyright (C) 2003 Sapient Corporation. All Rights Reserved.
 */


/*
 * TreeTableModel.java
 *
 * Copyright (c) 1998 Sun Microsystems, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Sun
 * Microsystems, Inc. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Sun.
 *
 * SUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. SUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */

/**
 * TreeTableModel is the model used by a JTreeTable. It extends TreeModel
 * to add methods for getting inforamtion about the set of columns each
 * node in the TreeTableModel may have. Each column, like a column in
 * a TableModel, has a name and a type associated with it. Each node in
 * the TreeTableModel can return a value for each of the columns and
 * set that value if isCellEditable() returns true.
 *
 * @author Philip Milne
 * @author Scott Violet
 */
 interface TreeTableModel extends TreeModel
{
    /**
     * Returns the number ofs availible column.
     */
    public int getColumnCount();

    /**
     * Returns the name for column number <code>column</code>.
     */
    public String getColumnName(int column);

    /**
     * Returns the type for column number <code>column</code>.
     */
    public Class getColumnClass(int column);

    /**
     * Returns the value to be displayed for node <code>node</code>,
     * at column number <code>column</code>.
     */
    public Object getValueAt(Object node, int column);

    /**
     * Indicates whether the the value for node <code>node</code>,
     * at column number <code>column</code> is editable.
     */
    public boolean isCellEditable(Object node, int column);

    /**
     * Sets the value for node <code>node</code>,
     * at column number <code>column</code>.
     */
    public void setValueAt(Object aValue, Object node, int column);
}

 /*
  * The contents of this file are subject to the Sapient Public License
  * Version 1.0 (the "License"); you may not use this file except in compliance
  * with the License. You may obtain a copy of the License at
  * http://carbon.sf.net/License.html.
  *
  * Software distributed under the License is distributed on an "AS IS" basis,
  * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
  * the specific language governing rights and limitations under the License.
  *
  * The Original Code is The Carbon Component Framework.
  *
  * The Initial Developer of the Original Code is Sapient Corporation
  *
  * Copyright (C) 2003 Sapient Corporation. All Rights Reserved.
  */


 /*
  * @(#)TreeTableModelAdapter.java    1.2 98/10/27
  *
  * Copyright 1997, 1998 by Sun Microsystems, Inc.,
  * 901 San Antonio Road, Palo Alto, California, 94303, U.S.A.
  * All rights reserved.
  *
  * This software is the confidential and proprietary information
  * of Sun Microsystems, Inc. ("Confidential Information").  You
  * shall not disclose such Confidential Information and shall use
  * it only in accordance with the terms of the license agreement
  * you entered into with Sun.
  */


 /**
  * This is a wrapper class takes a TreeTableModel and implements
  * the table model interface. The implementation is trivial, with
  * all of the event dispatching support provided by the superclass:
  * the AbstractTableModel.
  *
  * @version 1.2 10/27/98
  *
  * @author Philip Milne
  * @author Scott Violet
  */
  class TreeTableModelAdapter extends AbstractTableModel
 {
     JTree tree;
     TreeTableModel treeTableModel;

     public TreeTableModelAdapter(TreeTableModel treeTableModel, JTree tree) {
         this.tree = tree;
         this.treeTableModel = treeTableModel;

     tree.addTreeExpansionListener(new TreeExpansionListener() {
         // Don't use fireTableRowsInserted() here; the selection model
         // would get updated twice.
         public void treeExpanded(TreeExpansionEvent event) {
           fireTableDataChanged();
         }
             public void treeCollapsed(TreeExpansionEvent event) {
           fireTableDataChanged();
         }
     });

     // Install a TreeModelListener that can update the table when
     // tree changes. We use delayedFireTableDataChanged as we can
     // not be guaranteed the tree will have finished processing
     // the event before us.
     treeTableModel.addTreeModelListener(new TreeModelListener() {
         public void treeNodesChanged(TreeModelEvent e) {
         delayedFireTableDataChanged();
         }

         public void treeNodesInserted(TreeModelEvent e) {
         delayedFireTableDataChanged();
         }

         public void treeNodesRemoved(TreeModelEvent e) {
         delayedFireTableDataChanged();
         }

         public void treeStructureChanged(TreeModelEvent e) {
         delayedFireTableDataChanged();
         }
     });
     }

     // Wrappers, implementing TableModel interface.

     public int getColumnCount() {
     return treeTableModel.getColumnCount();
     }

     public String getColumnName(int column) {
     return treeTableModel.getColumnName(column);
     }

     public Class getColumnClass(int column) {
     return treeTableModel.getColumnClass(column);
     }

     public int getRowCount() {
     return tree.getRowCount();
     }

     protected Object nodeForRow(int row) {
     TreePath treePath = tree.getPathForRow(row);
     return treePath.getLastPathComponent();
     }

     public Object getValueAt(int row, int column) {
     return treeTableModel.getValueAt(nodeForRow(row), column);
     }

     public boolean isCellEditable(int row, int column) {
          return treeTableModel.isCellEditable(nodeForRow(row), column);
     }

     public void setValueAt(Object value, int row, int column) {
     treeTableModel.setValueAt(value, nodeForRow(row), column);
     }

     /**
      * Invokes fireTableDataChanged after all the pending events have been
      * processed. SwingUtilities.invokeLater is used to handle this.
      */
     protected void delayedFireTableDataChanged() {
     SwingUtilities.invokeLater(new Runnable() {
         public void run() {
         fireTableDataChanged();
         }
     });
     }
 }
/*
 * The contents of this file are subject to the Sapient Public License
 * Version 1.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * http://carbon.sf.net/License.html.
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 *
 * The Original Code is The Carbon Component Framework.
 *
 * The Initial Developer of the Original Code is Sapient Corporation
 *
 * Copyright (C) 2003 Sapient Corporation. All Rights Reserved.
 */

package org.sape.carbon.services.swing.treetable;

/*
 * @(#)AbstractTreeTableModel.java    1.2 98/10/27
 *
 * Copyright 1997, 1998 by Sun Microsystems, Inc.,
 * 901 San Antonio Road, Palo Alto, California, 94303, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Sun Microsystems, Inc. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Sun.
 */

import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreePath;

/**
 * @version 1.2 10/27/98
 * An abstract implementation of the TreeTableModel interface, handling the list
 * of listeners.
 * @author Philip Milne
 */

public abstract class AbstractTreeTableModel implements TreeTableModel {
    protected Object root;
    protected EventListenerList listenerList = new EventListenerList();

    public AbstractTreeTableModel(Object root) {
        this.root = root;
    }

    //
    // Default implmentations for methods in the TreeModel interface.
    //

    public Object getRoot() {
        return root;
    }

    public boolean isLeaf(Object node) {
        return getChildCount(node) == 0;
    }

    public void valueForPathChanged(TreePath path, Object newValue) {}

    // This is not called in the JTree's default mode: use a naive implementation.
    public int getIndexOfChild(Object parent, Object child) {
        for (int i = 0; i < getChildCount(parent); i++) {
        if (getChild(parent, i).equals(child)) {
            return i;
        }
        }
    return -1;
    }

    public void addTreeModelListener(TreeModelListener l) {
        listenerList.add(TreeModelListener.class, l);
    }

    public void removeTreeModelListener(TreeModelListener l) {
        listenerList.remove(TreeModelListener.class, l);
    }

    /*
     * Notify all listeners that have registered interest for
     * notification on this event type.  The event instance
     * is lazily created using the parameters passed into
     * the fire method.
     * @see EventListenerList
     */
    protected void fireTreeNodesChanged(Object source, Object[] path,
                                        int[] childIndices,
                                        Object[] children) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==TreeModelListener.class) {
                // Lazily create the event:
                if (e == null)
                    e = new TreeModelEvent(source, path,
                                           childIndices, children);
                ((TreeModelListener)listeners[i+1]).treeNodesChanged(e);
            }
        }
    }

    /*
     * Notify all listeners that have registered interest for
     * notification on this event type.  The event instance
     * is lazily created using the parameters passed into
     * the fire method.
     * @see EventListenerList
     */
    protected void fireTreeNodesInserted(Object source, Object[] path,
                                        int[] childIndices,
                                        Object[] children) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==TreeModelListener.class) {
                // Lazily create the event:
                if (e == null)
                    e = new TreeModelEvent(source, path,
                                           childIndices, children);
                ((TreeModelListener)listeners[i+1]).treeNodesInserted(e);
            }
        }
    }

    /*
     * Notify all listeners that have registered interest for
     * notification on this event type.  The event instance
     * is lazily created using the parameters passed into
     * the fire method.
     * @see EventListenerList
     */
    protected void fireTreeNodesRemoved(Object source, Object[] path,
                                        int[] childIndices,
                                        Object[] children) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==TreeModelListener.class) {
                // Lazily create the event:
                if (e == null)
                    e = new TreeModelEvent(source, path,
                                           childIndices, children);
                ((TreeModelListener)listeners[i+1]).treeNodesRemoved(e);
            }
        }
    }

    /*
     * Notify all listeners that have registered interest for
     * notification on this event type.  The event instance
     * is lazily created using the parameters passed into
     * the fire method.
     * @see EventListenerList
     */
    protected void fireTreeStructureChanged(Object source, Object[] path,
                                        int[] childIndices,
                                        Object[] children) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==TreeModelListener.class) {
                // Lazily create the event:
                if (e == null)
                    e = new TreeModelEvent(source, path,
                                           childIndices, children);
                ((TreeModelListener)listeners[i+1]).treeStructureChanged(e);
            }
        }
    }

    //
    // Default impelmentations for methods in the TreeTableModel interface.
    //

    public Class getColumnClass(int column) { return Object.class; }

   /** By default, make the column with the Tree in it the only editable one.
    *  Making this column editable causes the JTable to forward mouse
    *  and keyboard events in the Tree column to the underlying JTree.
    */
    public boolean isCellEditable(Object node, int column) {
         return getColumnClass(column) == TreeTableModel.class;
    }

    public void setValueAt(Object aValue, Object node, int column) {}


    // Left to be implemented in the subclass:

    /*
     *   public Object getChild(Object parent, int index)
     *   public int getChildCount(Object parent)
     *   public int getColumnCount()
     *   public String getColumnName(Object node, int column)
     *   public Object getValueAt(Object node, int column)
     */
}
/*
 * The contents of this file are subject to the Sapient Public License
 * Version 1.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * http://carbon.sf.net/License.html.
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 *
 * The Original Code is The Carbon Component Framework.
 *
 * The Initial Developer of the Original Code is Sapient Corporation
 *
 * Copyright (C) 2003 Sapient Corporation. All Rights Reserved.
 */


import java.util.EventObject;

import javax.swing.CellEditor;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;

public class AbstractCellEditor implements CellEditor {

    protected EventListenerList listenerList = new EventListenerList();

    public Object getCellEditorValue() { return null; }
    public boolean isCellEditable(EventObject e) { return true; }
    public boolean shouldSelectCell(EventObject anEvent) { return false; }
    public boolean stopCellEditing() { return true; }
    public void cancelCellEditing() {}

    public void addCellEditorListener(CellEditorListener l) {
    listenerList.add(CellEditorListener.class, l);
    }

    public void removeCellEditorListener(CellEditorListener l) {
    listenerList.remove(CellEditorListener.class, l);
    }

    /*
     * Notify all listeners that have registered interest for
     * notification on this event type.
     * @see EventListenerList
     */
    protected void fireEditingStopped() {
    // Guaranteed to return a non-null array
    Object[] listeners = listenerList.getListenerList();
    // Process the listeners last to first, notifying
    // those that are interested in this event
    for (int i = listeners.length-2; i>=0; i-=2) {
        if (listeners[i]==CellEditorListener.class) {
        ((CellEditorListener)listeners[i+1]).editingStopped(new ChangeEvent(this));
        }
    }
    }

    /*
     * Notify all listeners that have registered interest for
     * notification on this event type.
     * @see EventListenerList
     */
    protected void fireEditingCanceled() {
    // Guaranteed to return a non-null array
    Object[] listeners = listenerList.getListenerList();
    // Process the listeners last to first, notifying
    // those that are interested in this event
    for (int i = listeners.length-2; i>=0; i-=2) {
        if (listeners[i]==CellEditorListener.class) {
        ((CellEditorListener)listeners[i+1]).editingCanceled(new ChangeEvent(this));
        }
    }
    }
}
