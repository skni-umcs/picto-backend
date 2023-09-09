package pl.umcs.workshop.topology;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.Set;
import lombok.*;
import pl.umcs.workshop.game.Game;

@Entity
@Table(name = "topologies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Topology {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "probability_of_edge_redrawing")
  private double probabilityOfEdgeRedrawing;

  @Column(name = "max_vertex_degree")
  private int maxVertexDegree;

  // Relations
  @OneToMany(mappedBy = "topology", cascade = CascadeType.ALL)
  @JsonManagedReference(value = "topology-game-reference")
  private Set<Game> games;
}
