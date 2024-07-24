package FileService;

import TableCompnent.TableBlock;
import TableCompnent.TableComponent;

import java.io.*;
import java.util.ArrayList;
import java.util.Optional;

public class FileService {
    public int maxWidthRow = 0;
    public String path;
    public int countLines = 0;

    // массив байтов исходного файла
    private byte[] content = new byte[0];

    // строковое представление байтов в виде шестнадцатеричных значений, разд. пробелом
    private String contentHex = "";

    // массив символов на основе массива байтов content
    private String contentChar;

    // размер буфера
    byte[] buffer = null;

    RandomAccessFile randomAccessFile;

    public FileService(String path){
        setPath(path);
        largeFileReader();
//        try {
//            maxWidthRow = getMaxWidthRow();
//        }
//        catch (IOException ex){
//            ex.printStackTrace();
//        }
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

    public String getStringContent(){
        if (contentHex.isEmpty()){
            content = getContent();

            ArrayList<String> tmp = new ArrayList<String>();

            for (var el : content){
                tmp.add(byteToHex(el));
            }
            Optional<String> res =  tmp.stream().reduce((a, b) -> a + " " + b);
            contentHex = res.get();
        }
        return contentHex;
    }

    public static String byteToHex(byte b) {
        // & 0xFF преобразует байт к беззнаковому целому
        // Метод toHexString преобразует это значение в шестнадцатеричную строку
        String hex = Integer.toHexString(b & 0xFF);

        // Проверяем длину строки и добавляем ведущий ноль, если это необходимо
        if (hex.length() == 1) {
            hex = "0" + hex;
        }
        return hex.toUpperCase();  // Приводим к верхнему регистру для единства стиля
    }

    public String getCharContent(){
        if (contentChar == null){
            getContent();

            ArrayList<Character> tmp = new ArrayList<Character>();
            for(int i = 0; i < content.length; ++i){
                tmp.add(i, (char) (content[i] & 0xFF));
            }
            Optional<String> res = tmp.stream().map(ch -> Character.toString(ch)).reduce((a, b) -> a + " " + b);
            contentChar = res.get();
            System.out.println(contentChar);
        }
        return contentChar;
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

    public byte[] readBlock(long position, int buffer_size) throws IOException{
        buffer = new byte[buffer_size];
        randomAccessFile.seek(position);
        int bytesRead = randomAccessFile.read(buffer);
        if (bytesRead == -1)
            return new byte[] {};
        return buffer;
        //return new String(buffer, 0, bytesRead);
    }

    public ArrayList<String> readOneLine(int position) throws IOException{
        ArrayList<String> lines = new ArrayList<String>();
        randomAccessFile.seek(position);
        int b;
        do{
            b = randomAccessFile.read();
            if (b == -1)
                break;
            String hexView = byteToHex((byte) b);
            lines.add(hexView);
        }
        while ((b > 0) && (b != '\n'));

        return lines;
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
                if (b > 0){
                    if (firstBytePos == null)
                        firstBytePos = bytePosInFile;
                    if (numRow == null)
                        numRow = countRowsInFile;

                    countBytes += 1;
                    bytePosInFile += 1;

                }

                if ( b == '\n' || b <= 0){
                    countRows += 1;
                    countRowsInFile += 1;
                }

                if (countRows >= TableComponent.countLinesInBlock || b <= 0){

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
            while (b > 0);

        } catch (FileNotFoundException ex1) {
            System.out.println(ex1.getMessage());
            // вызвать окно программы, что файл не найден
        } catch (IOException ex2) {
            System.out.println(ex2.getMessage());
            // вызвать окно программы, что произошла проблема ввода вывода
        }
        return blocks;
    }
}
