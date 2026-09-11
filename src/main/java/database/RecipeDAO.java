package database;

import models.Recipe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static database.DatabaseConnection.getConnection;

/******************************************************************************

 File        : database.RecipeDAO.java

 Date        : Tuesday 1st September 2026

 Author      : Tom Melton

 Description : Data Access Object class which contains methods which insert or
               select from the Recipe table

 History     : 01/09/2026 - v1.00

 ******************************************************************************/

public class RecipeDAO
{
    private static final Connection conn = getConnection();

    public static void insertRecipe(int userID, Recipe recipe)
    {
        // method to insert recipe object into the database

        String name, ingredients, directions;
        double servingSize;

        recipe.resetMultiplier();

        name = recipe.getName();
        ingredients = recipe.getIngredients();
        directions = recipe.getDirections();
        servingSize = recipe.getServingSize();


        String sql =
                """
                INSERT INTO recipes (userID, name, ingredients, directions, servingSize)
                VALUES(?, ?, ?, ?, ?)
                """;

        try( PreparedStatement statement = conn.prepareStatement(sql))
        {
            statement.setInt(1, userID);
            statement.setString(2, name);
            statement.setString(3, ingredients);
            statement.setString(4, directions);
            statement.setDouble(5, servingSize);

            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static void updateRecipe(Recipe recipe)
    {
        // method to insert recipe object into the database

        String name, ingredients, directions;
        double servingSize;
        int recipeID;

        recipe.resetMultiplier();

        name = recipe.getName();
        ingredients = recipe.getIngredients();
        directions = recipe.getDirections();
        servingSize = recipe.getServingSize();
        recipeID = recipe.getRecipeID();


        String sql =
                """
                UPDATE recipes
                SET (name, ingredients, directions, servingSize) = (?, ?, ?, ?)
                WHERE recipeid = ?;
                """;

        try( PreparedStatement statement = conn.prepareStatement(sql))
        {
            statement.setString(1, name);
            statement.setString(2, ingredients);
            statement.setString(3, directions);
            statement.setDouble(4, servingSize);
            statement.setInt(5, recipeID);

            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static List<Recipe> userRecipes(String username)
    {
        // Selects a users recipes from the database and returns a list of users

        List<Recipe> recipes = new ArrayList<Recipe>();

        String sql =
                """
                SELECT r.*
                FROM recipes AS r
                JOIN users AS u
                ON r.userID = u.userID
                AND username = ?
                """;

        try
        {
            // Attempt to query database
            PreparedStatement statement = conn.prepareStatement(sql);

            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();

            // Compile results into Recipe objects and add to list
            while (resultSet.next())
            {
                recipes.add(new Recipe(
                        resultSet.getInt("recipeID"),
                        resultSet.getString("name"),
                        resultSet.getString("ingredients"),
                        resultSet.getString("directions"),
                        resultSet.getDouble("servingSize")
                ));
            }
            return recipes;
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }

    }

    public static void deleteRecipe(Recipe recipe)
    {
        String sql =
                """
                DELETE FROM recipes
                WHERE recipeID = ?;
                """;

        try( PreparedStatement statement = conn.prepareStatement(sql))
        {
            statement.setInt(1, recipe.getRecipeID());

            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }
}
