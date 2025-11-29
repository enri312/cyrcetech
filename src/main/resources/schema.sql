CREATE TABLE IF NOT EXISTS customers (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    tax_id VARCHAR(50),
    address VARCHAR(255),
    phone VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS equipment (
    id VARCHAR(50) PRIMARY KEY,
    brand VARCHAR(100) NOT NULL,
    model VARCHAR(100) NOT NULL,
    device_type VARCHAR(50) NOT NULL,
    serial_number VARCHAR(100),
    physical_condition TEXT,
    customer_id VARCHAR(50),
    FOREIGN KEY (customer_id) REFERENCES customers(id)
);

CREATE TABLE IF NOT EXISTS tickets (
    id VARCHAR(50) PRIMARY KEY,
    customer_id VARCHAR(50) NOT NULL,
    equipment_id VARCHAR(50) NOT NULL,
    problem_description TEXT,
    observations TEXT,
    status VARCHAR(50) NOT NULL,
    amount_paid DECIMAL(10, 2),
    estimated_cost DECIMAL(10, 2),
    date_created DATE,
    ai_diagnosis TEXT,
    FOREIGN KEY (customer_id) REFERENCES customers(id),
    FOREIGN KEY (equipment_id) REFERENCES equipment(id)
);
