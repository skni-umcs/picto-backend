package pl.umcs.workshop.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.*;
import pl.umcs.workshop.game.Game;
import pl.umcs.workshop.relation.ImageUserRoundRelation;
import pl.umcs.workshop.round.Round;

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
  private Long id;

  @Column(name = "score", nullable = false)
  private int score;

  @Column(name = "generation", nullable = false)
  private int generation;

  @Column(name = "last_seen", nullable = false)
  private LocalDateTime lastSeen;

  @Column(name = "cookie", length = 512)
  private String cookie;

  // Relations
  @OneToMany(mappedBy = "user")
  private Set<ImageUserRoundRelation> imageUserRoundRelations;

  @OneToMany(mappedBy = "userOne")
  @JsonManagedReference(value = "round-user-one-reference")
  private Set<Round> userOneRounds;

  @OneToMany(mappedBy = "userTwo")
  @JsonManagedReference(value = "round-user-two-reference")
  private Set<Round> userTwoRounds;

  @OneToMany(mappedBy = "user")
  @JsonBackReference(value = "round-image-user-user-reference")
  private Set<User> users;

  @ManyToOne
  @JoinColumn(name = "game_id")
  @JsonBackReference(value = "user-game-reference")
  private Game game;
}
