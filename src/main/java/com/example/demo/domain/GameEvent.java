package com.example.demo.domain;

public class GameEvent {

	public final int appId;
	public final int cardsSize;
	public final int awardThreeCard;
	public final int bonusThreeCard;
	public final int maxTicket;

	public GameEvent(int appId, int cardsSize, int awardThreeCard, int bonusThreeCard, int maxTicket) {
		this.appId = appId;
		this.cardsSize = cardsSize;
		this.awardThreeCard = awardThreeCard;
		this.bonusThreeCard = bonusThreeCard;
		this.maxTicket = maxTicket;
	}
}
