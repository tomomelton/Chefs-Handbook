package gui;


import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
        // Layout
        layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);

        buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER);


        // Label
        this.message = new Label(message);
        this.message.setStyle("-fx-font-size: 20; -fx-font-weight: bold");


        // Buttons
        confirmButton = new Button("Yes");
        confirmButton.setOnAction(e -> {response = true; window.close();});

        cancelButton = new Button("No");
        cancelButton.setOnAction(e -> {response = false; window.close();});


        // Build Layout
        layout.getChildren().addAll(this.message, buttons);
        buttons.getChildren().addAll(confirmButton, cancelButton);


        // Scene
        scene = new Scene(layout);


        // Window
        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setMinWidth(350);
        window.setMinHeight(250);
        window.setScene(scene);
        window.showAndWait();
    }


    // Getter
    public boolean getResponse()
    {
        return response;
    }
}