-- =============================================================
--  V1 — Table: users
-- =============================================================

CREATE TABLE users (
                       id            UUID            NOT NULL DEFAULT gen_random_uuid(),
                       name          VARCHAR(150)    NOT NULL,
                       email         VARCHAR(255)    NOT NULL UNIQUE,
                       password      VARCHAR(255)    NOT NULL,
                       role          VARCHAR(30)     NOT NULL DEFAULT 'BUYER',
                       active        BOOLEAN         NOT NULL DEFAULT TRUE,
                       created_at    TIMESTAMP       NOT NULL DEFAULT NOW(),
                       updated_at    TIMESTAMP       NOT NULL DEFAULT NOW(),

                       CONSTRAINT pk_users         PRIMARY KEY (id),
                       CONSTRAINT uq_users_email   UNIQUE (email),
                       CONSTRAINT ck_users_role    CHECK (role IN ('BUYER', 'ADMIN'))
);

COMMENT ON TABLE  users          IS 'Sales API users — buyers and admins';
COMMENT ON COLUMN users.role     IS 'BUYER = access to purchase flow | ADMIN = manages products';
COMMENT ON COLUMN users.password IS 'BCrypt password hash';