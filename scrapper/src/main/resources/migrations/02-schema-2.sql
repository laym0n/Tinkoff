--liquibase formatted sql

--changeset nvoxland:8
ALTER TABLE website_info
ADD COLUMN last_update_date_time TIMESTAMP NOT NULL DEFAULT now();

--changeset nvoxland:9
ALTER TABLE tracked_link
DROP COLUMN last_update_date_time;