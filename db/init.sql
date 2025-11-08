-- =========================
--  CABINETPRO-LITE SCHEMA
--  Postgres 15+
-- =========================

-- ⚠️ برای ریست کامل دیتابیس (همه‌چیز پاک می‌شود) این ۶ خط را از کامنت در بیاور:
 DROP TABLE IF EXISTS quote_lines     CASCADE;
 DROP TABLE IF EXISTS quotes          CASCADE;
 DROP TABLE IF EXISTS invoices        CASCADE;
 DROP TABLE IF EXISTS project_items   CASCADE;
 DROP TABLE IF EXISTS materials       CASCADE;
 DROP TABLE IF EXISTS tasks           CASCADE;
 DROP TABLE IF EXISTS projects        CASCADE;
 DROP TABLE IF EXISTS customers       CASCADE;
 DROP TABLE IF EXISTS invoice_seq     CASCADE;

-- ==============
--  customers
-- ==============
CREATE TABLE IF NOT EXISTS customers (
  id          BIGSERIAL PRIMARY KEY,
  full_name   VARCHAR(140) NOT NULL,
  phone       VARCHAR(40),
  email       VARCHAR(120),
  created_at  TIMESTAMP DEFAULT now()
);

-- برای سرچ نام (case-insensitive)
CREATE INDEX IF NOT EXISTS idx_customers_full_name_lower
  ON customers (LOWER(full_name));


-- ==============
--  projects
-- ==============
CREATE TABLE IF NOT EXISTS projects (
  id             BIGSERIAL PRIMARY KEY,
  customer_id    BIGINT NOT NULL REFERENCES customers(id) ON DELETE CASCADE,
  title          VARCHAR(140) NOT NULL,
  address        VARCHAR(2000),
  status         VARCHAR(20) NOT NULL DEFAULT 'NEW',
  scheduled_date DATE,
  created_at     TIMESTAMP DEFAULT now(),
  CONSTRAINT chk_projects_status
    CHECK (status IN ('NEW','MEASURED','SCHEDULED','IN_PROGRESS','DONE','CANCELLED'))
);

CREATE INDEX IF NOT EXISTS idx_projects_customer_id ON projects(customer_id);


-- ==============
--  materials  (برای Invoice از روی مواد)
-- ==============
CREATE TABLE IF NOT EXISTS materials (
  id          BIGSERIAL PRIMARY KEY,
  project_id  BIGINT NOT NULL REFERENCES projects(id) ON DELETE CASCADE,
  name        VARCHAR(240) NOT NULL,
  qty         NUMERIC(12,3) NOT NULL CHECK (qty >= 0),
  unit_price  NUMERIC(12,2) NOT NULL CHECK (unit_price >= 0),
  created_at  TIMESTAMP DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_materials_project_id ON materials(project_id);


-- ==============
--  tasks  (تسک‌های پروژه)
-- ==============
CREATE TABLE IF NOT EXISTS tasks (
  id          BIGSERIAL PRIMARY KEY,
  project_id  BIGINT NOT NULL REFERENCES projects(id) ON DELETE CASCADE,
  title       VARCHAR(240) NOT NULL,
  done        BOOLEAN NOT NULL DEFAULT FALSE,
  sort_order  INT NOT NULL DEFAULT 0,
  created_at  TIMESTAMP DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_tasks_project_id ON tasks(project_id);


-- ==============
--  project_items (آیتم‌های خطی برای کوُت؛ جدا از materials)
-- ==============
CREATE TABLE IF NOT EXISTS project_items (
  id           BIGSERIAL PRIMARY KEY,
  project_id   BIGINT NOT NULL REFERENCES projects(id) ON DELETE CASCADE,
  description  VARCHAR(240) NOT NULL,
  qty          NUMERIC(12,3) NOT NULL CHECK (qty >= 0),
  unit_price   NUMERIC(12,2) NOT NULL CHECK (unit_price >= 0),
  created_at   TIMESTAMP DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_project_items_project_id ON project_items(project_id);


-- ==============
--  quotes  /  quote_lines  (snapshot خطوط)
-- ==============
CREATE TABLE IF NOT EXISTS quotes (
  id         BIGSERIAL PRIMARY KEY,
  project_id BIGINT NOT NULL REFERENCES projects(id) ON DELETE CASCADE,
  status     VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
  subtotal   NUMERIC(12,2) NOT NULL DEFAULT 0,
  gst        NUMERIC(12,2) NOT NULL DEFAULT 0,
  total      NUMERIC(12,2) NOT NULL DEFAULT 0,
  gst_rate   NUMERIC(4,3)  NOT NULL DEFAULT 0.100, -- 10%
  created_at TIMESTAMP DEFAULT now(),
  CONSTRAINT chk_quotes_status
    CHECK (status IN ('DRAFT','APPROVED','SENT','REJECTED','VOID'))
);

CREATE INDEX IF NOT EXISTS idx_quotes_project_id ON quotes(project_id);

CREATE TABLE IF NOT EXISTS quote_lines (
  id           BIGSERIAL PRIMARY KEY,
  quote_id     BIGINT NOT NULL REFERENCES quotes(id) ON DELETE CASCADE,
  description  VARCHAR(240) NOT NULL,
  qty          NUMERIC(12,3) NOT NULL CHECK (qty >= 0),
  unit_price   NUMERIC(12,2) NOT NULL CHECK (unit_price >= 0),
  line_total   NUMERIC(12,2) NOT NULL,
  created_at   TIMESTAMP DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_quote_lines_quote_id ON quote_lines(quote_id);


-- ==============
--  invoices  (فاکتور)
-- ==============
CREATE TABLE IF NOT EXISTS invoices (
  id              BIGSERIAL PRIMARY KEY,
  project_id      BIGINT NOT NULL REFERENCES projects(id) ON DELETE CASCADE,
  subtotal        NUMERIC(12,2) NOT NULL DEFAULT 0,
  gst             NUMERIC(12,2) NOT NULL DEFAULT 0,
  total           NUMERIC(12,2) NOT NULL DEFAULT 0,
  status          VARCHAR(20)   NOT NULL DEFAULT 'DRAFT',
  issued_at       TIMESTAMP,
  invoice_number  VARCHAR(32),
  created_at      TIMESTAMP DEFAULT now(),
  CONSTRAINT chk_invoices_status
    CHECK (status IN ('DRAFT','ISSUED','PAID','VOID'))
);

CREATE INDEX IF NOT EXISTS idx_invoices_project_id ON invoices(project_id);

-- شماره فاکتور یکتا فقط وقتی مقدار دارد (ISSUED)
CREATE UNIQUE INDEX IF NOT EXISTS ux_invoices_invoice_number
  ON invoices(invoice_number) WHERE invoice_number IS NOT NULL;


-- ==============
--  invoice_seq (سکانس سادهٔ سالانه برای شماره فاکتور)
-- ==============
CREATE TABLE IF NOT EXISTS invoice_seq (
  y INT PRIMARY KEY,
  n BIGINT NOT NULL
);

-- مقدار اولیه برای سال جاری
INSERT INTO invoice_seq (y, n)
VALUES (EXTRACT(YEAR FROM now())::INT, 0)
ON CONFLICT (y) DO NOTHING;
