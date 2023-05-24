package com.example.javafxgame.service;

import com.example.javafxgame.dao.GameDao;
import com.example.javafxgame.dao.impl.DB;
import  com.example.javafxgame.dao.impl.GameDaoImpl;
import com.example.javafxgame.entities.Game;

import java.sql.*;
import java.util.List;

public class GameService {

    private GameDao gameDao = new GameDaoImpl();

    public List<Game> findAll() {
        return gameDao.findAll();
    }

    public void save(Game game) {

        gameDao.insert(game);
    }

    public void update(Game game, int gameID) {
        gameDao.update(game, gameID);
    }

    public void delete(Game game) {
        gameDao.delete(Integer.valueOf(game.getId()));
    }

    public void exporterVersExcel(String path){
        gameDao.exporterVersExcel(path);

    }
    public void importerDepuisExcel(String path){
        gameDao.importerDepuisExcel(path);
    }

    public void exporterVersTxt(String path) {
gameDao.exporterVersTxt(path);
    }

    public void importerDepuisTxt(String path) {
        gameDao.importerDepuisTxt(path);

    }
    public void exporterVersJson(String path) {
        gameDao.exporterVersJson(path);

    }

    public void importerDepuisJson(String path) {
        gameDao.importerDepuisJson(path);
    }

    // Method to get the number of games
    public int getNumberOfGames() {
        int count = 0;
        try {
            Connection conn= DB.getConnection();
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM games");

            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }

//            resultSet.close();
//            statement.close();
//            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }

}
