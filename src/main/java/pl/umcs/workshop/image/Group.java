package pl.umcs.workshop.image;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "groups")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    // Relations
    @ManyToMany
    private Set<Image> images;
}
