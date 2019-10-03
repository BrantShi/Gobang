public class Check implements Config {

    //连珠棋子计数变量
    private int index = 1;

    public boolean check(int r, int c) {
        if (checkRow(r, c) || checkColumn(r, c) || checkLean_Left(r, c) || checkLean_Right(r, c)) {
            return true;
        } else
            return false;
    }

    //纵向检查此位置上下部分连珠个数
    private boolean checkColumn(int x, int y) {
        index = 1;
        for (int i = y + 1; i < chessTable.length; i++) {
            if (chessTable[x][i] == chessTable[x][y]) {
                index++;
            } else
                break;
        }

        for (int i = y - 1; i >= 0; i--) {
            if (chessTable[x][i] == chessTable[x][y]) {
                index++;
            } else
                break;
        }
        if (index == 5) return true;
        else return false;
    }

    //横向检查此位置左右部分连珠个数
    private boolean checkRow(int x, int y) {// 每下一颗棋后计算横是否成五颗
        index = 1;
        for (int i = x + 1; i < chessTable.length; i++) {
            if (chessTable[i][y] == chessTable[x][y]) {
                index++;
            } else
                break;
        }
        for (int i = x - 1; i >= 0; i--) {
            if (chessTable[i][y] == chessTable[x][y]) {
                index++;
            } else
                break;
        }
        if (index == 5) return true;
        else return false;

    }

    //
    private boolean checkLean_Right(int x, int y) {
        index = 1;
        for (int i = 1; (x + i < chessTable.length) && (y + i < chessTable[x].length); i++) {
            if (chessTable[x + i][y + i] == chessTable[x][y]) {
                index++;
            } else
                break;
        }
        for (int i = 1; (x - i >= 0) && (y - i >= 0); i++) {
            if (chessTable[x - i][y - i] == chessTable[x][y]) {
                index++;
            } else
                break;
        }
        if (index == 5) return true;
        else return false;
    }

    private boolean checkLean_Left(int x, int y) {
        index = 1;

        for (int i = 1; (y - i >= 0) && (x + i < chessTable.length); i++) {
            if (chessTable[x + i][y - i] == chessTable[x][y]) {
                index++;
            } else
                break;
        }
        for (int i = 1; (x - i >= 0) && (y + i < chessTable[x].length); i++) {//条件判断应仅保证下标越界
            if (chessTable[x - i][y + i] == chessTable[x][y]) {
                index++;
            } else
                break;
        }

        if (index == 5) return true;
        else return false;
    }

}
