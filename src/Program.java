import org.mindrot.jbcrypt.BCrypt;
import org.postgresql.util.PSQLException;

import java.sql.*;

import java.util.ArrayList;

/******************************************************************************

 File        : Program.java

 Date        : Wednesday 26th August 2026

 Author      : Tom Melton

 Description : Program which manages users, recipes, and user interactions
               by interacting with the database

 History     : 26/08/2026 - v1.00

 ******************************************************************************/


public class Program
{
    User user;
    ArrayList<Recipe> recipes;

    // Database connection
    Connection conn = attemptConn();


    public boolean login(String username, String password) throws SQLException
    {
        // Attempts to log in a user
        // Returns true if successful, false if failed (either username or password is incorrect)

        // fetch rows from users table with this username
        try (ResultSet userResult = userDetails(username))
        {
            // If username exists
            if (userResult.next())
            {
                String queryID, queryPassword;

                queryID = userResult.getString("userID");
                queryPassword = userResult.getString("password");

                if (checkPassword(password, queryPassword))
                {
                    this.user = new User(username, queryID);
                    this.recipes = compileRecipes(userRecipes(username));

                    return true;
                }
            }
            // username or password is incorrect
            return false;
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }


    public boolean register(String username, String password) throws SQLException
    {
        // Attempt to add a new user to the database
        // username must be unique
        // Will return true if successful, false if username already exists

        String sql = "INSERT INTO users(username, password) VALUES(?, ?)";

        try (PreparedStatement statement = conn.prepareStatement(sql))
        {
            statement.setString(1, username);
            statement.setString(2, hash(password));

            statement.executeUpdate();

            return true;
        }
        catch (PSQLException e)
        {
            // Check for unique constraint violation
            if ("23505".equals(e.getSQLState()))
            {
                // Username already exists
                return false;
            }
            throw new RuntimeException(e);
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }


    public boolean createRecipe(String name, String ingredients, String directions) throws SQLException
    {
        // Creates a new recipe object and adds it to the database and arraylist
        // Returns true if user exists and creation is successful, false if not

        // Check user is logged in
        if (this.user != null)
        {
            // Insert recipe into database using current user
            insertRecipe(name, ingredients, directions);

            // Replace with new list
            this.recipes = compileRecipes(userRecipes(this.user.getUsername()));

            return true;
        }
        return false;

    }


    private void insertRecipe(String name, String ingredients, String directions) throws SQLException
    {
        // method to insert recipe object into the database

        int userID = Integer.parseInt(this.user.getId());

        String sql =
                """
                INSERT INTO recipes (userID, name, ingredients, directions)
                VALUES(?, ?, ?, ?)
                """;

       try( PreparedStatement statement = conn.prepareStatement(sql))
       {
           statement.setInt(1, userID);
           statement.setString(2, name);
           statement.setString(3, ingredients);
           statement.setString(4, directions);

           statement.executeUpdate();
       }
       catch (SQLException e)
       {
           throw new RuntimeException(e);
       }
    }


    private Connection attemptConn()
    {
        // Attempts to connect to the database using Connector
        try
        {
            return Connector.getConnection();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }


    private ResultSet userRecipes(String username) throws SQLException
    {
        // Selects a users recipes from the database

        String sql =
            """
            SELECT r.*
            FROM recipes AS r
            JOIN users AS u
            ON r.userID = u.userID
            AND username = ?
            """;

        PreparedStatement statement = conn.prepareStatement(sql);

        statement.setString(1, username);

        return  statement.executeQuery();
    }


    private ArrayList<Recipe> compileRecipes(ResultSet recipeResults) throws SQLException
    {
        // Compiles a resultSet of recipes into recipe objects

        String name, ingredients, directions;
        int id;
        ArrayList<Recipe> recipes = new ArrayList<>();

        while(recipeResults.next())
        {
            id = recipeResults.getInt("recipeID");
            name = recipeResults.getString("name");
            ingredients = recipeResults.getString("ingredients");
            directions = recipeResults.getString("directions");

            recipes.add(new Recipe(id, name, ingredients, directions));
        }
        return recipes;
    }


    private ResultSet userDetails(String username) throws SQLException
    {
        String sql = "SELECT userID, username, password FROM users WHERE username = ?";

        PreparedStatement statement = conn.prepareStatement(sql);

        statement.setString(1, username);

        return statement.executeQuery();
    }


    private String hash(String password)
    {
        // Function to hash a password, to be used before entry to database
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }


    private boolean checkPassword(String plainTextPassword, String hashedPassword)
    {
        // compares the plain text password to its hashed counterpart
        // Returns true if the arguments match via the method
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }



    public static void main(String[] args) throws SQLException
    {
        // testing details:
        // username = "Tom"
        // password = "password"

        Program p = new Program();


        String password = "password";



        p.register("Tom", "password");

        if (p.login("Tom", password))
        {
            System.out.println("Logged in!");

            System.out.println(p.user);

            System.out.println(p.recipes);
        }
        else
        {
            System.out.println("Username or password incorrect");
        }

//        p.createRecipe(
//                "Toffee Sauce",
//                "4 packs of butter\n100.5g caster sugar\ngolden syrup\n100ml double cream",
//                "1. heat butter, sugar, syrup in a pan on low heat until combined\n2. take off heat and add cream\n3. strain once cooled"
//        );

//        System.out.println(p.recipes);


    }
}
