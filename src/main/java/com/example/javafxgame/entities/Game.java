package com.example.javafxgame.entities;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Game {
    @SerializedName("id")
    private int id;
    private String title;
    private String genre;
    private int releaseYear;
    private double price;
    private int rating;
    private String company;


    public Game(int id,String title, String genre, int releaseYear, double price, int rating,String company ) {
        this.id=id;
        this.title = title;
        this.genre = genre;
        this.releaseYear = releaseYear;
        this.price = price;
        this.rating = rating;
        this.company=company;
    }

    public Game() {}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Game)) return false;
        Game game = (Game) o;
        return  getReleaseYear() == game.getReleaseYear() && Double.compare(game.getPrice(), getPrice()) == 0 && getRating() == game.getRating() && Objects.equals(getTitle(), game.getTitle())&& Objects.equals(getCompany(), game.getCompany()) && Objects.equals(getGenre(), game.getGenre());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getGenre(), getReleaseYear(), getPrice(), getRating(),getCompany());
    }

    @Override
    public String toString() {
        return "Game{" +
                "title='" + title + '\'' +
                ", genre='" + genre + '\'' +
                ", releaseYear=" + releaseYear +
                ", price=" + price +
                ", rating=" + rating +
                ", company= "+company+
                '}';
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
