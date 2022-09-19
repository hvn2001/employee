create table employee (
    id uuid not null primary key ,
    name varchar not null ,
    supervisor varchar null ,
    supervisor_id INT null
)