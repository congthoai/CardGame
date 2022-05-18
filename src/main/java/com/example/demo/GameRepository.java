package com.example.demo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.domain.GameAccount;

@Component
public class GameRepository {

	@Autowired
	private GamePersistent gamePersist;

	private Random random = new Random();

	public GameAccount handleResult(String username) {
		Map<String, Integer> config = gamePersist.getAllConfig();
		if (config == null) {
			return null;
		}
		List<Integer> cards = random.ints(0, 9).limit(config.getOrDefault("cards_size", 0)).boxed().collect(Collectors.toList());

		int numThreeCard = calNumOfThreeCard(config.getOrDefault("bonus_three_card", 3), cards);
		int chipAward = numThreeCard * config.getOrDefault("award_three_card", 0);

		return handleUpsert(username, numThreeCard, chipAward);
	}

	public GameAccount handleUpsert(String username, int numTreeCard, int chipAward) {
		Integer gameId = gamePersist.upsert(username, numTreeCard, chipAward);
		if (gameId == null) {
			return null;
		}

		return gamePersist.findGameById(gameId);
	}

	public int calNumOfThreeCard(final int bonusThreeCard, List<Integer> cards) {
		Map<Integer, Integer> cMap = new HashMap<>();
		cards.forEach(item -> cMap.put(item, cMap.getOrDefault(item, 0) + 1));

		int count = 0;
		for (int key : cMap.keySet()) {
			count += cMap.get(key) / bonusThreeCard;
		}

		return count;
	}
}
