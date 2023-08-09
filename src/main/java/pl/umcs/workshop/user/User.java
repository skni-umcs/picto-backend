package pl.umcs.workshop.user;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "game_id", nullable = false)
    private int gameId;

    @Column(name = "score", nullable = false)
    private int score;

    @Column(name = "last_seen", nullable = true)
    private LocalDate lastSeen;

    @Column(name = "cookie", nullable = true)
    private String cookie; // TODO: cookie type

    public User() {

    }

    public User(int gameId, int score) {
        this.gameId = gameId;
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

// TODO make builder for this class
}
