package pl.umcs.workshop.game;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.*;
import pl.umcs.workshop.group.Group;
import pl.umcs.workshop.round.Round;
import pl.umcs.workshop.topology.Topology;

@Entity
@Table(name = "games")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Game {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "user_1_number_of_images", nullable = false)
  private int userOneNumberOfImages;

  @Column(name = "user_2_number_of_images", nullable = false)
  private int userTwoNumberOfImages;

  @Column(name = "user_1_time", nullable = false)
  private double userOneTime;

  @Column(name = "user_2_time", nullable = false)
  private double userTwoTime;

  @Column(name = "symbol_groups_amount", nullable = false)
  private int symbolGroupsAmount;

  @Column(name = "symbols_in_group_amount", nullable = false)
  private int symbolsInGroupAmount;

  @Column(name = "correct_answer_points", nullable = false)
  private int correctAnswerPoints;

  @Column(name = "wrong_answer_points", nullable = false)
  private int wrongAnswerPoints;

  @Column(name = "create_date_time", nullable = false)
  private LocalDateTime createDateTime;

  @Column(name = "end_date_time")
  private LocalDateTime endDateTime;

  // Relations
  @OneToMany(mappedBy = "game")
  private Set<Round> rounds;

  @ManyToOne
  @JoinColumn(name = "topology_id")
  @JsonBackReference(value = "topology-reference")
  private Topology topology;

  @ManyToOne
  @JoinColumn(name = "image_group_id")
  @JsonBackReference(value = "group-reference")
  private Group group;
}
