package TableCompnent;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;

import App.App;
import FileService.FileService;
import TableCompnent.DialogFrame.DialogFrame;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class TableComponent implements ITableComponent {
    public int counter = 0;
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
    public ArrayList<TableBlock> blocks;

    public JButton nextPageBtn = null, prevPageBtn = null;
    public JLabel currentPage = null;

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

        prevPageBtn = new JButton("Предыдущая страница");
        prevPageBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (TableBlock.currentBlockPos > 0){
                    try{
                        TableBlock.currentBlockPos -= 1;
                        loadContentByIndexBlock(null);
                    }
                    catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
            }
        });
        parentObj.panel.add(prevPageBtn);

        currentPage = new JLabel("1 из ...");
        parentObj.panel.add(currentPage);

        nextPageBtn = new JButton("Следующая страница");
        nextPageBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (TableBlock.currentBlockPos < (blocks.size() - 1)){
                    try{
                        TableBlock.currentBlockPos += 1;
                        loadContentByIndexBlock(null);
                    }
                    catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
            }
        });
        parentObj.panel.add(nextPageBtn);
    }

    public JScrollPane getScrollPaneTableComponent(){
        JScrollPane scrollPane = new JScrollPane(table,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        return scrollPane;
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

    public void loadContentByIndexBloc(int index) throws IOException{
        if (index < 0)
            return;
        TableBlock.currentBlockPos = index - 2;

        tableModel.eraseDataTable(); // стираем всю таблицу

        //loadNextContent();
        //loadNextContent();

    }

    @Override
    public void loadContentByIndexBlock(Integer nullableIndex) throws IOException {
        int index = nullableIndex == null ? TableBlock.currentBlockPos : nullableIndex;

        tableModel.eraseDataTable();

        TableBlock.currentBlockPos = index;
        currentPage.setText(String.format("%d из %d", index + 1, blocks.size()));

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

    public void createTable(){
        table = new JTable(tableModel);
    }

    public JTable getTable(){
        return table;
    }

    public void setAppearance(){
        setWidthTable(maxWidthRow + 1);
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

    public void setWidthTable(int width){
        String[] names = new String[width];

        names[0] = "№";

        for(int i = 1; i < width; ++i)
            names[i] = String.valueOf(i);

        tableModel.setColumnIdentifiers(names);
    }

}
