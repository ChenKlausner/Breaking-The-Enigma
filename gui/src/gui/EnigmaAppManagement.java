package gui;

import engine.Engine;
import engine.EngineUtility;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import fullApp.FullAppController;

import java.net.URL;

public class EnigmaAppManagement extends Application {
    private EngineUtility engine;

    @Override
    public void start(Stage primaryStage) throws Exception {
        engine = new Engine();
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/fullApp/fullApp.fxml");
        fxmlLoader.setLocation(url);
        Parent root = fxmlLoader.load(url.openStream());

        FullAppController fullAppController = fxmlLoader.getController();
        fullAppController.setEngine(engine);

        Scene scene = new Scene(root, 1000, 900);
        primaryStage.setTitle("Enigma Machine");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
