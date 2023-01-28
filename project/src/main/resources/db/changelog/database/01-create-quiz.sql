
--liquibase formatted sql
--changeset vlplanner:2
CREATE TABLE IF NOT EXISTS quizzes(
   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   topic VARCHAR(512),
   photo BYTEA);


