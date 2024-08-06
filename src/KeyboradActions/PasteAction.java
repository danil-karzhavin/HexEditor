package KeyboradActions;

import TableCompnent.TableBlock;
import TableCompnent.TableComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import HexTools.HexTools;

public class PasteAction extends AbstractAction {
    TableComponent table;

    public PasteAction(TableComponent table){
        this.table = table;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable contents = clipboard.getContents(null);
        List<Integer> hexValues = new ArrayList<Integer>();;

        if (contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            // Извлекаем текст из буфера обмена
            try{
                String text = (String) contents.getTransferData(DataFlavor.stringFlavor);
                hexValues = HexTools.StringToBytes(text);
            }
            catch (UnsupportedFlavorException ex){}
            catch (IOException ex){}
        }
        int[] selectedRows = table.getTable().getSelectedRows();
        int[] selectedColumns = table.getTable().getSelectedColumns();

        if (selectedRows.length == 1 && selectedColumns.length == 1) {
            // Если выделена одна ячейка
            int row = selectedRows[0];
            int col = selectedColumns[0];
            int rowInFile = Integer.parseInt(table.tableModel.getDataVector().get(row).get(0).toString()) - 1;
            int posInRow = col - 1;
            table.fs.savePasteBytesInFile(rowInFile, posInRow, hexValues);

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



        } else {
            // Если ячейки не выделены
            System.out.println("No cells selected");
        }
    }
}
