CREATE TABLE IF NOT EXISTS news(
   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   date VARCHAR(64),
   title VARCHAR(64),
   photo BYTEA);


