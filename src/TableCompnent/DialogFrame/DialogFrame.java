package TableCompnent.DialogFrame;

import FileService.FileService;
import TableCompnent.CustomTableModel;
import TableCompnent.TableComponent;

import javax.swing.*;

public class DialogFrame {
    int row, col;
    public int[] bytes;
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
                "1 байт как число со знаком: " + String.valueOf(byteToSignInt()),
                "1 байт как число без знака: " + String.valueOf(byteToUnSignInt()),
                "2 байт как символ char: " + String.valueOf(bytesToChar()),
                "2 байт как число без знака: " + String.valueOf(bytesToUnSignValue()),
                "2 байт как число со знаком: " + String.valueOf(bytesToSignValue()),
                "4 байт как число без знака: " + String.valueOf(bytesToUnSignInt()),
                "4 байт как число со знаком: " + String.valueOf(bytesToSignInt()),
                "4 байт как вещественное число одинарной точности: " + String.valueOf(bytesToFloat()),
                "8 байт как вещественное число двойной точности: " + String.valueOf(bytesToDouble())};

        JOptionPane.showMessageDialog(tableComponent.app, data, "Отображение блока байт", JOptionPane.INFORMATION_MESSAGE);
    }

    public void setAppearance(){

    }

    public void readBytes(){
        CustomTableModel tableModel = tableComponent.tableModel;
        var data = tableModel.getDataVector();
        int curCol = col, curRow = row;

        bytes = new int[8];
        for(int i = 0; i < 8; ++i, ++curCol){
            try{
                String hex = data.get(curRow).get(curCol).toString();
                int el = FileService.hexStringToByte(hex);
                hexValues += hex + " ";
                bytes[i] = el;

                if (el == 10){
                    curRow += 1;
                    curCol = 1;
                }
            }
            catch(IllegalArgumentException ex){
                ex.printStackTrace();
            }
            catch (Exception ex){
                return;
            }
        }
    }

    public int byteToUnSignInt(){
        int element = bytes[0];
        return (int) element;
    }

    public int byteToSignInt(){
        byte element = (byte) bytes[0];
        return (int) element;
    }

    public char bytesToChar(){
        int[] arr = new int[2];

        for(int i = 0; i < arr.length; ++i)
            arr[i] = bytes[i];

        return (char) ((arr[1] & 0xFF) << 8 | (arr[0] & 0xFF));
    }

    public int bytesToUnSignValue(){
        int[] arr = new int[2];

        for(int i = 0; i < arr.length; ++i)
            arr[i] = bytes[i];

        return (((arr[0] & 0xFF) << 8)  |
                (arr[1] & 0xFF));
    }
    public short bytesToSignValue(){
        int[] arr = new int[2];

        for(int i = 0; i < arr.length; ++i)
            arr[i] = bytes[i];

        return (short) ((((short) (arr[0] & 0xFF)) << 8)  |
                ((short) (arr[1] & 0xFF)));
    }

    public int bytesToSignInt(){
        int[] arr = new int[4];

        for(int i = 0; i < arr.length; ++i)
            arr[i] = bytes[i];

        return ((arr[0] & 0xFF) << 24) |
                ((arr[1] & 0xFF) << 16) |
                ((arr[2] & 0xFF) << 8)  |
                (arr[3] & 0xFF);
    }

    public long bytesToUnSignInt(){
        int[] arr = new int[4];

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
