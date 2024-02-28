package view;

import model.Bet;
import model.Card;
import model.Player;
import model.Deck;

import javax.swing.*;
import java.awt.*;

/**
 * 扑克牌面板
 */
public class PokerPanel extends JPanel {
    private final Deck deck = new Deck();
    private final Player[] players;
    private final int n = 3;//玩家数量
    private final Player computer = new Player(2500);
    private final Bet[] bets;

    // 设置电脑停止要牌的临界点
    private static final int STOP_SCORE = 17;

    public PokerPanel() {
    	players = new Player[n];
    	bets = new Bet[n];
    	for(int i = 0; i < n; i++) {
    		this.players[i] = new Player(500);
    		this.bets[i] = new Bet(this.computer, this.players[i], 0);
    	}
        this.setVisible(true);
        repaint();
    }
    
    //返回能够下注的最大值
    public double maxBet() {
    	double max = (int)(1 << 30);
    	if (computer.getCash() < max * 3)
    		max = computer.getCash();
    	for (Player p : players) {
    		if (p.getCash() < max)
    			max = p.getCash();
    	}
    	return max;
    }

	/**
	 * 开始游戏时每个人发两张牌，庄家一明一暗
	 */
    public void initAddCard() {
    	for (Player p : players) {
    		p.addCard(deck.getCard());
    		p.addCard(deck.getCard());
    	}
    	computer.addCard(deck.getCard());
    	computer.addCard(deck.getCard());
    	computer.setUnvisible();
    	repaint();
    }

    /**
     * 发牌，玩家点击要牌和停牌后调用
     */
    public void playerAddCard() {
        // 玩家要牌
    	boolean isout = false;
        if (players[0].need()) {
        	players[0].addCard(deck.getCard());
        	if (players[0].score() > 21) {
        		players[0].stopDeal();
        		isout = true;
        	}	
        }
        //其他玩家要牌
        for (int i = 1; i < n; i++) {
        	if (players[i].need()) {
        		if(players[i].score() < STOP_SCORE) {
        			players[i].addCard(deck.getCard());
        		}
        		else {
        			players[i].stopDeal();
        		}
        	}
        }
        repaint();
        if (isout) {
    		JOptionPane.showMessageDialog(null, "你爆牌了！", "提示",
            		JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 玩家点击停牌后调用
     */
    public void playerStopDeal() {
        players[0].stopDeal();
    }


    /**
     * 电脑要牌（直接要到结束）
     */
    public void computerAddCard() {
    	while (computer.need()){
    		if (computer.score() >= STOP_SCORE) {
    			computer.stopDeal();
    		}else {
    			computer.addCard(deck.getCard());
    		}
    		if (players[0].score() <= 21 && players[0].score() > computer.score())
    			computer.addCard(deck.getCard());
    	}
    	computer.setVisible();
        repaint();
    }

    /**
     * 是否停止发牌，玩家和电脑都不需要牌为真
     *
     * @return 布尔值
     */
    public boolean notNeed() {
    	for (Player player : players) {
    		if (player.need())
    			return false;
    	}
        return !computer.need();
    }

    /**
     * 绘制面板
     *
     * @param graphics 默认调用传入
     */
    @Override
    public void paint(Graphics graphics) {
        graphics.clearRect(0, 0, this.getWidth(), this.getHeight());
        for (int i = 0; i < n; i++) {
            graphics.drawString("Player" + (i + 1) + " cash:" + players[i].getCash() + " state:" + players[i].state, 10 + 480 * i, 230);
            for (int j = 0; j < players[i].cards().size(); j++) {
                Card card = players[i].cards().get(j);
                graphics.drawImage(card.cardImage, 80 * j + 480 * i, 250, this);
            }
        }
        graphics.drawString("Computer " + "cash:" + computer.getCash() + " bet:" + bets[0].money, 10, 30);
        for (int j = 0; j < computer.cards().size(); j++) {
            Card card = computer.cards().get(j);
            if (card.isVisible)
            	graphics.drawImage(card.cardImage, 80 * j, 50, this);
            else
            	graphics.drawImage(deck.rearImage, 80 * j, 50, this);
        }
    }

    /**
     * 计算得分结果，提示信息后重置面板
     *
     * @return 是否成功计算结果
     */
    public boolean statistics() {
        // 当双方停止要牌才能计算结果
        if (notNeed()) {
            for (int i = 0; i < n; i++) {
            	if (players[i].score() > 21) {
            		bets[i].toComputer();
            		players[i].state = "lose";
            	}
            	else if (computer.score() > 21) {
            		bets[i].toPlayer();
            		if (i == 0) {
                		JOptionPane.showMessageDialog(null, "电脑爆牌了！", "提示",
                        		JOptionPane.ERROR_MESSAGE);
            		}
            		players[i].state = "win";
            	}
            	else if (players[i].score() < computer.score()) {
            		if (computer.isBlackjack()) {
            			bets[i].money *= 1.5;
            			bets[i].toComputer();
            			bets[i].money /= 1.5;
            			if (i == 0) {
                    		JOptionPane.showMessageDialog(null, "电脑是黑杰克！", "提示",
                            		JOptionPane.ERROR_MESSAGE);
                		}
            		}
            		bets[i].toComputer();
            		if (i == 0) {
                		JOptionPane.showMessageDialog(null, "你输了！", "提示",
                        		JOptionPane.ERROR_MESSAGE);
            		}
            		players[i].state = "lose";
            	}
            	else if (players[i].score() > computer.score()) {
            		if (players[i].isBlackjack()) {
            			bets[i].money *= 1.5;
            			bets[i].toPlayer();;
            			bets[i].money /= 1.5;
            			if (i == 0) {
                    		JOptionPane.showMessageDialog(null, "你是黑杰克！", "提示",
                            		JOptionPane.ERROR_MESSAGE);
                		}
            		}
            		bets[i].toPlayer();
            		if (i == 0) {
                		JOptionPane.showMessageDialog(null, "你赢了！", "提示",
                        		JOptionPane.ERROR_MESSAGE);
            		}
            		players[i].state = "win";
            	}
            	else {
            		if (i == 0) {
                		JOptionPane.showMessageDialog(null, "平局！", "提示",
                        		JOptionPane.ERROR_MESSAGE);
            		}
            		players[i].state = "draw";
            	}
            }
            this.repaint();
            JOptionPane.showMessageDialog(null, "本轮游戏结束！", "提示",
            		JOptionPane.ERROR_MESSAGE);
            //reset();
            return true;
        } else return false;
    }

    /**
     * 重置扑克牌游戏面板
     */
    public void reset(double money) {
        deck.resetAndShuffle();
        for(int i = 0; i < n; i++) {
    		this.players[i].clear();
    		this.bets[i].money = money;
    	}
        computer.clear();
        repaint();
    }
}