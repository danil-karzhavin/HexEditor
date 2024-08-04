package FileService;

import TableCompnent.TableBlock;
import TableCompnent.TableComponent;
import TextSearch.SearchSubArray;
import App.App;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileService {
    public static String path;
    public int countLines = 0;
    public App app;

    // массив байтов исходного файла
    private byte[] content = new byte[0];


    RandomAccessFile randomAccessFile;

    public FileService(String path){
        setPath(path);
        largeFileReader();
    }

    private void setPath(String path){
        if (path == null){
            // throw exception
        }
        this.path = path;
    }

    public byte[] getContent(){
        if (content.length == 0) {
            ArrayList<Byte> arrayBytes = new ArrayList<Byte>();

            try (FileInputStream fin = new FileInputStream(path)) {
                int oneByte;

                // читаем пока в потоке есть данные
                while ((oneByte = fin.read()) != -1) {
                    arrayBytes.add((byte) oneByte);
                }
            } catch (FileNotFoundException ex1) {
                System.out.println(ex1.getMessage());
                // вызвать окно программы, что файл не найден
            } catch (IOException ex2) {
                System.out.println(ex2.getMessage());
                // вызвать окно программы, что произошла проблема ввода вывода
            }

            content = new byte[arrayBytes.size()];
            for (int i = 0; i < arrayBytes.size(); ++i) {
                content[i] = arrayBytes.get(i);
            }
            return content;
        }
        else return content;
    }

    public static String byteToHex(int b) {
        // & 0xFF преобразует байт к беззнаковому целому
        // Метод toHexString преобразует это значение в шестнадцатеричную строку
        String hex = Integer.toHexString(b & 0xFF);

        // Проверяем длину строки и добавляем ведущий ноль, если это необходимо
        if (hex.length() == 1) {
            hex = "0" + hex;
        }
        return hex.toUpperCase();  // Приводим к верхнему регистру для единства стиля
    }

    public static int hexStringToByte(String hexString) throws NumberFormatException {
        if (hexString == null || hexString.length() != 2) {
            throw new IllegalArgumentException("Строка должна содержать ровно 2 символа.");
        }

        // Преобразование шестнадцатеричной строки в int, а затем в byte
        int intValue = Integer.parseInt(hexString, 16);

        // Преобразование int в byte (обработка переполнения)
        // Добавляем логическое "и" с 0xFF, чтобы избавиться от возможного знака
        return (intValue & 0xFF);
    }

    /////// new methods
    public void largeFileReader(){
        try{
            this.randomAccessFile = new RandomAccessFile(path, "r");
        }
        catch (FileNotFoundException ex) {
            System.out.println("Файл не найден: " + ex.getMessage());
        }
    }

    public ArrayList<String> readOneLine(int position) throws IOException{
        ArrayList<String> oneLine = new ArrayList<String>();
        randomAccessFile.seek(position);
        int b;
        do{
            b = randomAccessFile.read();
            if (b == -1)
                break;
            String hexView = byteToHex(b);
            oneLine.add(hexView);
        }
        while (b != '\n');

        return oneLine;
    }
    public long length() throws IOException {
        return randomAccessFile.length();
    }

    public void close() throws IOException {
        randomAccessFile.close();
    }

    public int getMaxWidthRow() throws IOException {
        int maxWidth = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            maxWidth = 0;
            while ((line = reader.readLine()) != null) {
                countLines += 1;
                if (line.length() > maxWidth)
                    maxWidth = line.length();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println(maxWidth);
        return maxWidth + 2;
    }

    public ArrayList<TableBlock> setBlockStatistics(){
        ArrayList<TableBlock> blocks = new ArrayList<TableBlock>();
        ArrayList<Byte> bytes = new ArrayList<Byte>();
        int countRows = 0;
        int countBytes = 0; // count bytes in block
        Integer firstBytePos = null; // first byte in block
        Integer numRow = null;

        int bytePosInFile = 0; // count bytes in file
        int countRowsInFile = 0;

        try (FileInputStream fin = new FileInputStream(path)) {
            int b;
            do{
                b = fin.read();
                if (b == -1){
                    countRows += 1;
                    throw new IOException();
                }

                if (firstBytePos == null)
                    firstBytePos = bytePosInFile;
                if (numRow == null)
                    numRow = countRowsInFile;

                countBytes += 1;
                bytePosInFile += 1;

                if (b == '\n'){
                    countRows += 1;
                    countRowsInFile += 1;
                }

                if (countRows >= TableComponent.countLinesInBlock){

                    var newBlock = new TableBlock();
                    newBlock.countBytes = countBytes;
                    newBlock.countRows = countRows;
                    newBlock.firstBytePos = firstBytePos;
                    newBlock.numRow = numRow + 1;
                    blocks.add(newBlock);

                    // восстанавливаем значения
                    countRows = 0;
                    countBytes = 0;
                    firstBytePos = null;
                    numRow = null;
                }
            }
            while (true);

        } catch (FileNotFoundException ex1) {
            System.out.println(ex1.getMessage());
            // вызвать окно программы, что файл не найден
        } catch (IOException ex2) {
            var newBlock = new TableBlock();
            newBlock.countBytes = countBytes;
            newBlock.countRows = countRows;
            newBlock.firstBytePos = firstBytePos;
            newBlock.numRow = numRow + 1;
            blocks.add(newBlock);
        }
        return blocks;
    }

    public List<SearchSubArray> SearchSubArray(List<Integer> data){
        TableComponent tableComponent = app.hexTable;
        ArrayList<SearchSubArray> positions = new ArrayList<SearchSubArray>();
        int countRowsInData = 0;

//        for(var el : data)
//            if (el == '\n')
//                countRowsInData += 1;

        for (int i = 0; i < data.size(); ++i){
            if(data.get(i) == '\n' && (i + 1) < data.size())
                countRowsInData += 1;
        }

        try (RandomAccessFile fin = new RandomAccessFile(path, "r")){
            int pos = 0, dopPos = 0, numRow = 0, posInCurRow = 0;
            Integer startRowPos = null;
            int byteCur = -1, bytePrev;

            do{
                fin.seek(pos);

                for(var el : data){
                    bytePrev = byteCur;
                    byteCur = fin.read(); // читаем байт

                    // если байт совпадает с байтов в data, dopPos - количество совпадений
                    if (el == byteCur){
                        dopPos += 1;
                    }

                    if (bytePrev == '\n'){
                        numRow += 1; // увеличиваем кол-во строк на 1
                        posInCurRow = 0; // обнуляем счетчик байтов в текущей строке
                    }

                    // startRowPos - позиция байта в строке файла, где начинается data
                    if (startRowPos == null){
                        startRowPos = posInCurRow;
                    }

                    posInCurRow += 1;

                    if (el != byteCur){
                        break;
                    }
                }
                if (data.size() == dopPos){
                    var obj = new SearchSubArray();
                    obj.startBytePos = pos;
                    obj.rowInFile = numRow - countRowsInData;
                    obj.posInRow = startRowPos;
                    obj.lengthSearchData = data.size();
                    positions.add(obj);

                    pos += dopPos;
                }
                else{
                    posInCurRow -= dopPos;
                    pos += 1;
                }

                dopPos = 0;
                startRowPos = null;
            }
            while(byteCur != -1);
        }
        catch (FileNotFoundException ex1) {
            System.out.println(ex1.getMessage());
            // вызвать окно программы, что файл не найден
        } catch (IOException ex2) {
            System.out.println(ex2.getMessage());
            // вызвать окно программы, что произошла проблема ввода вывода
        }

        for(var el : positions){
            for (int i = 0; i < tableComponent.blocks.size(); ++i){
                var block = tableComponent.blocks.get(i);

                int startPos = block.firstBytePos;
                int endPos = block.countBytes + startPos;
                if (startPos <= el.startBytePos && el.startBytePos <= endPos){
                    el.textBlockPos = i;
                    break;
                }
            }
        }
        return positions;
    }
}
