--create database gamedb;
--use gamedb;

create table game_config (
	app_id int not null,	
	cards_size int null,
	award_three_card int null,
	bonus_three_card int null,
	max_ticket int null
);
ALTER TABLE game_config ADD CONSTRAINT pk_game_config PRIMARY KEY (app_id);
insert into game_config (app_id, cards_size, award_three_card, bonus_three_card, max_ticket) values (1, 20, 1000, 3, 100);

create table game_account (
	id serial not null, 
	username varchar(255) not null,
	usergamecount int4 not null,
	chip int4 not null default 0,
	numthreecard int4 not null default 0
);

alter table game_account add  CONSTRAINT pk_game_account PRIMARY KEY (id);
alter table game_account add  CONSTRAINT fk_game_acccount_username_game UNIQUE (username, usergamecount);
