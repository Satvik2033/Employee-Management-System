
# Employee Management System (Scala)

A simple console-based Employee Management System built with Scala and JDBC. This project allows you to manage employee records in a MySQL database using basic CRUD operations via a menu-driven interface.

## 💼 Features

- Add new employees
- View all employee records
- Update employee details
- Delete employee records
- MySQL database integration via JDBC

## 🛠️ Tech Stack

- Scala
- JDBC (Java Database Connectivity)
- MySQL
- sbt (Scala Build Tool)

## 🚀 Getting Started

### Prerequisites

- [Scala](https://www.scala-lang.org/download/)
- [sbt](https://www.scala-sbt.org/download.html)
- [MySQL](https://www.mysql.com/)
- MySQL Connector/J (add to `lib` or sbt dependencies)

### 1. Clone the Repository

```bash
git clone https://github.com/Satvik2033/Employee-Management-System.git
cd Employee-Management-System
````

### 2. Set Up MySQL Database

Run the following SQL script:

```sql
CREATE DATABASE employee_db;

USE employee_db;

CREATE TABLE employees (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100),
    department VARCHAR(100),
    salary DOUBLE
);
```

### 3. Configure Database Connection

In your Scala code, update the JDBC configuration:

```scala
val url = "jdbc:mysql://localhost:3306/employee_db"
val username = "your_mysql_username"
val password = "your_mysql_password"
```

### 4. Run the Project

```bash
sbt run
```

> If you're not using `sbt`, compile with `scalac` and run using `scala` manually.

## 📂 Project Structure

```
.
├── src/
│   └── main/
│       └── scala/
│           ├── models/Employee.scala        # Case class for Employee
│           ├── dao/EmployeeDAO.scala        # JDBC operations
│           ├── services/EmployeeService.scala # Business logic
│           └── Main.scala                   # CLI interface
└── build.sbt                                # SBT project file
```

## 🔧 Future Improvements

* Migrate to a web-based UI (Play Framework or Akka HTTP)
* Add unit tests using ScalaTest
* Advanced search and filtering
* Authentication system

---

Made with 💻 and ☕ by [Satvik Gupta](https://github.com/Satvik2033)

