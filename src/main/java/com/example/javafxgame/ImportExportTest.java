package com.example.javafxgame;

import com.example.javafxgame.service.GameService;

public class ImportExportTest {
    public static void main(String[] args) {
        GameService game = new GameService();
        /*----------------------------JSON----------------------------*/
        game.exporterVersJson("src/main/resources/ImportExportFiles/ExportJson.json");
        game.importerDepuisJson("src/main/resources/ImportExportFiles/ImportJson.json");
        /*----------------------------EXCEL----------------------------*/
        game.importerDepuisTxt("src/main/resources/ImportExportFiles/ImportText.txt");
        game.exporterVersTxt("src/main/resources/ImportExportFiles/ExportTxt.xlsx");
        /*----------------------------TEXT----------------------------*/
        game.importerDepuisExcel("src/main/resources/ImportExportFiles/ImportExcel.xlsx");
        game.exporterVersExcel("src/main/resources/ImportExportFiles/ExportExcel.xlsx");
    }
}
