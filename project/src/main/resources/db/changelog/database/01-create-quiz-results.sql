
--liquibase formatted sql
--changeset vlplanner:2
CREATE TABLE IF NOT EXISTS quiz_results(
   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   result VARCHAR(64),
   user_id UUID,
   quiz_id UUID);

ALTER TABLE quiz_results ADD CONSTRAINT FK_USER FOREIGN KEY (user_id) REFERENCES users(id);
ALTER TABLE quiz_results ADD CONSTRAINT FK_QUIZ FOREIGN KEY (quiz_id) REFERENCES quizzes(id);

