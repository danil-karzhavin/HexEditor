package TableCompnent;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;

import App.App;
import FileService.FileService;
import KeyboradActions.CopyAction;
import KeyboradActions.CutAction;
import KeyboradActions.PasteAction;
import TableCompnent.DialogFrame.DialogFrame;
import TextSearch.SearchSubArray;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class TableComponent implements ITableComponent {
    public FileService fs = null;
    JTable table = null;
    public CustomTableModel tableModel = null;
    public JScrollPane scrollPane;
    public App app = null;
    public static int countLinesInBlock = 100;
    public static int countColumnsInBlock = 0;
    int maxWidthRow = 0;
    // Модель столбцов таблицы
    private TableColumnModel columnModel;
    public ArrayList<TableBlock> blocks;

    public JButton nextPageBtn = null, prevPageBtn = null;
    public JLabel currentPage = null;
    public JTextField sizeRows, sizeColumns;
    public JButton apply;

    public TableComponent(App parentObj){
        this.app = parentObj;
        this.tableModel = new CustomTableModel();
        createTable();
        columnModel = table.getColumnModel();

        createScrollPaneTableComponent();

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

    public void createListeners(){
        prevPageBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (TableBlock.currentBlockPos > 0){
                    try{
                        var block = blocks.get(TableBlock.currentBlockPos);
                        fs.compareBlockWithFile(block);
                        if (block.changed){
                            fs.saveChangedBlockInFile(block);

                            int blockPos = TableBlock.currentBlockPos;
                            app.createFileService(fs.path);
                            loadContentByIndexBlock(blockPos);
                            // как я реализую вставку: по таблице вычисляю позицию байта в файле, вставляю сразу туда данные и пересоздаю FileService
                        }

                        TableBlock.currentBlockPos -= 1;
                        loadContentByIndexBlock(null);
                    }
                    catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
            }
        });

        nextPageBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (TableBlock.currentBlockPos < (blocks.size() - 1)){
                    try{
                        var block = blocks.get(TableBlock.currentBlockPos);
                        fs.compareBlockWithFile(block);
                        if (block.changed){
                            fs.saveChangedBlockInFile(block);

                            int blockPos = TableBlock.currentBlockPos;
                            app.createFileService(fs.path);
                            loadContentByIndexBlock(blockPos);
                        }

                        TableBlock.currentBlockPos += 1;
                        loadContentByIndexBlock(null);
                    }
                    catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
            }
        });

        apply.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    if (sizeRows.getText() != null && sizeRows.getText() != ""){
                        int value = Integer.parseInt(sizeRows.getText());
                        if (value <= 0)
                           throw new NumberFormatException();
                        countLinesInBlock = value;
                    }
                }
                catch (NumberFormatException ex){
                    sizeRows.setText("Некорр. значение для строк");
                }

                try{
                    if (sizeColumns.getText() != null && sizeColumns.getText() != ""){
                        int value = Integer.parseInt(sizeColumns.getText());
                        if (value <= 0)
                            throw new NumberFormatException();
                        countColumnsInBlock = value;
                    }
                }
                catch (NumberFormatException ex){
                    sizeColumns.setText("Некорр. значение для столбцов");
                }
                if (fs.path != null){
                    app.createFileService(fs.path);
                    try{
                        loadContentByIndexBlock(0);
                    }
                    catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    public JScrollPane createScrollPaneTableComponent(){
        scrollPane = new JScrollPane(table,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        return scrollPane;
    }

    @Override
    public FileService createFileService(FileService fs) {
        this.fs = fs;
        try {
            maxWidthRow = fs.getMaxWidthRow(); // нельзя вызывать больше одного раза
            if (maxWidthRow <= countColumnsInBlock){
                maxWidthRow = countColumnsInBlock;
            }
            else if (countColumnsInBlock != 0){
                sizeColumns.setText("Значение не может быть меньше самой длинной строки в файле");
            }

            blocks = fs.setBlockStatistics();
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
        setAppearance();
        return fs;
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
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        registerKeyBindings();

    }

    public void registerKeyBindings(){
        // Создаем действия
        Action copyAction = new CopyAction(table);
        Action cutAction = new CutAction(this);
        Action pasteAction = new PasteAction(this);

        // Регистрация действий для копирования, вырезания и вставки
        InputMap inputMap = table.getInputMap(JComponent.WHEN_FOCUSED);
        ActionMap actionMap = table.getActionMap();

        // Копирование
        inputMap.put(KeyStroke.getKeyStroke("ctrl C"), "copy");
        actionMap.put("copy", copyAction);

        // Вырезание
        inputMap.put(KeyStroke.getKeyStroke("ctrl X"), "cut");
        actionMap.put("cut", cutAction);

        // Вставка
        inputMap.put(KeyStroke.getKeyStroke("ctrl V"), "paste");
        actionMap.put("paste", pasteAction);
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
        table.setCellSelectionEnabled(true);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION );
    }

    public void setWidthTable(int width){
        String[] names = new String[width];

        names[0] = "№";

        for(int i = 1; i < width; ++i)
            names[i] = String.valueOf(i);

        tableModel.setColumnIdentifiers(names);
    }

    public void highlightSubText(SearchSubArray item){
        Integer startRow = null, endRow = null;
        Integer startCol = null, endCol = null;

        eraseSelectedCell();

        int sizeData = item.lengthSearchData;
        int posInRow = item.posInRow;

        int i = item.rowInFile - blocks.get(item.textBlockPos).numRow + 1;

        for(; i < tableModel.getDataVector().size(); ++i){
            for (int j = posInRow + 1; j < tableModel.getDataVector().get(i).indexOf("0A") + 1; ++j){
                if (startRow == null && startCol == null){
                    startRow = i;
                    startCol = j;
                }
                table.changeSelection(i, j, true, false);
                sizeData -= 1;
                if (sizeData == 0){
                    endRow = i;
                    endCol = j;
                    break;
                }
            }
            posInRow = 0;
            if (sizeData == 0)
                break;
        }
    }

    public void eraseSelectedCell(){
        // Снимаем выделение строк
        table.clearSelection();

        // Снимаем выделение столбцов (не обязательно, так как clearSelection сбросит все, но для наглядности)
        ListSelectionModel columnModel = table.getColumnModel().getSelectionModel();
        columnModel.clearSelection();
    }

}
