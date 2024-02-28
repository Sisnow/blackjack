package model;

import java.awt.Image;
import java.awt.Toolkit;

public class Deck {
	private static final String[] suits = {"heart", "club", "spade", "diamond"};
    private static final String[] values = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
    private static final Toolkit toolkit = Toolkit.getDefaultToolkit();
    // 牌组
    private final Card[] cards;
    // 剩余牌的数量
    private int residual;

    // 扑克牌背面图
    public final Image rearImage = toolkit.getImage("image\\rear.png");

    /**
     * 扑克构建，生成52张牌，并对应图片
     */
    public Deck() {
        cards = new Card[52];
        residual = cards.length;
        int maxValue = values.length;
        for (int i = 0; i < suits.length; i++) {
            for (int j = 0; j < maxValue; j++) {
                int number = i * maxValue + j;
                // 54张牌图片图片文件为0-53.jpg
                cards[number] = new Card(suits[i], values[j],
                        toolkit.getImage("image\\" + number + ".jpg"));
            }
        }
    }

    /**
     * 洗牌
     */
    public void shuffle() {
        int i, j;
        Card temp;
        for (int n = 0; n < 500; n++) {
            i = (int) (Math.random() * 52);
            j = (int) (Math.random() * 52);
            temp = cards[i];
            cards[i] = cards[j];
            cards[j] = temp;
        }
    }

    /**
     * 重置剩余牌数并洗牌
     */
    public void resetAndShuffle() {
        shuffle();
        residual = cards.length;
    }

    /**
     * 获得一张牌
     *
     * @return Card
     */
    public Card getCard() {
        if (residual > 0) {
            return cards[--residual];
        } else throw new IndexOutOfBoundsException();
    }
    
}
