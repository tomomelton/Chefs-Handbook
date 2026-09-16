package utils;


import models.Recipe;

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

            writer.write("Name,Ingredients,Directions\n");

            for (Recipe recipe : recipes)
            {
                writer.write(
                    csvField(recipe.getName()) + "," +
                        csvField(recipe.getIngredients()) + "," +
                        csvField(recipe.getDirections()) + "\n"
                );
            }

        }
        catch (IOException e)
        {
            return false;
        }
        return true;
    }

    public static void toJSON(Collection<Recipe> recipes)
    {
        // Exports a users recipes to a JSON file
    }


    // Support Methods
    private static String csvField(String value)
    {
        // Returns a CSV acceptable String
        // Wraps in quotes and escape any quotes inside them
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
}