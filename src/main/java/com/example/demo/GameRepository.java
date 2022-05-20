package com.example.demo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Component;

import com.example.demo.domain.GameAccount;
import com.example.demo.domain.GameEvent;
import com.example.demo.domain.GameReward;

@Component
public class GameRepository {

	@Autowired
	private GamePersistent gamePersist;

	@Autowired
	private GameEventPersistent gameEventPersist;

	@Autowired
	private GameRewardPersistent gameRewardPersist;

	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;

	private static final int APP_ID = 1;
	private static final int NUM_PER_ADD_RANK = 5;
	private static final String RANK_KEY = "rank";

	private Random random = new Random();

	private final Map<Integer, Optional<GameEvent>> cache = new ConcurrentHashMap<>();

	@PostConstruct
	void init() {
		initRanks(NUM_PER_ADD_RANK);
	}

	public void initRanks(int numPerAddRank) {
		if (!redisTemplate.hasKey(RANK_KEY)) {
			List<GameReward> rankList = gameRewardPersist.getGameRewardList();
			Set<TypedTuple<Object>> rankSet = new HashSet<>();
			rankList.forEach(item -> rankSet.add(new DefaultTypedTuple<>(item.username, (double) item.chip)));

			for (int i = 0; i < rankSet.size(); i += numPerAddRank) {
				redisTemplate.opsForZSet().add(RANK_KEY,
						rankSet.stream().skip(i).limit(numPerAddRank).collect(Collectors.toSet()));
			}
		}
	}

	public List<TypedTuple<Object>> getTop10() {
		return redisTemplate.opsForZSet().reverseRangeWithScores(RANK_KEY, 0, 10).stream().collect(Collectors.toList());
	}

	private GameEvent getConfig(final int appId) {
		Optional<GameEvent> result = cache.get(appId);
		if (result != null) {
			return result.orElseGet(null);
		}

		GameEvent data = gameEventPersist.getEventByAppId(APP_ID);
		cache.put(appId, Optional.ofNullable(data));
		return data;
	}

	public GameAccount handleResult(String username) {
		GameEvent config = getConfig(APP_ID);
		if (config == null || !gameEventPersist.upCurrentTicketByAppId(APP_ID)) {
			return null;
		}

		List<Integer> cards = random.ints(0, 9).limit(config.cardsSize).boxed().collect(Collectors.toList());
		int numThreeCard = calNumOfThreeCard(config.bonusThreeCard, cards);
		int chipAward = numThreeCard * config.awardThreeCard;

		return upRank(username, numThreeCard, chipAward, APP_ID);
	}

	private GameAccount upRank(String username, int numThreeCard, int chip, int appId) {
		GameAccount acc = gamePersist.insert(username, numThreeCard, chip);

		if (acc != null) {
			final int reward = gameRewardPersist.upsert(username, chip);
			redisTemplate.opsForZSet().add(RANK_KEY, username, reward);
		}

		return acc;
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
