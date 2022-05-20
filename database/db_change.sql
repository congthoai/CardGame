--create database gamedb;
--use gamedb;

CREATE TABLE game_event (
	app_id int not null,	
	cards_size int not null,
	award_three_card int not null,
	bonus_three_card int not null,
	max_ticket int not null,
	current_ticket int not null default 0
);

ALTER TABLE game_event ADD CONSTRAINT pk_game_event PRIMARY KEY (app_id);
INSERT INTO game_event (app_id, cards_size, award_three_card, bonus_three_card, max_ticket, current_ticket) VALUES (1, 20, 1000, 3, 100, 0);

CREATE TABLE game_account (
	id serial not null, 
	username text not null,
	chip int not null default 0,
	numthreecard int not null default 0,
	play_time timestamp with time zone default now()
);

ALTER TABLE game_account ADD CONSTRAINT pk_game_account PRIMARY KEY (id);

CREATE TABLE game_reward (
	username text not null,
	gamecount smallint not null default 1,
	chip int not null default 0,
	last_time timestamp with time zone not null default now()
);

ALTER TABLE game_reward ADD CONSTRAINT pk_game_reward PRIMARY KEY (username);



