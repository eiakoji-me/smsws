--create schema if not exists course-schema;

create table if not exists course (
    id serial primary key,
    name text,
    department varchar(255),
    credits int,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp,
    created_by varchar(50) default 'System',
    updated_by varchar(50) default 'System'
);

CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS '
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
' LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER course_updated_at_trigger
    BEFORE UPDATE ON course
    FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();