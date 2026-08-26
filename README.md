# Chefs Handbook

## Table of Contents

- [Documentation](#documentation)
  - [Database](#database)



## Documentation

### Database

``` mermaid
erDiagram
    users ||--o{ recipes : creates

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
