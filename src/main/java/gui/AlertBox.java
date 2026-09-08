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

 File        : AlertBox.java

 Project     : Chefs-Handbook

 Package     : gui

 Date        : Sunday 06 September 2026

 Author      : Tom Melton

 Description : GUI to give the user a custom alert

 History     : 06/09/2026 - v1.00
 ******************************************************************************/

public class AlertBox
{
    // Window
    private Stage window;

    // Scene
    private Scene scene;

    // Layout
    private VBox layout;

    // Label
    private Label message;

    // Buttons
    private Button confirmButton;


    // Constructor
    public AlertBox(String message)
    {
        // Layout
        layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);


        // Label
        this.message = new Label(message);
        this.message.setStyle("-fx-font-size: 20; -fx-font-weight: bold");


        // Buttons
        confirmButton = new Button("Okay");
        confirmButton.setOnAction(e -> {window.close();});


        // Build Layout
        layout.getChildren().addAll(this.message, confirmButton);


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
}