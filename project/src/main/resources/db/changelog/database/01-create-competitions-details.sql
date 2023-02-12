CREATE TABLE IF NOT EXISTS competitions_details(
   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   points INT,
   pet_name VARCHAR(32),
   photo BYTEA,
   competition_id uuid);

ALTER TABLE competitions_details ADD CONSTRAINT FK_COMPETITION FOREIGN KEY (competition_id) REFERENCES competitions(id);