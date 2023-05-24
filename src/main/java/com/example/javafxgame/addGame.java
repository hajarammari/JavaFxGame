package com.example.javafxgame;

import com.example.javafxgame.entities.Game;
import com.example.javafxgame.service.GameService;


import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class addGame {

    @FXML
    private TextField title;
    @FXML
    private TextField genre;
    @FXML
    private TextField year;
    @FXML
    private TextField price;
    @FXML
    private TextField rating;
    @FXML
    private TextField company;
    private  boolean update;
    int gameID=0;

    @FXML
    protected void onHelloButtonClick(ActionEvent event) throws IOException {
        String title = this.title.getText();
        String genre = this.genre.getText();
        String year = this.year.getText();
        String price = this.price.getText();
        String rating = this.rating.getText();
        String company = this.company.getText();

        GameService gameService = new GameService();
        Game game = new Game(gameID,title, genre, parseInt(year), parseDouble(price),  parseInt(rating), company);
        if(update==false){
            gameService.save(game);
        }
        else{
            gameService.update(game,gameID);
        }
        onClearButtonClick();

        this.title.clear();
        this.genre.clear();
        this.year.clear();
        this.price.clear();
        this.rating.clear();
        this.company.clear();

        // Reload the gameTableView
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("gameTableView.fxml"));
        Parent root = fxmlLoader.load();
        TableViewController controller = fxmlLoader.getController();
        controller.setGameService(gameService);
        controller.loadData();

        Scene newScene = new Scene(root);

        // Reapply the CSS styles to the new scene
        newScene.getStylesheets().add("styles.css");

        // Get the current stage
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        // Set the new scene on the current stage
        currentStage.setScene(newScene);
        currentStage.show();
    }



    private Stage stage;
    private Scene scene;
    private Parent root;
    public void onBackButtonClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("gameTableView.fxml"));
        Parent root = fxmlLoader.load();
        TableViewController controller = fxmlLoader.getController();
        GameService gameService = new GameService();
        controller.setGameService(gameService);
        controller.loadData();

        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        scene.getStylesheets().addAll(primaryStage.getScene().getStylesheets()); // Add the current stylesheets to the new scene

        primaryStage.setScene(scene);
        primaryStage.show();
    }



    public void onClearButtonClick() {
        this.title.clear();
        this.genre.clear();
        this.year.clear();
        this.price.clear();
        this.rating.clear();
        this.company.clear();
    }

    public void setUpdate(boolean b) {
        this.update=b;
    }

    public void setTextField(int id, String title, String genre, int year, double price, int rating, String company) {
        this.gameID=id;
        this.title.setText(title);
        this.genre.setText(genre);
        this.year.setText(String.valueOf(year));
        this.price.setText(String.valueOf(price));
        this.rating.setText(String.valueOf(rating));
        this.company.setText(company);
    }
}