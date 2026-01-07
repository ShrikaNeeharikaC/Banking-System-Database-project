Mini Banking System Project Overview

The Mini Banking System project involves a Java application that is created with the Maven build tool, Hibernate ORM, and JavaFX. This is a banking system that is designed to simulate all aspects of banking, including managing customers, accounts, and transactions using a Layered Architecture with DAO Pattern for Data Access Object.

The purpose of this project is educational, allowing students to learn about Enterprise Java Concepts, including ORM and the MVC (Model View Controller) style separation of the application and the Database Driven application development process.

Objectives

To implement a simple banking system in Java.

To understand the DAO Design Pattern.

To use Hibernate to interact with the Database.

To create a modular and maintainable application.

To learn about project structure and management using Maven.

Technologies Used

- Language: Java
- Build Tool: Maven
- ORM Framework: Hibernate
- UI Framework: JavaFX (FXML based UI)
- Architecture: DAO + MVC style separation
- IDE: IntelliJ IDEA / Eclipse / VS Code

Project Structure:
MINI_BANKING
│── pom.xml
│
├── src/main/java/com/minibanking
│ ├── Main.java
│ ├── App.java
│ │
│ ├── dao
│ │ ├── AccountDao.java
│ │ ├── AccountDaoImpl.java
│ │ ├── CustomerDao.java
│ │ ├── CustomerDaoImpl.java
│ │ ├── TransactionDao.java
│ │ └── TransactionDaoImpl.java
│ │
│ ├── model
│ │ ├── Account.java
│ │ ├── Customer.java
│ │ └── Transaction.java
│ │
│ ├── util
│ │ └── HibernateUtil.java
│ │
│ └── view
│ ├── MainView.fxml
│ └── MainViewController.java
│
└── target
└── compiled classes

Client Management

- Adding new clients to the system
- Storing client data in a database using Hibernate ORM


Account Administrations

- Creating bank accounts for clients
- Processing of account balances
- Accessing clients' accounts


Transaction Tracking & Management

- Recording of transactions (adding money or withdrawing money)
- Storing of transaction information in a database
- Maintenance of transaction/account relationship


Technical Components

- Database Access Object (DAO) pattern for data operations
- Hibernate Session handling via HibernateUtil
- JavaFX User Interface via FXML files
- Complete separation of Model, DAO, and View Layers
