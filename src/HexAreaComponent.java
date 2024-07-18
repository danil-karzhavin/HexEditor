import javax.swing.*;
import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.IOException;

public class HexAreaComponent {
    FileService fs = null;
    JTextArea area = null;
    JScrollPane scrollPane = null;
    App parentObj;
    private final int THRESHOLD = 100;
    int currentPos = 0;
    Boolean eraseEnableandWasRun = false;
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
                if (!e.getValueIsAdjusting()) {
                    JScrollBar scrollbar = (JScrollBar) e.getSource();
                    int max = scrollbar.getModel().getMaximum();

                    // Возвращает размер окна отображаемой части из всего содержимого (величина видимой части прокручиваемого содержимого)
                    int extent = scrollbar.getModel().getExtent();
                    int value = scrollbar.getModel().getValue();
//                    if (max - (value + extent) <= THRESHOLD) {
                    if (fs != null) {
                        if (((max - extent) == value)) {
                            System.out.print(scrollbar.getValue());
                            try {
                                if (currentPos < fs.length()) {
                                    loadNextContent();
                                    if (eraseEnableandWasRun)
                                        erasePrevContent();
                                    eraseEnableandWasRun = true;


                                    scrollPane.revalidate();
                                    scrollPane.repaint();

                                }
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
//                        if ((scrollbar.getValue() == scrollbar.getMinimum()) && eraseEnableandWasRun && currentPos != 0) {
//                            System.out.print(scrollbar.getValue());
//                            try {
//                                loadPrevContent();
//                                eraseEndContent();
//                            } catch (IOException ex) {
//                                ex.printStackTrace();
//                            }
//                        }
                    }
                }
            }
        });

        return scrollPane;
    }

    public void loadNextContent() throws IOException {
        String content = fs.readBlock(currentPos);
        currentPos += fs.BUFFER_SIZE;
        area.append(content);
        // System.out.println("loadMoreContent() called");
    }
    public void erasePrevContent() throws IOException{
        area.replaceRange("", 0, fs.BUFFER_SIZE);
        // System.out.println("erasePrevContent() called");
    }

    public void loadPrevContent() throws IOException{
        //currentPos -= 2 * fs.BUFFER_SIZE;
        int pos = currentPos - 2 * fs.BUFFER_SIZE >= 0 ? currentPos - 2 * fs.BUFFER_SIZE : 0;
        String content = fs.readBlock(pos);
        area.insert(content, 0);
        System.out.println("loadPrevContent() called");
    }

    public void eraseEndContent() throws IOException{
        currentPos = currentPos - fs.BUFFER_SIZE >= 0 ? currentPos - fs.BUFFER_SIZE : 0;
        area.replaceRange("", currentPos, fs.BUFFER_SIZE);
        System.out.println("eraseEndContent() called");
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
