CREATE TABLE IF NOT EXISTS roles(
   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   name VARCHAR(32)
   );
