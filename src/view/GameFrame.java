package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 游戏窗口
 */
public class GameFrame extends JFrame {
    public GameFrame() {
        // 顶部面板，放置一个封面图片
        JPanel topPanel = new JPanel(new BorderLayout()) {{
        	ImageIcon imageIcon = new ImageIcon("image\\blackjack.jpg");
        	Image newImage = imageIcon.getImage().getScaledInstance(300, 200, Image.SCALE_DEFAULT);
        	imageIcon = new ImageIcon(newImage);
            add(new JLabel(imageIcon), BorderLayout.CENTER);
        }};
        // 创建一个JLabel，设置文本内容和字体
        //JLabel textLabel = new JLabel("你和你的兄弟，为了狠狠地赚一笔，来到了帝爱公司的游轮！输光的人要被送到缅北！");
        JLabel textLabel = new JLabel("欢迎来到21点游戏！你是Player1，所有玩家停牌后电脑再要牌，玩家爆牌视为输，电脑在点数小于17时会继续拿牌.");
        textLabel.setFont(new Font("宋体", Font.BOLD, 20));
        // 将JLabel添加到面板上
        topPanel.add(textLabel, BorderLayout.SOUTH);

        // 扑克面板，位于中间。显示电脑与玩家的牌
        PokerPanel pokerPanel = new PokerPanel();
        pokerPanel.setLayout(new BorderLayout());
        // 底部面板，显示按钮
        JPanel bottomPanel = new JPanel(new FlowLayout());
        JButton bt1 = new JButton("玩家要牌") {{
            setEnabled(false);
        }};
        JButton bt2 = new JButton("玩家停牌") {{
            setEnabled(false);
        }};
        JButton bt3 = new JButton("开始游戏");
        JButton bt4 = new JButton("退出游戏");
        bottomPanel.add(bt1);
        bottomPanel.add(bt2);
        bottomPanel.add(bt3);
        bottomPanel.add(bt4);
        // 将面板添加到窗口
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(topPanel, BorderLayout.NORTH);
        this.getContentPane().add(pokerPanel, BorderLayout.CENTER);
        this.getContentPane().add(bottomPanel, BorderLayout.SOUTH);

        //设置窗口样式
        this.setSize(1400, 700);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("21点扑克牌游戏");
        this.setVisible(true);
        // 设置窗口居中
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;
        this.setBounds((width - 1400) / 2,
                (height - 700) / 2, 1400, 700);

        // 设置玩家要牌动作
        bt1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	pokerPanel.playerAddCard();
            }
        });
        // 设置玩家停牌动作
        bt2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pokerPanel.playerStopDeal();
                bt1.setEnabled(false);
                bt2.setEnabled(false);
                // 玩家停牌继续给其他玩家和电脑发牌直至结束
                while (!pokerPanel.notNeed()) {
                    pokerPanel.playerAddCard();
                    pokerPanel.computerAddCard();
                }
                pokerPanel.statistics();
            }
        });
        // 设置开始游戏动作
        bt3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	//输入下注金额
            	JFrame frame = new JFrame("赌注");
            	frame.setSize(300, 200);
            	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            	double betMoney = 0;
            	try {
            		String input = JOptionPane.showInputDialog(frame, "请输入下注金额：");
                    betMoney = Double.parseDouble(input);
                    if (betMoney < 50 || betMoney > pokerPanel.maxBet()) {
                    	JOptionPane.showMessageDialog(null, "赌注金额需大于50，且不超过某人的余额！");
                    	return;
                    }
            	}catch(Exception e1) {
            		JOptionPane.showMessageDialog(null, "输入不合法！");
            		return;
            	}
                // 重置面板
                pokerPanel.reset(betMoney);
                pokerPanel.initAddCard();
                bt1.setEnabled(true);
                bt2.setEnabled(true);
            }
        });
        // 设置退出游戏动作
        bt4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }
}