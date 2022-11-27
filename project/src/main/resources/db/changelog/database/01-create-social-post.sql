CREATE TABLE IF NOT EXISTS social_posts(
   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   title VARCHAR(32),
   content VARCHAR(32),
   date VARCHAR(32),
   profile_id UUID
   );

ALTER TABLE social_posts ADD CONSTRAINT FK_SOCIALPOSTS FOREIGN KEY (profile_id) REFERENCES profiles(id);

