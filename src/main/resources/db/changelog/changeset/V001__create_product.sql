create table if not exists product
(
    id          bigserial primary key,
    name        varchar(255)   not null,
    description varchar(512)   not null,
    stock_count int            not null check ( stock_count >= 0 ),
    price       numeric(19, 2) not null check ( price > 0 ),
    version     bigint         not null
);