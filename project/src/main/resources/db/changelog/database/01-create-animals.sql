CREATE TABLE IF NOT EXISTS animals(
   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   age INT,
   type VARCHAR(32),
   weight INT,
   gender VARCHAR(32),
   name VARCHAR(32),
   user_id UUID);

ALTER TABLE animals ADD CONSTRAINT FK_ANIMALS FOREIGN KEY (user_id) REFERENCES users(id);