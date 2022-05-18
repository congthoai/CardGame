package com.example.demo.domain.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.example.demo.domain.GameAccount;

public class GameAccountMapper implements RowMapper<GameAccount> {

	@Override
	public GameAccount mapRow(ResultSet rs, int rowNum) throws SQLException {
		GameAccount game = new GameAccount();
		game.setUsername(rs.getString("username"));
		game.setUsergamecount(rs.getInt("usergamecount"));
		game.setChip(rs.getInt("chip"));
		game.setNumThreeCard(rs.getInt("numthreecard"));
		return game;
	}
	
}
