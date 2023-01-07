CREATE TABLE IF NOT EXISTS post_comment(
   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   content VARCHAR(128),
   date VARCHAR(32),
   user_id UUID,
   social_posts_id UUID);

ALTER TABLE post_comment ADD CONSTRAINT FK_POSTS_COMMENT FOREIGN KEY (social_posts_id) REFERENCES social_posts(id);
ALTER TABLE post_comment ADD CONSTRAINT FK_POSTS_COMMENT_USER FOREIGN KEY (user_id) REFERENCES users(id);


