package pl.umcs.workshop.round;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.Set;
import lombok.*;
import pl.umcs.workshop.game.Game;
import pl.umcs.workshop.image.Image;
import pl.umcs.workshop.relation.ImageUserRoundRelation;
import pl.umcs.workshop.symbol.Symbol;
import pl.umcs.workshop.user.User;

@Entity
@Table(name = "rounds")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Round {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "generation", nullable = false)
  private int generation;

  @Column(name = "user_1_answer_time")
  private double userOneAnswerTime;

  @Column(name = "user_2_answer_time")
  private double userTwoAnswerTime;

  // Relations
  @OneToMany(mappedBy = "round")
  @JsonManagedReference(value = "round-image-user-round-reference")
  private Set<ImageUserRoundRelation> imageUserRoundRelations;

  @ManyToOne
  @JoinColumn(name = "game_id")
  @JsonBackReference(value = "game-round-reference")
  private Game game;

  @ManyToOne
  @JoinColumn(name = "user_1_id")
  @JsonBackReference(value = "round-user-one-reference")
  private User userOne;

  @ManyToOne
  @JoinColumn(name = "user_2_id")
  @JsonBackReference(value = "round-user-two-reference")
  private User userTwo;

  @ManyToOne
  @JoinColumn(name = "topic")
  @JsonIgnoreProperties({"imageUserRoundRelations", "imageSelected", "groups", "topics"})
  private Image topic;

  @ManyToOne
  @JoinColumn(name = "image_selected")
  @JsonIgnoreProperties({"imageUserRoundRelations", "imageSelected", "groups", "topics"})
  private Image imageSelected;

  @ManyToMany
  @JoinColumn(name = "symbol_id")
  @JsonBackReference(value = "symbols-rounds-id")
  private Set<Symbol> symbols;
}
