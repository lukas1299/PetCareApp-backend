CREATE TABLE IF NOT EXISTS role_user(
   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   user_id uuid,
   role_id uuid);

ALTER TABLE role_user ADD CONSTRAINT FK_USER FOREIGN KEY (user_id) REFERENCES users(id);
ALTER TABLE role_user ADD CONSTRAINT FK_ROLE FOREIGN KEY (role_id) REFERENCES roles(id);