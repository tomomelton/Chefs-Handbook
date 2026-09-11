package gui;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.User;

import java.util.ArrayList;
import java.util.List;

import static database.UserDAO.createUser;
import static database.UserDAO.getUser;

/******************************************************************************

 File        : RegisterPage.java

 Project     : Chefs-Handbook

 Package     : gui

 Date        : Wednesday 02 September 2026

 Author      : Tom Melton

 Description : Static class to build a register GUI to create a new user

 History     : 02/09/2026 - v1.00
 ******************************************************************************/

public class RegisterWindow
{
    private static User user;

    // Window
    private static Stage window;

    // Scene
    private static Scene scene;

    // Layouts
    private static GridPane grid;
    private static HBox buttons;

    // Labels
    private static Label nameLabel;
    private static Label passLabel;
    private static Label passConfLabel;
    private static Label takenLabel;
    private static Label matchLabel;
    private static Label validPassLabel;

    // Fields
    private static TextField nameInput;
    private static PasswordField passInput;
    private static PasswordField passConfInput;

    // Lists
    private static List<Label> errorLabels;

    // Buttons
    private static Button registerButton;
    private static Button cancelButton;


    // Public Methods
    public static User load()
    {
        // Window
        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Chefs Handbook - Register");
        window.setMinHeight(250);
        window.setMinWidth(400);

        // Grid
        grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);
        grid.setAlignment(Pos.CENTER);


        // Name label
        nameLabel = new Label("*Username: ");
        GridPane.setConstraints(nameLabel, 0, 0);

        // Name input
        nameInput = new TextField();
        GridPane.setConstraints(nameInput, 1, 0);


        // Password label
        passLabel = new Label("*Password: ");
        GridPane.setConstraints(passLabel, 0, 1);

        // Password input
        passInput = new PasswordField();
        GridPane.setConstraints(passInput, 1, 1);


        // Password confirm label
        passConfLabel = new Label("*Confirm Password: ");
        GridPane.setConstraints(passConfLabel, 0, 2);

        // Password confirm input
        passConfInput = new PasswordField();
        GridPane.setConstraints(passConfInput, 1, 2);


        // Error labels
        errorLabels = new ArrayList<>();

        // Taken username label
        takenLabel = new Label("Username already taken");
        errorLabels.add(takenLabel);

        // Password match label
        matchLabel = new Label("Passwords do not match");
        errorLabels.add(matchLabel);

        // Invalid password label
        validPassLabel = new Label("Password must be at least 8 characters\nand contain a special character");
        errorLabels.add(validPassLabel);

        for (Label label : errorLabels)
        {
            label.setStyle("-fx-text-fill: red;" + "-fx-font-weight: bold;");
            label.setVisible(false);
            GridPane.setConstraints(label, 1, 4);
        };


        // Buttons
        buttons = new HBox(10);

        // Register button
        registerButton = new Button("Register");
        registerButton.setOnAction(e -> register());

        // Cancel button
        cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> window.close());

        buttons.getChildren().addAll(registerButton, cancelButton);
        GridPane.setConstraints(buttons, 1, 3);




        grid.getChildren().addAll(
                nameLabel, nameInput,
                passLabel, passInput,
                passConfLabel, passConfInput,
                buttons
        );

        grid.getChildren().addAll(errorLabels);

        scene = new Scene(grid);

        window.setScene(scene);
        window.showAndWait();

        return user;
    }


    // Button Methods
    private static void register()
    {
        User tempUser = getUser(nameInput.getText());

        // Check password entries are the same
        if (!passInput.getText().equals(passConfInput.getText()))
        {
            takenLabel.setVisible(false);
            matchLabel.setVisible(true);
            validPassLabel.setVisible(false);

            passInput.clear();
            passConfInput.clear();
        }
        // Check username isn't taken
        else if (tempUser != null)
        {
            takenLabel.setVisible(true);
            matchLabel.setVisible(false);
            validPassLabel.setVisible(false);

            nameInput.clear();
        }
        // Check password is valid
        else if (!validatePassword(passInput.getText()))
        {
            takenLabel.setVisible(false);
            matchLabel.setVisible(false);
            validPassLabel.setVisible(true);

            passInput.clear();
            passConfInput.clear();
        }
        // Create user
        else
        {
            createUser(
                    nameInput.getText(),
                    passInput.getText()
            );

            user = getUser(nameInput.getText());

            window.close();
        }
    }


    // Support Methods
    private static boolean validatePassword(String password)
    {
        // Check min length
        if (password.length() < 8) return false;

        // Check contains special characters
        if (!password.matches(".*[^a-zA-Z0-9].*")) return false;

        return true;
    }
}