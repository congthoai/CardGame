package com.example.demo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.example.demo.domain.AccountRank;
import com.example.demo.domain.GameAccount;

@Component
public class GamePersistent {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public GameAccount upsert(String username, int numthreecard, int chip, int appId) {
		String sql = """
				INSERT INTO game_account (username, numthreecard, chip)
				SELECT ?, ?, ? 
				WHERE (SELECT COUNT(id) FROM GAME_ACCOUNT) < (SELECT max_ticket FROM game_config WHERE app_id = ?)
				RETURNING *
				""";
		return jdbcTemplate.queryForObject(sql, new GameAccountMapper(), username, numthreecard, chip, appId);
	}
	
	public List<AccountRank> getAccountRankList() {
		String sql = """
				SELECT username, SUM(chip) as chip, MAX(playtime) as playtime
				FROM game_account
				GROUP BY username
				""";
		return jdbcTemplate.query(sql, new AccountRankMapper());
	}

	public class GameAccountMapper implements RowMapper<GameAccount> {
		@Override
		public GameAccount mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new GameAccount(rs.getInt("id"), rs.getString("username"), rs.getInt("chip"), rs.getInt("numthreecard"), rs.getDate("playtime"));
		}
	}
	
	public class AccountRankMapper implements RowMapper<AccountRank> {
		@Override
		public AccountRank mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new AccountRank(rs.getString("username"), rs.getInt("chip"), rs.getDate("playtime"));
		}
	}
}
