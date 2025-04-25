
ALTER TABLE franchises ADD CONSTRAINT uq_franchise_name UNIQUE (name);


ALTER TABLE branches ADD CONSTRAINT uq_branch_name_franchise UNIQUE (name);

ALTER TABLE products ADD CONSTRAINT uq_product_name_branch UNIQUE (name);