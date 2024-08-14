package App;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import FileService.FileService;

public class MenuBar extends JFrame {
    App app;
    JMenuItem open;

    public MenuBar(App app){
        this.app = app;
    }
    public JMenu createFileMenu()
    {

        // Создание экземпляра JFileChooser
        JFileChooser fileChooser = new JFileChooser();

        // Создание выпадающего меню
        JMenu file = new JMenu("Файл");

        // Пункт меню "Открыть" с изображением
        open = new JMenuItem("Открыть",
                new ImageIcon("images/open.png"));

        // Добавим в меню пункты open and exit
        file.add(open);

        open.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileChooser.setDialogTitle("Выбор директории");
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int result = fileChooser.showOpenDialog(null);
                // Если директория выбрана, покажем ее в сообщении
                String path = null;
                if (result == JFileChooser.APPROVE_OPTION )
                    path = fileChooser.getSelectedFile().getAbsolutePath(); // need call back

                app.createFileService(path);
                try{
                    app.hexTable.loadContentByIndexBlock(0);
                }
                catch (IOException ex){
                    ex.printStackTrace();
                }
            }
        });
        return file;
    }
}
