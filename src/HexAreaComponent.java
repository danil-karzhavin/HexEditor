import javax.swing.*;
import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.File;
import java.io.IOException;

public class HexAreaComponent {
    FileService fs = null;
    JTextArea area = null;
    JScrollPane scrollPane = null;
    App parentObj;
    private final int THRESHOLD = 100;
    long currentPos = 0;
    private static final int LOAD_DELAY_MS = 200; // Задержка между загрузками в миллисекундах
    // Таймер для задержки загрузки
    private Timer loadTimer;
    HexAreaComponent(App parentObj){
        this.parentObj = parentObj;
        area = new JTextArea("Hex area", 20, 50);
        setAppearance();
    }

    void setAppearance(){
        Font font = new Font("Dialog", Font.PLAIN, 14);
        area.setFont(font);
        area.setTabSize(10);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
    }

    public JScrollPane getScrollPaneComponent(){
        JScrollPane scrollPane = new JScrollPane(area);

        scrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                if (e.getValueIsAdjusting()) {
                    JScrollBar scrollbar = (JScrollBar) e.getSource();
                    int max = scrollbar.getMaximum();
                    int extent = scrollbar.getModel().getExtent();
                    int value = scrollbar.getValue();
                    if (max - (value + extent) <= THRESHOLD) {
                        try {
                            if (currentPos < fs.length()){
                                loadMoreContent();
                                erasePrevContent();
                                System.out.println(value);
                            }
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });

        return scrollPane;
    }

    public void loadMoreContent() throws IOException {
        String content = fs.readBlock(currentPos);
        currentPos += fs.BUFFER_SIZE;
        area.append(content);
        System.out.println("loadMoreContent() called");
    }
    public void erasePrevContent() throws IOException{
        area.replaceRange("", 0, fs.BUFFER_SIZE);
        System.out.println("erasePrevContent() called");
    }

    public JTextArea getJtextArea(){
        return area;
    }

    public FileService createFileService(String path){
        fs = new FileService(path);
        return fs;
    }
    public FileService getFileService(){
        if (fs == null)
            return null;
        return fs;
    }

}
