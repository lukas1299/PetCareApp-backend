CREATE TABLE IF NOT EXISTS vaccinations(
   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   name VARCHAR(512),
   interval VARCHAR(512));
