package gui;

import javafx.geometry.HPos;
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

import java.util.Objects;

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

public class LoginWindow
{
    private static User user;

    // Scene
    private static Scene scene;

    // Window
    private static Stage window;

    // Layouts
    private static GridPane grid;
    private static HBox buttons;

    // Labels
    private static Label nameLabel;
    private static Label passLabel;
    private static Label errorLabel;

    // Fields
    private static TextField nameInput;
    private static TextField passVisibleInput;
    private static PasswordField passHiddenInput;

    // Buttons
    private static Button loginButton;
    private static Button cancelButton;
    private static Button showPasswordButton;


    // Public Methods
    public static User load()
    {
        // Labels
        nameLabel = new Label("Username: ");
        passLabel = new Label("Password: ");

        errorLabel = new Label("Username or password incorrect");
        errorLabel.setVisible(false);
        errorLabel.getStyleClass().add("error");


        // Fields
        nameInput = new TextField();
        nameInput.setPromptText("Enter Username...");
        nameInput.setText("Tom");
        nameInput.getStyleClass().add("field-border");

        passVisibleInput = new TextField();
        passVisibleInput.setVisible(false);
        passVisibleInput.setPromptText("Enter password...");
        passVisibleInput.setText("password");
        passVisibleInput.getStyleClass().add("field-border");

        passHiddenInput = new PasswordField();
        passHiddenInput.setVisible(true);
        passHiddenInput.setPromptText("Enter password...");
        passHiddenInput.setText("password");
        passHiddenInput.getStyleClass().add("field-border");
        passHiddenInput.textProperty().bindBidirectional(passVisibleInput.textProperty());


        // Buttons
        loginButton = new Button("Login");
        loginButton.getStyleClass().add("red-button");
        loginButton.setDefaultButton(true);
        loginButton.setOnAction(e -> {

            boolean valid = login(nameInput.getText(), passHiddenInput.getText());

            if (valid)
                window.close();
            else
            {
                errorLabel.setVisible(true);
                passHiddenInput.clear();
            }
        });

        cancelButton = new Button("Cancel");
        cancelButton.getStyleClass().add("red-button");
        cancelButton.setOnAction(e -> window.close());

        showPasswordButton = new Button("Show");
        showPasswordButton.getStyleClass().add("red-button");
        showPasswordButton.setPrefWidth(55);
        showPasswordButton.setOnAction(e -> togglePasswordVisible(
                showPasswordButton, passHiddenInput, passVisibleInput
        ));


        // Layouts
        buttons = new HBox(10);
        buttons.getChildren().addAll(loginButton, cancelButton);

        grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);
        grid.setAlignment(Pos.CENTER);
        grid.getChildren().addAll(
                nameLabel, nameInput,
                passLabel, passVisibleInput, passHiddenInput, showPasswordButton,
                buttons, errorLabel
        );


        // Grid
        GridPane.setConstraints(nameLabel,             0, 0);
        GridPane.setConstraints(nameInput,             1, 0);
        GridPane.setConstraints(passLabel,             0, 1);
        GridPane.setConstraints(passVisibleInput,      1, 1);
        GridPane.setConstraints(passHiddenInput,       1, 1);
        GridPane.setConstraints(showPasswordButton,    2, 1);
        GridPane.setConstraints(buttons,               1, 2);

        GridPane.setConstraints(errorLabel,            0, 3);
        GridPane.setColumnSpan(errorLabel, 3);
        GridPane.setHalignment(errorLabel, HPos.CENTER);


        // Scene
        scene = new Scene(grid);
        scene.getStylesheets().add(
                Objects.requireNonNull(LoginWindow.class.getResource("/styles/main.css")).toExternalForm()
        );

        // Window
        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Chefs Handbook - Login");
        window.setMinHeight(250);
        window.setMinWidth(400);
        window.setScene(scene);
        window.showAndWait();


        return user;
    }


    // Button Methods
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

    private static void togglePasswordVisible(Button button, PasswordField passwordField, TextField visiblePassword)
    {
        boolean showing = visiblePassword.isVisible();

        visiblePassword.setVisible(!showing);

        passwordField.setVisible(showing);

        button.setText(showing ? "Show" : "Hide");
    }
}
