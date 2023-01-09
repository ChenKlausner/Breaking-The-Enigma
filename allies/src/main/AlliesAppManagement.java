package main;

import alliesApp.AlliesFullAppController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class AlliesAppManagement extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/alliesApp/AlliesFullApp.fxml");
        fxmlLoader.setLocation(url);
        Parent root = fxmlLoader.load(url.openStream());

        AlliesFullAppController fullAppController = fxmlLoader.getController();

        Scene scene = new Scene(root, 1000, 900);
        primaryStage.setTitle("Allies");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
