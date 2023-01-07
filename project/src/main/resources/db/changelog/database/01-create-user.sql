
--liquibase formatted sql
--changeset vlplanner:2
CREATE TABLE IF NOT EXISTS users(
   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   email VARCHAR(32),
   username VARCHAR(32),
   password VARCHAR(256),
   photo BYTEA);

