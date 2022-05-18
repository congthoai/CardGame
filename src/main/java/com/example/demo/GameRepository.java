package com.example.demo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.domain.GameAccount;
import com.example.demo.domain.GameConfig;

@Component
public class GameRepository {

	@Autowired
	private GamePersistent gamePersist;

	@Autowired
	private GameConfigPersistent gameConfigPersist;

	private static final int APP_ID = 1;
	private Random random = new Random();
	
	private final Map<Integer, GameConfig> cache = new ConcurrentHashMap<>();

	private GameConfig getConfig(final int appId) {
		GameConfig result = cache.get(appId);
		if (result != null) {
			return result;
		}
		result = gameConfigPersist.getConfigByAppId(APP_ID);
		cache.put(appId, result);
		return result;
	}
	
	public GameAccount handleResult(String username) {
		GameConfig config = getConfig(APP_ID);
		List<Integer> cards = random.ints(0, 9).limit(config.cardsSize).boxed().collect(Collectors.toList());

		int numThreeCard = calNumOfThreeCard(config.bonusThreeCard, cards);
		int chipAward = numThreeCard * config.awardThreeCard;

		return gamePersist.upsert(username, numThreeCard, chipAward, APP_ID);
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
