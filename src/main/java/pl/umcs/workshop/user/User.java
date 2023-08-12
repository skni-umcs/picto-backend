package pl.umcs.workshop.user;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "game_id", nullable = false)
    private int gameId;

    @Column(name = "score", nullable = false)
    private int score;

    @Column(name = "last_seen", nullable = true)
    private LocalDateTime lastSeen;

    @Column(name = "cookie", nullable = true)
    private String cookie; // TODO: cookie type
}
