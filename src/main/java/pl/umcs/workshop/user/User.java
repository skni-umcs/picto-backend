package pl.umcs.workshop.user;

import java.time.LocalDate;

public class User {
    private final int id;
    private int numberOfImages;
    private int availableTime;
    private LocalDate lastSeen;

    public User(int id, int numberOfImages, int availableTime, LocalDate lastSeen) {
        this.id = id;
        this.numberOfImages = numberOfImages;
        this.availableTime = availableTime;
        this.lastSeen = lastSeen;
    }

    public int getId() {
        return id;
    }

    public int getNumberOfImages() {
        return numberOfImages;
    }

    public void setNumberOfImages(int numberOfImages) {
        this.numberOfImages = numberOfImages;
    }

    public int getAvailableTime() {
        return availableTime;
    }

    public void setAvailableTime(int availableTime) {
        this.availableTime = availableTime;
    }

    public LocalDate getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(LocalDate lastSeen) {
        this.lastSeen = lastSeen;
    }
}
