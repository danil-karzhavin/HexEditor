package App;

import TableCompnent.ITableComponent;
import TableCompnent.TableComponent;
import TextSearch.TextSearchComponent;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class App extends JFrame {
    public JButton nextPageBtn, prevPageBtn;
    public JLabel currentPage;
    public JMenuBar jmenuBar;
    public MenuBar menuBar;
    public TableComponent hexTable;
    public JTextArea charArea;
    //Box box1;
    public JPanel panel;
    // FileService.FileService fs = null;
    DialogExitComponent dialogExitComp;

    JTextField textSearch = null;
    JLabel searchRes = null;
    JButton searchBtn, backBtn, nextBtn;
    TextSearchComponent textSearchComp;

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
        panel = new JPanel();
        //////////////////////////////////////// JTableArea ////////////////////////////////////////
        hexTable = new TableComponent(this);

        panel.add(hexTable.getScrollPaneTableComponent());

        ///////////////////////////////////////////////////////////////////////////////////////////

        //////////////////////////////////////// TextSearch ////////////////////////////////////////
        textSearch = new JTextField(100);
        searchRes = new JLabel("Найдено 2 совпадения");
        searchBtn = new JButton("Поиск");
        backBtn = new JButton("Назад");
        nextBtn = new JButton("Вперед");

        panel.add(textSearch);
        panel.add(searchRes);
        panel.add(searchBtn);
        panel.add(backBtn);
        panel.add(nextBtn);

        textSearchComp = new TextSearchComponent(textSearch, searchRes, searchBtn, backBtn, nextBtn, this);

        pack();
        setContentPane(panel);

        // Открытие окна
        setSize(1200, 800);
        setVisible(true);
    }
}
