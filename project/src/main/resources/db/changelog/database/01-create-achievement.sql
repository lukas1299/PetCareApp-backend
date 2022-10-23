CREATE TABLE IF NOT EXISTS achievements(
   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   name VARCHAR(32));
