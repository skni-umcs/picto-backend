package pl.umcs.workshop.image;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

  // Relations
  @OneToMany(mappedBy = "image")
  @JsonIgnore
  private Set<ImageUserRoundRelation> imageUserRoundRelations;

  @OneToMany(mappedBy = "topic")
  @JsonIgnore
  private Set<Round> topics;

  @OneToMany(mappedBy = "imageSelected")
  @JsonIgnore
  private Set<Round> imagesSelected;

  @ManyToOne
  @JoinColumn(name = "group_id")
  private Group group;
}
