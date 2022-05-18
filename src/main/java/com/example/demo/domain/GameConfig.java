package com.example.demo.domain;

public class GameConfig {

	public final int appId;
	public final Integer cardsSize;
	public final Integer awardThreeCard;
	public final Integer bonusThreeCard;
	public final Integer maxTicket;
	
	public GameConfig(int appId, int cardsSize, int awardThreeCard, int bonusThreeCard, int maxTicket) {
		this.appId = appId;
		this.cardsSize = cardsSize;
		this.awardThreeCard = awardThreeCard;
		this.bonusThreeCard = bonusThreeCard;
		this.maxTicket = maxTicket;
	}

}
