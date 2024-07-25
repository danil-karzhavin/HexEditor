package TableCompnent;

import javax.swing.*;
import java.io.IOException;
import FileService.FileService;

public interface ITableComponent {
    public void createFileService(String path);

    public void loadNextContent() throws IOException;
    public int erasePrevContent() throws IOException;
    public void loadPrevContent() throws IOException;

    public JScrollPane getScrollPaneTableComponent();

    public void createTable();
    public JTable getTable();

    public void updateWidthTable();
}
