import java.awt.*;
import java.util.Vector;

public interface Config {


    public static final int x0 = 40;
    public static final int y0 = 40;
    public static final int chessSize = 35;
    public static final int gridSize = 40;
    public static final int RowsAndColumns = 15;
    //chessTable记录棋盘每一个位置的棋子情况，0,1,2 分别表示无棋子，黑棋，白棋，用于判断胜负，判断某处是否可下棋
    public static final int[][] chessTable = new int[15][15];
    //pointArray按下棋顺序记录每一个棋子的坐标，用于悔棋，AI
    public static final Point[] pointArray = new Point[15 * 15];
    //存放AI下棋权值法参数的二维数组
    public static final int[][] blackScore = new int[15][15];
    public static final int[][] whiteScore = new int[15][15];
    //棋盘界面和选项界面的颜色
    public static final Color menuPaneColor = new Color(120,117,116);
    public static final Color chessTableColor = new Color(156, 156, 155);
}
