package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import uboatApp.FullAppController;

import java.net.URL;

public class UBoatAppManagement extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/uboatApp/fullApp.fxml");
        fxmlLoader.setLocation(url);
        Parent root = fxmlLoader.load(url.openStream());

        FullAppController fullAppController = fxmlLoader.getController();

        Scene scene = new Scene(root, 1000, 900);
        primaryStage.setTitle("UBoat");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
