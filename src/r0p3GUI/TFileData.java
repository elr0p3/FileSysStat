package r0p3GUI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import r0p3.FileData;

public class TFileData implements TableModel {

	private List<FileData> fileData;
    private String[] columnNames = {EXTENTION, PERCENTAGE, NUMBER, SIZE};
    public static final String EXTENTION = "Extention";
    public static final String PERCENTAGE = "Percentage";
    public static final String NUMBER = "Number";
    public static final String SIZE = "Size";
	private List<FileData> auxData;

    public static final String UP = "UP";
    public static final String DOWN = "DOWN";



	public TFileData () {
		fileData = new ArrayList<FileData>();
        auxData = fileData;
	}

    public List<FileData> getTableData () {
        return fileData;
    }

    public void setElements (Map<String, FileData> fd) {
        ArrayList<Long> sizes = new ArrayList<Long>();

        for (FileData f : fd.values())
            sizes.add(f.getSize());

        Collections.sort(sizes, Collections.reverseOrder());

        for (Long size : sizes) {
            for (Map.Entry<String, FileData> entry : fd.entrySet()) {
                if (entry.getValue().getSize() == size
                        && !fileData.contains(entry.getValue())){
                    fileData.add(entry.getValue());
                    break;
                }
            }
        }
    }

    public void reverseElements () {
        Collections.reverse(fileData);
        // for (int i = 0; i < fileData.size(); i++) {
            // for (int j = 0; j < columnNames.length; j++) {
                // System.out.println(getValueAt(i, j));
            // }
            // System.out.println();
        // }
    }

    public void filterExtention (String extention) {
        if (auxData.size() == fileData.size())
            auxData = new ArrayList<FileData>(fileData);
   		Iterator<FileData> it = fileData.iterator();
		while(it.hasNext()) {
			FileData next = it.next();
			if(!extention.equals(next.getExtention()) &&
                        !extention.equals("." + next.getExtention()))
				it.remove();
		}
    }

    public void filterPercentage (Float percentage, String order) {
        if (auxData.size() == fileData.size())
            auxData = new ArrayList<FileData>(fileData);
   		Iterator<FileData> it = fileData.iterator();
		while(it.hasNext()) {
			FileData next = it.next();
			if(percentage > next.getPercentage() && order.equals(UP))
				it.remove();
			if(percentage < next.getPercentage() && order.equals(DOWN))
				it.remove();
		}
    }

    public void filterNumber (Long number, String order) {
        if (auxData.size() == fileData.size())
            auxData = new ArrayList<FileData>(fileData);
   		Iterator<FileData> it = fileData.iterator();
		while(it.hasNext()) {
			FileData next = it.next();
			if(number > next.getNumberOfFiles() && order.equals(UP))
				it.remove();
			if(number < next.getNumberOfFiles() && order.equals(DOWN))
				it.remove();
		}
    }

    public void filterSize (Long size, String order) {
        if (auxData.size() == fileData.size())
            auxData = new ArrayList<FileData>(fileData);
   		Iterator<FileData> it = fileData.iterator();
		while(it.hasNext()) {
			FileData next = it.next();
			if(size > next.getSize() && order.equals(UP))
				it.remove();
			if(size < next.getSize() && order.equals(DOWN))
				it.remove();
		}
    }

    public void unfilter () {
        fileData = new ArrayList<FileData>(auxData);
    }

	@Override
	public void addTableModelListener(TableModelListener l) {

	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if(columnIndex == 3)
			return Long.class;
			
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
		return fileData.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {

		FileData fd = fileData.get(rowIndex);

		if (columnIndex == 0)
			return "." + fd.getExtention();

		if (columnIndex == 1)
			return fd.getPercentageFormat();

		if (columnIndex == 2)
			return fd.getNumberOfFiles();

		if (columnIndex == 3)
			return fd.getSizeUnit();

		return "";
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return true;
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {

	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		
	}

}
