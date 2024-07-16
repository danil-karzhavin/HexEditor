import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class App extends JFrame {
    public JMenuBar jmenuBar;
    public MenuBar menuBar;
    public JTextArea hexArea, charArea;
    JPanel panel;
    FileService fs;
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
                dialogExitComp = new DialogExitComponent("Выход");
                dialogExitComp.createDialogFrame();
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
        //////////////////////////////////////// JTextField ////////////////////////////////////////
        hexArea = new JTextArea("Hex Area", 20, 50);
        TextFieldHexChar.getFormatTextHexComponent(hexArea);

        charArea = new JTextArea("Char Area", 20, 50);
        TextFieldHexChar.getFormatTextCharComponent(charArea);
        panel.add(new JScrollPane(hexArea));
        panel.add(new JScrollPane(charArea));
        ///////////////////////////////////////////////////////////////////////////////////////////
        setContentPane(panel);

        // Открытие окна
        setSize(1200, 800);
        setVisible(true);
    }
}
