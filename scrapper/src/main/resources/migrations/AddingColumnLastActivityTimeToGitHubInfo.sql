--liquibase formatted sql

--changeset nvoxland:14
ALTER TABLE github_info
ADD COLUMN last_activity_date_time TIMESTAMP NOT NULL;