package com.example.demo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Component;

import com.example.demo.domain.AccountRank;
import com.example.demo.domain.GameAccount;
import com.example.demo.domain.GameConfig;

@Component
public class GameRepository {

	@Autowired
	private GamePersistent gamePersist;

	@Autowired
	private GameConfigPersistent gameConfigPersist;

	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;

	private static final int APP_ID = 1;
	private static final String RANK_KEY = "rank";

	private Random random = new Random();

	private final Map<Integer, Optional<GameConfig>> cache = new ConcurrentHashMap<>();

	@PostConstruct
	void init() {
		initRanks();
	}

	public void initRanks() {
		if (!redisTemplate.hasKey(RANK_KEY)) {
			List<AccountRank> rankList = gamePersist.getAccountRankList();
			rankList.forEach(item -> redisTemplate.opsForZSet().add(RANK_KEY, item.username, item.chip));
		}
	}

	public void upRank(String username, int chip) {
		redisTemplate.opsForZSet().incrementScore(RANK_KEY, username, chip);
	}

	public List<TypedTuple<Object>> getTop10() {
		return redisTemplate.opsForZSet().reverseRangeWithScores(RANK_KEY, 0, 10).stream()
				.collect(Collectors.toList());
	}

	private GameConfig getConfig(final int appId) {
		Optional<GameConfig> result = cache.get(appId);
		if (result != null) {
			return result.orElseGet(null);
		}
		
		GameConfig data = gameConfigPersist.getConfigByAppId(APP_ID);
		cache.put(appId, Optional.ofNullable(data));
		return data;
	}

	public GameAccount handleResult(String username) {
		GameConfig config = getConfig(APP_ID);
		List<Integer> cards = random.ints(0, 9).limit(config.cardsSize).boxed().collect(Collectors.toList());

		int numThreeCard = calNumOfThreeCard(config.bonusThreeCard, cards);
		int chipAward = numThreeCard * config.awardThreeCard;

		GameAccount game = gamePersist.upsert(username, numThreeCard, chipAward, APP_ID);
		
		if(game != null) {
			upRank(game.username, game.chip);
		}
		
		return game;
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
