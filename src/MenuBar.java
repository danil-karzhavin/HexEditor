import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class MenuBar extends JFrame {
    App app;
    JMenuItem open, save, saveAnotherFile;

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
        save = new JMenu("Сохранить");
        saveAnotherFile = new JMenu("Сохранить в другой файл");


        // Добавим в меню пункты open and exit
        file.add(open);
        file.add(save);
        file.add(saveAnotherFile);

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


                app.hexArea.createFileService(path);
//              app.hexArea.getJtextArea().setText(app.fs.getStringContent());
//              app.charArea.setText(app.fs.getCharContent());
//              System.out.print(app.fs.getCharContent());
                try{
                    app.hexArea.loadMoreContent();
                }
                catch (IOException ex){
                    ex.printStackTrace();
                }
            }
        });

        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // save file
            }
        });

        saveAnotherFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                // save another file
            }
        });
        return file;
    }
}
