package gui;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

import org.kordamp.ikonli.fontawesome6.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import models.Recipe;
import models.User;

import gui.StartWindow;

import java.util.NoSuchElementException;

import static database.RecipeDAO.userRecipes;

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
    private boolean editing;

    // Window
    private Stage window;

    // Scene
    private Scene scene;

    // Layouts
    private BorderPane borderPane;
    private StackPane topMenu;
    private VBox leftMenu;
    private HBox searchRow;

    // Labels
    private Label welcomeLabel;
    private Label recipeLabel;

    // Lists
    private ObservableList<Recipe> recipes;
    private FilteredList<Recipe> filteredRecipes;
    private ListView<Recipe> recipeList;

    // Text Fields
    private TextField recipeSearch;

    // Buttons
    private Button newRecipeButton;

    // Menus
    private MenuBar menuBar;
    private Menu userMenu;
    private Menu fileMenu;

    // Menu Items
    private MenuItem changeUserMenuItem;
    private MenuItem createUserMenuItem;
    private MenuItem exitMenuItem;
    private MenuItem importMenuItem;
    private MenuItem exportMenuItem;

    // Tooltips
    private static final Tooltip newRecipeTooltip = new Tooltip("New recipe");


    // Constructors
    public HomeWindow(User user)
    {
        this.user = user;

        editing = false;

        // Window
        window = new Stage();
        window.setTitle("Chefs Handbook - " + this.user.getUsername());
        window.setMinHeight(700);
        window.setMinWidth(1000);


        // Layouts
        borderPane = new BorderPane();
        borderPane.setPadding(new Insets(20, 20, 20, 20));

        topMenu = new StackPane();
        topMenu.setAlignment(Pos.CENTER);
        topMenu.setPadding(new Insets(20, 20, 20, 20));

        leftMenu = new VBox(10);
        leftMenu.setAlignment(Pos.TOP_CENTER);
        leftMenu.setPadding(new Insets(20, 20, 20, 20));
        leftMenu.setId("left-menu");

        searchRow = new HBox(5);
        searchRow.setAlignment(Pos.BASELINE_CENTER);


        // Labels
        welcomeLabel = new Label(this.user.getUsername() + "'s Handbook");
        welcomeLabel.getStyleClass().add("red-label");
        StackPane.setAlignment(welcomeLabel, Pos.CENTER);

        recipeLabel = new Label("Recipes");
        recipeLabel.getStyleClass().add("red-label");
        recipeLabel.setStyle("-fx-font-size: 20;");
        recipeLabel.setMaxWidth(Double.MAX_VALUE);
        recipeLabel.setAlignment(Pos.CENTER);


        // Lists
        recipes = FXCollections.observableArrayList();
        recipes.addAll(
                userRecipes(this.user.getUsername())
//            new Recipe(
//                    1,
//                    "Toffee Sauce",
//                    "4 packs of butter\n100.5g caster sugar\ngolden syrup\n100ml double cream",
//                    "1. heat butter, sugar, syrup in a pan on low heat until combined\n2. take off heat and add cream\n3. strain once cooled"
//            ),
//            new Recipe(
//                    2,
//                    "Panna Cotta",
//                    "250g sugar\n500ml milk\n1500ml double cream\n6 gelatin leaves",
//                    "Bring sugar, milk, and cream to a simmer on a low heat\nTake off heat and add gelatin\nStrain and pour into moulds"
//            )
        );

        filteredRecipes = new FilteredList<>(recipes, recipe -> true);

        recipeList = new ListView<>(filteredRecipes);
        recipeList.setStyle("-fx-font-size: 15");
        recipeList.setCellFactory(list -> new RecipeListCell());
        recipeList.setOnMouseClicked(e -> {if (!editing) displayRecipe();});
        recipeList.getStyleClass().add("field-border");
        recipeList.getSelectionModel().clearSelection();
        VBox.setVgrow(recipeList, Priority.ALWAYS);


        // Searchbar
        recipeSearch = new TextField();
        recipeSearch.setPromptText("Search recipes...");
        recipeSearch.getStyleClass().add("field-border");
        recipeSearch.textProperty().addListener(
                (observable, oldValue, newValue) -> {

            String search = newValue.toLowerCase();

            filteredRecipes.setPredicate(recipe -> recipe.getName().toLowerCase().contains(search));
        }
        );
        HBox.setHgrow(recipeSearch, Priority.ALWAYS);


        // Buttons
        newRecipeButton = new Button();
        newRecipeButton.setGraphic(new FontIcon("fas-plus"));
        newRecipeButton.setOnAction(e -> newRecipe());
        newRecipeButton.getStyleClass().add("red-button");


        // Menus
        userMenu = new Menu();
        userMenu.setGraphic(new FontIcon(FontAwesomeSolid.USER));
        userMenu.getStyleClass().add("red-button");

        fileMenu = new Menu();
        fileMenu.setGraphic(new FontIcon(FontAwesomeSolid.FILE));
        fileMenu.getStyleClass().add("red-button");

        menuBar = new MenuBar(userMenu, fileMenu);
        menuBar.setMaxWidth(Region.USE_PREF_SIZE);
        StackPane.setAlignment(menuBar, Pos.CENTER_LEFT);


        // Menu Items
        changeUserMenuItem = new MenuItem("Switch User");
        changeUserMenuItem.setOnAction(e -> changeUser());

        createUserMenuItem = new MenuItem("New User");
        createUserMenuItem.setOnAction(e -> newUser());

        exitMenuItem = new MenuItem("Exit");
        exitMenuItem.setOnAction(e -> exit());

        importMenuItem = new MenuItem("Import");

        exportMenuItem = new MenuItem("Export");

        userMenu.getItems().addAll(changeUserMenuItem, createUserMenuItem, exitMenuItem);
        fileMenu.getItems().addAll(importMenuItem, exportMenuItem);


        // Tooltips
        Tooltip.install(newRecipeButton, newRecipeTooltip);
        newRecipeTooltip.setShowDelay(Duration.seconds(0.25));


        // Build Layouts
        topMenu.getChildren().addAll(menuBar, welcomeLabel);
        leftMenu.getChildren().addAll(recipeLabel, searchRow, recipeList);
        searchRow.getChildren().addAll(recipeSearch, newRecipeButton);

        borderPane.setTop(topMenu);
        borderPane.setLeft(leftMenu);
        borderPane.setCenter(recipeLayout);
        borderPane.setBottom(new HBox(0));


        // Set Scene
        scene = new Scene(borderPane);
        scene.getStylesheets().add(
                getClass().getResource("/styles/main.css").toExternalForm()
        );

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

    public User getUser() {
        return user;
    }

    public void setEditing(boolean editing) {
        this.editing = editing;
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

    private void newRecipe()
    {
        recipeLayout = null;
        borderPane.setCenter(new RecipeEditor(this));
    }


    // Menu Methods
    private void exit()
    {
        window.close();
        StartWindow.load();
    }

    private void changeUser()
    {
        User user = LoginWindow.load();
        if (user != null)
        {
            window.close();
            new HomeWindow(user);
        }    }

    private void newUser()
    {
        User user = RegisterWindow.load();
        if (user != null)
        {
            window.close();
            new HomeWindow(user);
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
        recipeList.refresh();
    }
}