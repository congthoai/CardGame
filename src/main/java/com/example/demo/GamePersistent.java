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

	public GameAccount upsert(String username, int numthreecard, int chip, int appId) {
		String sql = "INSERT INTO game_account (username, usergamecount, numthreecard, chip) "
				+ "SELECT ?, COALESCE(MAX(usergamecount), 0) + 1, ?, ? from game_account "
				+ "WHERE username = ? AND (SELECT COUNT(id) FROM GAME_ACCOUNT) < (SELECT max_ticket FROM game_config WHERE app_id = ?)"
				+ "ON CONFLICT (username, usergamecount) DO NOTHING " + "RETURNING *";
		return jdbcTemplate.queryForObject(sql,  new GameAccountMapper(), username, numthreecard, chip, username, appId);
	}

	public class GameAccountMapper implements RowMapper<GameAccount> {
		@Override
		public GameAccount mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new GameAccount(rs.getInt("id"), rs.getString("username"), rs.getInt("usergamecount"), rs.getInt("chip"), rs.getInt("numthreecard"));
		}
	}
}
