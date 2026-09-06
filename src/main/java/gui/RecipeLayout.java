package gui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import models.Recipe;

import org.kordamp.ikonli.fontawesome6.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

/******************************************************************************

 File        : RecipeLayout.java

 Project     : Chefs-Handbook

 Package     : gui

 Date        : Wednesday 02 September 2026

 Author      : Tom Melton

 Description : Class to construct a GUI layout to represent a recipe within HomeWindow

 History     : 05/09/2026 - v2.00
 ******************************************************************************/


public class RecipeLayout extends VBox
{

    private final Recipe recipe;
    private final HomeWindow parent;

    // Labels
    private final Label nameLabel;
    private final Label ingredientsHeading;
    private final Label ingredientsContent;
    private final Label directionsHeading;
    private final Label directionsContent;

    // Buttons
    private final Button editButton;
    private final Button deleteButton;

    // Tooltips
    private static final Tooltip editTooltip = new Tooltip("Edit recipe");
    private static final Tooltip deleteTooltip = new Tooltip("Delete recipe");


    // Constructors
    public RecipeLayout(HomeWindow parent, Recipe recipe)
    {
        super(10);

        this.parent = parent;
        this.recipe = recipe;

        // Labels
        nameLabel = new Label(recipe.getName());
        nameLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold");

        ingredientsHeading = new Label("Ingredients:\n\n");
        ingredientsHeading.setStyle("-fx-font-size: 18; -fx-font-weight: bold");

        ingredientsContent = new Label(this.recipe.getIngredients());
        ingredientsContent.setStyle("-fx-font-size: 13");

        directionsHeading = new Label("Directions:\n\n");
        directionsHeading.setStyle("-fx-font-size: 18; -fx-font-weight: bold");

        directionsContent = new Label(recipe.getDirections());
        directionsContent.setStyle("-fx-font-size: 13");


        // Buttons
        editButton = new Button();
        editButton.setGraphic(new FontIcon(FontAwesomeSolid.PEN));
        editButton.setOnAction(e -> edit());

        deleteButton = new Button();
        deleteButton.setGraphic(new FontIcon(FontAwesomeSolid.TRASH));
        deleteButton.setOnAction(e -> delete());


        // Tooltips
        Tooltip.install(editButton, editTooltip);
        Tooltip.install(deleteButton, deleteTooltip);

        editTooltip.setShowDelay(Duration.seconds(0.25));
        deleteTooltip.setShowDelay(Duration.seconds(0.25));


        // Top Row
        HBox titleRow = new HBox(5);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        titleRow.getChildren().addAll(nameLabel, spacer, editButton, deleteButton);


        // Layout
        setPadding(new Insets(20, 20, 20, 20));

        getChildren().addAll(
                titleRow,
                ingredientsHeading, ingredientsContent,
                directionsHeading, directionsContent
        );

        setStyle(
                """
                -fx-background-color: #ededed;
                -fx-border-color: #c7c7c7;
                -fx-border-width: 3
                """
        );
    }


    // Button Methods
    private void edit()
    {
        parent.setCenter(new RecipeEditor(parent, recipe));
    }

    private void delete()
    {
        ConfirmationBox confirmationBox = new ConfirmationBox("Are you sure you want to delete?");

        if (confirmationBox.getResponse())
        {
            parent.removeRecipe();
            parent.displayTopRecipe();
        }
    }


    // Public Methods
    public void refresh()
    {
        // Sets labels with current recipe information
        nameLabel.setText(recipe.getName());
        ingredientsContent.setText(recipe.getIngredients());
        directionsContent.setText(recipe.getDirections());
    }
}