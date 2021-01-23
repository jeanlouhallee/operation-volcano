create table reservations (
    reservation_id uuid not null primary key,
    check_in date not null unique,
    check_out date not null unique,
    first_name varchar(255) not null,
    last_name varchar(255) not null,
    email varchar(255) not null
);