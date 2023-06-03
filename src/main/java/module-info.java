module com.example.javafxgame {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.apache.poi.ooxml;
    requires com.google.gson;
    requires json.simple;

    opens com.example.javafxgame.entities to gson;
    opens com.example.javafxgame to javafx.fxml,javafx.base;
    exports com.example.javafxgame;
    exports com.example.javafxgame.entities;
    exports com.example.javafxgame.service;
    exports com.example.javafxgame.dao;
    exports com.example.javafxgame.dao.impl;
}