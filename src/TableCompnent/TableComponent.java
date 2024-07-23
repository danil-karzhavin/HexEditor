package TableCompnent;

import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.TreeMap;

import App.App;
import FileService.FileService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

public class TableComponent implements ITableComponent {
    FileService fs = null;
    JTable table = null;
    CustomTableModel tableModel = null;
    JScrollPane scrollPane = null;
    App parentObj = null;
    int position = 0;
    int currentRow = 0;
    final int countLines= 100;
    int maxWidthRow = 1;
    // Модель столбцов таблицы
    private TableColumnModel columnModel;
    private int countLoadedBlocks = 0;

    public TableComponent(App parentObj){
        this.parentObj = parentObj;
        this.tableModel = new CustomTableModel();
        createTable();
        columnModel = table.getColumnModel();
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
                        if (((max - extent) == value) && (currentRow < fs.countLines)) {
                            try {
                                loadNextContent();
                                erasePrevContent();

                                // Выбираем строку после отображения окна
                                int tmp = countLines;

                                SwingUtilities.invokeLater(() -> setSelectedRow(tmp));
                            }
                            catch(IOException ex){
                                ex.printStackTrace();
                            }
                        }
//                        else if((scrollbar.getValue() == min) && position != 0){
//                            try {
//
//                            }
//                            catch (IOException ex){
//                                ex.printStackTrace();
//                            }
//                        }

                    }
                }
            }
        });
        return scrollPane;
    }

    // установка выделения на определенной строке
    public void setSelectedRow(int rowIndex) {
        // Проверяем, что индекс допустим
        if (rowIndex >= 0) {

            Rectangle cellRect = table.getCellRect(rowIndex, 0, true);
            // Смещаем прямоугольник вверх на высоту видимой области, чтобы строка оказалась внизу
            JViewport viewport = (JViewport) table.getParent();
            Rectangle viewRect = viewport.getViewRect();
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
            maxWidthRow = fs.getMaxWidthRow();
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
        setAppearance();
    }

    @Override
    public void loadNextContent() throws IOException {
        for(int i = 0; i < countLines; ++i){
            ArrayList<String> data = fs.readOneLine(position);

            if (data.size() == 0)
                break;

            if (data.size() > maxWidthRow)
                maxWidthRow = data.size();

            currentRow += 1;
            position += data.size();

            tableModel.addRowEnd(currentRow, data);
        }
        countLoadedBlocks += 1;
    }

    public void erasePrevContent() throws IOException{
        tableModel.eraseStartRow(currentRow, countLines);
        //System.out.println("erasePrevContent() called");
    }

    public void loadPrevContent() throws IOException{

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
