package com.example.demo.domain;

public class GameAccount {

	private Long id;
	private String username;
	private Integer usergamecount;
	private Integer chip;
	private Integer numThreeCard;

	public Long getId() {
		return id;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getUsergamecount() {
		return usergamecount;
	}

	public void setUsergamecount(Integer usergamecount) {
		this.usergamecount = usergamecount;
	}

	public void setChip(Integer chip) {
		this.chip = chip;
	}

	public Integer getChip() {
		return chip;
	}

	public Integer getNumThreeCard() {
		return numThreeCard;
	}
	
	public void setNumThreeCard(Integer numThreeCard) {
		this.numThreeCard = numThreeCard;
	}
	
}
