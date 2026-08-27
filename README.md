# Chefs Handbook

This repository contains the documentation and code for my Chefs Handbook project.

## Table of Contents

- [Documentation](#documentation)
  - [Database Schema](#database-schema)
  - [Java Classes](#java-classes)
    - [Recipes](#recipes)
    - [User](#user)
    - [Connector](#connector)
    - [Program](#program)

## Documentation

### Database Schema

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
        TIMESTAMP creationDate
    }
```

For this project, this schema is implemented in Postgresql and connected to via Java code

---

### Java Classes

#### **Recipe**

Class describing a recipe object and containing accessing methods

##### Constructors

> - Recipe(String name, String ingredients, String directions)


