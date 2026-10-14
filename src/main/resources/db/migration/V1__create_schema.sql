CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       full_name VARCHAR(255) NOT NULL,
                       created_at TIMESTAMP NOT NULL
);

CREATE TABLE categories (
                            id BIGSERIAL PRIMARY KEY,
                            name VARCHAR(255) NOT NULL,
                            user_id BIGINT NOT NULL REFERENCES users(id)
);

CREATE TABLE expenses (
                          id BIGSERIAL PRIMARY KEY,
                          amount NUMERIC(12,2) NOT NULL,
                          description VARCHAR(255),
                          date DATE NOT NULL,
                          category_id BIGINT NOT NULL REFERENCES categories(id),
                          user_id BIGINT NOT NULL REFERENCES users(id)
);

CREATE TABLE budgets (
                         id BIGSERIAL PRIMARY KEY,
                         monthly_limit NUMERIC(12,2) NOT NULL,
                         month VARCHAR(7) NOT NULL,
                         category_id BIGINT NOT NULL REFERENCES categories(id),
                         user_id BIGINT NOT NULL REFERENCES users(id),
                         CONSTRAINT uq_budget_category_month UNIQUE (category_id, month)
);