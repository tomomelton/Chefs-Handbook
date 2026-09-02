package gui;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
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

public class RegisterPage 
{
    private static User user;

    public static User load()
    {
        // Window
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Chefs Handbook - Register");
        window.setMinHeight(250);
        window.setMinWidth(400);

        // Grid
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);
        grid.setAlignment(Pos.CENTER);


        // Name label
        Label nameLabel = new Label("*Username: ");
        GridPane.setConstraints(nameLabel, 0, 0);

        // Name input
        TextField nameInput = new TextField();
        GridPane.setConstraints(nameInput, 1, 0);


        // Password label
        Label passLabel = new Label("*Password: ");
        GridPane.setConstraints(passLabel, 0, 1);

        // Password input
        PasswordField passInput = new PasswordField();
        GridPane.setConstraints(passInput, 1, 1);


        // Password confirm label
        Label passConfLabel = new Label("*Confirm Password: ");
        GridPane.setConstraints(passConfLabel, 0, 2);

        // Password confirm input
        PasswordField passConfInput = new PasswordField();
        GridPane.setConstraints(passConfInput, 1, 2);


        // Error labels
        List<Label> errorLabels = new ArrayList<>();

        // Taken username label
        Label takenLabel = new Label("Username already taken");
        errorLabels.add(takenLabel);

        // Password match label
        Label matchLabel = new Label("Passwords do not match");
        errorLabels.add(matchLabel);

        // Invalid password label
        Label validPassLabel = new Label("Password must be at least 8 characters\nand contain a special character");
        errorLabels.add(validPassLabel);

        for (Label label : errorLabels)
        {
            label.setStyle("-fx-text-fill: red;" + "-fx-font-weight: bold;");
            label.setVisible(false);
            GridPane.setConstraints(label, 1, 4);
        };
        

        // Register button
        Button registerButton = new Button("Register");
        GridPane.setConstraints(registerButton, 1, 3);
        registerButton.setOnAction(e -> {

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
        });


        grid.getChildren().addAll(
                nameLabel, nameInput,
                passLabel, passInput,
                passConfLabel, passConfInput,
                registerButton
        );

        grid.getChildren().addAll(errorLabels);

        Scene scene = new Scene(grid);

        window.setScene(scene);
        window.showAndWait();

        return user;
    }
    
    private static boolean validatePassword(String password)
    {
        // Check min length
        if (password.length() < 8) return false;

        // Check contains special characters
        if (!password.matches(".*[^a-zA-Z0-9].*")) return false;

        return true;
    }
}