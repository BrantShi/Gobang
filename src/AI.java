import java.awt.*;
import java.util.HashMap;

public class AI implements Config {
    /**
     * 通过对某一位置周围黑白棋不同连珠情况的综合评分来决定AI的攻击性
     * 此处难免会犯一些低级错误
     */
    static HashMap<String, Integer> whiteMap = new HashMap<String, Integer>();

    {
        whiteMap.put("020", 10);// 白棋活一
        whiteMap.put("0220", 30);// 白棋活二
        whiteMap.put("02220", 180);// 白棋活三
        whiteMap.put("022220", 200);// 白棋活四
        whiteMap.put("021", 2);// 白棋眠一
        whiteMap.put("0221", 4);// 白棋眠二
        whiteMap.put("02221", 30);// 白棋眠三
        whiteMap.put("022221", 200);// 白棋眠四
    }

    static HashMap<String, Integer> blackMap = new HashMap<String, Integer>();

    {
        blackMap.put("010", 12);// 黑棋活一
        blackMap.put("0110", 32);// 黑棋活二
        blackMap.put("01110", 170);// 黑棋活三
        blackMap.put("011110", 190);// 黑棋活四
        blackMap.put("012", 1);// 黑棋眠一
        blackMap.put("0112", 10);// 黑棋眠二
        blackMap.put("01112", 20);// 黑棋眠三，不足为惧
        blackMap.put("011112", 190);// 黑棋眠四
    }


    // 该方法遍历棋盘，算出此情况下整个棋盘每个位置（目前没棋子）的权值 对黑白棋打不同分
    public Point AIPutChess() {

        /**
         * Attention! 二维数组第一个坐标表示竖着数有几行，即纵坐标 第二个坐标表示横着数有几排，即横坐标
         * 和数组的基本展开情况有关，先确定是第几行（先向下找），在确定是第几列（再往右找） 即对应坐标应是(y,x)
         */
        for (int r = 0; r < chessTable.length; r++) {
            for (int c = 0; c < chessTable[r].length; c++) {
                // 判断此位置是否有棋子，如果没棋子则进行权值判断
                if (chessTable[r][c] == 0) {
                    // 分成8个方向去查找周围棋子相连的情况
                    String[] code = new String[8];
                    code[0] = countHL(r, c);
                    code[1] = countDL(r, c);
                    code[2] = countDR(r, c);
                    code[3] = countHR(r, c);
                    code[4] = countUL(r, c);
                    code[5] = countUR(r, c);
                    code[6] = countVD(r, c);
                    code[7] = countVU(r, c);
                    for (int i = 0; i < code.length; i++) {
                        // 根据code从map中获取对应的权值
                        Integer value = whiteMap.get(code[i]);
                        // value很有可能是空的
                        if (value != null) {
                            whiteScore[r][c] += value;
                        }
                        Integer value_2 = blackMap.get(code[i]);
                        if (value_2 != null) {
                            blackScore[r][c] += value_2;
                        }

                    }
                }
            }
        }
        int maxScore = 0;
        int max_r = 0, max_c = 0;// 最大权值对应的行列
        for (int i = 0; i < RowsAndColumns; i++) {// y坐标
            for (int j = 0; j < RowsAndColumns; j++) {// x坐标
                //两if语句的顺序决定了AI的倾向，此处选择更具攻击性的AI
                if (whiteScore[i][j] > maxScore && (chessTable[i][j] == 0)) {// 如果此步权值比原有最大权值大，就记录下来其权值和横纵坐标
                    max_r = i;// 最大的行数，即ymax
                    max_c = j;// 最大的列数，即xmax
                    maxScore = whiteScore[i][j];
                }
                if (blackScore[i][j] > maxScore && (chessTable[i][j] == 0)) {// 如果此步权值比原有最大权值大，就记录下来其权值和横纵坐标
                    max_r = i;// 最大的行数，即ymax
                    max_c = j;// 最大的列数，即xmax
                    maxScore = blackScore[i][j];
                }
            }
        }

        for (int i = 0; i < RowsAndColumns; i++) {// 下完一步就将权值数组清零，便于下一步下棋
            for (int j = 0; j < RowsAndColumns; j++) {
                whiteScore[i][j] = 0;
                blackScore[i][j] = 0;
            }
        }
        chessTable[max_r][max_c] = 2;
        Point point = new Point(max_r, max_c);
        return point;
    }

