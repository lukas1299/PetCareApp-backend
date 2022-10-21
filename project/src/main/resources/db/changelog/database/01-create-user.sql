
--liquibase formatted sql
--changeset vlplanner:2
CREATE TABLE IF NOT EXISTS users(
   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   email VARCHAR(32),
   username VARCHAR(32),
   password VARCHAR(32));


--ALTER TABLE tournaments ADD CONSTRAINT FK_SECOND_PLACE FOREIGN KEY (second_place) REFERENCES users(id);
--
--ALTER TABLE tournaments ADD CONSTRAINT FK_THIRD_PLACE FOREIGN KEY (third_place) REFERENCES users(id);
--
