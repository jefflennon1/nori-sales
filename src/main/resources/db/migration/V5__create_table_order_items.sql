-- =============================================================
--  V5 — Table: order_items
-- =============================================================

CREATE TABLE order_items (
    id          UUID            NOT NULL DEFAULT gen_random_uuid(),
    order_id    UUID            NOT NULL,
    product_id  UUID            NOT NULL,
    quantity    INTEGER         NOT NULL,
    unit_price  NUMERIC(12, 2)  NOT NULL,
    subtotal    NUMERIC(12, 2)  NOT NULL,

    CONSTRAINT pk_order_items             PRIMARY KEY (id),
    CONSTRAINT fk_order_items_order       FOREIGN KEY (order_id)   REFERENCES orders   (id),
    CONSTRAINT fk_order_items_product     FOREIGN KEY (product_id) REFERENCES products (id),
    CONSTRAINT uq_order_items_product     UNIQUE (order_id, product_id),
    CONSTRAINT ck_order_items_quantity    CHECK (quantity > 0),
    CONSTRAINT ck_order_items_unit_price  CHECK (unit_price > 0),
    CONSTRAINT ck_order_items_subtotal    CHECK (subtotal > 0)
);

COMMENT ON TABLE  order_items            IS 'Items that make up each order';
COMMENT ON COLUMN order_items.unit_price IS 'Price at the time of purchase — snapshot to avoid dependency on current product price';
COMMENT ON COLUMN order_items.subtotal   IS 'unit_price × quantity — calculated by the application before persisting';

CREATE INDEX idx_order_items_order   ON order_items (order_id);
CREATE INDEX idx_order_items_product ON order_items (product_id);