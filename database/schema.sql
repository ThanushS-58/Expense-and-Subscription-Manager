CREATE DATABASE IF NOT EXISTS expense_manager;
USE expense_manager;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(160) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS expenses (
    id BIGINT NOT NULL AUTO_INCREMENT,
    amount DECIMAL(12, 2) NOT NULL,
    category VARCHAR(24) NOT NULL,
    description VARCHAR(255),
    payment_method VARCHAR(24) NOT NULL,
    expense_date DATE NOT NULL,
    user_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_expenses_user FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS subscriptions (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    amount DECIMAL(12, 2) NOT NULL,
    billing_cycle VARCHAR(16) NOT NULL,
    category VARCHAR(50) NOT NULL,
    next_billing_date DATE NOT NULL,
    status VARCHAR(16) NOT NULL,
    user_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_subscriptions_user FOREIGN KEY (user_id) REFERENCES users (id)
);