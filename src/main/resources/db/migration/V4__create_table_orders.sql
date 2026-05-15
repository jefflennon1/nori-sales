-- =============================================================
--  V4 — Table: orders
-- =============================================================

CREATE TABLE orders (
                        id          UUID            NOT NULL DEFAULT gen_random_uuid(),
                        user_id     UUID            NOT NULL,
                        status      VARCHAR(30)     NOT NULL DEFAULT 'PENDING_PAYMENT',
                        total_price NUMERIC(12, 2)  NOT NULL,
                        created_at  TIMESTAMP       NOT NULL DEFAULT NOW(),
                        updated_at  TIMESTAMP       NOT NULL DEFAULT NOW(),

                        CONSTRAINT pk_orders             PRIMARY KEY (id),
                        CONSTRAINT fk_orders_user        FOREIGN KEY (user_id) REFERENCES users (id),
                        CONSTRAINT ck_orders_status      CHECK (status IN (
                                                                           'PENDING_PAYMENT',
                                                                           'PAYMENT_CONFIRMED',
                                                                           'CANCELLED'
                            )),
                        CONSTRAINT ck_orders_total_price CHECK (total_price > 0)
);

COMMENT ON TABLE  orders             IS 'Orders placed by buyers';
COMMENT ON COLUMN orders.status      IS 'PENDING_PAYMENT → PAYMENT_CONFIRMED or CANCELLED';
COMMENT ON COLUMN orders.total_price IS 'Sum of (price × quantity) for all order items';

CREATE INDEX idx_orders_user       ON orders (user_id);
CREATE INDEX idx_orders_status     ON orders (status);
CREATE INDEX idx_orders_created_at ON orders (created_at DESC);