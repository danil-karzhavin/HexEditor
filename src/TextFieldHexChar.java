import javax.swing.*;
import java.awt.*;

public class TextFieldHexChar {
    static JTextArea createTextAreaComponent(){
        return new JTextArea(15, 10);
    }

    static void getFormatTextHexComponent(JTextArea area){
        Font font = new Font("Dialog", Font.PLAIN, 14);
        area.setFont(font);
        area.setTabSize(10);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
    }

    static void getFormatTextCharComponent(JTextArea area){
        Font font = new Font("Dialog", Font.PLAIN, 14);
        area.setFont(font);
        area.setTabSize(10);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
    }
}
