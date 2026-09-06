package gui;


import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Duration;
import models.Recipe;
import org.kordamp.ikonli.fontawesome6.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

/******************************************************************************

 File        : RecipeEditor.java

 Project     : Chefs-Handbook

 Package     : gui

 Date        : Saturday 05 September 2026

 Author      : Tom Melton

 Description : GUI which allows a user to create a new recipe or edit an existing one

 History     : 05/09/2026 - v1.00
 ******************************************************************************/

public class RecipeEditor extends VBox
{
    private Recipe recipe;
    private HomeWindow parent;

    // Text Editors
    private TextField nameField;
    private TextArea ingredientsField;
    private TextArea directionsField;

    // Labels
    private Label ingredientsLabel;
    private Label directionsLabel;

    // Buttons
    private Button saveButton;
    private Button cancelButton;

    // Tooltips
    private static final Tooltip saveToolip = new Tooltip("Save");
    private static final Tooltip cancelTooltip = new Tooltip("Cancel");

    // Constructors
    public RecipeEditor(HomeWindow parent)
    {
        super(10);

        this.recipe = new Recipe();
        this.parent = parent;

        // Text Editors
        nameField = new TextField();
        nameField.setPromptText("Recipe name...");

        ingredientsField = new TextArea();
        ingredientsField.setPromptText("Ingredient 1...  Ingredient 2...  Ingredient 3...");

        directionsField = new TextArea();
        directionsField.setPromptText("Step 1...  Step 2...  Step 3...");


        // Labels
        ingredientsLabel = new Label("Ingredients:\n\n");
        ingredientsLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold");

        directionsLabel = new Label("Directions:\n\n");
        directionsLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold");


        // Buttons
        saveButton = new Button();
        saveButton.setGraphic(new FontIcon(FontAwesomeSolid.SAVE));
        saveButton.setOnAction(e -> save());

        cancelButton = new Button();
        cancelButton.setGraphic(new FontIcon(FontAwesomeSolid.TIMES));
        cancelButton.setOnAction(e -> cancel());


        // Tooltips
        Tooltip.install(saveButton, saveToolip);
        Tooltip.install(cancelButton, cancelTooltip);

        saveToolip.setShowDelay(Duration.seconds(0.25));
        cancelTooltip.setShowDelay(Duration.seconds(0.25));


        // Top Row
        HBox topRow = new HBox(5);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        topRow.getChildren().addAll(nameField, spacer, saveButton, cancelButton);


        // Layout
        setPadding(new Insets(20, 20, 20, 20));

        getChildren().addAll(
                topRow,
                ingredientsLabel, ingredientsField,
                directionsLabel, directionsField
        );
    }

    public RecipeEditor(HomeWindow parent, Recipe recipe)
    {
        this(parent);
        this.recipe = recipe;

        // Text Editors
        nameField.setText(recipe.getName());
        ingredientsField.setText(recipe.getIngredients());
        directionsField.setText(recipe.getDirections());
    }


    // Button Methods
    private void cancel()
    {
        ConfirmationBox confirmationBox = new ConfirmationBox("Are you want to cancel?");

        if (confirmationBox.getResponse()) parent.resetRecipe();
    }

    private void save()
    {
        // Update recipe object
        updateRecipe();

        // If no previous recipe
        if (parent.getRecipeLayout() == null)
        {
            // Add recipe to recipe list
            parent.addRecipe(recipe);
            // Set recipe as current layout
            parent.setRecipeLayout(new RecipeLayout(parent, recipe));
        }

        parent.resetRecipe();
        parent.getRecipeLayout().refresh();

    }


    // Support Methods
    private void updateRecipe()
    {
        // Update recipe object
        recipe.setName(nameField.getText());
        recipe.setIngredients(ingredientsField.getText());
        recipe.setDirections(directionsField.getText());
    }
}