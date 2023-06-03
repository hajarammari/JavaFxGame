package com.example.javafxgame;

import com.example.javafxgame.entities.Game;
import com.example.javafxgame.service.GameService;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TableViewController implements Initializable {
    private GameService gameService;
    @FXML
    private TableColumn<Game, Integer> id;
    @FXML
    private TableColumn<Game, String> title;
    @FXML
    private TableColumn<Game, String> genre;
    @FXML
    private TableColumn<Game, Integer> releaseYear;
    @FXML
    private TableColumn<Game, Double> price;
    @FXML
    private TableColumn<Game, Integer> rating;
    @FXML
    private TableColumn<Game, String> company;
    @FXML
    private TableView<Game> gametable;
    @FXML
    private TextField rowCountLabel;
    @FXML
    private Button ajouter;
    @FXML
    private Label gameCountText;
    private IntegerProperty rowCount;
    @FXML
    private Label gameCountLabel;
    ObservableList<Game> gameList = FXCollections.observableArrayList();

    public void reload(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("gameTableView.fxml"));
        Parent root = fxmlLoader.load();
        TableViewController controller = fxmlLoader.getController();
        GameService gameService = new GameService();
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configureTableColumns();
        addDeleteAndUpdateButtons();
        gameService = new GameService();
        updateGameCountLabel();
    }

    private void updateGameCountLabel() {
        int gameCount = gameService.getNumberOfGames();
        gameCountLabel.setText(" (" + gameCount + " games)");
    }

    private void configureTableColumns() {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        genre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        releaseYear.setCellValueFactory(new PropertyValueFactory<>("releaseYear"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        rating.setCellValueFactory(new PropertyValueFactory<>("rating"));
        company.setCellValueFactory(new PropertyValueFactory<>("company"));
    }

    void setGameService(GameService gameService) {
        this.gameService = gameService;
    }
    @FXML
    public  void exporter() {
        gameService.exporterVersExcel("src/main/resources/ImportExportFiles/ExportExcel.xlsx");
    }


    void loadData() {
        gameList.addAll(gameService.findAll());
        gametable.setItems(gameList);
    }

    @FXML
    private void handleAjouterButtonAction(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("addGameForms.fxml"));
            Parent afficherPage = loader.load();

            // Create the scene to display the loaded FXML file
            Scene afficherScene = new Scene(afficherPage);

            // Load the styles.css file
            afficherScene.getStylesheets().add("styles.css");

            // Get the primary stage and update its scene
            Stage primaryStage = (Stage) ajouter.getScene().getWindow();
            primaryStage.setScene(afficherScene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public  void importer(ActionEvent event) throws IOException {
        gameService.importerDepuisExcel("src/main/resources/ImportExportFiles/ImportExcel.xlsx");
        reload(event);


    }
    private void addDeleteAndUpdateButtons(){
        // Create the "Supprimer" column
        TableColumn<Game, Void> deleteColumn = new TableColumn<>("Supprimer");
        deleteColumn.setCellFactory(new Callback<TableColumn<Game, Void>, TableCell<Game, Void>>() {
            @Override
            public TableCell<Game, Void> call(TableColumn<Game, Void> param) {
                return new TableCell<Game, Void>() {
                    private final Button deleteButton = new Button("Delete");

                    {
                        deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
                        deleteButton.setOnAction(event -> {
                            Game game = getTableView().getItems().get(getIndex());
                            gameService.delete(game);
                            try {
                                reload(event);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    }


                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(deleteButton);
                        }
                    }
                };
            }
        });

        // Create the "Modifier" column
        TableColumn<Game, Void> updateColumn = new TableColumn<>("Modifier");
        updateColumn.setCellFactory(new Callback<TableColumn<Game, Void>, TableCell<Game, Void>>() {
            @Override
            public TableCell<Game, Void> call(TableColumn<Game, Void> param) {
                return new TableCell<Game, Void>() {
                    private final Button updateButton = new Button("Update");

                    {
                        updateButton.setOnAction(event -> {
                            Game game = getTableView().getItems().get(getIndex());

                            FXMLLoader loader = new FXMLLoader(getClass().getResource("updateGameForm.fxml"));
                            try {
                                Parent root = loader.load();
                                addGame addGameController = loader.getController();
                                addGameController.setUpdate(true);
                                addGameController.setTextField(game.getId(), game.getTitle(), game.getGenre(), game.getReleaseYear(), game.getPrice(), game.getRating(), game.getCompany());

                                Scene scene = new Scene(root);
                                scene.getStylesheets().add("styles.css");

                                Stage stage = new Stage();
                                stage.setScene(scene);
                                stage.initStyle(StageStyle.UTILITY);
                                stage.show();
                            } catch (IOException e) {
                                Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, e);
                            }
                        });

                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(updateButton);
                        }
                    }
                };
            }
        });

        // Add the delete and update columns to the TableView
        gametable.getColumns().add(deleteColumn);
        gametable.getColumns().add(updateColumn);
    }


}
