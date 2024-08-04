package KeyboradActions;

import javax.swing.*;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;

public class CutAction extends AbstractAction {
    private JTable table;

    public CutAction(JTable table) {
        this.table = table;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int[] selectedRows = table.getSelectedRows();
        int[] selectedColumns = table.getSelectedColumns();

        if (selectedRows.length == 0 || selectedColumns.length == 0) {
            return;
        }

        StringBuilder sb = new StringBuilder();

        for (int row : selectedRows) {
            for (int col : selectedColumns) {
                Object cellValue = table.getValueAt(row, col);
                sb.append(cellValue == null ? "" : cellValue.toString());
                sb.append("\t");

                // Обнуляем ячейку после вырезания
                table.setValueAt(null, row, col);
            }
            sb.setLength(sb.length() - 1);
            sb.append("\n");
        }

        StringSelection stringSelection = new StringSelection(sb.toString());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }
}