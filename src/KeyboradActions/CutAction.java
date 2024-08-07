package KeyboradActions;

import TableCompnent.TableBlock;
import TableCompnent.TableComponent;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Vector;

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
            if (col == 0)
                return;

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
            int firstRow = selectedRows[0];
            int endRow = selectedRows[selectedRows.length - 1];
            int firstCol = selectedColumns[0];
            int endCol = selectedColumns[selectedColumns.length - 1];
            if (firstCol == 0 || endCol == 0)
                return;

            int startRowInFile = Integer.parseInt(table.tableModel.getDataVector().get(firstRow).get(0).toString()) - 1;
            int startColInFile = firstCol - 1;
            int endRowInFile = Integer.parseInt(table.tableModel.getDataVector().get(endRow).get(0).toString()) - 1;

            int position = table.fs.getPositionByRowCol(startRowInFile, startColInFile);
            int cutLength = table.tableModel.getLengthSelectBlock(startRowInFile, firstCol, endRowInFile, endCol);
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
}