    /**
     * 记录水平向左方向的情况
     */
    private String countHL(int r, int c) {
        String code = "0";// 记录棋子相连的情况
        int chess = 0;
        for (int c1 = c - 1; c1 >= 0; c1--) {
            if (chessTable[r][c1] == 0) {
                if (c1 + 1 == c) {// 是否相连
                    break;// 如果有相连的空位就直接退出该循环
                } else {// 不相连的空位
                    code += chessTable[r][c1];
                    break;
                }
            } else {
                if (chess == 0) {// 记录第一次出现的棋子
                    code += chessTable[r][c1];
                    chess = chessTable[r][c1];
                } else if (chess == chessTable[r][c1]) {
                    code += chessTable[r][c1];
                } else {// 出现不同颜色的棋，此时已眠连，将此堵住活路的棋记录下来就好
                    code += chessTable[r][c1];
                    break;
                }
            }

        }

        return code;
    }

    /**
     * 此方法来查找horizontal right 右方向上的活连或眠连情况
     */
    private String countHR(int r, int c) {
        String code = "0";// 记录棋子相连的情况
        int chess = 0;
        for (int c1 = c + 1; c1 < chessTable[r].length; c1++) {
            if (chessTable[r][c1] == 0) {
                if (c1 - 1 == c) {// 是否相连
                    break;// 如果有相连的空位就直接退出该循环，则在左方向上是活一
                } else {// 不相连的空位
                    code += chessTable[r][c1];
                    break;
                }
            } else {
                if (chess == 0) {// 记录第一次出现的棋子
                    code += chessTable[r][c1];
                    chess = chessTable[r][c1];
                } else if (chess == chessTable[r][c1]) {
                    code += chessTable[r][c1];
                } else {// 出现不同颜色的棋，此时已眠连，将此堵住活路的棋记录下来就好
                    code += chessTable[r][c1];
                    break;
                }
            }

        }

        return code;
    }

    /**
     * 竖直向下方向的相连情况
     */
    private String countVD(int r, int c) {
        String code = "0";// 记录棋子相连的情况
        int chess = 0;
        for (int r1 = r + 1; r1 < chessTable.length; r1++) {
            if (chessTable[r1][c] == 0) {
                if (r1 - 1 == r) {// 是否相连
                    break;// 如果有相连的空位就直接退出该循环，则在左方向上是活一
                } else {// 不相连的空位
                    code += chessTable[r1][c];
                    break;
                }
            } else {
                if (chess == 0) {// 记录第一次出现的棋子的颜色
                    code += chessTable[r1][c];
                    chess = chessTable[r1][c];
                } else if (chess == chessTable[r1][c]) {
                    code += chessTable[r1][c];
                } else {// 出现不同颜色的棋，此时已眠连，将此堵住活路的棋记录下来就好
                    code += chessTable[r1][c];
                    break;
                }
            }

        }

        return code;
    }

    /**
     * 竖直向上方向的相连情况，与y轴正方向相反，此处与x轴横向不同
     */
    private String countVU(int r, int c) {
        String code = "0";// 记录棋子相连的情况
        int chess = 0;
        for (int r1 = r - 1; r1 >= 0; r1--) {
            if (chessTable[r1][c] == 0) {
                if (r1 + 1 == r) {// 是否相连
                    break;// 如果有相连的空位就直接退出该循环，则在左方向上是活一
                } else {// 不相连的空位
                    code += chessTable[r1][c];
                    break;
                }
            } else {
                if (chess == 0) {// 记录第一次出现的棋子的颜色
                    code += chessTable[r1][c];
                    chess = chessTable[r1][c];
                } else if (chess == chessTable[r1][c]) {
                    code += chessTable[r1][c];
                } else {// 出现不同颜色的棋，此时已眠连，将此堵住活路的棋记录下来就好
                    code += chessTable[r1][c];
                    break;
                }
            }

        }

        return code;
    }

