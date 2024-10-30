create table if not exists payment
(
    id            bigserial primary key,
    price         numeric(19, 2) not null check ( price > 0 ),
    bank_response varchar(255)   not null
);