package pl.umcs.workshop.topology;

import jakarta.persistence.*;
import lombok.*;

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
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "number_of_sets")
    private int numberOfSets;

    @Column(name = "users_in_set")
    private int usersInSet;
}