    /**
     * 右下方向记录
     */
    private String countDR(int r, int c) {
        String code = "0";// 记录棋子相连的情况
        int chess = 0;// r的大小不应该等于chessTable.length，最大值应是其减一
        for (int r1 = r + 1, c1 = c + 1; r1 < chessTable.length && c1 < chessTable[r].length; r1++, c1++) {
            if (chessTable[r1][c1] == 0) {
                if ((r1 - 1 == r) && (c1 - 1 == c)) {// 是否相连
                    break;// 如果有相连的空位就直接退出该循环，则在左方向上是活一
                } else {// 不相连的空位
                    code += chessTable[r1][c1];
                    break;
                }
            } else {
                if (chess == 0) {// 记录第一次出现的棋子的颜色
                    code += chessTable[r1][c1];
                    chess = chessTable[r1][c1];
                } else if (chess == chessTable[r1][c1]) {
                    code += chessTable[r1][c1];
                } else {// 出现不同颜色的棋，此时已眠连，将此堵住活路的棋记录下来就好
                    code += chessTable[r1][c1];
                    break;
                }
            }

        }

        return code;
    }

    /**
     * 右上方向情况记录
     */
    private String countUR(int r, int c) {
        String code = "0";// 记录棋子相连的情况
        int chess = 0;
        for (int r1 = r - 1, c1 = c + 1; r1 >= 0 && c1 < chessTable[r].length; r1--, c1++) {
            if (chessTable[r1][c1] == 0) {
                if ((r1 + 1 == r) && (c1 - 1 == c)) {// 是否相连
                    break;// 如果有相连的空位就直接退出该循环，则在左方向上是活一
                } else {// 不相连的空位
                    code += chessTable[r1][c1];
                    break;
                }
            } else {
                if (chess == 0) {// 记录第一次出现的棋子的颜色
                    code += chessTable[r1][c1];
                    chess = chessTable[r1][c1];
                } else if (chess == chessTable[r1][c1]) {
                    code += chessTable[r1][c1];
                } else {// 出现不同颜色的棋，此时已眠连，将此堵住活路的棋记录下来就好
                    code += chessTable[r1][c1];
                    break;
                }
            }

        }
        return code;
    }

    /**
     * 左上方向情况记录
     */
    private String countUL(int r, int c) {
        String code = "0";// 记录棋子相连的情况
        int chess = 0;
        for (int r1 = r - 1, c1 = c - 1; r1 >= 0 && c1 >= 0; r1--, c1--) {
            if (chessTable[r1][c1] == 0) {
                if ((r1 + 1 == r) && (c1 + 1 == c)) {// 是否相连
                    break;// 如果有相连的空位就直接退出该循环，则在左方向上是活一
                } else {// 不相连的空位
                    code += chessTable[r1][c1];
                    break;
                }
            } else {
                if (chess == 0) {// 记录第一次出现的棋子的颜色
                    code += chessTable[r1][c1];
                    chess = chessTable[r1][c1];
                } else if (chess == chessTable[r1][c1]) {
                    code += chessTable[r1][c1];
                } else {// 出现不同颜色的棋，此时已眠连，将此堵住活路的棋记录下来就好
                    code += chessTable[r1][c1];
                    break;
                }
            }

        }

        return code;
    }

    /**
     * 记录左下情况的方法，最后一个方法
     */
    private String countDL(int r, int c) {
        String code = "0";// 记录棋子相连的情况
        int chess = 0;
        for (int r1 = r + 1, c1 = c - 1; r1 < chessTable.length && c1 >= 0; r1++, c1--) {
            if (chessTable[r1][c1] == 0) {
                if ((r1 - 1 == r) && (c1 + 1 == c)) {// 是否相连
                    break;// 如果有相连的空位就直接退出该循环，则在左方向上是活一
                } else {// 不相连的空位
                    code += chessTable[r1][c1];
                    break;
                }
            } else {
                if (chess == 0) {// 记录第一次出现的棋子的颜色
                    code += chessTable[r1][c1];
                    chess = chessTable[r1][c1];
                } else if (chess == chessTable[r1][c1]) {
                    code += chessTable[r1][c1];
                } else {// 出现不同颜色的棋，此时已眠连，将此堵住活路的棋记录下来就好
                    code += chessTable[r1][c1];
                    break;
                }
            }

        }

        return code;
    }

}
