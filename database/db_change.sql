--create database gamedb;
--use gamedb;

create table if not exists game_config (
	id bigserial not null,
	"key" varchar(255) not null,
	value int not null default 0	
);
alter table game_config add  CONSTRAINT pk_game_config PRIMARY KEY (id);

insert into game_config ("key", value) values ('cards_size', 20);
insert into game_config ("key", value) values ('award_three_card', 1000);
insert into game_config ("key", value) values ('bonus_three_card', 3);
insert into game_config ("key", value) values ('max_ticket', 100);

create table if not exists game_account (
	id bigserial not null, 
	username varchar(255) not null,
	usergamecount int4 not null,
	chip int4 not null default 0,
	numthreecard int4 not null default 0
	
);

alter table game_account add  CONSTRAINT pk_game_account PRIMARY KEY (id);
alter table game_account add  CONSTRAINT uc_user_game UNIQUE (username, usergamecount);
