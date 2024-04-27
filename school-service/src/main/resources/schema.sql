--create schema if not exists school-schema;
create extension if not exists "uuid-ossp";

create table if not exists school (
    id uuid primary key default uuid_generate_v4(),
    name text not null,
    created_at timestamp not null,
    updated_at timestamp not null default current_timestamp
);