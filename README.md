Monthly Payment Calculator Backend

DB Table (MariaDB)

create table user
(
    id         bigint auto_increment
        primary key,
    username   varchar(50)                             not null,
    password   varchar(255)                            not null,
    role       varchar(20) default 'USER'              not null,
    created_at timestamp   default current_timestamp() not null,
    constraint username
        unique (username)
);

create table payment
(
    id                 bigint auto_increment
        primary key,
    product_name       varchar(255)                                   not null,
    total_price        decimal(10, 2)                                 null,
    monthly_rate       decimal(10, 2)                                 not null,
    remaining_months   int                                            not null,
    is_rate_adjustable tinyint(1) default 0                           null,
    loan_type          enum ('ZERO_INTEREST', 'PERSONAL', 'MORTGAGE') not null,
    user_id            bigint                                         not null,
    created_at         timestamp  default current_timestamp()         not null,
    constraint unique_product_name
        unique (product_name),
    constraint payments_FK
        foreign key (user_id) references user (id)
);
