package pl.umcs.workshop.graph;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "game_graph_snapshot")
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GraphEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "game_id")
  private Long gameId;

  @Column(name = "user_1")
  private Long userOneId;

  @Column(name = "user_2")
  private Long userTwoId;
}
