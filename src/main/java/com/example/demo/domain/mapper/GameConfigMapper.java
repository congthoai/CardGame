package com.example.demo.domain.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.example.demo.domain.GameConfig;

public class GameConfigMapper implements RowMapper<GameConfig> {

	@Override
	public GameConfig mapRow(ResultSet rs, int rowNum) throws SQLException {
		GameConfig config = new GameConfig();
		config.setKey(rs.getString("key"));
		config.setValue(rs.getInt("value"));
		return config;
	}
	
}
