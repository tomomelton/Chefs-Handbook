package gui;


import javafx.scene.control.ListCell;
import models.Recipe;

/******************************************************************************

 File        : RecipeListCell.java

 Project     : Chefs-Handbook

 Package     : gui

 Date        : Saturday 05 September 2026

 Author      : Tom Melton

 Description : Class which extents ListCell to describe a Recipe within a ListView

 History     : 05/09/2026 - v1.00
 ******************************************************************************/

public class RecipeListCell extends ListCell<Recipe>
{
    @Override
    protected void updateItem(Recipe recipe, boolean empty)
    {
        super.updateItem(recipe, empty);

        setText(empty || recipe == null ? null : recipe.getName());
    }
}