--liquibase formatted sql

--changeset nvoxland:1
CREATE TABLE website_info_type (
id SERIAL PRIMARY KEY,
name VARCHAR(255) NOT NULL
);
--rollback drop table website_info_type;

--changeset nvoxland:2
INSERT INTO website_info_type (name) VALUES ('GitHub'), ('StackOverflow');

--changeset nvoxland:3
CREATE TABLE website_info (
id SERIAL PRIMARY KEY,
type_id INT NOT NULL,
FOREIGN KEY (type_id) REFERENCES website_info_type(id)
);
--rollback drop table website_info;

--changeset nvoxland:4
CREATE TABLE chat (
    id INT PRIMARY KEY
);
--rollback drop table chat;

--changeset nvoxland:5
CREATE TABLE tracked_link (
id SERIAL PRIMARY KEY,
website_info_id INT NOT NULL,
chat_id INT NOT NULL,
last_update_date_time TIMESTAMP NOT NULL,
FOREIGN KEY (website_info_id) REFERENCES website_info(id) on delete cascade,
FOREIGN KEY (chat_id) REFERENCES chat(id) on delete cascade
);
--rollback drop table tracked_link;

--changeset nvoxland:6
CREATE TABLE github_info (
website_info_id INT PRIMARY KEY,
user_name VARCHAR(255) NOT NULL,
repository_name VARCHAR(255) NOT NULL,
FOREIGN KEY (website_info_id) REFERENCES website_info(id)  on delete cascade,
constraint link_info_git_hub_info unique (user_name, repository_name)
);
--rollback drop table github_info;

--changeset nvoxland:7
CREATE TABLE stackoverflow_info (
website_info_id INT PRIMARY KEY,
question_id INT NOT NULL,
FOREIGN KEY (website_info_id) REFERENCES website_info(id) on delete cascade,
constraint link_info_stack_overflow_info unique (question_id)
);
--rollback drop table stackoverflow_info;