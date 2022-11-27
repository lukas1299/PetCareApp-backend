CREATE TABLE IF NOT EXISTS post_comment(
   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   content VARCHAR(32),
   date VARCHAR(32),
   social_posts_id UUID);

ALTER TABLE post_comment ADD CONSTRAINT FK_POSTS_COMMENT FOREIGN KEY (social_posts_id) REFERENCES social_posts(id);


