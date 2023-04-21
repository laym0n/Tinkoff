--liquibase formatted sql

--changeset nvoxland:10
CREATE TABLE github_commit (
sha VARCHAR(40) NOT NULL,
website_info_id INT NOT NULL,
PRIMARY KEY (sha, website_info_id),
FOREIGN KEY (website_info_id)
REFERENCES github_info (website_info_id) on delete cascade
);

--changeset nvoxland:11
CREATE TABLE github_branch (
name VARCHAR(40) NOT NULL,
website_info_id INT NOT NULL,
PRIMARY KEY (name, website_info_id),
FOREIGN KEY (website_info_id)
REFERENCES github_info (website_info_id) on delete cascade
);

--changeset nvoxland:12
CREATE TABLE stack_overflow_comment (
id INT NOT NULL,
website_info_id INT NOT NULL,
PRIMARY KEY (id, website_info_id),
FOREIGN KEY (website_info_id)
REFERENCES stackoverflow_info (website_info_id) on delete cascade
);

--changeset nvoxland:13
CREATE TABLE stack_overflow_answer (
id INT NOT NULL,
user_name VARCHAR(40) NOT NULL,
last_edit_date_time TIMESTAMP NOT NULL,
website_info_id INT,
PRIMARY KEY (id, website_info_id),
FOREIGN KEY (website_info_id)
REFERENCES stackoverflow_info (website_info_id) on delete cascade
);