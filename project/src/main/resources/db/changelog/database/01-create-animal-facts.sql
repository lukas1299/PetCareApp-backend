create TABLE IF NOT EXISTS animal_facts(
   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   name VARCHAR(128),
   animal_breed_id UUID);

ALTER TABLE animal_facts ADD CONSTRAINT FK_FACTS FOREIGN KEY (animal_breed_id) REFERENCES animal_breed(id);
