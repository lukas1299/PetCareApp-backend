CREATE TABLE IF NOT EXISTS profiles(
   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   user_id UUID);

ALTER TABLE profiles ADD CONSTRAINT FK_PROFILES FOREIGN KEY (user_id) REFERENCES users(id);



