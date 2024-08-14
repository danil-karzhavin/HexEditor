package TableCompnent;

import FileService.FileService;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;

public interface ITableComponent {
    public JButton nextPageBtn = null, prevPageBtn = null;
    public JLabel currentPage = null;
    public ArrayList<TableBlock> blocks = null;
    public FileService createFileService(FileService fs);
    public void loadContentByIndexBlock(Integer index) throws IOException;

    public JScrollPane createScrollPaneTableComponent();

    public void createTable();
}
