package com.example.cinema_ticket_booking_system.models;

import javafx.beans.property.*;

import java.time.LocalDate;

public class MovieModel {
    private final IntegerProperty id;
    private final StringProperty title;
    private final StringProperty genre;
    private final IntegerProperty duration;
    private final ObjectProperty<LocalDate> releaseDate;
    private final StringProperty description;

    public MovieModel(int id, String title, String genre, int duration, LocalDate releaseDate, String description) {
        this.id = new SimpleIntegerProperty(id);
        this.title = new SimpleStringProperty(title);
        this.genre = new SimpleStringProperty(genre);
        this.duration = new SimpleIntegerProperty(duration);
        this.releaseDate = new SimpleObjectProperty<>(releaseDate);
        this.description = new SimpleStringProperty(description != null ? description : "");
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty titleProperty() {
        return title;
    }

    public StringProperty genreProperty() {
        return genre;
    }

    public IntegerProperty durationProperty() {
        return duration;
    }

    public ObjectProperty<LocalDate> releaseDateProperty() {
        return releaseDate;
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public int getId() {
        return id.get();
    }

    public String getTitle() {
        return title.get();
    }

    public String getGenre() {
        return genre.get();
    }

    public int getDuration() {
        return duration.get();
    }

    public LocalDate getReleaseDate() {
        return releaseDate.get();
    }

    public String getDescription() {
        return description.get();
    }

    public String getFormattedDuration() {
        int hours = duration.get() / 60;
        int minutes = duration.get() % 60;
        
        if (hours > 0) {
            return hours + " hr " + (minutes > 0 ? minutes + " min" : "");
        } else {
            return minutes + " min";
        }
    }
}
