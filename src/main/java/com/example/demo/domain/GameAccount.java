package com.example.demo.domain;

import java.util.Date;

public class GameAccount {

	public final int gameId;
	public final String username;
	public final Integer chip;
	public final Integer numThreeCard;
	public final Date  playtime;

	public GameAccount(int id, String username, int chip, int numThreeCard, Date playtime) {
		this.gameId = id;
		this.username = username;
		this.playtime = playtime;
		this.chip = chip;
		this.numThreeCard = numThreeCard;
	}
}
