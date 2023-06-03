package com.example.javafxgame.dao.impl;

import com.example.javafxgame.dao.GameDao;
import com.example.javafxgame.entities.Game;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;

import java.io.*;

import com.google.gson.Gson;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class GameDaoImpl implements GameDao {
    private Connection conn= DB.getConnection();

    public PreparedStatement jeux(Game game, PreparedStatement ps) throws SQLException {
        ps.setString(1, game.getTitle());
        ps.setString(2, game.getGenre());
        ps.setInt(3, game.getReleaseYear());
        ps.setDouble(4, game.getPrice());
        ps.setInt(5, game.getRating());
        ps.setString(6, game.getCompany());
        return ps;
    }

    @Override
    public void insert(Game game) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("INSERT INTO games (title, genre, releaseYear, price, rating, company) VALUES (?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            jeux(game,ps);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    game.setTitle(String.valueOf(id));
                }
                DB.closeResultSet(rs);
            } else {
                System.out.println("Insert failed");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            DB.closeStatement(ps);
        }
    }

    @Override
    public void update(Game game ,Integer id ) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("UPDATE games SET title = ? , genre = ? , releaseYear = ? , price = ? , rating = ? , company = ? WHERE id = ?");
            jeux(game,ps);
            ps.setInt(7, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Update failed");;
        } finally {
            DB.closeStatement(ps);
        }
    }

    @Override
    public void delete(Integer id) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("DELETE FROM games WHERE id = ?");
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Delete failed");;
        } finally {
            DB.closeStatement(ps);
        }
    }

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

    @Override
    public List<Game> findAll() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = this.conn.prepareStatement("SELECT * FROM games");
            rs = ps.executeQuery();

            List<Game> listDepartment = new ArrayList<>();
            while (rs.next()) {
                Game game = new Game();
                game.setId(rs.getInt("id"));
                game.setTitle(rs.getString("title"));
                game.setGenre(rs.getString("genre"));
                game.setReleaseYear(rs.getInt("releaseYear"));
                game.setPrice(rs.getDouble("price"));
                game.setRating(rs.getInt("rating"));
                game.setCompany(rs.getString("company"));
                listDepartment.add(game);
            }
            return listDepartment;
        } catch (SQLException e) {
            System.err.println("problème de requête pour sélectionner ");;
            return null;
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(ps);
        }

    }

    @Override
    public void exporterVersExcel(String path) {
        // Base de données -> Fichiers Excel
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Games");
            List<Game> games = findAll();
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Title");
            headerRow.createCell(1).setCellValue("Genre");
            headerRow.createCell(2).setCellValue("Release Year");
            headerRow.createCell(3).setCellValue("Price");
            headerRow.createCell(4).setCellValue("Rating");
            headerRow.createCell(5).setCellValue("Company");
            int rowNum = 1;
            for (Game game : games) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(game.getTitle());
                row.createCell(1).setCellValue(game.getGenre());
                row.createCell(2).setCellValue(game.getReleaseYear());
                row.createCell(3).setCellValue(game.getPrice());
                row.createCell(4).setCellValue(game.getRating());
                row.createCell(5).setCellValue(game.getCompany());
            }
            try (FileOutputStream outputStream = new FileOutputStream(path)) {
                workbook.write(outputStream);
            }
            System.out.println("The export to Excel file has been completed successfully!");
        } catch (IOException e) {
            System.out.println("Error occurred during the export to the Excel file: " + e.getMessage());
        }
    }

    @Override
    public void importerDepuisExcel(String path) {
        try (Workbook workbook = new XSSFWorkbook(path)) {
            Sheet sheet = workbook.getSheetAt(0);
            int numRow = 1;
            Row row = sheet.getRow(numRow++);
            while (row != null) {
                Game game = new Game();
                game.setTitle(row.getCell(0).getStringCellValue());
                game.setGenre(row.getCell(1).getStringCellValue());
                game.setReleaseYear((int) row.getCell(2).getNumericCellValue());
                game.setPrice(row.getCell(3).getNumericCellValue());
                game.setRating((int) row.getCell(4).getNumericCellValue());
                game.setCompany(row.getCell(5).getStringCellValue());
                insert(game);
                row = sheet.getRow(numRow++);
            }
            System.out.println("The import from EXCEL file has been completed successfully!");
        } catch (IOException e) {
            System.out.println("Error occurred during the import from the EXCEL file:" + e.getMessage());
        }
    }

    @Override
    public void exporterVersJson(String path) {
        String url = "jdbc:mysql://localhost:3306/Game";
        String username = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM games")) {

            List<Game> games = new ArrayList<>();
            while (resultSet.next()) {
                Game game = new Game();
                game.setId(resultSet.getInt("id"));
                game.setTitle(resultSet.getString("title"));
                game.setGenre(resultSet.getString("genre"));
                game.setReleaseYear(resultSet.getInt("releaseYear"));
                game.setPrice(resultSet.getDouble("price"));
                game.setRating(resultSet.getInt("rating"));
                game.setCompany(resultSet.getString("company"));
                games.add(game);
            }

            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .registerTypeAdapter(Game.class, new GameAdapter())
                    .create();

            try (FileWriter writer = new FileWriter(path)) {
                gson.toJson(games, writer);
                System.out.println("Exportation vers le fichier JSON réussie !");
            } catch (IOException e) {
                System.out.println("Erreur lors de l'exportation vers le fichier JSON : " + e.getMessage());
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de la connexion à la base de données : " + e.getMessage());
        }
    }

    private static class GameAdapter extends TypeAdapter<Game> {
        @Override
        public void write(JsonWriter out, Game game) throws IOException {
            out.beginObject();
            out.name("id").value(game.getId());
            out.name("title").value(game.getTitle());
            out.name("genre").value(game.getGenre());
            out.name("releaseYear").value(game.getReleaseYear());
            out.name("price").value(game.getPrice());
            out.name("rating").value(game.getRating());
            out.name("company").value(game.getCompany());
            out.endObject();
        }

        @Override
        public Game read(JsonReader in) throws IOException {
            Game game = new Game();
            in.beginObject();
            while (in.hasNext()) {
                String name = in.nextName();
                switch (name) {
                    case "id":
                        game.setId(in.nextInt());
                        break;
                    case "title":
                        game.setTitle(in.nextString());
                        break;
                    case "genre":
                        game.setGenre(in.nextString());
                        break;
                    case "releaseYear":
                        game.setReleaseYear(in.nextInt());
                        break;
                    case "price":
                        game.setPrice(in.nextDouble());
                        break;
                    case "rating":
                        game.setRating(in.nextInt());
                        break;
                    case "company":
                        game.setCompany(in.nextString());
                        break;
                    default:
                        in.skipValue();
                        break;
                }
            }
            in.endObject();
            return game;
        }
    }
    @Override
    public void importerDepuisJson(String path) {
        String url = "jdbc:mysql://localhost:3306/Game";
        String username = "root";
        String password = "";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement()) {

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Game.class, new GameAdapter())
                    .create();
            try (FileReader reader = new FileReader(path)) {
                Game[] games = gson.fromJson(reader, Game[].class);

                for (Game game : games) {
                    String insertQuery = "INSERT INTO games (title, genre, releaseYear, price, rating, company) " +
                            "VALUES (?, ?, ?, ?, ?, ?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
                    preparedStatement.setString(1, game.getTitle());
                    preparedStatement.setString(2, game.getGenre());
                    preparedStatement.setInt(3, game.getReleaseYear());
                    preparedStatement.setDouble(4, game.getPrice());
                    preparedStatement.setInt(5, game.getRating());
                    preparedStatement.setString(6, game.getCompany());

                    preparedStatement.executeUpdate();
                }

                System.out.println("The import from JSON file has been completed successfully!");
            } catch (IOException e) {
                System.out.println("Error occurred during the import from the JSON file " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Error Establishing a Database Connection : " + e.getMessage());
        }
    }

    @Override
    public void exporterVersTxt(String path) {

        String url = "jdbc:mysql://localhost:3306/Game";
        String username = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM games")) {

            List<String> lines = new ArrayList<>();

            while (resultSet.next()) {
                StringBuilder line = new StringBuilder();
                line.append(resultSet.getString("id")).append("\t");
                line.append(resultSet.getString("title")).append("\t");
                line.append(resultSet.getString("genre")).append("\t");
                line.append(resultSet.getString("releaseYear")).append("\t");
                line.append(resultSet.getString("price")).append("\t");
                line.append(resultSet.getString("rating")).append("\t");
                line.append(resultSet.getString("company"));
                lines.add(line.toString());
            }

            try (FileWriter writer = new FileWriter(path)) {
                for (String line : lines) {
                    writer.write(line);
                    writer.write(System.lineSeparator());
                }
                System.out.println("The export to TEXT file has been completed successfully!");
            } catch (IOException e) {
                System.out.println("Error occurred during the export to the TEXT file : " + e.getMessage());
            }

        } catch (SQLException e) {
            System.out.println("Error Establishing a Database Connection : " + e.getMessage());
        }

    }

    @Override
    public void importerDepuisTxt(String path) {
        String url = "jdbc:mysql://localhost:3306/Game";
        String username = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement()) {

            List<String> lines = new ArrayList<>();

            try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
            } catch (IOException e) {
                System.out.println("Erreur lors de la lecture du fichier texte : " + e.getMessage());
                return;
            }

            String insertQuery = "INSERT INTO games (title, genre, releaseYear, price, rating, company) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            for (String line : lines) {
                String[] values = line.split("\t");
                preparedStatement.setString(1, values[1]);
                preparedStatement.setString(2, values[2]);
                preparedStatement.setInt(3, Integer.parseInt(values[3]));
                preparedStatement.setDouble(4, Double.parseDouble(values[4]));
                preparedStatement.setInt(5, Integer.parseInt(values[5]));
                preparedStatement.setString(6, values[6]);
                preparedStatement.executeUpdate();
            }
            System.out.println("The import from TEXT file has been completed successfully!");
        } catch (SQLException e) {
            System.out.println("Error Establishing a Database Connection: " + e.getMessage());
        }
    }


    @Override
    public Game findById(Integer id) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement("SELECT * FROM games WHERE id = ?");
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                Game game = new Game();
                game.setTitle(rs.getString("title"));
                return game;
            }
            return null;
        } catch (SQLException e) {
            System.err.println("problème de requête pour trouver game");;
            return null;
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(ps);
        }
    }
}
