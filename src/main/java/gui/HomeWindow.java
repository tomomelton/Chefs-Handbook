package gui;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import org.kordamp.ikonli.javafx.FontIcon;

import models.Recipe;
import models.User;

import java.util.NoSuchElementException;

/******************************************************************************

 File        : HomePage.java

 Project     : Chefs-Handbook

 Package     : gui

 Date        : Wednesday 02 September 2026

 Author      : Tom Melton

 Description : Class to build a users home page GUI / collection of recipes

 History     : 02/09/2026 - v1.00
 ******************************************************************************/

public class HomeWindow
{
    private final User user;
    private RecipeLayout recipeLayout;

    // Window
    private Stage window;

    // Scene
    private Scene scene;

    // Layouts
    private BorderPane borderPane;
    private HBox topMenu;
    private VBox leftMenu;
    private HBox searchRow;

    // Labels
    private Label welcomeLabel;
    private Label recipeLabel;

    // Lists
    private ObservableList<Recipe> recipes;
    private ListView<Recipe> recipeList;

    // Text Fields
    private TextField recipeSearch;

    // Buttons
    private Button newRecipeButton;

    // Tooltips
    private static final Tooltip newRecipeTooltip = new Tooltip("New recipe");


    // Constructors
    public HomeWindow(User user)
    {
        this.user = user;

        // Window
        window = new Stage();
        window.setTitle("Chefs Handbook - " + this.user.getUsername());
        window.setMinHeight(700);
        window.setMinWidth(1000);


        // Layouts
        borderPane = new BorderPane();
        borderPane.setPadding(new Insets(20, 20, 20, 20));

        topMenu = new HBox(10);
        topMenu.setAlignment(Pos.CENTER);
        topMenu.setPadding(new Insets(20, 20, 20, 20));
        topMenu.setStyle("-fx-background-color: #d3d3d3");

        leftMenu = new VBox(10);
        leftMenu.setAlignment(Pos.TOP_CENTER);
        leftMenu.setPadding(new Insets(20, 20, 20, 20));
        leftMenu.setStyle("-fx-background-color: #e0e0e0");

        searchRow = new HBox(5);


        // Labels
        welcomeLabel = new Label("Welcome " + this.user.getUsername() + "!");
        welcomeLabel.setStyle("-fx-font-size: 25; -fx-font-weight: bold;");

        recipeLabel = new Label("Recipes");
        recipeLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");


        // Lists
        recipes = FXCollections.observableArrayList();

        recipeList = new ListView<>(recipes);
        recipeList.setStyle("-fx-font-size: 15");
        recipeList.getItems().addAll(
//                userRecipes(this.user.getUsername())

                // Temp fix while VPN is down
                new Recipe(
                        1,
                        "Toffee Sauce",
                        "4 packs of butter\n100.5g caster sugar\ngolden syrup\n100ml double cream",
                        "1. heat butter, sugar, syrup in a pan on low heat until combined\n2. take off heat and add cream\n3. strain once cooled"
                ),
                new Recipe(
                        2,
                        "Panna Cotta",
                        "250g sugar\n500ml milk\n1500ml double cream\n6 gelatin leaves",
                        "Bring sugar, milk, and cream to a simmer on a low heat\nTake off heat and add gelatin\nStrain and pour into moulds"
                )
        );
        recipeList.setCellFactory(list -> new RecipeListCell());
        recipeList.setOnMouseClicked(e -> displayRecipe());


        // Searchbar
        recipeSearch = new TextField();
        recipeSearch.setPromptText("Search recipes...");
        recipeSearch.setOnKeyTyped(e -> {
            System.out.println(recipeSearch.getText());
        });
        HBox.setHgrow(recipeSearch, Priority.ALWAYS);


        // Buttons
        newRecipeButton = new Button();
        newRecipeButton.setGraphic(new FontIcon("fas-plus"));
        newRecipeButton.setOnAction(e -> borderPane.setCenter(new RecipeEditor(this)));


        // Tooltips
        Tooltip.install(newRecipeButton, newRecipeTooltip);
        newRecipeTooltip.setShowDelay(Duration.seconds(0.25));


        // Build Layouts
        topMenu.getChildren().add(welcomeLabel);
        leftMenu.getChildren().addAll(recipeLabel, searchRow, recipeList);
        searchRow.getChildren().addAll(recipeSearch, newRecipeButton);

        borderPane.setTop(topMenu);
        borderPane.setLeft(leftMenu);
        borderPane.setCenter(recipeLayout);
        borderPane.setBottom(new HBox(0));


        // Set Scene
        scene = new Scene(borderPane);
        window.setScene(scene);
        window.show();
    }


    // Getters and Setters
    public RecipeLayout getRecipeLayout() {
        return recipeLayout;
    }

    public void setRecipeLayout(RecipeLayout recipeLayout) {
        this.recipeLayout = recipeLayout;
    }


    // Button Methods
    private void displayRecipe()
    {
        Recipe selectedRecipe = recipeList.getSelectionModel().getSelectedItem();

        if (selectedRecipe != null)
        {
            recipeLayout = new RecipeLayout(this, selectedRecipe);
            borderPane.setCenter(recipeLayout);
        }
    }


    // Public Methods
    public void resetRecipe()
    {
        // Resets the center box by setting center to current recipeLayout
        borderPane.setCenter(recipeLayout);
        recipeList.refresh();
    }

    public void setCenter(Node node)
    {
        borderPane.setCenter(node);
        recipeList.refresh();

    }

    public void removeRecipe()
    {
        // Removes the current Recipe

        Recipe selected = recipeList.getSelectionModel().getSelectedItem();

        recipes.remove(selected);
    }

    public void displayTopRecipe()
    {
        // Will display the first recipe in recipeList

        try
        {
            Recipe topRecipe = recipeList.getItems().getFirst();

            recipeLayout = new RecipeLayout(this, topRecipe);
        }
        // If recipeList is empty
        catch (NoSuchElementException e)
        {
            recipeLayout = null;
        }
        resetRecipe();
    }

    public void addRecipe(Recipe recipe)
    {
        // Adds a new recipe to recipes
        recipes.add(recipe);
    }
}