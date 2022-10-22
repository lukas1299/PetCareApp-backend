create TABLE IF NOT EXISTS friends(
   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   user_id UUID,
   profile_id UUID,
   status VARCHAR(32));

alter table friends add CONSTRAINT FK_USER FOREIGN KEY (user_id) REFERENCES users(id);
alter table friends add CONSTRAINT FK_PROFILE FOREIGN KEY (profile_id) REFERENCES profiles(id);