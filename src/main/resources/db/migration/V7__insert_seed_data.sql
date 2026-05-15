-- =============================================================
--  V7 — Seed data
--  NOTE: passwords below are placeholder hashes.
--  Generate real BCrypt hashes before running in any environment.
--  Example (Java): new BCryptPasswordEncoder().encode("demo1234")
-- =============================================================

-- Admin user (password: admin1234)
INSERT INTO users (id, name, email, password, role) VALUES
    (gen_random_uuid(),
     'Nori Admin',
     'admin@nori.com',
     '$2a$10$REPLACE_WITH_REAL_BCRYPT_HASH_ADMIN1234_____________',
     'ADMIN');

-- Demo buyer user (password: demo1234)
INSERT INTO users (id, name, email, password, role) VALUES
    (gen_random_uuid(),
     'Demo Buyer',
     'buyer@demo.com',
     '$2a$10$REPLACE_WITH_REAL_BCRYPT_HASH_DEMO1234______________',
     'BUYER');

-- Categories
INSERT INTO categories (id, name, description) VALUES
                                                   ('a1b2c3d4-0000-0000-0000-000000000001', 'Electronics',  'Smartphones, laptops and accessories'),
                                                   ('a1b2c3d4-0000-0000-0000-000000000002', 'Books',        'Physical and digital books'),
                                                   ('a1b2c3d4-0000-0000-0000-000000000003', 'Peripherals',  'Keyboards, mice and headsets');

-- Products
INSERT INTO products (id, category_id, name, description, price, available_quantity) VALUES
                                                                                         (gen_random_uuid(), 'a1b2c3d4-0000-0000-0000-000000000001',
                                                                                          'Smartphone X Pro',   'Flagship smartphone 256GB',           3999.90, 50),
                                                                                         (gen_random_uuid(), 'a1b2c3d4-0000-0000-0000-000000000001',
                                                                                          'UltraBook Laptop',   'Lightweight laptop 16GB RAM 512GB SSD', 4599.00, 30),
                                                                                         (gen_random_uuid(), 'a1b2c3d4-0000-0000-0000-000000000002',
                                                                                          'Clean Code',         'Robert C. Martin — best practices',    89.90,  100),
                                                                                         (gen_random_uuid(), 'a1b2c3d4-0000-0000-0000-000000000002',
                                                                                          'Domain-Driven Design','Eric Evans — DDD in practice',        120.00,  80),
                                                                                         (gen_random_uuid(), 'a1b2c3d4-0000-0000-0000-000000000003',
                                                                                          'RGB Mechanical Keyboard', 'Red switch, US layout',           349.90,  60),
                                                                                         (gen_random_uuid(), 'a1b2c3d4-0000-0000-0000-000000000003',
                                                                                          'Wireless Gaming Mouse',   '12000 DPI optical mouse',         199.90,  75);