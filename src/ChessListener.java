import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ChessListener extends MouseAdapter implements ActionListener, Config {

    private boolean accessFlag = false;//标志能否在棋盘下棋
    private JPanel chessBoard;//棋盘面板对象
    private int index = 0;//标志棋子的个数
    private Check check = new Check();//判断输赢
    private int r;//每次放置棋子时计算出的棋子对应行列坐标
    private int c;
    private JRadioButton rb_1, rb_2;
    private AI ai = new AI();
    private String gameMode = new String();
//    private Graphics2D ig;

    public ChessListener(JPanel chessBoard) {
        this.chessBoard = chessBoard;
    }

    //控制菜单按钮监听
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        String s = actionEvent.getActionCommand();
        if (s.equals("开始游戏")) {
            restoreDefault();//恢复默认设置
            //确定游戏模式
            if (rb_1.isSelected()) {
                gameMode = rb_1.getText();
            } else if (rb_2.isSelected()) {
                gameMode = rb_2.getText();
            } else
                gameMode = "0";

        } else if (s.equals("认输") && accessFlag) {
            if (index % 2 != 0) {
                JOptionPane.showMessageDialog(chessBoard, "黑棋获胜！");
            } else {
                JOptionPane.showMessageDialog(chessBoard, "白棋获胜！");
            }
            accessFlag = false;
            rb_1.setEnabled(true);
            rb_2.setEnabled(true);

        } else if (s.equals("悔棋") && accessFlag) {
            if (index > 0) {
                if (gameMode.equals("玩家对战")) {
                    Point point = new Point(pointArray[--index]);
                    chessTable[point.x][point.y] = 0;
                    pointArray[index] = null;
                } else if (gameMode.equals("人机对战")) {
                    for (int i = 0; i < 2; i++) {//执行两遍该操作，连同AI的棋一同消除
                        Point point = new Point(pointArray[--index]);
                        chessTable[point.x][point.y] = 0;
                        pointArray[index] = null;
                    }
                }
            }
            chessBoard.repaint();

        }

    }

    public void mousePressed(MouseEvent e) {
        if (accessFlag) {
            int x = e.getX();
            int y = e.getY();
            Graphics2D ig = (Graphics2D) chessBoard.getGraphics();
            if (gameMode.equals("玩家对战")) {
                placeChess(x, y, ig, "玩家");
                checkIfWin();
            } else if (gameMode.equals("人机对战")) {//规定玩家先下棋，在每次玩家下棋后AI下棋
                if (placeChess(x, y, ig, "玩家")&&!checkIfWin()) {//如果玩家此步成功下棋才进入AI下棋，避免同一位置重复下棋
                    Point AIChess = ai.AIPutChess();
                    placeChess(AIChess.x, AIChess.y, ig, "AI");
                    checkIfWin();
                }
            }
        }
    }

    private void restoreDefault() {
        chessBoard.repaint();
        for (int i = 0; i < RowsAndColumns; i++) {
            for (int j = 0; j < RowsAndColumns; j++) {
                chessTable[i][j] = 0;
                whiteScore[i][j] = 0;
                blackScore[i][j] = 0;
            }
        }
        for (int i = 0; i < RowsAndColumns * RowsAndColumns; i++) {
            pointArray[i] = null;
        }
        accessFlag = true;
        rb_1.setEnabled(false);
        rb_2.setEnabled(false);
        index = 0;


    }

    //由index决定下什么棋,返回布尔值方便判断是否成功下棋
    private boolean placeChess(int x, int y, Graphics2D ig, String mode) {

        ig.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);//抗锯齿，改善图片/所绘图形质量
        if (mode.equals("玩家")) {

            if (x > x0 - gridSize / 2 && x < x0 + gridSize * (RowsAndColumns - 1) && y > y0 - gridSize / 2 && y < y0 + gridSize * (RowsAndColumns - 1)) {

                r = (x - x0 + gridSize / 2) / gridSize;
                c = (y - y0 + gridSize / 2) / gridSize;

                if (chessTable[r][c] == 0) {

                    x = r * gridSize + x0 - chessSize / 2;
                    y = c * gridSize + y0 - chessSize / 2;

                    if (index % 2 == 0) {
                        chessTable[r][c] = 1;
                        ig.setColor(Color.BLACK);
                        ig.fillOval(x, y, chessSize, chessSize);
                        pointArray[index++] = new Point(r, c);
                    } else {
                        chessTable[r][c] = 2;
                        ig.setColor(Color.WHITE);
                        ig.fillOval(x, y, chessSize, chessSize);
                        pointArray[index++] = new Point(r, c);
                    }
                    return true;
                }
            }
        } else if (mode.equals("AI")) {

            r = x;
            c = y;
            x = x * gridSize + x0 - chessSize / 2;
            y = y * gridSize + y0 - chessSize / 2;
            ig.setColor(Color.WHITE);
            ig.fillOval(x, y, chessSize, chessSize);
            pointArray[index++] = new Point(r, c);
            return true;

        }
        return false;
    }


    public void setJRButton(JRadioButton rb_1, JRadioButton rb_2) {
        this.rb_1 = rb_1;
        this.rb_2 = rb_2;
    }

    private boolean checkIfWin() {
        if (check.check(r, c)) {
            if (chessTable[r][c] == 1) {
                JOptionPane.showMessageDialog(chessBoard, "黑棋获胜");
            } else {
                JOptionPane.showMessageDialog(chessBoard, "白棋获胜");
            }
            accessFlag = false;
            return true;
        }
        return false;
    }
}

