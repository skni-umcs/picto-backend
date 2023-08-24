package pl.umcs.workshop.topology;

import jakarta.persistence.*;
import lombok.*;
import pl.umcs.workshop.game.Game;

import java.util.Set;

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
    private Set<Game> games;
}
