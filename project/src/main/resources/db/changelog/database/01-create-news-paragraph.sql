CREATE TABLE IF NOT EXISTS news_paragraph(
   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   title VARCHAR(64),
   text VARCHAR(2048),
   counter INT,
   news_id UUID);

ALTER TABLE news_paragraph ADD CONSTRAINT FK_NEWS FOREIGN KEY (news_id) REFERENCES news(id);


