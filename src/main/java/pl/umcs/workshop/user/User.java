package pl.umcs.workshop.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import lombok.*;
import pl.umcs.workshop.game.Game;
import pl.umcs.workshop.relation.ImageUserRoundRelation;
import pl.umcs.workshop.round.Round;
import pl.umcs.workshop.symbol.Symbol;

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
  @JsonIgnore
  private Set<ImageUserRoundRelation> imageUserRoundRelations;

  @OneToMany(mappedBy = "userOne")
  @JsonIgnore
  private Set<Round> userOneRounds;

  @OneToMany(mappedBy = "userTwo")
  @JsonIgnore
  private Set<Round> userTwoRounds;

  @ManyToOne
  @JoinColumn(name = "game_id")
  @JsonBackReference(value = "user-game-reference")
  private Game game;

  @ManyToMany
  @JoinColumn(name = "symbol_id")
  @JsonBackReference(value = "symbols-users-id")
  private Set<Symbol> symbols;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return Objects.equals(id, user.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
