package com.example.demo.domain;

public class GameAccount {

	public final int id;
	public final String username;
	public final Integer usergamecount;
	public final Integer chip;
	public final Integer numThreeCard;
	
	public GameAccount(int id, String username, int usergamecount, int chip, int numThreeCard) {
		this.id = id;
		this.username = username;
		this.usergamecount = usergamecount;
		this.chip = chip;
		this.numThreeCard = numThreeCard;
	}
}
