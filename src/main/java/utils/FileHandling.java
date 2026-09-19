package utils;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Recipe;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.ArrayList;
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
    // Enums
    public enum Operation
    {
        EXPORT, IMPORT
    }

    public enum Extension
    {
        CSV, JSON
    }


    // Public Methods
    public static boolean toCSV(Collection<Recipe> recipes, File file)
    {
        // Exports a users recipes to a CSV file

        try (FileWriter writer = new FileWriter(file)) {

            writer.write("name,ingredients,directions,serves\n");

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

    public static boolean toJSON(Collection<Recipe> recipes, File file)
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
        try (FileWriter writer = new FileWriter(file))
        {
            writer.write(jsonRecipes.toJSONString());
        }
        catch (IOException e)
        {
            return false;
        }

        return true;
    }

    public static Collection<Recipe> loadFile(File file) throws Exception
    {
        // Return a collection of recipes from a given file

        // Turn file into collection of recipes
        return switch (getExtension(file))
        {
            case CSV -> readCSV(file);
            case JSON -> readJSON(file);
        };
    }


    // Support Methods
    private static Collection<Recipe> readCSV(File file)
    {
        ArrayList<Recipe> recipes = new ArrayList<>();

        try (FileReader reader = new FileReader(file))
        {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT
                    .builder().setHeader().setSkipHeaderRecord(true).get().parse(reader);

            Recipe recipe;

            for (CSVRecord record : records)
            {
                // Compile recipe from record and add to recipes

                recipe = new Recipe();
                recipe.setName(record.get("name"));
                recipe.setIngredients(record.get("ingredients"));
                recipe.setDirections(record.get("directions"));
                recipe.setServingSize(Double.parseDouble(record.get("serves")));

                recipes.add(recipe);
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        return recipes;
    }

    private static Collection<Recipe> readJSON(File file)
    {
        ArrayList<Recipe> recipes = new ArrayList<>();

        ObjectMapper mapper = new ObjectMapper();

        try
        {
            JsonNode json = mapper.readTree(file);

            Recipe recipe;

            for (JsonNode node : json)
            {
                recipe = new Recipe();
                recipe.setName(node.get("name").asText());
                recipe.setIngredients(node.get("ingredients").asText());
                recipe.setDirections(node.get("directions").asText());
                recipe.setServingSize(node.get("serves").asDouble());

                recipes.add(recipe);
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        return recipes;
    }

    private static String csvField(String value)
    {
        // Returns a CSV acceptable String
        // Wraps in quotes and escape any quotes inside them
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }

    public static Extension getExtension(File file) throws Exception
    {
        // Extracts the extension of a file

        String fileName = file.getName();

        int dotIndex = fileName.lastIndexOf('.');

        // handle cases with no extension or multiple dots
        if (dotIndex == -1 || dotIndex == fileName.length() - 1)
        {
            // no extension found
            throw new Exception("Invalid file extension");
        }

        // Return corresponding extension
        return switch (fileName.substring(dotIndex + 1).toLowerCase())
        {
            case "csv" -> Extension.CSV;
            case "json" -> Extension.JSON;
            default ->
                    throw new IllegalStateException(
                            "Invalid file extension: " + fileName.substring(dotIndex + 1).toLowerCase()
                    );
        };
    }
}