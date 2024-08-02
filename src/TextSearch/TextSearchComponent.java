package TextSearch;

import javax.swing.*;
import App.App;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import FileService.FileService;

public class TextSearchComponent {
    List<Byte> hexValues;
    //    List<SearchSubArray> positionsInFile;
    List<SearchSubArray> indexBlockByPosInFile;
    int currentBlockPos = 0;
    public JTextField textSearch;
    public JLabel searchRes = null;
    public JButton searchBtn, backBtn, nextBtn;
    public App app;

    public TextSearchComponent(App app) {
        this.app = app;
    }

    public void createListeners(){
        searchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String data = textSearch.getText();
                hexValues = StringToBytes(data);
                indexBlockByPosInFile = app.fs.SearchSubArray(hexValues);

                try {
                    currentBlockPos = 0;
                    int indexBlock = indexBlockByPosInFile.get(currentBlockPos).textBlockPos;
                    app.hexTable.loadContentByIndexBlock(indexBlock);
                    searchRes.setText(String.format("%d из %d", currentBlockPos + 1, indexBlockByPosInFile.size()));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ((currentBlockPos > 0)) {
                    try {
                        currentBlockPos -= 1;
                        int indexBlock = indexBlockByPosInFile.get(currentBlockPos).textBlockPos;

                        app.hexTable.loadContentByIndexBlock(indexBlock);
                        searchRes.setText(String.format("%d из %d", currentBlockPos + 1, indexBlockByPosInFile.size()));
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        nextBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentBlockPos < indexBlockByPosInFile.size() - 1) {
                    try {
                        currentBlockPos += 1;
                        int indexBlock = indexBlockByPosInFile.get(currentBlockPos).textBlockPos;

                        app.hexTable.loadContentByIndexBlock(indexBlock);
                        searchRes.setText(String.format("%d из %d", currentBlockPos + 1, indexBlockByPosInFile.size()));
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    public void setAppearance() {
    }

    public List<Byte> StringToBytes(String hexString) {
        hexValues = new ArrayList<Byte>();

        // Регулярное выражение для пары шестнадцатеричных символов
        Pattern pattern = Pattern.compile("([0-9A-Fa-f]{2})");
        Matcher matcher = pattern.matcher(hexString);

        // Поиск всех пар шестнадцатеричных символов
        while (matcher.find()) {
            byte b = FileService.hexStringToByte(matcher.group());
            hexValues.add(b);
        }
        return hexValues;
    }
}
