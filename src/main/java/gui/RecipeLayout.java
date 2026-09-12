package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import models.Recipe;

import org.kordamp.ikonli.fontawesome6.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import static database.RecipeDAO.deleteRecipe;

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

    // Layouts
    private final HBox titleRow;
    private final HBox scaleRow;
    private final VBox contentBox;

    // Labels
    private final Label nameLabel;
    private final Label ingredientsHeading;
    private final Label ingredientsContent;
    private final Label directionsHeading;
    private final Label directionsContent;
    private final Label scaleLabel;
    private final Label servingSizeLabel;

    // ScrollPane
    private final ScrollPane scrollPane;

    // Text Fields
    private final TextField scaleInput;

    // Buttons
    private final Button editButton;
    private final Button deleteButton;
    private final Button setScaleButton;
    private final Button resetScaleButton;

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
        nameLabel.getStyleClass().add("red-label");
        nameLabel.setStyle("-fx-font-size: 20");

        servingSizeLabel = new Label("Serves: " + recipe.getServingSizeString());
        servingSizeLabel.getStyleClass().add("content-text");

        ingredientsHeading = new Label("Ingredients:\n\n");
        ingredientsHeading.getStyleClass().add("subheading-text");

        ingredientsContent = new Label(this.recipe.getIngredients());
        ingredientsContent.getStyleClass().add("content-text");
        ingredientsContent.setWrapText(true);

        directionsHeading = new Label("Directions:\n\n");
        directionsHeading.getStyleClass().add("subheading-text");

        directionsContent = new Label(recipe.getDirections());
        directionsContent.getStyleClass().add("content-text");
        directionsContent.setWrapText(true);

        scaleLabel = new Label("Scale Multiplier:");
        scaleLabel.setStyle("-fx-font-size: 15");


        // Text Fields
        scaleInput = new TextField(recipe.getMultiplierString());
        scaleInput.setMinWidth(5);
        scaleInput.getStyleClass().add("field-border");


        // Buttons
        editButton = new Button();
        editButton.setGraphic(new FontIcon(FontAwesomeSolid.PEN));
        editButton.setOnAction(e -> edit());
        editButton.getStyleClass().add("red-button");

        deleteButton = new Button();
        deleteButton.setGraphic(new FontIcon(FontAwesomeSolid.TRASH));
        deleteButton.setOnAction(e -> delete());
        deleteButton.getStyleClass().add("red-button");

        setScaleButton = new Button("Set");
        setScaleButton.setOnAction(e -> setScale());
        setScaleButton.getStyleClass().add("red-button");

        resetScaleButton = new Button("Reset");
        resetScaleButton.setOnAction(e -> resetScale());
        resetScaleButton.getStyleClass().add("red-button");


        // Tooltips
        Tooltip.install(editButton, editTooltip);
        Tooltip.install(deleteButton, deleteTooltip);

        editTooltip.setShowDelay(Duration.seconds(0.25));
        deleteTooltip.setShowDelay(Duration.seconds(0.25));


        // Top Row
        titleRow = new HBox(5);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        titleRow.getChildren().addAll(nameLabel, spacer, editButton, deleteButton);


        // Scale Row
        scaleRow = new HBox(5);
        scaleRow.setAlignment(Pos.CENTER_LEFT);
        scaleRow.getChildren().addAll(scaleLabel, scaleInput, setScaleButton, resetScaleButton);


        // Content Box
        contentBox = new VBox(5);
        contentBox.getChildren().addAll(
                ingredientsHeading, ingredientsContent,
                directionsHeading, directionsContent
        );


        // Scroll Pane
        scrollPane = new ScrollPane(contentBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);


        // Layout
        setPadding(new Insets(20, 20, 20, 20));

        getChildren().addAll(
                titleRow,
                scaleRow,
                servingSizeLabel,
                scrollPane
        );

        setId("recipe");
    }


    // Button Methods
    private void edit()
    {
        parent.setCenter(new RecipeEditor(parent, recipe));
        parent.setEditing(true);
    }

    private void delete()
    {
        ConfirmationBox confirmationBox = new ConfirmationBox("Are you sure you want to delete?");

        if (confirmationBox.getResponse())
        {
            parent.removeRecipe();
            parent.displayTopRecipe();

            // Delete recipe from database
            deleteRecipe(recipe);
        }
    }

    private void setScale()
    {
        double multiplier = Double.parseDouble(scaleInput.getText());

        recipe.setMultiplier(multiplier);

        refresh();
    }

    private void resetScale()
    {
        scaleInput.setText("1");

        recipe.resetMultiplier();

        refresh();
    }


    // Public Methods
    public void refresh()
    {
        // Sets labels with current recipe information
        nameLabel.setText(recipe.getName());
        servingSizeLabel.setText("Serves: " + recipe.getServingSizeString());
        ingredientsContent.setText(recipe.getIngredients());
        directionsContent.setText(recipe.getDirections());
    }
}