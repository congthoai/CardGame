package com.example.demo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.example.demo.domain.GameAccount;
import com.example.demo.domain.GameConfig;
import com.example.demo.domain.mapper.GameAccountMapper;
import com.example.demo.domain.mapper.GameConfigMapper;

@Component
public class GamePersistent {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public GameAccount findGameById(int gameid) {
		String sql = "SELECT * from game_account where id = ?";
		return jdbcTemplate.queryForObject(sql, new GameAccountMapper(), gameid);
	}
	
	public Integer upsert(String username, int numthreecard, int chip) {
		String sql = "INSERT INTO game_account (username, usergamecount, numthreecard, chip) "
				+ "SELECT ?, coalesce(Max(usergamecount), 0) + 1, ?, ? from game_account "
				+ "where username = ? and (select count(id) from game_account) < (select value from game_config where key = 'max_ticket')"
				+ "ON CONFLICT (username, usergamecount) DO NOTHING "
				+ "RETURNING id";
		return jdbcTemplate.queryForObject(sql, Integer.class, username, numthreecard, chip, username);
	}
	
	public Map<String, Integer> getAllConfig() {
		String sql = "SELECT * from game_config";
		List<GameConfig> confList = jdbcTemplate.query(sql, new GameConfigMapper());
		
		if(confList == null || confList.isEmpty()) {
			return null;
		}
		
		Map<String, Integer> confMap = new HashMap<>();
		for(GameConfig c : confList) {
			confMap.put(c.getKey(), c.getValue());
		}
		return confMap;
	}

}
