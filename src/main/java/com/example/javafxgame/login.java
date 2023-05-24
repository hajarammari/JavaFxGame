package com.example.javafxgame;

import com.example.javafxgame.dao.impl.DB;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class login {
    @FXML
    private TextField userId;

    @FXML
    private PasswordField pwd;

    @FXML
    private Button okBtn;

    @FXML
    private Button cancelBtn;
    private Runnable onLoginSuccess;


    // Méthode exécutée lorsque le bouton "OK" est cliqué
    @FXML
    private void okClicked() {
        String username = userId.getText();
        String password = pwd.getText();

        // Votre code pour vérifier l'authentification avec la base de données MySQL
        try {
            // Récupérer une connexion à la base de données
            Connection connection = DB.getConnection();

            // Préparer la requête SQL pour vérifier les informations d'authentification
            String sql = "SELECT * FROM admin WHERE userId = ? AND pwd = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);

            // Exécuter la requête
            ResultSet resultSet = statement.executeQuery();

            // Vérifier si l'utilisateur est authentifié avec succès
            if (resultSet.next()) {
                System.out.println("Authentification réussie !");
                if (onLoginSuccess != null) {
                    onLoginSuccess.run();
                }
            } else {
                System.out.println("Échec de l'authentification !");

            }

            // Fermer les ressources
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Méthode exécutée lorsque le bouton "Cancel" est cliqué
    @FXML
    private void cancelClicked() {
        // Votre code pour gérer l'annulation
        System.out.println("Opération annulée !");
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }
    public  void setOnLoginSuccess(Runnable onLoginSuccess) {
        this.onLoginSuccess = onLoginSuccess;
    }


}
