package pl.umcs.workshop.symbol;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.util.Set;
import lombok.*;
import pl.umcs.workshop.game.Game;
import pl.umcs.workshop.round.Round;

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
  @ManyToOne
  @JoinColumn(name = "game_id")
  @JsonBackReference(value = "game-symbols-reference")
  private Game game;

  @ManyToMany
  @JoinColumn(name = "round_id")
  @JsonBackReference(value = "symbols-rounds-id")
  private Set<Round> rounds;
}
