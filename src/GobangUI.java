import javax.swing.*;
import java.awt.*;

public class GobangUI extends JPanel implements Config {
    public static void main(String[] args) {
        GobangUI gb = new GobangUI();
        gb.initUI();
    }

    private void initUI() {

        //顶级容器的具体设置
        JFrame frame = new JFrame();
        frame.setTitle("Gobang");
        frame.setSize(750, 600);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(3);
        frame.setLayout(new BorderLayout());

        this.setPreferredSize(new Dimension(640, 640));
        this.setLayout(new FlowLayout());
        this.setBackground(chessTableColor);

        ChessListener listener = new ChessListener(this);
//        frame.addMouseListener(listener);
        this.addMouseListener(listener);

        //控制面板的具体设置
        JPanel menuPane = new JPanel();
        menuPane.setLayout(new FlowLayout(1, 10, 45));
        menuPane.setBackground(menuPaneColor);
        menuPane.setPreferredSize(new Dimension(200, 0));

        //为menuPanel添加控制按钮
        String[] array = {"开始游戏", "认输", "悔棋"};
        for (int i = 0; i < array.length; i++) {
            JButton button = new JButton(array[i]);
            button.setFont(new Font("楷体", 1, 20));
            button.setBackground(Color.white);
            button.setPreferredSize(new Dimension(120, 80));
            button.setBorder(BorderFactory.createRaisedSoftBevelBorder());
            menuPane.add(button);
            button.addActionListener(listener);
        }

        JPanel btPanel = new JPanel();
        btPanel.setPreferredSize(new Dimension(120, 80));
        btPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        btPanel.setBackground(menuPaneColor);

        //游戏模式-单选框
        ButtonGroup bt = new ButtonGroup();
        JRadioButton rb_1 = new JRadioButton("玩家对战", true);
        JRadioButton rb_2 = new JRadioButton("人机对战");
        rb_1.setBackground(menuPaneColor);
        rb_2.setBackground(menuPaneColor);
        rb_1.setFont(new Font("楷体", 1, 20));
        rb_2.setFont(new Font("楷体", 1, 20));
        bt.add(rb_1);
        bt.add(rb_2);
        btPanel.add(rb_1);
        btPanel.add(rb_2);
        rb_1.setEnabled(true);
        rb_2.setEnabled(true);
        rb_1.addActionListener(listener);
        rb_2.addActionListener(listener);
        listener.setJRButton(rb_1, rb_2);
        menuPane.add(btPanel);


        frame.add(this, BorderLayout.CENTER);
        frame.add(menuPane, BorderLayout.EAST);
        frame.pack();
        frame.setVisible(true);
    }

    //重写panel-this的paint方法
    public void paint(Graphics g) {
        super.paint(g);
        drawTable(g);
        drawChess(g);
    }

    //使用Graphics2D绘制棋盘
    private void drawTable(Graphics g) {
        Image img = this.createImage(this.getWidth(), this.getHeight());
        Graphics2D ig = (Graphics2D) img.getGraphics();
        //重绘背景，否则默认为Frame的颜色填充
        ig.setColor(this.getBackground());
        ig.fillRect(0, 0, img.getWidth(this), img.getHeight(this));

        //绘制棋盘，最外边的线加粗
        ig.setColor(Color.BLACK);
        for (int i = 0; i < RowsAndColumns; i++) {
            if (i == 0 || i == RowsAndColumns - 1) ig.setStroke(new BasicStroke(3.5f));
            else if (i == 1) ig.setStroke(new BasicStroke(2.0f));
            ig.drawLine(x0, x0 + i * gridSize, x0 + (RowsAndColumns - 1) * gridSize, y0 + i * gridSize);//绘制横线
            ig.drawLine(x0 + i * gridSize, y0, x0 + i * gridSize, y0 + (RowsAndColumns - 1) * gridSize);//绘制竖线
        }
        ig.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);//抗锯齿，改善图片/所绘图形质量
        ig.fillOval(313, 313, 14, 14);
        g.drawImage(img, 0, 0, this);
    }

    //在悔棋时重绘棋盘上原有棋子
    public void drawChess(Graphics g) {
        Graphics2D ig = (Graphics2D) g;
        ig.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);//抗锯齿，改善图片/所绘图形质量
        for (int i = 0; pointArray[i] != null; i++) {
            int x = pointArray[i].x * gridSize + x0 - chessSize / 2;
            int y = pointArray[i].y * gridSize + y0 - chessSize / 2;
            if (chessTable[pointArray[i].x][pointArray[i].y] == 2) {// 如果是白棋
                ig.setColor(Color.WHITE);
                ig.fillOval(x, y, chessSize, chessSize);
            } else if (chessTable[pointArray[i].x][pointArray[i].y] == 1) {// 如果是黑棋
                ig.setColor(Color.BLACK);
                ig.fillOval(x, y, chessSize, chessSize);
            }
        }

    }

}
