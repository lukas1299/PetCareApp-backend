CREATE TABLE IF NOT EXISTS collections(
   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   title VARCHAR(32),
   target DECIMAL,
   description VARCHAR(32),
   creation_date VARCHAR(32),
   profile_id UUID);

alter table collections add CONSTRAINT FK_COLLECTIONS FOREIGN KEY (profile_id) REFERENCES profiles(id);
