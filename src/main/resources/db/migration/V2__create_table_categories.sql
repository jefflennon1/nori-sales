-- =============================================================
--  V2 — Table: categories
-- =============================================================

CREATE TABLE categories (
                            id          UUID            NOT NULL DEFAULT gen_random_uuid(),
                            name        VARCHAR(100)    NOT NULL,
                            description VARCHAR(255),
                            active      BOOLEAN         NOT NULL DEFAULT TRUE,
                            created_at  TIMESTAMP       NOT NULL DEFAULT NOW(),
                            updated_at  TIMESTAMP       NOT NULL DEFAULT NOW(),

                            CONSTRAINT pk_categories      PRIMARY KEY (id),
                            CONSTRAINT uq_categories_name UNIQUE (name)
);

COMMENT ON TABLE categories IS 'Product categories';