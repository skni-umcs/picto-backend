package pl.umcs.workshop.symbol;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.util.Set;
import lombok.*;
import pl.umcs.workshop.game.Game;
import pl.umcs.workshop.group.Group;
import pl.umcs.workshop.round.Round;
import pl.umcs.workshop.user.User;

@Entity
@Table(name = "symbols")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Symbol {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "path")
  private String path;

  // Relations
  @ManyToMany
  @JoinColumn(name = "game_id")
  @JsonBackReference(value = "game-symbols-reference")
  private Set<Game> game;

  @ManyToMany
  @JoinColumn(name = "round_id")
  @JsonBackReference(value = "symbols-rounds-id")
  private Set<Round> rounds;

  @ManyToOne
  @JoinColumn(name = "group_id")
  @JsonBackReference(value = "symbols-groups-id")
  private Group group;

  @ManyToMany
  @JoinColumn(name = "user_id")
  @JsonBackReference(value = "symbols-users-id")
  private Set<User> users;
}
