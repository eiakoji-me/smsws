--create schema if not exists student-schema;

create extension if not exists "uuid-ossp";

create table if not exists student (
   id uuid primary key default uuid_generate_v4(),
   first_name varchar(100),
   last_name varchar(100),
   date_of_birth date,
   gender varchar(10),
   address text,
   email varchar(255),
   created_at timestamp default current_timestamp,
   updated_at timestamp default current_timestamp,
   created_by varchar(50) default 'System',
   updated_by varchar(50) default 'System'
);

create or replace function update_updated_at_column()
    returns trigger as '
    begin
        new.updated_at = current_timestamp;
        return new;
    end;
' language plpgsql;

create or replace trigger student_updated_at_trigger
    before update on student
    for each row
execute function update_updated_at_column();