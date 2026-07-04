ALTER TABLE users
    ADD CONSTRAINT uk_users_username UNIQUE (username);

ALTER TABLE users
    ADD CONSTRAINT uk_users_email UNIQUE (email);