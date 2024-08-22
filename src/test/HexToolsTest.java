package test;

import HexTools.HexTools;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class HexToolsTest {
    int testValuesInt[] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 125, 126, 127, 128, 129, 130, 131, 132, 133, 134, 135, 136, 137, 138, 139, 140, 141, 142, 143, 144, 145, 146, 147, 148, 149, 150, 151, 152, 153, 154, 155, 156, 157, 158, 159, 160, 161, 162, 163, 164, 165, 166, 167, 168, 169, 170, 171, 172, 173, 174, 175, 176, 177, 178, 179, 180, 181, 182, 183, 184, 185, 186, 187, 188, 189, 190, 191, 192, 193, 194, 195, 196, 197, 198, 199, 200, 201, 202, 203, 204, 205, 206, 207, 208, 209, 210, 211, 212, 213, 214, 215, 216, 217, 218, 219, 220, 221, 222, 223, 224, 225, 226, 227, 228, 229, 230, 231, 232, 233, 234, 235, 236, 237, 238, 239, 240, 241, 242, 243, 244, 245, 246, 247, 248, 249, 250, 251, 252, 253, 254, 255};
    String testValuesString[] = {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "0A", "0B", "0C", "0D", "0E", "0F", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "1A", "1B", "1C", "1D", "1E", "1F", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "2A", "2B", "2C", "2D", "2E", "2F", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "3A", "3B", "3C", "3D", "3E", "3F", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "4A", "4B", "4C", "4D", "4E", "4F", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "5A", "5B", "5C", "5D", "5E", "5F", "60", "61", "62", "63", "64", "65", "66", "67", "68", "69", "6A", "6B", "6C", "6D", "6E", "6F", "70", "71", "72", "73", "74", "75", "76", "77", "78", "79", "7A", "7B", "7C", "7D", "7E", "7F", "80", "81", "82", "83", "84", "85", "86", "87", "88", "89", "8A", "8B", "8C", "8D", "8E", "8F", "90", "91", "92", "93", "94", "95", "96", "97", "98", "99", "9A", "9B", "9C", "9D", "9E", "9F", "A0", "A1", "A2", "A3", "A4", "A5", "A6", "A7", "A8", "A9", "AA", "AB", "AC", "AD", "AE", "AF", "B0", "B1", "B2", "B3", "B4", "B5", "B6", "B7", "B8", "B9", "BA", "BB", "BC", "BD", "BE", "BF", "C0", "C1", "C2", "C3", "C4", "C5", "C6", "C7", "C8", "C9", "CA", "CB", "CC", "CD", "CE", "CF", "D0", "D1", "D2", "D3", "D4", "D5", "D6", "D7", "D8", "D9", "DA", "DB", "DC", "DD", "DE", "DF", "E0", "E1", "E2", "E3", "E4", "E5", "E6", "E7", "E8", "E9", "EA", "EB", "EC", "ED", "EE", "EF", "F0", "F1", "F2", "F3", "F4", "F5", "F6", "F7", "F8", "F9", "FA", "FB", "FC", "FD", "FE", "FF"};
    String testIncorrectValuesString[] = {"0", "z1", "020", "5G", "0AB", "", "aa"};
    public void checkLenSequence() throws Exception{
        if (testValuesInt.length != testValuesString.length)
            throw new Exception("Длины должны совпадать");
    }
    @Test
    public void byteToHexTest() throws Exception{
        checkLenSequence();
        for(int i = 0; i < testValuesInt.length; ++i){
            String res = HexTools.byteToHex(testValuesInt[i]);
            Assert.assertEquals(testValuesString[i], res);
        }
    }
    @Test
    public void hexStringToByteTest() throws Exception{
        checkLenSequence();
        for(int i = 0; i < testValuesString.length; ++i){
            int res = HexTools.hexStringToByte(testValuesString[i]);
            Assert.assertEquals(testValuesInt[i], res);
        }
    }

    @Test
    public void isValidHexByteTest() throws Exception{
        checkLenSequence();
        for(int i = 0; i < testValuesString.length; ++i){
            Boolean res = HexTools.isValidHexByte(testValuesString[i]);
            Assert.assertEquals(true, res);
        }
        for(int i = 0; i< testIncorrectValuesString.length; ++i){
            Boolean res = HexTools.isValidHexByte((testIncorrectValuesString[i]));
            Assert.assertEquals(false, res);
        }
    }

    @Test
    public void stringToBytesTest(){
        String testCorrectSequences[] = {"AA FF 10", "10 BA 11", "10BA11", "AF"};
        ArrayList<Integer[]> testExpectedSequences = new ArrayList<>();
        testExpectedSequences.add(new Integer[] {170, 255, 16});
        testExpectedSequences.add(new Integer[] {16, 186, 17});
        testExpectedSequences.add(new Integer[] {16, 186, 17});
        testExpectedSequences.add(new Integer[] {175});


        //String testIncorrectSequences[] = {"Hello", "1", "A"};
        for(int i = 0; i < testCorrectSequences.length; ++i){
            List<Integer> res = HexTools.stringToBytes(testCorrectSequences[i]);
            Assert.assertEquals(testExpectedSequences.get(i), res.toArray());
        }

    }
}
