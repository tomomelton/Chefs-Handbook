package gui;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Objects;

/******************************************************************************

 File        : ConfirmationBox.java

 Project     : Chefs-Handbook

 Package     : gui

 Date        : Sunday 06 September 2026

 Author      : Tom Melton

 Description : Confirmation GUI which requires a user input before they can proceed

 History     : 06/09/2026 - v1.00
 ******************************************************************************/

public class ConfirmationBox
{
    private boolean response;

    // Window
    private Stage window;

    // Scene
    private Scene scene;

    // Layout
    private VBox layout;
    private HBox buttons;

    // Label
    private Label message;

    // Buttons
    private Button confirmButton;
    private Button cancelButton;


    // Constructor
    public ConfirmationBox(String message)
    {
        // Label
        this.message = new Label(message);
        this.message.getStyleClass().add("red-label");


        // Buttons
        confirmButton = new Button("Yes");
        confirmButton.getStyleClass().add("red-button");
        confirmButton.setDefaultButton(false);
        confirmButton.setOnAction(e -> {response = true; window.close();});

        cancelButton = new Button("No");
        cancelButton.getStyleClass().add("red-button");
        cancelButton.setDefaultButton(true);
        cancelButton.setOnAction(e -> {response = false; window.close();});


        // Layout
        buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(confirmButton, cancelButton);

        layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.getChildren().addAll(this.message, buttons);


        // Scene
        scene = new Scene(layout);
        scene.getStylesheets().add(
            Objects.requireNonNull(ConfirmationBox.class.getResource("/styles/main.css")).toExternalForm()
    );


        // Window
        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setMinWidth(350);
        window.setMinHeight(250);
        window.setScene(scene);
        window.setOnShown(e -> cancelButton.requestFocus());
        window.showAndWait();
    }


    // Getter
    public boolean getResponse()
    {
        return response;
    }
}