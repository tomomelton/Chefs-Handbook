package gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.User;


public class StartPage
{
    public static void load()
    {
        // Window
        Stage window = new Stage();
        window.setTitle("Chefs Handbook - Start");
        window.setMinHeight(500);
        window.setMinWidth(800);
        window.setOnCloseRequest(e -> System.out.println("Goodbye!"));

        // Welcome label
        Label titleLabel = new Label("Welcome to Chefs Handbook!");
        titleLabel.setStyle("-fx-font-size: 20;" + "-fx-font-weight: bold");

        // Login button
        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> {
            User user = LoginPage.load();
            window.close();
            new HomePage(user);
        });

        // Layout
        VBox layout = new VBox(10);
        layout.getChildren().addAll(titleLabel, loginButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);

        window.setScene(scene);
        window.showAndWait();
    }
}
