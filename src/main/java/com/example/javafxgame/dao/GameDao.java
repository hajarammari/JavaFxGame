package com.example.javafxgame.dao;

import com.example.javafxgame.entities.Game;

import java.util.List;

public interface GameDao {

    void insert(Game game);

    void update(Game game, Integer id);

    void delete(Integer id);

    Game findById(Integer id);

    List<Game> findAll();

    public int getNumberOfGames();

    public void exporterVersExcel(String path);
    public void importerDepuisExcel(String path) ;

    public void exporterVersJson(String path);
    public void importerDepuisJson(String path) ;

    public void exporterVersTxt(String path);
    public void importerDepuisTxt(String path) ;

}
