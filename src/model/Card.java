package model;

import java.awt.Image;

public class Card {
	public final String suit;//花色
	public final String faceValue;//面值
	public int value;//实际值
    public final Image cardImage;//卡的图片
    public boolean isVisible = true;//牌的明暗
 
    public Card(String suit, String faceValue, Image cardImage) {
        this.suit = suit;
        this.faceValue = faceValue;
        this.cardImage = cardImage; 
        this.value = this.getValue();
    }
    
    //获得实际值
    public int getValue() {
    	int value = 0;
    	try {
    		value = Integer.parseInt(faceValue) % 11;
    	}
    	catch(NumberFormatException e) {
    		if(this.faceValue == "A")
    			value = 11;
    		else if(this.faceValue == "J" || this.faceValue == "Q" || this.faceValue == "K")
    			value = 10;
    	}
    	return value;
    }
    
    //面值为A的牌看作11或1
    public int changeAce() {
    	if (this.faceValue == "A") {
    		if (this.value == 11) {
    			this.value = 1;
    			return 1;
    		}else {
    			this.value = 11;
    		}
    	}
    	return this.value;
    }
    
}
