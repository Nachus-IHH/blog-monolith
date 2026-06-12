-- V1__initial_schema.sql

-- Users
CREATE TABLE IF NOT EXISTS public.users (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username VARCHAR(30) UNIQUE, 
    password VARCHAR(256) NOT NULL, 
    email TEXT NOT NULL UNIQUE, 
    -- USER / ADMIN 
    role VARCHAR(20) NOT NULL DEFAULT 'USER'
);

-- Posts
CREATE TABLE IF NOT EXISTS public.post (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title VARCHAR(120) NOT NULL,
    content TEXT NOT NULL,
    creation_date TIMESTAMPTZ DEFAULT NOW(),
    user_id BIGINT NOT NULL REFERENCES public.users(id)
);

-- Comments
CREATE TABLE IF NOT EXISTS public.comments (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    content TEXT NOT NULL,
    creation_date TIMESTAMPTZ DEFAULT NOW(),
    post_id BIGINT NOT NULL REFERENCES public.post(id),
    user_id BIGINT NOT NULL REFERENCES public.users(id)
);
