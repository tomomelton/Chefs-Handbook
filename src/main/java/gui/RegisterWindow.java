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
import org.kordamp.ikonli.fontawesome6.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static database.UserDAO.createUser;
import static database.UserDAO.getUser;

/******************************************************************************

 File        : RegisterPage.java

 Project     : Chefs-Handbook

 Package     : gui

 Date        : Wednesday 02 September 2026

 Author      : Tom Melton

 Description : Static class to build a register GUI to create a new user

 History     : 11/09/2026 - v2.00
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
    private static TextField passVisibleInput;
    private static TextField passConfVisibleInput;
    private static PasswordField passHiddenInput;
    private static PasswordField passConfHiddenInput;

    // Lists
    private static List<Label> errorLabels;

    // Buttons
    private static Button showPasswordButton;
    private static Button showConfirmPasswordButton;
    private static Button registerButton;
    private static Button cancelButton;


    // Public Methods
    public static User load()
    {
        // Labels
        nameLabel = new Label("*Username: ");
        passLabel = new Label("*Password: ");
        passConfLabel = new Label("*Confirm Password: ");
        takenLabel = new Label("Username already taken");
        matchLabel = new Label("Passwords do not match");
        validPassLabel = new Label("Password must be at least 8 characters and contain a special character");
        validPassLabel.setWrapText(true);


        // Fields
        nameInput = new TextField();

        passVisibleInput = new TextField();
        passVisibleInput.setVisible(false);

        passConfVisibleInput = new TextField();
        passConfVisibleInput.setVisible(false);

        passHiddenInput = new PasswordField();
        passHiddenInput.textProperty().bindBidirectional(passVisibleInput.textProperty());

        passConfHiddenInput = new PasswordField();
        passConfHiddenInput.textProperty().bindBidirectional(passConfVisibleInput.textProperty());


        // Lists
        errorLabels = new ArrayList<>();
        errorLabels.add(takenLabel);
        errorLabels.add(matchLabel);
        errorLabels.add(validPassLabel);

        // Configure labels
        for (Label label : errorLabels)
        {
            label.getStyleClass().add("error");
            label.setVisible(false);
            GridPane.setConstraints(label, 1, 4);
        };


        // Buttons
        registerButton = new Button("Register");
        registerButton.setOnAction(e -> register());
        registerButton.setDefaultButton(true);

        cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> window.close());

        showPasswordButton = new Button("Show");
        showPasswordButton.setOnAction(e -> togglePasswordVisible(
                showPasswordButton, passHiddenInput, passVisibleInput
        ));

        showConfirmPasswordButton = new Button("Show");
        showConfirmPasswordButton.setOnAction(e -> togglePasswordVisible(
                showConfirmPasswordButton, passConfHiddenInput, passConfVisibleInput
        ));


        // Layouts
        buttons = new HBox(10);
        buttons.getChildren().addAll(registerButton, cancelButton);

        grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);
        grid.setAlignment(Pos.CENTER);
        grid.getChildren().addAll(errorLabels);
        grid.getChildren().addAll(
                nameLabel, nameInput,
                passLabel, passHiddenInput, passVisibleInput, showPasswordButton,
                passConfLabel, passConfHiddenInput, passConfVisibleInput, showConfirmPasswordButton,
                buttons
        );


        // Grid
        GridPane.setConstraints(nameLabel,                  0, 0);
        GridPane.setConstraints(nameInput,                  1, 0);
        GridPane.setConstraints(passLabel,                  0, 1);
        GridPane.setConstraints(passHiddenInput,            1, 1);
        GridPane.setConstraints(passVisibleInput,           1, 1);
        GridPane.setConstraints(showPasswordButton,         2, 1);
        GridPane.setConstraints(passConfLabel,              0, 2);
        GridPane.setConstraints(passConfHiddenInput,        1, 2);
        GridPane.setConstraints(passConfVisibleInput,       1, 2);
        GridPane.setConstraints(showConfirmPasswordButton,  2, 2);
        GridPane.setConstraints(buttons,                    1, 3);


        // Scene
        scene = new Scene(grid);
        scene.getStylesheets().add(
                Objects.requireNonNull(RegisterWindow.class.getResource("/styles/main.css")).toExternalForm()
        );


        // Window
        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Chefs Handbook - Register");
        window.setMinHeight(250);
        window.setMinWidth(400);
        window.setScene(scene);
        window.showAndWait();

        return user;
    }


    // Button Methods
    private static void register()
    {
        User tempUser = getUser(nameInput.getText());

        // Check password entries are the same
        if (!passHiddenInput.getText().equals(passConfHiddenInput.getText()))
        {
            takenLabel.setVisible(false);
            matchLabel.setVisible(true);
            validPassLabel.setVisible(false);

            passHiddenInput.clear();
            passConfHiddenInput.clear();
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
        else if (!validatePassword(passHiddenInput.getText()))
        {
            takenLabel.setVisible(false);
            matchLabel.setVisible(false);
            validPassLabel.setVisible(true);

            passHiddenInput.clear();
            passConfHiddenInput.clear();
        }
        // Create user
        else
        {
            createUser(
                    nameInput.getText(),
                    passHiddenInput.getText()
            );

            user = getUser(nameInput.getText());

            window.close();
        }
    }

    private static void togglePasswordVisible(Button button, PasswordField passwordField, TextField visiblePassword)
    {
        boolean showing = visiblePassword.isVisible();

        visiblePassword.setVisible(!showing);

        passwordField.setVisible(showing);

        button.setText(showing ? "Show" : "Hide");
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