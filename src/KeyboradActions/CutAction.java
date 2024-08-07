package KeyboradActions;

import TableCompnent.TableBlock;
import TableCompnent.TableComponent;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class CutAction extends AbstractAction {
    private TableComponent table;

    public CutAction(TableComponent table) {
        this.table = table;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int[] selectedRows = table.getTable().getSelectedRows();
        int[] selectedColumns = table.getTable().getSelectedColumns();

        if (selectedRows.length == 1 && selectedColumns.length == 1) {
            // Если выделена одна ячейка
            int row = selectedRows[0];
            int col = selectedColumns[0];

            int rowInFile = Integer.parseInt(table.tableModel.getDataVector().get(row).get(0).toString()) - 1;
            int posInRow = col - 1;
            int cutLength = 1;

            int position = table.fs.getPositionByRowCol(rowInFile, posInRow);
            table.fs.removeCutBytesInFile(position, cutLength);

            int blockPos = TableBlock.currentBlockPos;
            table.app.createFileService(table.fs.path);
            try {
                table.loadContentByIndexBlock(blockPos);
            } catch (IOException ex) {
                ex.printStackTrace();
            }


            System.out.println("Single cell selected at ()");
        } else if (selectedRows.length > 0 && selectedColumns.length > 0) {
            // Если выделено несколько ячеек
            int firstRow = selectedRows[0];
            int endRow = selectedRows[selectedRows.length - 1];
            int firstCol = selectedColumns[0];
            int endCol = selectedColumns[selectedColumns.length - 1];

            int startRowInFile = Integer.parseInt(table.tableModel.getDataVector().get(firstRow).get(0).toString()) - 1;
            int startColInFile = firstCol - 1;
            int endRowInFile = Integer.parseInt(table.tableModel.getDataVector().get(endRow).get(0).toString()) - 1;
            int endColInFile = endCol - 1;

            int position = table.fs.getPositionByRowCol(startRowInFile, startColInFile);
            int cutLength = getLengthSelectBlock(startRowInFile, firstCol, endRowInFile, endCol);
            table.fs.removeCutBytesInFile(position, cutLength);

            int blockPos = TableBlock.currentBlockPos;
            table.app.createFileService(table.fs.path);
            try {
                table.loadContentByIndexBlock(blockPos);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        } else {
            // Если ячейки не выделены
            System.out.println("No cells selected");
        }
    }

    public int getLengthSelectBlock(int startRow, int startCol, int endRow, int endCol){
        var data = table.tableModel.getDataVector();
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