drop table if exists invoices cascade;
drop table if exists materials cascade;
drop table if exists tasks cascade;
drop table if exists projects cascade;
drop table if exists customers cascade;

create table customers (
  id          bigserial primary key,
  full_name   varchar(255) not null,
  phone       varchar(50),
  email       varchar(255),
  created_at  timestamp not null default now()
);

create table projects (
  id             bigserial primary key,
  customer_id    bigint not null references customers(id) on delete cascade,
  title          varchar(255) not null,
  address        varchar(255),
  status         varchar(30) not null default 'NEW',
  scheduled_date date,
  notes          text,
  photo_url      text,
  created_at     timestamp not null default now()
);

create table materials (
  id          bigserial primary key,
  project_id  bigint not null references projects(id) on delete cascade,
  name        varchar(255) not null,
  qty         numeric(12,2) not null check (qty >= 0),
  unit_price  numeric(12,2) not null check (unit_price >= 0)
);

create table tasks (
  id          bigserial primary key,
  project_id  bigint not null references projects(id) on delete cascade,
  title       varchar(255) not null,
  done        boolean not null default false,
  sort_order  int not null default 0
);

create table invoices (
  id          bigserial primary key,
  project_id  bigint not null references projects(id) on delete cascade,
  subtotal    numeric(12,2) not null check (subtotal >= 0),
  gst         numeric(12,2) not null check (gst >= 0),
  total       numeric(12,2) not null check (total >= 0),
  status      varchar(30) not null default 'DRAFT',
  issued_at   timestamp not null default now()
);

create index if not exists idx_projects_customer_id on projects(customer_id);
create index if not exists idx_projects_title on projects(title);
create index if not exists idx_projects_status on projects(status);
create index if not exists idx_materials_project_id on materials(project_id);
create index if not exists idx_tasks_project_id on tasks(project_id);
create index if not exists idx_invoices_project_id on invoices(project_id);
