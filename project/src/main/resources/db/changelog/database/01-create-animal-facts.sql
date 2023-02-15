create TABLE IF NOT EXISTS animal_facts(
   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   name VARCHAR(2048),
   animal_breed_id UUID);