import java.io.*;
import java.util.ArrayList;
import java.util.Optional;

public class FileService {
    public String path;

    // массив байтов исходного файла
    private byte[] content = new byte[0];

    // строковое представление байтов в виде шестнадцатеричных значений, разд. пробелом
    private String contentHex = "";

    // массив символов на основе массива байтов content
    private String contentChar;

    // размер буфера
    public final int BUFFER_SIZE = 4096;
    // размер буфера
    byte[] buffer = null;

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

    public void SetContent(){
        try(FileOutputStream fos = new FileOutputStream(path)){
            fos.write(content, 0, content.length);
        }
        catch (IOException ex2){
            System.out.println(ex2.getMessage());
        }
    }

    public void SetContentNewFile(String path){
        try(FileOutputStream fos = new FileOutputStream(path)){
            fos.write(content, 0, content.length);
        }
        catch (IOException ex2){
            System.out.println(ex2.getMessage());
        }
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

        public String readBlock(long position) throws IOException{
            buffer = new byte[BUFFER_SIZE];
            randomAccessFile.seek(position);
            int bytesRead = randomAccessFile.read(buffer);
            if (bytesRead == -1)
                return "";
            return new String(buffer, 0, bytesRead);
        }
        public long length() throws IOException {
            return randomAccessFile.length();
        }

        public void close() throws IOException {
            randomAccessFile.close();
        }
}
