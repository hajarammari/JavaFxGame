package com.example.javafxgame;

import com.example.javafxgame.service.GameService;

public class main1 {
    public static void main(String[] args) {
        GameService game = new GameService();
        //game.exporterVersJson("fileJson.json");
        //game.importerDepuisJson("fileJson.json");
       game.importerDepuisTxt("txt.txt");
        //game.exporterVersTxt("txt.txt");


    }
}
