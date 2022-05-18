package com.example.demo;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.example.demo.domain.GameConfig;

@Component
public class GameConfigPersistent {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public GameConfig getConfigByAppId(int appId) {
		String sql = "SELECT * from game_config where app_id = ?";
		return jdbcTemplate.queryForObject(sql, new GameConfigMapper(), appId);
	}

	public class GameConfigMapper implements RowMapper<GameConfig> {
		@Override
		public GameConfig mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new GameConfig(rs.getInt("app_id"), rs.getInt("cards_size"), rs.getInt("award_three_card"), rs.getInt("bonus_three_card"), rs.getInt("max_ticket"));
		}
	}
}
