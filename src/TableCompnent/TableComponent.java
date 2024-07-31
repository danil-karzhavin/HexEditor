package TableCompnent;

import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.TreeMap;

import App.App;
import FileService.FileService;
import TableCompnent.DialogFrame.DialogFrame;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class TableComponent implements ITableComponent {
    FileService fs = null;
    JTable table = null;
    public CustomTableModel tableModel = null;
    JScrollPane scrollPane = null;
    public App parentObj = null;
    public final static int countLinesInBlock = 100;
    int maxWidthRow = 1;
    boolean loadWasRun = false;
    // Модель столбцов таблицы
    private TableColumnModel columnModel;
    private ArrayList<TableBlock> blocks;

    public TableComponent(App parentObj){
        this.parentObj = parentObj;
        this.tableModel = new CustomTableModel();
        createTable();
        columnModel = table.getColumnModel();

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int row = table.rowAtPoint(e.getPoint());
                    int col = table.columnAtPoint(e.getPoint());

                    if (row != -1 && col != -1) {
                        new DialogFrame(TableComponent.this, row, col);
                    }
                }
            }
        });
    }

    public JScrollPane getScrollPaneTableComponent(){
        JScrollPane scrollPane = new JScrollPane(table,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);


        scrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                if (!e.getValueIsAdjusting()) {
                    JScrollBar scrollbar = (JScrollBar) e.getSource();

                    int max = scrollbar.getModel().getMaximum();
                    int min = scrollbar.getModel().getMinimum();
                    int extent = scrollbar.getModel().getExtent(); // Возвращает размер окна отображаемой части из всего содержимого (величина видимой части прокручиваемого содержимого)
                    int value = scrollbar.getModel().getValue();

                    if (fs != null) {
                        if (((max - extent) == value) && (TableBlock.currentBlockPos + 1 < blocks.size())) {
                            try {
                                loadWasRun = true;
                                loadNextContent();
                                erasePrevContent();

                                SwingUtilities.invokeLater(() -> setSelectedRow(countLinesInBlock, true));
                            }
                            catch(IOException ex){
                                ex.printStackTrace();
                            }
                        }
                        else if((scrollbar.getValue() == min) && (TableBlock.currentBlockPos > 1) && loadWasRun){
                            try {
                                loadPrevContent();
                                eraseEndContent();

                                //SwingUtilities.invokeLater(() -> setSelectedRow(countLinesInBlock, false));
                            }
                            catch (IOException ex){
                                ex.printStackTrace();
                            }
                            catch (IndexOutOfBoundsException ex_1){
                                ex_1.printStackTrace();
                            }
                        }

                    }
                }
            }
        });
        return scrollPane;
    }

    // установка выделения на определенной строке
    public void setSelectedRow(int rowIndex, boolean isNext) {
        // Проверяем, что индекс допустим
        if (rowIndex >= 0) {

            Rectangle cellRect = table.getCellRect(rowIndex, 0, true);
            // Смещаем прямоугольник вверх на высоту видимой области, чтобы строка оказалась внизу
            JViewport viewport = (JViewport) table.getParent();
            Rectangle viewRect = viewport.getViewRect();
            if (isNext)
                cellRect.y -= viewRect.height;
            else
                cellRect.y -= viewRect.height;

            // Прокручиваем таблицу, чтобы выбранная строка была видна внизу
            table.scrollRectToVisible(cellRect);
        } else {
            System.err.println("Invalid row index: " + rowIndex);
        }
    }

    @Override
    public void createFileService(String path) {
        fs = new FileService(path);
        try {
            maxWidthRow = fs.getMaxWidthRow(); // нельзя вызывать больше одного раза
            blocks = fs.setBlockStatistics();
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
        setAppearance();
    }

    @Override
    public void loadNextContent() throws IOException {
        TableBlock.currentBlockPos += 1;
        TableBlock.countBlocksOnScreen += 1;

        int posByteNextBlock = blocks.get(TableBlock.currentBlockPos).firstBytePos;
        var block = blocks.get(TableBlock.currentBlockPos);

        for(int i = 0; i < blocks.get(TableBlock.currentBlockPos).countRows; ++i){
            ArrayList<String> data = fs.readOneLine(posByteNextBlock);

            if (data.size() == 0)
                break;

            if (data.size() > maxWidthRow)
                maxWidthRow = data.size();

            posByteNextBlock += data.size();

            tableModel.addRowEnd(block.numRow + i, data);
        }
    }

    public int erasePrevContent() throws IOException{
        int pos = TableBlock.currentBlockPos - (TableBlock.countBlocksOnScreen - 1);
        int countRowsErase = blocks.get(pos).countRows;

        tableModel.eraseStartRow(countRowsErase);
        TableBlock.countBlocksOnScreen -= 1;
        //System.out.println("erasePrevContent() called");
        return countRowsErase;
    }

    public void loadPrevContent() throws IndexOutOfBoundsException, IOException{
        int blockPos = TableBlock.currentBlockPos - TableBlock.countBlocksOnScreen;
        var block = blocks.get(blockPos);
        int posByteNextBlock = block.firstBytePos;
        ArrayList<String> data;

        if (blockPos < 0)
            throw new IndexOutOfBoundsException();

        for (int i = 0; i < block.countRows; i++){
            data = fs.readOneLine(posByteNextBlock);

            if (data.size() == 0)
                break;

            posByteNextBlock += data.size();
            tableModel.addRowStart(block.numRow + i, data, i);
        }

        TableBlock.countBlocksOnScreen += 1;

    }

    public void eraseEndContent(){
        int blockPos = TableBlock.currentBlockPos;
        var block = blocks.get(blockPos);
        for(int i = 0; i < block.countRows; ++i)
            tableModel.eraseEndRow();

        TableBlock.currentBlockPos -= 1;
        TableBlock.countBlocksOnScreen -= 1;
    }

    public void createTable(){
        table = new JTable(tableModel);
    }

    public JTable getTable(){
        return table;
    }

    public void setAppearance(){
        setWidthTable(maxWidthRow);
        table.setVisible(true);

        //table.setSize(100, 100);
        table.setPreferredScrollableViewportSize(new Dimension(1000, 500));
        Enumeration<TableColumn> e = columnModel.getColumns();
        while ( e.hasMoreElements() ) {
            TableColumn column = (TableColumn)e.nextElement();
            column.setMinWidth(30);
            column.setMaxWidth(40);
        }
    }

    public void updateWidthTable(){
        var currentWidth = tableModel.getColumnCount();
        if (maxWidthRow > currentWidth){

            for(int i = currentWidth; i < maxWidthRow; ++i) {
                TableColumn сolumn = new TableColumn(i, 50);

                сolumn.setHeaderValue(String.valueOf(i));

                columnModel.addColumn(сolumn);
            }
            //tableModel.setColumnIdentifiers(names);
        }
    }

    public void setWidthTable(int width){
        String[] names = new String[width];

        names[0] = "№";

        for(int i = 1; i < width; ++i)
            names[i] = String.valueOf(i);

        tableModel.setColumnIdentifiers(names);
    }

}
