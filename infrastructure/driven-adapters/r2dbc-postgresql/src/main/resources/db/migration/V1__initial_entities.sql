
CREATE TABLE franchises (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE branches (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    franchise_id BIGINT NOT NULL,
    CONSTRAINT fk_franchise
        FOREIGN KEY (franchise_id)
        REFERENCES franchises (id)
        ON DELETE CASCADE
);

CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    stock INTEGER NOT NULL DEFAULT 0,
    branch_id BIGINT NOT NULL,
    CONSTRAINT fk_branch
        FOREIGN KEY (branch_id)
        REFERENCES branches (id)
        ON DELETE CASCADE
);

CREATE INDEX idx_branches_franchise_id ON branches (franchise_id);
CREATE INDEX idx_products_branch_id ON products (branch_id);