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
    public void eraseStartRow(int currentRow, int countLines){
        //int start = (currentRow - 2 * countLines) >= 0 ? (currentRow - 2 * countLines) : 0;
        int start = 0;
        //int end = currentRow - countLines;
        int end = countLines;
        final int startPos = 0;

        for (int i = start; i < end; ++i)
            dataVector.removeElementAt(startPos);
        //fireTableRowsDeleted(startIndex, startIndex);
    }

    // add() ------------
    public void addRowStart(ArrayList<String> bytes){
    }
    // ------------ erase()
    public void eraseEndRow(){
        int pos = data.size() - 1;
        data.remove(pos);

        //fireTableRowsDeleted(pos, pos);
    }

    public int getCountBytesInTable(){
        int countBytes = 0;
        for (var row : getDataVector()){
            countBytes += (row.size() - 1);
        }
        return countBytes;
    }
}
