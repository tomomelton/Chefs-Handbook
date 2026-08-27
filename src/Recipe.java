import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/******************************************************************************

 File        : Recipe.java

 Date        : Tuesday 25th August 2026

 Author      : Tom Melton

 Description : Class describing a recipe object and containing accessing methods

 History     : 25/08/2026 - v1.00

 ******************************************************************************/

public class Recipe
{
    private int recipeID;
    private String name;
    private String ingredients;
    private String directions;
    private double multiplier = 1;

    public Recipe(int recipeID, String name, String ingredients, String directions)
    {
        this.recipeID = recipeID;
        this.name = name;
        this.ingredients = ingredients;
        this.directions = directions;
    }

    public int getRecipeID() {
        return recipeID;
    }

    public void setRecipeID(int recipeID) {
        this.recipeID = recipeID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIngredients() {
        return scale(ingredients, multiplier);
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getDirections() {
        return directions;
    }

    public void setDirections(String directions) {
        this.directions = directions;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(double multiplier) {
        this.multiplier = multiplier;
    }

    public void resetMultiplier() {
        this.multiplier = 1;
    }

    @Override
    public String toString()
    {
        return
            name +
            "\n\nIngredients:\n" + getIngredients() +
            "\n\nDirections:\n" + getDirections();
    }

    private String scale(String ingredients, double multiplier)
    {
        // scales the given ingredients numerical amounts by the given multiplier

        Pattern pattern = Pattern.compile("\\d+(?:\\.\\d+)?");
        Matcher matcher = pattern.matcher(ingredients);

        return matcher.replaceAll(
            match -> {
                double value = (Double.parseDouble(match.group()) * multiplier);
                return BigDecimal.valueOf(value).stripTrailingZeros().toPlainString();
            }
        );
    }

    public static void main(String[] args)
    {
        Recipe toffeeSauce = new Recipe(
        1,
        "Toffee Sauce",
        "4 packs of butter\n100.5g caster sugar\ngolden syrup\n100ml double cream",
        "1. heat butter, sugar, syrup in a pan on low heat until combined\n2. take off heat and add cream\n3. strain once cooled"
        );

        toffeeSauce.setMultiplier(0.9);

        System.out.println(toffeeSauce);

        toffeeSauce.resetMultiplier();

        System.out.println("\n\n");

        System.out.println(toffeeSauce);

    }
}
