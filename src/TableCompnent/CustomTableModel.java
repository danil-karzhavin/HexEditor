package TableCompnent;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class CustomTableModel extends DefaultTableModel {
    private List<byte[]> data;

    public CustomTableModel() {
        this.data = new ArrayList<byte[]>();
    }
    // ------------ add()
    public void addRowEnd(int currentRow, ArrayList<String> bytes) {
        Object[] rowData = new Object[bytes.size() + 1];
        rowData[0] = currentRow;
        for (int i = 1; i < bytes.size(); i++) {
            rowData[i] = bytes.get(i);
        }
        super.addRow(rowData);
    }
    // erase() ------------
    public void eraseStartRow(int countLines){
        int start = 0;
        int end = countLines;
        final int startPos = 0;

        for (int i = start; i < end; ++i)
            dataVector.removeElementAt(startPos);
    }

    // add() ------------
    public void addRowStart(int numRow, ArrayList<String> bytes, int posRow){
        Object[] rowData = new Object[bytes.size() + 1];
        rowData[0] = numRow;
        for (int i = 1; i < bytes.size(); i++) {
            rowData[i] = bytes.get(i);
        }
        super.insertRow(posRow, rowData);
    }
    // ------------ erase()
    public void eraseEndRow(){
        int pos = getDataVector().size() - 1;
        super.removeRow(pos);
    }

    // erase()
    public void eraseDataTable(){
        var data = getDataVector();
        data.clear();
    }

    public int getCountBytesInTable(){
        int countBytes = 0;
        for (var row : getDataVector()){
            countBytes += (row.size() - 1);
        }
        return countBytes;
    }
}
