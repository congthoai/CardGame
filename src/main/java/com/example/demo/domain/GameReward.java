package com.example.demo.domain;

import java.util.Date;

public class GameReward {
	
	public final String username;
	public final Integer gameCount;
	public final Integer chip;
	public final Date  lastTime;
	
	public GameReward(String username, Integer gamecount, Integer chip, Date lasttime) {
		this.username = username;
		this.gameCount = gamecount;
		this.chip = chip;
		this.lastTime = lasttime;
	}
	
}
