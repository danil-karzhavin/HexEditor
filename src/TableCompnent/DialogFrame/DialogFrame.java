package TableCompnent.DialogFrame;

import FileService.FileService;
import TableCompnent.CustomTableModel;
import TableCompnent.TableComponent;

import javax.swing.*;

public class DialogFrame {
    int row, col;
    public byte[] bytes;
    public String hexValues = "";

    TableComponent tableComponent;

    public DialogFrame(TableComponent tableComponent, int row, int col){
        this.row = row;
        this.col = col;
        this.tableComponent = tableComponent;
        readBytes();
        createDialog();
    }

    public void createDialog(){
        String[] data = new String[]{"Считанные блок: " + hexValues,
                "2 байт как символ char: " + String.valueOf(bytesToChar()),
                "2 байт как число без знака: " + String.valueOf(bytesToUnSignValue()),
                "2 байт как число со знаком: " + String.valueOf(bytesToSignValue()),
                "4 байт как число без знака: " + String.valueOf(bytesToUnSignInt()),
                "4 байт как число со знаком: " + String.valueOf(bytesToSignInt()),
                "4 байт как вещественное число одинарной точности: " + String.valueOf(bytesToFloat()),
                "8 байт как вещественное число двойной точности: " + String.valueOf(bytesToDouble())};

        JOptionPane.showMessageDialog(tableComponent.parentObj, data, "Отображение блока байт", JOptionPane.INFORMATION_MESSAGE);
    }

    public void setAppearance(){

    }

    public void readBytes(){
        CustomTableModel tableModel = tableComponent.tableModel;

        bytes = new byte[8];
        for(int i = 0; i < 8; ++i){
            try{
                String hex = tableModel.getDataVector().get(row).get(i + col).toString();
                byte el = (byte) FileService.hexStringToByte(hex);
                hexValues += hex + " ";
                bytes[i] = el;
            }
            catch(IllegalArgumentException ex){
                ex.printStackTrace();
            }
            catch (Exception ex){
                return;
            }
        }
    }

    public char bytesToChar(){
        byte[] arr = new byte[2];

        for(int i = 0; i < arr.length; ++i)
            arr[i] = bytes[i];

        return (char) ((arr[1] & 0xFF) << 8 | (arr[0] & 0xFF));
    }

    public int bytesToUnSignValue(){
        byte[] arr = new byte[2];

        for(int i = 0; i < arr.length; ++i)
            arr[i] = bytes[i];

        return (((arr[0] & 0xFF) << 8)  |
                (arr[1] & 0xFF));
    }
    public short bytesToSignValue(){
        byte[] arr = new byte[2];

        for(int i = 0; i < arr.length; ++i)
            arr[i] = bytes[i];

        return (short) ((((short) (arr[0] & 0xFF)) << 8)  |
                ((short) (arr[1] & 0xFF)));
    }

    public int bytesToSignInt(){
        byte[] arr = new byte[4];

        for(int i = 0; i < arr.length; ++i)
            arr[i] = bytes[i];

        return ((arr[0] & 0xFF) << 24) |
                ((arr[1] & 0xFF) << 16) |
                ((arr[2] & 0xFF) << 8)  |
                (arr[3] & 0xFF);
    }

    public long bytesToUnSignInt(){
        byte[] arr = new byte[4];

        for(int i = 0; i < arr.length; ++i)
            arr[i] = bytes[i];

        return ((long)(arr[0] & 0xFF) << 24) |
                ((long)(arr[1] & 0xFF) << 16) |
                ((long)(arr[2] & 0xFF) << 8)  |
                ((long)(arr[3] & 0xFF));
    }

    public float bytesToFloat(){
        int value = bytesToSignInt();
        return Float.intBitsToFloat(value);
    }

    public double bytesToDouble(){
        long value = bytesToUnSignInt();
        return Double.longBitsToDouble(value);
    }
}
