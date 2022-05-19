package com.example.demo.domain;

import java.util.Date;

public class AccountRank {

	public final String username;
	public final Integer chip;
	public final Date  playtime;

	public AccountRank(String username, int chip, Date playtime) {
		this.username = username;
		this.playtime = playtime;
		this.chip = chip;
	}
	
}
