package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.GameAccount;

@RestController
public class GameController {
	@Autowired
	GameRepository gameRepo;

	@GetMapping("/start/{username}")
	public ResponseEntity<?> startGame(@PathVariable(required = true) String username) {
		try {
			GameAccount game = gameRepo.handleResult(username);
			if (game == null) {
				return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Max ticket");
			}
			return ResponseEntity.status(HttpStatus.OK).body(game);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@GetMapping("/ranks")
	public ResponseEntity<?> getRanks() {
		try {
			return ResponseEntity.status(HttpStatus.OK).body(gameRepo.getTop10());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
}
