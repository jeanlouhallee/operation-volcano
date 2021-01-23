create table reservations (
    reservation_id uuid not null primary key,
    check_in date not null,
    check_out date not null,
    first_name varchar(255) not null,
    last_name varchar(255) not null,
    email varchar(255) not null
);