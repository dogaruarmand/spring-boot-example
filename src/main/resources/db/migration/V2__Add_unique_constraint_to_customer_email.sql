alter table customer
add CONSTRAINT customer_email_unique UNIQUE (email);
