package r0p3GUI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import r0p3.FileData;

public class TableFileData implements TableModel {

	private List<FileData> fileData;
    private String[] columnNames = {EXTENTION, PERCENTAGE, NUMBER, SIZE};
    public static final String EXTENTION = "Extension";
    public static final String PERCENTAGE = "Percentage";
    public static final String NUMBER = "Number";
    public static final String SIZE = "Size";
	private List<FileData> auxData;

    public static final String UP = "UP";
    public static final String DOWN = "DOWN";


    /**
     * TableFileData constructor
     * */
	public TableFileData () {
		fileData = new ArrayList<FileData>();
        // Lo hice por un motivo que no recuerdo, solo recuerdo que petaba si no lo ponia
        auxData = fileData;
	}

    /**
     * File Data list getter
     *
     * @return  a sorted List containing the File Data
     * */
    public List<FileData> getTableData () {
        return fileData;
    }

    /**
     * File Data elements setter
     *
     * @param fd    a Map containing all the file data
     * */
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

    /**
     * Reverse the file data List
     * */
    public void reverseElements () {
        Collections.reverse(fileData);
        // for (int i = 0; i < fileData.size(); i++) {
            // for (int j = 0; j < columnNames.length; j++) {
                // System.out.println(getValueAt(i, j));
            // }
            // System.out.println();
        // }
    }

    /**
     * Filter the file data by extension
     *
     * @param extension the file extension to be filtered
     * */
    public void filterExtension (String extension) {
        if (auxData.size() == fileData.size())
            auxData = new ArrayList<FileData>(fileData);
   		Iterator<FileData> it = fileData.iterator();
		while(it.hasNext()) {
			FileData next = it.next();
			if(!extension.equals(next.getExtension()) &&
                        !extension.equals("." + next.getExtension()))
				it.remove();
		}
    }

    /**
     * Filter the file data by percentage
     *
     * @param percentage    the file size percentage
     * @param order         the order to be filtered
     * */
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

    /**
     * Filter the file data by number of files
     *
     * @param number        the file number
     * @param order         the order to be filtered
     * */
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

    /**
     * Filter the file data by size of files
     *
     * @param size          the file size
     * @param order         the order to be filtered
     * */
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

    /**
     * Undo the filter to the normal state
     * */
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
			return "." + fd.getExtension();

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
		return false;
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {

	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		
	}

}
