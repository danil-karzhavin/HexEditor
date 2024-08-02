package App;

import TableCompnent.ITableComponent;
import TableCompnent.TableComponent;
import TextSearch.TextSearchComponent;
import FileService.FileService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

public class App extends JFrame {
    public JButton nextPageBtn, prevPageBtn;
    public JLabel currentPage;
    public JMenuBar jmenuBar;
    public MenuBar menuBar;
    public TableComponent hexTable;
    public JPanel panel;
    public FileService fs = null;

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
        panel = new JPanel();

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


        createJMenuBar();
        createTableComponent();
        createTextSearchComponent();

        pack();
        setContentPane(panel);
        setSize(1200, 800);
        setVisible(true);
    }

    public void createFileService(String path){
        FileService.path = path;

        fs = new FileService(path);
        fs.app = this;

        hexTable.createFileService(fs);
    }

    void createJMenuBar(){
        jmenuBar = new JMenuBar();
        menuBar = new MenuBar(this);
        jmenuBar.add(menuBar.createFileMenu()); // for file
        setJMenuBar(jmenuBar);
    }

    void createTableComponent(){
        hexTable = new TableComponent(this);

        panel.add(hexTable.getScrollPaneTableComponent());
        prevPageBtn = new JButton("Предыдущая страница");
        currentPage = new JLabel("1 из ...");
        nextPageBtn = new JButton("Следующая страница");

        hexTable.prevPageBtn = prevPageBtn;
        hexTable.nextPageBtn = nextPageBtn;
        hexTable.currentPage = currentPage;

        panel.add(prevPageBtn);
        panel.add(currentPage);
        panel.add(nextPageBtn);

        hexTable.createListeners();
    }

    void createTextSearchComponent(){
        textSearchComp = new TextSearchComponent(this);

        textSearch = new JTextField(100);
        searchRes = new JLabel();
        searchBtn = new JButton("Поиск");
        backBtn = new JButton("Назад");
        nextBtn = new JButton("Вперед");

        textSearchComp.textSearch = textSearch;
        textSearchComp.searchRes = searchRes;
        textSearchComp.searchBtn = searchBtn;
        textSearchComp.backBtn = backBtn;
        textSearchComp.nextBtn = nextBtn;
        textSearchComp.createListeners();

        panel.add(textSearch);
        panel.add(searchRes);
        panel.add(searchBtn);
        panel.add(backBtn);
        panel.add(nextBtn);
    }
}
