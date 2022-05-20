package com.example.demo;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.example.demo.domain.GameEvent;

@Component
public class GameEventPersistent {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public GameEvent getEventByAppId(int appId) {
		return jdbcTemplate.queryForObject("SELECT * from GAME_EVENT where app_id = ?", new GameEventMapper(), appId);
	}
	
	public boolean upCurrentTicketByAppId(int appId) {
		String sql = """
				UPDATE GAME_EVENT set current_ticket = GAME_EVENT.current_ticket + 1 
				WHERE app_id = ? and current_ticket < max_ticket
				""";
		return jdbcTemplate.update(sql, appId) > 0;
	}

	public class GameEventMapper implements RowMapper<GameEvent> {
		@Override
		public GameEvent mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new GameEvent(rs.getInt("app_id"), rs.getInt("cards_size"), rs.getInt("award_three_card"),
					rs.getInt("bonus_three_card"), rs.getInt("max_ticket"));
		}
	}
}
