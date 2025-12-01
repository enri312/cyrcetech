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

CREATE TABLE IF NOT EXISTS spare_parts (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    stock INTEGER NOT NULL DEFAULT 0,
    provider VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS invoices (
    id VARCHAR(50) PRIMARY KEY,
    ticket_id VARCHAR(50) NOT NULL,
    invoice_number VARCHAR(50) UNIQUE NOT NULL,
    issue_date DATE NOT NULL,
    due_date DATE,
    subtotal DECIMAL(10, 2) NOT NULL,
    tax_amount DECIMAL(10, 2) DEFAULT 0,
    total_amount DECIMAL(10, 2) NOT NULL,
    payment_status VARCHAR(20) NOT NULL,
    payment_date DATE,
    payment_method VARCHAR(20),
    notes TEXT,
    FOREIGN KEY (ticket_id) REFERENCES tickets(id)
);
