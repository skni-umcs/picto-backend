package pl.umcs.workshop.image;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.util.Set;
import lombok.*;
import pl.umcs.workshop.group.Group;
import pl.umcs.workshop.relation.ImageUserRoundRelation;
import pl.umcs.workshop.round.Round;

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

  @Transient private String url;

  // Relations
  @OneToMany(mappedBy = "image")
  @JsonBackReference(value = "round-image-user-image-reference")
  private Set<ImageUserRoundRelation> imageUserRoundRelations;

  @OneToMany(mappedBy = "topic")
  private Set<Round> topics;

  @OneToMany(mappedBy = "imageSelected")
  private Set<Round> imagesSelected;

  @ManyToMany
  @JoinColumn(name = "group_id")
  @JsonBackReference(value = "group-images-reference")
  private Set<Group> groups;
}
