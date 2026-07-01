# BridgeLabz Training Database

A comprehensive repository containing Java database projects and SQL/DBMS practice exercises.

This repository showcases hands-on learning of Database Management Systems (DBMS), SQL, PostgreSQL, JDBC, and backend application development using Java. It includes real-world console applications, raw JDBC & Spring JdbcTemplate projects, and SQL/DBMS practice queries.

## 📌 Overview

This repository is organized into two major sections, spread across branches:

- Database JDBC Projects
- SQL / DBMS Practice

### Objectives

- Build a strong foundation in DBMS concepts
- Learn SQL and PostgreSQL
- Understand JDBC architecture and connectivity
- Compare raw JDBC vs Spring `JdbcTemplate`
- Perform CRUD operations
- Design relational databases with constraints, triggers, and stored procedures
- Develop backend database applications with role-based access

## 📂 Repository Structure

Since each project was built during a different stage of training, they are organized as separate branches instead of folders:

```
BridgeLabz-Training-Database
│
├── main                                          → Overview (this README)
├── dbms                                          → SQL / DBMS Practice Queries
├── jdbc-raw-project                              → Greeting JDBC App (raw JDBC)
├── jdbc-template-project                         → Greeting JDBC Template App (Spring JdbcTemplate)
├── jdbc-raw-employee-payroll-project             → Employee Payroll Application (raw JDBC)
└── jbbc-template-employee-payroll-project        → Employee Payroll Application (Spring JdbcTemplate + Maven)
```

## 💻 Database JDBC Projects

### 1. Employee Payroll Application (`jdbc-raw-employee-payroll-project`)

A Java and JDBC-based payroll management console system.

**Features**
- Employee Management (Add / View / Edit / Delete)
- Department-wise Payroll Calculation
- Role-Based Authentication (Admin / User)
- Password Hashing
- Audit Logging of Salary Changes (via PostgreSQL trigger)

**Technologies**
- Java
- Raw JDBC
- PostgreSQL
- SQL

### 2. Employee Payroll Application – Maven (`jbbc-template-employee-payroll-project`)

A Maven + Spring `JdbcTemplate` version of the payroll application with an improved, layered project structure.

**Features**
- Employee Management
- Payroll Processing
- Role-Based Access
- Repository Layer (`EmployeeRepository`, `UserRepository`)
- Externalized Configuration (`application.properties`, `AppConfig`)

**Technologies**
- Java
- Maven
- Spring JdbcTemplate
- PostgreSQL

### 3. Greeting JDBC App (`jdbc-raw-project`)

A simple Java console application demonstrating raw JDBC connectivity and CRUD operations.

**Features**
- Database Connectivity via `DBUtil`
- User Registration / Login
- Greeting CRUD Operations
- Audit Trigger + Stored Function on Greetings

### 4. Greeting JDBC Template App (`jdbc-template-project`)

Greeting management application rebuilt using Spring `JdbcTemplate`.

**Features**
- CRUD Operations
- Spring JdbcTemplate
- PostgreSQL Integration
- Repository Layer + Basic Unit Test (`AppTest`)

**Technologies**
- Spring JdbcTemplate
- Maven
- PostgreSQL

## 🗄️ SQL / DBMS Practice (`dbms` branch)

This branch contains SQL practice queries and database design exercises.

**SQL Commands**
- **DDL**: `CREATE`, `ALTER`, `DROP`, `TRUNCATE`
- **DML**: `INSERT`, `UPDATE`, `DELETE`
- **DQL**: `SELECT`, `WHERE`, `ORDER BY`, `GROUP BY`, `HAVING`

**Joins**
- `INNER JOIN`
- `LEFT JOIN`
- `RIGHT JOIN`
- `FULL JOIN`

**Keys & Constraints**
- Primary Key
- Foreign Key
- Unique Key
- `NOT NULL`
- `CHECK` Constraint

**Advanced Database Concepts**
- Multiple Table Queries
- Stored Procedures
- Triggers
- Review Questions / Practice Sets

## 🛠️ Technologies Used

- Java
- JDBC
- PostgreSQL
- SQL
- DBMS
- Maven
- Spring JdbcTemplate
- IntelliJ IDEA

## 🎯 Learning Outcomes

- Understand relational database concepts
- Design normalized, constraint-driven databases
- Write efficient SQL queries, joins, and stored procedures
- Connect Java applications with PostgreSQL
- Implement CRUD operations using both raw JDBC and Spring JdbcTemplate
- Build role-based, audit-logged backend applications

## 🚀 Getting Started

### Clone the Repository

```bash
git clone https://github.com/Swechchha07/BridgeLabz-Training-Database.git
```

### Switch to the Branch You Want

```bash
git checkout <branch-name>
```

### Create a Database

```sql
CREATE DATABASE project_db;
```

Run the `schema.sql` file for the respective project to set up tables, triggers, and stored procedures.

### Configure Database Credentials

Update the connection details in `DBUtil` (raw JDBC projects) or `application.properties` (Spring JdbcTemplate projects):

```
URL=jdbc:postgresql://localhost:5432/project_db
USER=postgres
PASSWORD=your_password
```

### Run Java Projects

For raw JDBC projects, compile and run the main class directly, e.g.:

```bash
javac payroll/PayrollApp.java
java payroll.PayrollApp
```

For Maven / Spring JdbcTemplate projects:

```bash
mvn clean install
mvn exec:java
```

## 📚 Key Concepts Covered

- Database Management System (DBMS)
- Relational Database Design
- SQL Queries
- PostgreSQL
- JDBC Architecture (Raw JDBC & Spring JdbcTemplate)
- CRUD Operations
- Connection Management
- Role-Based Authentication
- Stored Procedures & Functions
- Triggers & Audit Logging

## 👩‍💻 Author

**Swechchha**
