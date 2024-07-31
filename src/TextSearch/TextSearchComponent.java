package TextSearch;

import javax.swing.*;
import App.App;

public class TextSearchComponent {
    public JTextField textSearch;
    public JLabel searchRes = null;
    public JButton searchBtn, backBtn, nextBtn;
    public App parentObj;

    public TextSearchComponent(JTextField textSearch, JLabel searchRes, JButton searchBtn, JButton backBtn, JButton nextBtn, App parentObj){
        this.textSearch = textSearch;
        this.searchRes = searchRes;
        this.searchBtn = searchBtn;
        this.backBtn = backBtn;
        this.nextBtn = nextBtn;
        this.parentObj = parentObj;

    }
    public void setAppearance(){
    }

}
