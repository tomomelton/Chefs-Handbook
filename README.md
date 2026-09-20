# Chefs Handbook

## A functional Java project using Maven and JavaFX

This project is a hobby that has been worked on while learning JavaFX and Maven, and to practice using PostgreSQL within Java.

The end product is a system which allows different users to store and access recipes via a visually appealing and accessible GUI

## Table of Contents

- [Documentation](#documentation)
  - [Project Diagram](#project-diagram)
  - [Directory Structure](#directory-structure)
  - [Database Schema](#database-schema)

## Project Diagram

<img width="4132" height="6232" alt="diagram" src="https://github.com/user-attachments/assets/359e0bef-499a-4548-ac22-ed1e1f4f04cb" />


## Directory Structure

```
Directory structure:
└── tomomelton-chefs-handbook/
    ├── README.md
    ├── chefs-handbook-DDL.sql
    ├── pom.xml
    └── src/
        └── main/
            ├── java/
            │   ├── Main.java
            │   ├── database/
            │   │   ├── DatabaseConnection.java
            │   │   ├── RecipeDAO.java
            │   │   └── UserDAO.java
            │   ├── gui/
            │   │   ├── AlertBox.java
            │   │   ├── ConfirmationBox.java
            │   │   ├── HomeWindow.java
            │   │   ├── LoginWindow.java
            │   │   ├── RecipeEditor.java
            │   │   ├── RecipeLayout.java
            │   │   ├── RecipeListCell.java
            │   │   ├── RegisterWindow.java
            │   │   └── StartWindow.java
            │   ├── models/
            │   │   ├── Recipe.java
            │   │   └── User.java
            │   └── utils/
            │       ├── FileHandling.java
            │       └── Hash.java
            └── resources/
                └── styles/
                    └── main.css
```


## Database Schema

This is the current database schema as of this version:

``` mermaid
erDiagram
    direction LR
    users ||--o{ recipes : ""

    users {
        SERIAL userID PK
        VARCHAR username UK
        VARCHAR password
        DATE joinDate
    }

    recipes {
        SERIAL recipeID PK
        SERIAL userID FK
        VARCHAR ingredients
        VARCHAR directions
        NUMERIC servingSize
        TIMESTAMP creationDate
    }
```
