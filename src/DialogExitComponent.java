import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DialogExitComponent extends JDialog {
    JButton buttonSave, buttonExit;
    String title;
    JPanel panel;

    public DialogExitComponent(String title){
        this.title = title;
    }

    public JDialog createDialogFrame()
    {
        JDialog dialog = new JDialog(this, title, true);
        dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        dialog.setSize(400, 200);

        buttonSave = new JButton("Сохранить изменения");
        buttonExit = new JButton("Выйти, изменения будут потеряны");

        buttonSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                // saving functionality
                System.exit(0);
                // dialog.dispose();
            }
        });

        buttonExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                System.exit(0);
            }
        });

        dialog.add(buttonSave);
        dialog.add(buttonExit);

        panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.add(buttonSave);
        panel.add(buttonExit);

        // Добавляем панель с кнопками в диалог
        dialog.add(panel, BorderLayout.CENTER);

        // Автоматически подстраиваем размер окна под компоненты
        dialog.pack();

        // Центрируем диалог
        dialog.setLocationRelativeTo(null);

        // Отображаем диалог
        dialog.setVisible(true);
        return dialog;
    }

}
