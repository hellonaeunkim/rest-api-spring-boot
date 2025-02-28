CREATE TABLE products
(
    product_id BINARY(16) PRIMARY KEY,
    product_name VARCHAR(20), NOT NULL,
    category VARCHAR(50), NOT NULL,
    price NOT NULL,
    description VARCHAR(500), DEFAULT NULL,
    created_at DATETIME(6) NOT NULL,
    update_at DATETIME(6) DEFAULT NULL
)