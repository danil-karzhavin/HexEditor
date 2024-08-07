package TableCompnent;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import TextSearch.SearchSubArray;

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

    public int getCountBytesInTable(){
        int countBytes = 0;
        for (var row : getDataVector()){
            countBytes += (row.size() - 1);
        }
        return countBytes;
    }

    public int getLengthSelectBlock(int startRow, int startCol, int endRow, int endCol){
        var data = getDataVector();
        int countRes = 0;

        if (startRow < endRow)
            for(int i = startRow; i <= startRow; ++i)
                for(int j = startCol; j < data.get(i).size(); ++j){
                    var el = data.get(i).get(j);
                    if (el != null)
                        countRes += 1;
                }


        for(int i = startRow + 1; i < endRow; ++i)
            for(int j = 1; j < data.get(i).size(); ++j){
                var el = data.get(i).get(j);
                if (el != null)
                    countRes += 1;
            }

        for(int i = endRow; i <= endRow; ++i)
            for(int j = 1; j <= endCol; ++j){
                var el = data.get(i).get(j);
                if (el != null)
                    countRes += 1;
            }

        return countRes;
    }
}
