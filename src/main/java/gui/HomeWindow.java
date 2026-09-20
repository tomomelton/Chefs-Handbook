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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import org.kordamp.ikonli.fontawesome6.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import models.Recipe;
import models.User;
import utils.FileHandling;

import java.io.File;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Objects;

import static database.RecipeDAO.insertRecipe;
import static database.RecipeDAO.userRecipes;
import static utils.FileHandling.*;

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
    private Menu exportMenu;

    // Menu Items
    private MenuItem changeUserMenuItem;
    private MenuItem createUserMenuItem;
    private MenuItem exitMenuItem;
    private MenuItem importMenuItem;
    private MenuItem exportCSVMenuItem;
    private MenuItem exportJSONMenuItem;

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

        filteredRecipes = new FilteredList<>(recipes, recipe -> true);

        recipeList = new ListView<>(filteredRecipes);
        recipeList.setStyle("-fx-font-size: 15");
        recipeList.setCellFactory(list -> new RecipeListCell());
        recipeList.setOnMouseClicked(e -> {if (!editing) displayRecipe();});
        recipeList.getStyleClass().add("field-border");
        recipeList.getSelectionModel().clearSelection();
        VBox.setVgrow(recipeList, Priority.ALWAYS);

        populateRecipes();

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

        exportMenu = new Menu("Export");
        exportMenu.getStyleClass().add("menu-item");

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
        importMenuItem.setOnAction(e -> importFile());

        exportCSVMenuItem = new MenuItem("CSV");
        exportCSVMenuItem.setOnAction(e -> exportRecipes(Extension.CSV));

        exportJSONMenuItem = new MenuItem("JSON");
        exportJSONMenuItem.setOnAction(e -> exportRecipes(Extension.JSON));

        exportMenu.getItems().addAll(exportCSVMenuItem, exportJSONMenuItem);
        userMenu.getItems().addAll(changeUserMenuItem, createUserMenuItem, exitMenuItem);
        fileMenu.getItems().addAll(importMenuItem, exportMenu);


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
                Objects.requireNonNull(getClass().getResource("/styles/main.css")).toExternalForm()
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
        }
    }

    private void newUser()
    {
        User user = RegisterWindow.load();
        if (user != null)
        {
            window.close();
            new HomeWindow(user);
        }
    }

    private void exportRecipes(Extension exportType)
    {
        // Save an export of extension at a selected file location

        // Determine extension
        String extension = switch (exportType)
        {
            case CSV -> "CSV";
            case JSON -> "JSON";
        };

        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Save Export");
        fileChooser.setInitialFileName("recipes." + extension.toLowerCase());
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"), "Downloads")
        );
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(extension + " File", "*." + extension.toLowerCase())
        );

        File file = fileChooser.showSaveDialog(window);

        if (file != null)
        {
            boolean status = switch (exportType)
            {
                case CSV -> toCSV(recipes, file);
                case JSON -> toJSON(recipes, file);
            };

            successfulIO(status, FileHandling.Operation.EXPORT);
        }
    }

    private void importFile()
    {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"), "Downloads")
        );

        File file = fileChooser.showOpenDialog(window);

        // If import is cancelled
        if (file == null)
        {
            return;
        }

        // Attempt to load file
        try
        {
            Collection<Recipe> importedRecipes = loadFile(file);

            // Add recipes to list and database
            try
            {
                // Insert recipes into database
                insertRecipe(user.getId(), importedRecipes);

                // Add recipes to recipe list
                recipes.addAll(importedRecipes);
            }
            catch (Exception e)
            {
                successfulIO(false, Operation.IMPORT);
            }
        }
        catch (Exception e)
        {
            // Invalid file error
            new AlertBox(e.getMessage());
            successfulIO(false, Operation.IMPORT);
        }

        successfulIO(true, Operation.IMPORT);
    }


    // Support Methods
    private void successfulIO(boolean status, FileHandling.Operation IO)
    {
        String operation = switch (IO)
        {
            case EXPORT -> "Export";
            case IMPORT -> "Import";
        };

        if (status)
        {
            new AlertBox(operation + " Successful!");
        }
        else
        {
            new AlertBox(operation + " Failed");
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

    public void removeRecipe(Recipe recipe)
    {
        // Removes the current Recipe
        recipes.remove(recipe);
    }

    public void displayTopRecipe()
    {
        // Will display the first recipe in recipeList

        try
        {
            Recipe topRecipe = recipeList.getItems().getFirst();

            recipeList.getSelectionModel().selectFirst();

            recipeLayout = new RecipeLayout(this, topRecipe);
        }
        // If recipeList is empty
        catch (NoSuchElementException e)
        {
            recipeLayout = null;
        }
        resetRecipe();
    }


    public void populateRecipes()
    {
        // Clears recipes and refills with recipes from database
        recipes.clear();
        recipes.addAll(userRecipes(user.getUsername()));

        recipeList.refresh();
    }
}