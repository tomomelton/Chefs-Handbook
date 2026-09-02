package gui;


import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Label;

import models.User;

/******************************************************************************

 File        : HomePage.java

 Project     : Chefs-Handbook

 Package     : gui

 Date        : Wednesday 02 September 2026

 Author      : Tom Melton

 Description : Class to build a users home page GUI / collection of recipes

 History     : 02/09/2026 - v1.00
 ******************************************************************************/

public class HomePage
{
    private User user;

    public HomePage(User user)
    {
        this.user = user;

        // Window
        Stage window = new Stage();
        window.setTitle("Chefs Handbook - Home");
        window.setMinHeight(700);
        window.setMinWidth(1000);

        // Layout
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);

        // Name label
        Label nameLabel = new Label("Welcome " + this.user.getUsername() + "!");


        layout.getChildren().add(nameLabel);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }
}