CREATE TABLE IF NOT EXISTS competitions(
   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   title VARCHAR(128),
   photo BYTEA);
