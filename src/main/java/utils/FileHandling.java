package utils;


import models.Recipe;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

/******************************************************************************

 File        : FileHandling.java

 Project     : Chefs-Handbook

 Package     : utils

 Date        : Wednesday 16 September 2026

 Author      : Tom Melton

 Description : Collection of static methods to manage file handling for the project

 History     : 16/09/2026 - v1.00
 ******************************************************************************/

public class FileHandling
{
    // Public Methods
    public static boolean toCSV(Collection<Recipe> recipes)
    {
        // Exports a users recipes to a CSV file

        try (FileWriter writer = new FileWriter("recipes.csv")) {

            writer.write("Name,Ingredients,Directions,Serves\n");

            for (Recipe recipe : recipes)
            {
                recipe.resetMultiplier();

                writer.write(
                    csvField(recipe.getName()) + "," +
                        csvField(recipe.getIngredients()) + "," +
                        csvField(recipe.getDirections()) + "," +
                        csvField((recipe.getServingSizeString())) + "\n"
                );
            }
        }
        catch (IOException e)
        {
            return false;
        }
        return true;
    }

    public static boolean toJSON(Collection<Recipe> recipes)
    {
        // Exports a users recipes to a JSON file

        JSONArray jsonRecipes = new JSONArray();
        JSONObject jsonRecipe;

        // Construct JSON file as a collection of recipes within a JSON array
        for (Recipe recipe : recipes)
        {
            recipe.resetMultiplier();

            jsonRecipe = new JSONObject();

            jsonRecipe.put("name", recipe.getName());
            jsonRecipe.put("ingredients", recipe.getIngredients());
            jsonRecipe.put("directions", recipe.getDirections());
            jsonRecipe.put("serves", recipe.getServingSizeString());

            jsonRecipes.add(jsonRecipe);
        }

        // Write to file
        try (FileWriter writer = new FileWriter("recipes.json"))
        {
            writer.write(jsonRecipes.toJSONString());
        }
        catch (IOException e)
        {
            return false;
        }

        return true;
    }


    // Support Methods
    private static String csvField(String value)
    {
        // Returns a CSV acceptable String
        // Wraps in quotes and escape any quotes inside them
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
}