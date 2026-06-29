
-- EMPLOYEE PAYROLL DATABASE SCHEMA


-- Drop tables if already exist

DROP TABLE IF EXISTS employee_departments CASCADE;
DROP TABLE IF EXISTS employees CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS payroll_audit CASCADE;


-- USERS TABLE
CREATE TABLE users
(
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(64) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    role VARCHAR(20) NOT NULL
        CHECK(role IN ('ADMIN','USER')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


-- EMPLOYEES TABLE
CREATE TABLE employees
(
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,

    profile_image VARCHAR(100) NOT NULL,

    gender VARCHAR(10)
        CHECK(gender IN ('Male','Female')),

    salary NUMERIC(10,2)
        CHECK(salary>=0),

    start_date DATE NOT NULL,

    notes TEXT,

    created_by INTEGER,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_created_by
        FOREIGN KEY(created_by)
            REFERENCES users(id)
            ON DELETE SET NULL
);


-- EMPLOYEE DEPARTMENTS TABLE
CREATE TABLE employee_departments
(
    employee_id INTEGER,

    department VARCHAR(50),

    PRIMARY KEY(employee_id,department),

    CONSTRAINT fk_employee
        FOREIGN KEY(employee_id)
            REFERENCES employees(id)
            ON DELETE CASCADE
);


-- PAYROLL AUDIT
CREATE TABLE payroll_audit
(
    id SERIAL PRIMARY KEY,

    employee_id INTEGER NOT NULL,

    action_type VARCHAR(10) NOT NULL,

    old_salary NUMERIC(10,2),

    new_salary NUMERIC(10,2),

    changed_by VARCHAR(50),

    changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


-- INDEXES

CREATE UNIQUE INDEX idx_users_username
    ON users(username);

CREATE INDEX idx_emp_department
    ON employee_departments(employee_id);


-- TRIGGER FUNCTION
CREATE OR REPLACE FUNCTION log_salary_change()

RETURNS TRIGGER AS
$$

BEGIN

IF(TG_OP='INSERT') THEN

INSERT INTO payroll_audit
(employee_id,action_type,old_salary,new_salary,changed_by)

VALUES
(NEW.id,'INSERT',NULL,NEW.salary,CURRENT_USER);

RETURN NEW;

ELSIF(TG_OP='UPDATE') THEN

IF OLD.salary<>NEW.salary THEN

INSERT INTO payroll_audit(employee_id,action_type,old_salary,new_salary,changed_by)

VALUES(NEW.id,'UPDATE',OLD.salary,NEW.salary,CURRENT_USER);

END IF;

RETURN NEW;

ELSIF(TG_OP='DELETE') THEN

INSERT INTO payroll_audit(employee_id,action_type,old_salary,new_salary,changed_by)

VALUES(OLD.id,'DELETE',OLD.salary,NULL,CURRENT_USER);

RETURN OLD;

END IF;

RETURN NULL;

END;

$$
LANGUAGE plpgsql;


-- TRIGGER
CREATE TRIGGER trg_log_salary

    AFTER INSERT
        OR UPDATE OR DELETE

           ON employees

               FOR EACH ROW

               EXECUTE FUNCTION log_salary_change();


-- STORED FUNCTION
CREATE OR REPLACE FUNCTION
get_total_payroll_by_dept(p_dept VARCHAR)
RETURNS NUMERIC
AS
$$

BEGIN

RETURN
    (
        SELECT COALESCE(SUM(e.salary),0)

        FROM employees e

        JOIN employee_departments d

       ON e.id=d.employee_id

        WHERE d.department=p_dept
    );

END;

$$

LANGUAGE plpgsql;


-- SEED USERS
INSERT INTO users(username,password,email,role)
VALUES

    (
        'admin',
        '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918',
        'admin@gmail.com',
        'ADMIN'
    ),

    (
        'user',
        '04f8996da763b7a969b1028ee3007569eaf3a635486ddab211d512c85b9df8fb',
        'user@gmail.com',
        'USER'
    );


-- SEED EMPLOYEE
INSERT INTO employees(name,profile_image,gender,salary,start_date,notes,created_by)
VALUES

    (
        'Amarpa Keerthi Kumar',
        'ellipse-1.png',
        'Female',
        10000,
        '2019-10-29',
        'Senior specialist account manager',
        1
    );


-- SEED DEPARTMENTS
INSERT INTO employee_departments

VALUES

    (1,'Sales'),

    (1,'HR'),

    (1,'Finance');