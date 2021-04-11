create user 'joy'@'localhost' identified by 'password';

grant all privileges on *.* to 'joy'@'localhost';

flush privileges;


DROP DATABASE chess_db;

CREATE DATABASE chess_db DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

USE chess_db;


DROP TABLE chess_game;

CREATE TABLE chess_game
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    name       TEXT                   NOT NULL,
    is_end     BOOLEAN  DEFAULT false NULL,
    created_at DATETIME DEFAULT NOW() NOT NULL
);


DROP TABLE move_command;

CREATE TABLE move_command
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    game_id         BIGINT                 NOT NULL,
    source_position VARCHAR(20)            NOT NULL,
    target_position VARCHAR(20)            NOT NULL,
    turn            ENUM ('BLACK','WHITE') NOT NULL,
    created_at      DATETIME DEFAULT NOW() NOT NULL,
    CONSTRAINT move_command_chess_game_id_fk
        FOREIGN KEY (game_id) REFERENCES chess_game (id)
            ON DELETE CASCADE
);