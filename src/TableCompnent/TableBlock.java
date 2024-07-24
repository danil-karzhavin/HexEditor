package TableCompnent;

public class TableBlock {

    public static Integer indexEndBlock = null;
    public static Integer posByteStartCurBlock = 0; // от этой позиции отнимаю сумму всех объемов байнтов в блоках до этого текущего => получаю ключ, по нему читаю текстовый блок
    // обновляю переменную posByteStartCurBlock

    Integer bytePosition = null;
    Integer countLines = 0;
    Integer countBytes = 0;
}
