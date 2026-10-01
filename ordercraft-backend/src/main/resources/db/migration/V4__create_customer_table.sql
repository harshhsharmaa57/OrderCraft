-- =====================================================
-- V4: Create Customer table
-- =====================================================

CREATE SEQUENCE oc_customers_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE oc_customer_code_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE oc_customers (
    id              NUMBER          DEFAULT oc_customers_seq.NEXTVAL PRIMARY KEY,
    customer_code   VARCHAR2(20)    NOT NULL,
    name            VARCHAR2(150)   NOT NULL,
    email           VARCHAR2(100),
    phone           VARCHAR2(20),
    address_line1   VARCHAR2(255),
    address_line2   VARCHAR2(255),
    city            VARCHAR2(100),
    state           VARCHAR2(100),
    postal_code     VARCHAR2(20),
    country         VARCHAR2(100)   DEFAULT 'India',
    is_active       NUMBER(1)       DEFAULT 1,
    created_by      NUMBER,
    created_at      TIMESTAMP       DEFAULT SYSTIMESTAMP,
    updated_by      NUMBER,
    updated_at      TIMESTAMP,
    CONSTRAINT uk_oc_customers_code UNIQUE (customer_code),
    CONSTRAINT fk_oc_customers_created_by FOREIGN KEY (created_by) REFERENCES oc_users(id),
    CONSTRAINT fk_oc_customers_updated_by FOREIGN KEY (updated_by) REFERENCES oc_users(id)
);

CREATE INDEX idx_oc_customers_name   ON oc_customers(name);
CREATE INDEX idx_oc_customers_city   ON oc_customers(city);
CREATE INDEX idx_oc_customers_active ON oc_customers(is_active);
CREATE INDEX idx_oc_customers_email  ON oc_customers(email);
CREATE INDEX idx_oc_customers_phone  ON oc_customers(phone);