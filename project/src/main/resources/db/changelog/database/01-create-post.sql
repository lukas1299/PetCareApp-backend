CREATE TABLE IF NOT EXISTS posts(
   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   message VARCHAR(32),
   negative_opinion_amount INT,
   positive_opinion_amount INT,
   post_creation_date VARCHAR(32),
   topic_id UUID);

ALTER TABLE posts ADD CONSTRAINT FK_POSTS FOREIGN KEY (topic_id) REFERENCES topics(id);
