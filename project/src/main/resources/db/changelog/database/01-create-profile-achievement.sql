CREATE TABLE IF NOT EXISTS profile_achievement(
   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   profile_id UUID,
   achievement_id UUID,
   date VARCHAR(32));

ALTER TABLE profile_achievement ADD CONSTRAINT FK_PROFILE FOREIGN KEY (profile_id) REFERENCES profiles(id);
ALTER TABLE profile_achievement ADD CONSTRAINT FK_ACHIEVEMENT FOREIGN KEY (achievement_id) REFERENCES achievements(id);