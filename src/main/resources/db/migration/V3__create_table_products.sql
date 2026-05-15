-- =============================================================
--  V3 — Table: products
-- =============================================================

CREATE TABLE products (
                          id                  UUID            NOT NULL DEFAULT gen_random_uuid(),
                          category_id         UUID            NOT NULL,
                          name                VARCHAR(200)    NOT NULL,
                          description         TEXT,
                          price               NUMERIC(12, 2)  NOT NULL,
                          available_quantity  INTEGER         NOT NULL DEFAULT 0,
                          active              BOOLEAN         NOT NULL DEFAULT TRUE,
                          created_at          TIMESTAMP       NOT NULL DEFAULT NOW(),
                          updated_at          TIMESTAMP       NOT NULL DEFAULT NOW(),

                          CONSTRAINT pk_products                      PRIMARY KEY (id),
                          CONSTRAINT fk_products_category             FOREIGN KEY (category_id) REFERENCES categories (id),
                          CONSTRAINT ck_products_price_positive       CHECK (price > 0),
                          CONSTRAINT ck_products_quantity_non_negative CHECK (available_quantity >= 0)
);

COMMENT ON TABLE  products                    IS 'Product catalog for the Sales API';
COMMENT ON COLUMN products.available_quantity IS 'Stock mirror — updated via Kafka event published by Stock API';
COMMENT ON COLUMN products.price              IS 'Selling price in BRL';

CREATE INDEX idx_products_category ON products (category_id);
CREATE INDEX idx_products_active   ON products (active);