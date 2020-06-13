package r0p3GUI;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import r0p3.Directory;
import r0p3.FileData;

public class TableDirContent implements TableModel {

    private List<String[]> dirData;
    private String[] columnNames = {TYPE, NAME, SIZE};
    public static final String TYPE = "Type";
    public static final String NAME = "Name";
    public static final String SIZE = "Size";

    /**
     * TableDirContent constructor
     * */
    public TableDirContent () {
        dirData = new ArrayList<String[]>();
    }

    /**
     * Table Directory Content getter
     *
     * @return  the content of the table
     * */
    public List<String[]> getTableDirectory () {
        return dirData;
    }

    /**
     * Table Directory elements setter
     *
     * @param dir   Directory node
     * */
    public void setElements (Directory dir) {
        dirData = new ArrayList<String[]>();
        
        for (Map.Entry<String, Long> m : dir.getFilesContent().entrySet()) {
            String[] content = new String[3];
            content[0] = "f";
            content[1] = m.getKey();
            content[2] = FileData.getSizeUnit(m.getValue());
            dirData.add(content);
        }

        for (String s : dir.getSymLinkContent()) {
            String[] content = new String[3];
            content[0] = "l";
            content[1] = s;
            content[2] = "-";
            dirData.add(content);
        }

        for (Directory d : dir.getDirsContent()) {
            String[] content = new String[3];
            content[0] = "d";
            content[1] = d.getName();
            content[2] = FileData.getSizeUnit(d.getSizeContent());
            dirData.add(content);
        }

    }


	@Override
	public void addTableModelListener(TableModelListener l) {

	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return String.class;
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return columnNames[columnIndex];
	}

	@Override
	public int getRowCount() {
		return dirData.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {

		String[] dd = dirData.get(rowIndex);

		if (columnIndex == 0)
			return dd[0];

		if (columnIndex == 1)
			return dd[1];

		if (columnIndex == 2)
			return dd[2];

		return "";
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {

	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		
	}

}
