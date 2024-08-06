package HexTools;

import FileService.FileService;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HexTools {
    public static List<Integer> StringToBytes(String hexString) {
        var hexValues = new ArrayList<Integer>();

        // Регулярное выражение для пары шестнадцатеричных символов
        Pattern pattern = Pattern.compile("([0-9A-Fa-f]{2})");
        Matcher matcher = pattern.matcher(hexString);

        // Поиск всех пар шестнадцатеричных символов
        while (matcher.find()) {
            int b = FileService.hexStringToByte(matcher.group());
            hexValues.add(b);
        }
        return hexValues;
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
}
