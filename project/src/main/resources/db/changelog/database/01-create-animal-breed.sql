create TABLE IF NOT EXISTS animal_breed(
   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   name VARCHAR(128));
