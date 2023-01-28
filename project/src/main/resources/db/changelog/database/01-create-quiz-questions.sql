
--liquibase formatted sql
--changeset vlplanner:2
CREATE TABLE IF NOT EXISTS quiz_questions(
   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   content VARCHAR(512),
   photo BYTEA,
   quiz_id UUID);

ALTER TABLE quiz_questions ADD CONSTRAINT FK_QUIZ FOREIGN KEY (quiz_id) REFERENCES quizzes(id);

