import gui.HomeWindow;

import javafx.application.Application;
import javafx.stage.Stage;
import models.User;

public class Main extends Application
{
    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage)
    {
//        StartWindow.load();

        // Temp fix while VPN is down
        new HomeWindow(new User("Tom", "1"));
    }


}
