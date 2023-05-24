package com.example.javafxgame;

import com.example.javafxgame.service.GameService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("login.fxml"));
        Parent loginRoot = loginLoader.load();
        login loginController = loginLoader.getController();
        //Scene loginScene = new Scene(loginRoot);
        FXMLLoader tableLoader = new FXMLLoader(getClass().getResource("gameTableView.fxml"));
        Parent tableRoot = tableLoader.load();
        TableViewController affichage = tableLoader.getController();
        GameService gameService = new GameService(); // Ici on l'initialise correctement.
        affichage.setGameService(gameService);
        Scene tableScene = new Scene(tableRoot);
        loginController.setOnLoginSuccess(() -> {
            stage.setScene(tableScene);

            affichage.loadData();
        });

        //CSS
        VBox root = new VBox();
        Scene loginScene = new Scene(loginRoot, 1000, 300);
        loginScene.getStylesheets().add("styles.css");
        tableScene.getStylesheets().add("styles.css");

        // Set the login scene as the primary stage scene
        stage.setScene(loginScene);
        stage.show();

        // Set up the Enter key press event handler for okBtn
        Button okBtn = (Button) loginScene.lookup("#okBtn");
        loginScene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                okBtn.fire(); // Simulate button click
            }
        });
    }

    public static void main(String[] args) {
        launch();
    }
}