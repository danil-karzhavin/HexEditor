package TableCompnent;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;

public interface ITableComponent {
    public JButton nextPageBtn = null, prevPageBtn = null;
    public JLabel currentPage = null;
    public ArrayList<TableBlock> blocks = null;
    public void createFileService(String path);
    public void loadContentByIndexBlock(Integer index) throws IOException;

    public JScrollPane getScrollPaneTableComponent();

    public void createTable();
}
