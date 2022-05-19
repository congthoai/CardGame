--create database gamedb;
--use gamedb;

CREATE TABLE game_config (
	app_id int not null,	
	cards_size int null,
	award_three_card int null,
	bonus_three_card int null,
	max_ticket int null
);
ALTER TABLE game_config ADD CONSTRAINT pk_game_config PRIMARY KEY (app_id);
INSERT INTO game_config (app_id, cards_size, award_three_card, bonus_three_card, max_ticket) VALUES (1, 20, 1000, 3, 100);

CREATE TABLE game_account (
	id serial not null, 
	username text not null,
	chip int4 not null default 0,
	numthreecard int4 not null default 0,
	playtime timestamp with time zone default now()
);

ALTER TABLE game_account ADD CONSTRAINT pk_game_account PRIMARY KEY (id);


