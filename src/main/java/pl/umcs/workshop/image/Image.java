package pl.umcs.workshop.image;

import jakarta.persistence.*;
import lombok.*;
import pl.umcs.workshop.round.Round;

import java.util.Set;

@Entity
@Table(name = "images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "path", nullable = false)
    private String path;

//    @Column(name = "group_id")
//    private Long groupId;

    // Relations
    @OneToMany(mappedBy = "image")
    private Set<ImageUserRoundRelation> imageUserRoundRelations;

    @OneToMany(mappedBy = "topic")
    private Set<Round> topics;

    @OneToMany(mappedBy = "imageSelected")
    private Set<Round> imagesSelected;

    @ManyToMany
    @JoinColumn(name = "group_id")
    private Set<Group> groups;
}
