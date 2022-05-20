package com.example.demo;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.example.demo.domain.GameAccount;

@Component
public class GamePersistent {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public GameAccount insert(String username, int numthreecard, int chip) {
		String sql = """
				INSERT INTO GAME_ACCOUNT (username, numthreecard, chip)
				SELECT ?, ?, ? 
				RETURNING * 
				""";
		return jdbcTemplate.queryForObject(sql, new GameAccountMapper(), username, numthreecard, chip);
	}
	
	public class GameAccountMapper implements RowMapper<GameAccount> {
		@Override
		public GameAccount mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new GameAccount(rs.getInt("game_id"), rs.getString("username"), rs.getInt("chip"), rs.getInt("numthreecard"), rs.getDate("play_time"));
		}
	}
	
}
