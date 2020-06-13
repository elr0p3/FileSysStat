package r0p3GUI;

import java.io.IOException;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import r0p3.Directory;

public class FileSystemTree implements TreeModel {

    private Directory root;
   	private List<TreeModelListener> listenerList = new ArrayList<TreeModelListener>();


    /**
     * Tree constructor
     *
     * @param fs_dir    a Directory node
     * */
    public FileSystemTree (Directory fs_dir) throws IOException {
        root = fs_dir;
    }

    @Override
	public void addTreeModelListener(TreeModelListener l) {
        listenerList.add(l);
    }

	@Override
	public Object getChild(Object parent, int index) {
		if (parent instanceof Directory) {
            int i = 0;
            int init  = ((Directory) parent).getDirsContent().size();
            int half  = init + ((Directory) parent).getFilesContent().size();
            int total = half + ((Directory) parent).getSymLinkContent().size();

            System.out.println(" CHILD --> " + index + " /// " + init + ":" + half + ":" + total);
            if (index < init)
                return directoryPosition(parent, index, i);

            else if (index < half)
                return filePosition(parent, index, init);

            else if (index < total)
                return linkPosition(parent, index, half);

            // else {
                // if (forFile(index,
                            // ((Directory) parent).getFilesContent().size() +
                            // ((Directory) parent).getSymLinkContent().size(),
                            // ((Directory) parent).getFilesContent().size()))
                    // return filePosition(parent, index % ((Directory) parent).getFilesContent().size());
                // else
                    // return linkPosition(parent, index % ((Directory) parent).getSymLinkContent().size());
            // }
        }
		return null;
	}

    private Directory directoryPosition (Object parent, int index, int i) {
        for (Directory d : ((Directory) parent).getDirsContent()) {
            if (i == index)
                return d;
            i++;
        }
        return null;
    }

    private String filePosition (Object parent, int index, int i) {
        for (Map.Entry<String, Long> m : ((Directory) parent).getFilesContent().entrySet()) {
            if (i == index)
                return m.getKey();
            i++;
        }
        return null;
    }

    private String linkPosition (Object parent, int index, int i) {
        for (String l : ((Directory) parent).getSymLinkContent()) {
            if (i == index)
                return l;
            i++;
        }
        return null;
    }

    private boolean forFile (int index, long total, int min) {
        while (index >= total)
            index -= total;
        if (index < min)    return true;
        return false;
    }

	@Override
	public int getChildCount(Object parent) {
		if (parent instanceof Directory) {
            System.out.println(" NAME -> " + ((Directory) parent).getName() + " - - - - -");
            System.out.println(" DIRs -> " + ((Directory) parent).getDirsContent().size());
            System.out.println(" FILs -> " + ((Directory) parent).getFilesContent().size());
            System.out.println(" LNKs -> " + ((Directory) parent).getSymLinkContent().size());
            int total = ((Directory) parent).getDirsContent().size() +
                        ((Directory) parent).getFilesContent().size() +
                        ((Directory) parent).getSymLinkContent().size();
            System.out.println("TOTAL -> " + total);

            return total;
            // return ((Directory) parent).getDirsContent().size();
        }
		return 0;
	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
        if (parent instanceof Directory) {
            int i = 0;
            for (Directory d : ((Directory) parent).getDirsContent()) {
                if (d.equals(parent))
                    return i;
                i++;
            }
            for (Map.Entry<String, Long> m : ((Directory) parent).getFilesContent().entrySet()) {
                if (child.equals(m.getKey()))
                    return i;
                i++;
            }
            for (String l : ((Directory) parent).getSymLinkContent()) {
                if (child.equals(l))
                    return i;
                i++;
            }
        }
		return 0;
	}

	@Override
	public Object getRoot() {
		return root;
	}

	@Override
	public boolean isLeaf(Object node) {
		return !(node instanceof Directory);
	}

	@Override
	public void removeTreeModelListener(TreeModelListener l) {
        listenerList.remove(l);
	}

	@Override
	public void valueForPathChanged(TreePath arg0, Object arg1) {
	}

}
