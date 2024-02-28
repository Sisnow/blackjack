package model;

public class Bet {
	private final Player player;
	private final Player computer;
	public double money;
	
	public Bet(Player computer, Player player, double money) {
		this.computer = computer;
		this.player = player;
		this.money = money;
	}
	
	//转钱给电脑
	public void toComputer() {
		this.player.setCash(this.player.getCash() - money);
		this.computer.setCash(this.computer.getCash() + money);
	}
	
	//转钱给玩家
	public void toPlayer() {
		this.computer.setCash(this.computer.getCash() - money);
		this.player.setCash(this.player.getCash() + money);
	}
	
}
