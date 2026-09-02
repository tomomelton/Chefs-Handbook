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

import static database.UserDAO.getUser;
import static utils.Hash.checkPassword;

/******************************************************************************

 File        : LoginPage.java

 Project     : Chefs-Handbook

 Package     : gui

 Date        : Wednesday 02 September 2026

 Author      : Tom Melton

 Description : Static class which builds the login GUI

 History     : 02/09/2026 - v1.00
 ******************************************************************************/

public class LoginPage
{
    private static User user;

    public static User load()
    {
        // Window
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Chefs Handbook - Login");
        window.setMinHeight(250);
        window.setMinWidth(400);

        // Grid
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);
        grid.setAlignment(Pos.CENTER);

        // Name label
        Label nameLabel = new Label("Username: ");
        GridPane.setConstraints(nameLabel, 0, 0);

        // Name input
        TextField nameInput = new TextField();
        nameInput.setText("Tom");
        GridPane.setConstraints(nameInput, 1, 0);

        // Password label
        Label passLabel = new Label("Password: ");
        GridPane.setConstraints(passLabel, 0, 1);

        // Password input
        PasswordField passInput = new PasswordField();
        passInput.setPromptText("password");
        passInput.setText("password");
        GridPane.setConstraints(passInput, 1, 1);

        // Error label
        Label errorLabel = new Label("Username or password incorrect");
        errorLabel.setStyle(
                "-fx-text-fill: red;" +
                "-fx-font-weight: bold;"
        );
        errorLabel.setVisible(false);
        GridPane.setConstraints(errorLabel, 1, 3);

        // Login button
        Button loginButton = new Button("Login");
        GridPane.setConstraints(loginButton, 1, 2);
        loginButton.setOnAction(e -> {

            boolean valid = login(nameInput.getText(), passInput.getText());

            if (valid)
                window.close();
            else
            {
                errorLabel.setVisible(true);
                passInput.clear();
            }
        });


        grid.getChildren().addAll(nameLabel, nameInput, passLabel, passInput, loginButton, errorLabel);

        Scene scene = new Scene(grid);

        window.setScene(scene);
        window.showAndWait();

        return user;
    }

    private static boolean login(String username, String password)
    {
        try
        {
            // Get users details from database
            user = getUser(username);

            // Check users password matches password given
            assert user != null;
            return checkPassword(password, user.getPasswordHash());
        }
        catch (RuntimeException e)
        {
            return false;
        }

    }
}
