
--liquibase formatted sql
--changeset vlplanner:2
CREATE TABLE IF NOT EXISTS topics(
   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   title VARCHAR(32),
   creation_date VARCHAR(32),
   description VARCHAR(256),
   topic_category VARCHAR(256),
   user_id UUID);

ALTER TABLE topics ADD CONSTRAINT FK_TOPIC FOREIGN KEY (user_id) REFERENCES users(id);

