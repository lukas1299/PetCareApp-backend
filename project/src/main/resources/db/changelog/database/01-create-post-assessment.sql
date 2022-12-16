CREATE TABLE IF NOT EXISTS post_assessment(
   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   post_id UUID,
   user_id UUID
   );

ALTER TABLE post_assessment ADD CONSTRAINT FK_POSTS_ASSESSMENT_POST FOREIGN KEY (post_id) REFERENCES posts(id);
ALTER TABLE post_assessment ADD CONSTRAINT FK_POSTS_ASSESSMENT_USER FOREIGN KEY (user_id) REFERENCES users(id);

