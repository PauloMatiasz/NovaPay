CREATE TABLE customers (
  id UUID PRIMARY KEY,
  full_name VARCHAR(120) NOT NULL,
  cpf VARCHAR(11) NOT NULL UNIQUE,
  email VARCHAR(254) NOT NULL UNIQUE,
  password_hash VARCHAR(100) NOT NULL,
  status VARCHAR(20) NOT NULL,
  created_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE accounts (
  id UUID PRIMARY KEY,
  customer_id UUID NOT NULL REFERENCES customers(id),
  number VARCHAR(12) NOT NULL UNIQUE,
  balance NUMERIC(19, 2) NOT NULL CHECK (balance >= 0),
  status VARCHAR(20) NOT NULL,
  version BIGINT NOT NULL DEFAULT 0
);

CREATE INDEX idx_accounts_customer_id ON accounts (customer_id);
CREATE SEQUENCE account_number_seq START WITH 1;
