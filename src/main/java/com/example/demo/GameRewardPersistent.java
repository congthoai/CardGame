package com.example.demo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.example.demo.domain.GameReward;

@Component
public class GameRewardPersistent {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public int upsert(String username, int chip) {
		String sql = """
				INSERT INTO GAME_REWARD AS gr (username, chip)
				SELECT ?, ?
				ON CONFLICT (username)
				DO UPDATE SET chip = gr.chip + EXCLUDED.chip,
					gamecount = gr.gamecount + 1, last_time = NOW()
				RETURNING chip
				""";
		return jdbcTemplate.queryForObject(sql, Integer.class, username, chip);
	}

	public List<GameReward> getGameRewardList() {
		String sql = """
				SELECT * FROM game_reward order by chip DESC, last_time DESC
				""";
		return jdbcTemplate.query(sql, new GameRewartMapper());
	}

	public class GameRewartMapper implements RowMapper<GameReward> {
		@Override
		public GameReward mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new GameReward(rs.getString("username"), rs.getInt("gamecount"), rs.getInt("chip"),
					rs.getDate("last_time"));
		}
	}
}
