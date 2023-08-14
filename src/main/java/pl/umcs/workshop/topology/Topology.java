package pl.umcs.workshop.topology;

import jakarta.persistence.*;
import lombok.*;
import pl.umcs.workshop.user.User;

import java.util.List;
import java.util.stream.Stream;

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

    // Returns list of round ids
    public List<Integer> generateBrackets(List<User> users) {
        // TODO: generate brackets based on topology
        // For now just returns a dummy list

        return Stream.of(1, 2, 3, 4, 5).toList();
    }
}
