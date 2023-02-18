create TABLE IF NOT EXISTS animal_facts(
   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   name VARCHAR(2048),
   animal_breed_id UUID);

alter table animal_facts add CONSTRAINT FK_FACTS FOREIGN KEY (animal_breed_id) REFERENCES animal_breed(id);