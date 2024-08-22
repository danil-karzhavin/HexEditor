package HexTools;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HexTools {
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
    public static List<Integer> stringToBytes(String hexString) {
        var hexValues = new ArrayList<Integer>();

        // Регулярное выражение для пары шестнадцатеричных символов
        Pattern pattern = Pattern.compile("([0-9A-Fa-f]{2})");
        Matcher matcher = pattern.matcher(hexString);

        // Поиск всех пар шестнадцатеричных символов
        while (matcher.find()) {
            int b = hexStringToByte(matcher.group());
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

    // Метод для проверки, является ли строка корректным шестнадцатеричным значением байта
    public static boolean isValidHexByte(String str) {
        // Проверяем, что строка имеет ровно 2 символа
        if (str == null || str.length() != 2) {
            return false;
        }

        // Проверяем каждый символ, является ли он корректным шестнадцатеричным символом
        for (char ch : str.toCharArray()) {
            if (!isHexChar(ch)) {
                return false;
            }
        }

        return true;
    }

    // Метод проверки, является ли символ корректным шестнадцатеричным символом
    private static boolean isHexChar(char ch) {
        return (ch >= '0' && ch <= '9') ||
                (ch >= 'A' && ch <= 'F');
    }
}
