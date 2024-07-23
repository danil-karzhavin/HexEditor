//package App;
//
//import FileService.FileService;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.AdjustmentEvent;
//import java.awt.event.AdjustmentListener;
//import java.io.IOException;
//
//public class HexAreaComponent {
//    FileService fs = null;
//    JTextArea area = null;
//    JScrollPane scrollPane = null;
//    App parentObj;
//    // размер буфера
//    public final int BUFFER_SIZE = 2048;
//    int currentPos = 0;
//    Boolean eraseEnableandWasRun = false;
//    int pos = 1;
//
//    HexAreaComponent(App parentObj){
//        this.parentObj = parentObj;
//        area = new JTextArea("Hex area", 20, 50);
//        setAppearance();
//    }
//
//    void setAppearance(){
//        Font font = new Font("Dialog", Font.PLAIN, 14);
//        area.setFont(font);
//        area.setTabSize(10);
//        area.setLineWrap(true);
//        area.setWrapStyleWord(true);
//    }
//
//    public JScrollPane getScrollPaneComponent(){
//        JScrollPane scrollPane = new JScrollPane(area);
//
//        scrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
//            @Override
//            public void adjustmentValueChanged(AdjustmentEvent e) {
//                if (!e.getValueIsAdjusting()) {
//                    JScrollBar scrollbar = (JScrollBar) e.getSource();
//
//                    int max = scrollbar.getModel().getMaximum();
//                    // Возвращает размер окна отображаемой части из всего содержимого (величина видимой части прокручиваемого содержимого)
//                    int extent = scrollbar.getModel().getExtent();
//                    int value = scrollbar.getModel().getValue();
//
//                    if (fs != null) {
//                        if (((max - extent) == value)) {
//                            try {
//                                pos = 1;
//                                if (currentPos < fs.length()) {
//                                    loadNextContent();
//                                    if (eraseEnableandWasRun) {
//                                        erasePrevContent();
//                                    }
//                                    eraseEnableandWasRun = true;
//                                }
//                            } catch (IOException ex) {
//                                ex.printStackTrace();
//                            }
//                        }
//                        if ((scrollbar.getValue() == scrollbar.getMinimum()) && eraseEnableandWasRun && currentPos != 0) {
//                            try {
//                                // если pos = 0, значит достигли начала файла
//                                if (pos > 0) {
//                                    loadPrevContent();
//
//                                    currentPos = currentPos - BUFFER_SIZE >= 0 ? currentPos - BUFFER_SIZE : 0;
//                                    eraseEndContent();
//                                }
//                            } catch (IOException ex) {
//                                ex.printStackTrace();
//                            }
//                        }
//                    }
//                }
//            }
//        });
//        return scrollPane;
//    }
//    // ok
//    public void loadNextContent() throws IOException {
//        String content = fs.readBlock(currentPos, BUFFER_SIZE);
//        currentPos += content.length();
//        area.append(content);
//        //System.out.println("loadMoreContent() called");
//    }
//
//    // ok
//    public void erasePrevContent() throws IOException{
//        area.replaceRange("", 0, BUFFER_SIZE);
//        area.setCaretPosition(area.getText().length() - BUFFER_SIZE);
//        //System.out.println("erasePrevContent() called");
//    }
//
//    // ok
//    public void loadPrevContent() throws IOException{
//        int sizeArea = area.getText().length();
//        pos = (currentPos - (BUFFER_SIZE + sizeArea)) >= 0 ? (currentPos - (BUFFER_SIZE + sizeArea)) : 0;
//        String content = fs.readBlock(pos, BUFFER_SIZE);
//        area.insert(content, 0);
//        //System.out.println("loadPrevContent() called");
//    }
//
//    // ok
//    public void eraseEndContent() throws IOException{
//        int sizeArea = area.getText().length();
//        area.replaceRange("", area.getText().length() - BUFFER_SIZE, sizeArea);
//        area.setCaretPosition(area.getText().length() - 100);
//        //System.out.println("eraseEndContent() called");
//    }
//
//    public JTextArea getJtextArea(){
//        return area;
//    }
//
//    public FileService createFileService(String path){
//        fs = new FileService(path);
//        return fs;
//    }
//    public FileService getFileService(){
//        if (fs == null)
//            return null;
//        return fs;
//    }
//
//}
