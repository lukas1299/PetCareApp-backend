
--liquibase formatted sql
--changeset vlplanner:2
CREATE TABLE IF NOT EXISTS quiz_answers(
   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   answer VARCHAR(64),
   correctness boolean,
   question_id UUID);

ALTER TABLE quiz_answers ADD CONSTRAINT FK_QUESTION FOREIGN KEY (question_id) REFERENCES quiz_questions(id);

