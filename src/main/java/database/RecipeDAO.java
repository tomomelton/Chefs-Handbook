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

        name = recipe.getName();
        ingredients = recipe.getIngredients();
        directions = recipe.getDirections();


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

    public static void updateRecipe(Recipe recipe)
    {
        // method to insert recipe object into the database

        String name, ingredients, directions;
        int recipeID;


        name = recipe.getName();
        ingredients = recipe.getIngredients();
        directions = recipe.getDirections();
        recipeID = recipe.getRecipeID();


        String sql =
                """
                UPDATE recipes
                SET (name, ingredients, directions) = (?, ?, ?)
                WHERE recipeid = ?;
                """;

        try( PreparedStatement statement = conn.prepareStatement(sql))
        {
            statement.setString(1, name);
            statement.setString(2, ingredients);
            statement.setString(3, directions);
            statement.setInt(4, recipeID);

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
                        resultSet.getString("directions")
                ));
            }
            return recipes;
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }

    }
}
