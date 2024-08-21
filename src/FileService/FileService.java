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
    public App app;


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
        int maxWidth = 0, b = 0, rowLen = 0;
        try (BufferedInputStream reader = new BufferedInputStream(new FileInputStream(path))) {
            while(true){
                b = reader.read();
                if (b == -1){
                    if (rowLen > maxWidth)
                        maxWidth = rowLen;
                    throw new EOFException();
                }
                rowLen += 1;

                if (b == '\n'){
                    if (rowLen > maxWidth)
                        maxWidth = rowLen;
                    rowLen = 0;
                }
            }

        } catch (EOFException ex) {
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        // System.out.println(maxWidth);
        return maxWidth;
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

    public void compareBlockWithFile(TableBlock block){
        var dataTable = app.hexTable.tableModel.getDataVector();
        int b = 0, countBytesInBlockFile = 0;
        int countElInTable = 0;

        try(var fin = new RandomAccessFile(path, "r")){
            fin.seek(block.firstBytePos);

            for(int i = 0; i < dataTable.size(); ++i){
                for (int j = 1; j < dataTable.get(i).size(); ++j){
                    var el = dataTable.get(i).get(j);
                    if (el == null)
                        continue;

                    countElInTable += 1;

                    b = fin.read();
                    countBytesInBlockFile += 1;

                    if (hexStringToByte(el.toString()) != b){
                        block.changed = true;
                        block.startPosChanged = block.firstBytePos + countBytesInBlockFile;
                        return;
                    }

                    if (countBytesInBlockFile >= block.countBytes){
                        if (countElInTable != block.countBytes){
                            block.changed = true;
                        }
                        return;
                    }
                }
            }
        }
        catch (IOException ex){
            ex.printStackTrace();
        }

    }

    public void saveChangedBlockInFile(TableBlock block){
        TableBlock nextBlock = null;
        boolean needReadTmpFile = true;
        try{
            nextBlock = app.hexTable.blocks.get(TableBlock.currentBlockPos + 1);
            saveInTmpFile(nextBlock.firstBytePos);
        }
        catch (IndexOutOfBoundsException ex){
            nextBlock = null;
            needReadTmpFile = false;
        }

        var dataTable = app.hexTable.tableModel.getDataVector();

        try(var fin = new RandomAccessFile(path, "rw")){
            fin.setLength(block.firstBytePos); // обрезаем файл
            fin.seek(block.firstBytePos);

            // сохраняем изменные данные из таблицы
            for(int i = 0; i < dataTable.size(); ++i){
                for (int j = 1; j < dataTable.get(i).size(); ++j) {
                    var el = dataTable.get(i).get(j);

                    if (el == null)
                        continue;

                    int value = hexStringToByte(el.toString());
                    fin.write(value);
                }
            }
            if (needReadTmpFile){
                // переносим данные из файла
                readFromTmpFile(fin);
            }
            fin.getFD().sync();
            block.changed = false;
        }
        catch (IOException ex2){

        }
    }

    public void saveInTmpFile(int offset){
        int b = 0;
        try(var fin = new RandomAccessFile(path, "r")){
            fin.seek(offset);

            try(var tmpFile = new FileOutputStream("tmpFile", false)){
                while(true){
                    b = fin.read();
                    if (b == -1)
                        throw new EOFException();
                    tmpFile.write(b);
                }
            }
        }
        catch (EOFException ex1){
        }
        catch (IOException ex2){
        }
    }

    public void readFromTmpFile(RandomAccessFile fin){
        try(var tmpFile = new FileInputStream("tmpFile")){
            int b = 0;
            do{
                b = tmpFile.read();
                if (b == -1)
                    throw new EOFException();

                fin.write(b);
            }
            while(b != -1);
        }
        catch (IOException ex){

        };
    }

    public int getPositionByRowCol(int row, int col){
        int position = 0, b = 0;
        int curRow = 0, curCol = 0;
        try(var fin = new RandomAccessFile(path, "r")){
            while (curRow <= row){
                b = fin.read();
                if (b == -1)
                    throw new EOFException();
                curCol += 1;

                if (b == '\n'){
                    curRow += 1;
                    curCol = 0;
                }
                position += 1;
                if (curRow == row && curCol == col){
                    return position;
                }
            }
        }
        catch (IOException ex){

        }
        return 0;
    }

    public void savePasteBytesInFile(int position, List<Integer> bytes){
        saveInTmpFile(position);

        try(var fin = new RandomAccessFile(path, "rw")){
            fin.setLength(position);
            fin.seek(position);

            for(int i = 0; i < bytes.size(); ++i){
                int value = bytes.get(i);
                fin.write(value);
            }

            readFromTmpFile(fin);
        }
        catch (IOException ex){

        }
    }

    public void removeCutBytesInFile(int position, int cutLength){
        saveInTmpFile(position + cutLength);

        try(var fin = new RandomAccessFile(path, "rw")){
            fin.setLength(position);
            fin.seek(position);
            readFromTmpFile(fin);
        }
        catch (IOException ex){

        }
    }
}
