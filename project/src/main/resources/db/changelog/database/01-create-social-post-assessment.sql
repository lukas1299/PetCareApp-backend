CREATE TABLE IF NOT EXISTS social_posts_assessment(
   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   social_post_id UUID,
   profile_id UUID
   );

ALTER TABLE social_posts_assessment ADD CONSTRAINT FK_SOCIALPOSTS_ASSESSMENT_PROFILE FOREIGN KEY (profile_id) REFERENCES profiles(id);
ALTER TABLE social_posts_assessment ADD CONSTRAINT FK_SOCIALPOSTS_ASSESSMENT_SOCIAL_POST FOREIGN KEY (social_post_id) REFERENCES social_posts(id);

