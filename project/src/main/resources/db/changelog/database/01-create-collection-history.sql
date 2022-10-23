CREATE TABLE IF NOT EXISTS collection_history(
   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   user_id UUID,
   collection_id UUID,
   money DECIMAL);

alter table collection_history add CONSTRAINT FK_USER FOREIGN KEY (user_id) REFERENCES users(id);
alter table collection_history add CONSTRAINT FK_COLLECTION FOREIGN KEY (collection_id) REFERENCES collections(id);