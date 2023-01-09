package main;

import agentApp.AgentFullAppController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class AgentAppManagement extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/agentApp/AgentFullApp.fxml");
        fxmlLoader.setLocation(url);
        Parent root = fxmlLoader.load(url.openStream());

        AgentFullAppController fullAppController = fxmlLoader.getController();

        Scene scene = new Scene(root, 1000, 900);
        primaryStage.setTitle("Agent");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
