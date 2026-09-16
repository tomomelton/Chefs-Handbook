package gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.User;

import java.util.Objects;


public class StartWindow
{
    // Window
    private static Stage window;

    // Scene
    private static Scene scene;

    // Layouts
    private static VBox layout;
    private static HBox buttons;

    // Labels
    private static Label titleLabel;

    // Buttons
    private static Button loginButton;
    private static Button registerButton;

    // Public Methods
    public static void load()
    {
        // Labels
        titleLabel = new Label("Welcome to Chefs Handbook!");
//        titleLabel.setStyle("-fx-font-size: 20;" + "-fx-font-weight: bold");
        titleLabel.getStyleClass().add("red-label");

        // Buttons
        loginButton = new Button("Login");
        loginButton.setOnAction(e -> login());
        loginButton.setDefaultButton(true);
        loginButton.getStyleClass().add("red-button");

        registerButton = new Button("Register");
        registerButton.setOnAction(e -> register());
        registerButton.getStyleClass().add("red-button");


        // Layouts
        buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(loginButton, registerButton);

        layout = new VBox(10);
        layout.getChildren().addAll(titleLabel, buttons);
        layout.setAlignment(Pos.CENTER);


        // Scene
        scene = new Scene(layout);
        scene.getStylesheets().add(
                Objects.requireNonNull(StartWindow.class.getResource("/styles/main.css")).toExternalForm()
        );


        // Window
        window = new Stage();
        window.setTitle("Chefs Handbook - Start");
        window.setMinHeight(500);
        window.setMinWidth(800);
        window.setScene(scene);
        window.showAndWait();
    }


    // Button Methods
    private static void login()
    {
        User user = LoginWindow.load();
        if (user != null)
        {
            window.close();
            new HomeWindow(user);
        }
    }

    private static void  register()
    {
        User user = RegisterWindow.load();
        if (user != null)
        {
            window.close();
            new HomeWindow(user);
        }
    }
}
