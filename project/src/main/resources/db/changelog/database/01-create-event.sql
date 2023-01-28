CREATE TABLE IF NOT EXISTS events(
   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   name VARCHAR(64),
   date VARCHAR(32),
   event_type INT,
   animal_id UUID);

ALTER TABLE events ADD CONSTRAINT FK_EVENTS FOREIGN KEY (animal_id) REFERENCES animals(id);

