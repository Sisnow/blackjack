package model;

import java.util.ArrayList;

/**
 * 玩家类
 */
public class Player {
    private ArrayList<Card> cards = new ArrayList<>();
    private int score = 0;//点数
    private double cash = 0;//现金
    private boolean need = true;
    public String state = "waiting";//当前胜负状态
    
    public Player() {
    	;
    }
    
    public Player(double cash) {
    	this.cash = cash;
    }
    
    public void addCard(Card card) {
        cards.add(card);
        score += card.value;
        //如果爆牌，将手牌中一张A的值看作1
        if (score > 21) {
        	for (Card c : cards) {
            	if (c.changeAce() == 1) {
            		score -= 10;
            		break;
            	}
            }
        }
    }
    
    //手牌是blackjack（有且仅有两张牌，总点数为21）
    public boolean isBlackjack() {
    	if (this.score == 21 && cards.size() == 2)
    		return true;
    	return false;
    }
    
    //设置暗牌
    public void setUnvisible() {
    	Card newCard = this.cards.get(1);
    	newCard.isVisible = false;
    	this.cards.set(1, newCard);
    }
    
    //设置明牌
    public void setVisible() {
    	Card newCard = this.cards.get(1);
    	newCard.isVisible = true;
    	this.cards.set(1, newCard);
    }

    public void stopDeal() {
        need = false;
    }

    public ArrayList<Card> cards() {
        return cards;
    }

    public int score() {
        return score;
    }
    
    //明牌点数
    public int visibleScore() {
    	int sum = 0;
    	for (Card card : cards) {
    		if (card.isVisible)
    			sum += card.value;
    	}
    	return sum;
    }

    public boolean need() {
        return need;
    }

    public void clear() {
        cards.clear();
        score = 0;
        need = true;
    }

	public double getCash() {
		return cash;
	}

	public void setCash(double cash) {
		this.cash = cash;
	}
}