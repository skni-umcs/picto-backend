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

    @Column(name = "last_seen", nullable = false)
    private LocalDate lastSeen;

    @Column(name = "cookie", nullable = false)
    private String cookie; // TODO: cookie type

    // TODO make builder for this class
}
