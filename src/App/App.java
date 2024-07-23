package App;

import TableCompnent.ITableComponent;
import TableCompnent.TableComponent;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class App extends JFrame {
    public JMenuBar jmenuBar;
    public MenuBar menuBar;
    public ITableComponent hexTable;
    public JTextArea charArea;
    Box box;
    JPanel panel;
    // FileService.FileService fs = null;
    DialogExitComponent dialogExitComp;

    public static void main(String[] args) {
        // Подключение украшений для окон
        JFrame.setDefaultLookAndFeelDecorated(true);
        App app = new App();
    }
    public App(){
        super("Hex Editor");
        setDefaultCloseOperation( DO_NOTHING_ON_CLOSE );
        addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) { }
            @Override
            public void windowClosed(WindowEvent e) { }
            @Override
            public void windowIconified(WindowEvent e) { }
            @Override
            public void windowDeiconified(WindowEvent e) { }
            @Override
            public void windowActivated(WindowEvent e) { }
            @Override
            public void windowDeactivated(WindowEvent e) { }
            @Override
            public void windowClosing(WindowEvent event) {
//                if((hexArea.getFileService() == null) || (hexArea.getJtextArea().getText() == hexArea.getFileService().getStringContent()) || hexArea.getJtextArea().getText().length() == 0){
//                    System.exit(0);
//                }
//                else {
//                    dialogExitComp = new App.DialogExitComponent(App.App.this);
//                    dialogExitComp.createDialogFrame("Выход");
//                }
                System.exit(0);
            }
        });

        //////////////////////////////////////// JMenuBar ////////////////////////////////////////
        // Создание объекта строки главного меню
        jmenuBar = new JMenuBar();
        menuBar = new MenuBar(this);

        // Добавление в главное меню выпадающих пунктов меню
        jmenuBar.add(menuBar.createFileMenu()); // for file

        // Подключаем меню к интерфейсу приложения (JFrame.setJMenuBar)
        setJMenuBar(jmenuBar);
        ///////////////////////////////////////////////////////////////////////////////////////////
        Box box1 = new Box(BoxLayout.Y_AXIS);
        //////////////////////////////////////// JTableArea ////////////////////////////////////////
        hexTable = new TableComponent(this);

        box1.add(hexTable.getScrollPaneTableComponent());
        pack();

        ///////////////////////////////////////////////////////////////////////////////////////////
        setContentPane(box1);

        // Открытие окна
        setSize(1200, 800);
        setVisible(true);
    }
}
