package TableCompnent;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class CustomTableModel extends DefaultTableModel {

    public CustomTableModel() {
    }
    // ------------ add()
    public void addRowEnd(int currentRow, ArrayList<String> bytes) {
        Object[] rowData = new Object[bytes.size() + 1];
        rowData[0] = currentRow;
        for (int i = 1; i < rowData.length; i++) {
            rowData[i] = bytes.get(i - 1);
        }
        super.addRow(rowData);
    }

    // erase()
    public void eraseDataTable(){
        var data = getDataVector();
        data.clear();
    }

    public void highlightSubText(ArrayList<Byte> bytes){
        for (int i = 0; i < dataVector.size(); ++i){
            for (int j = 0; i < dataVector.get(i).size(); ++j){

            }
        }
    }

    public int getCountBytesInTable(){
        int countBytes = 0;
        for (var row : getDataVector()){
            countBytes += (row.size() - 1);
        }
        return countBytes;
    }
}
