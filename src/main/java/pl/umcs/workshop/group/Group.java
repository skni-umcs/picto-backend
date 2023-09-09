package pl.umcs.workshop.group;

import jakarta.persistence.*;
import java.util.Set;
import lombok.*;
import pl.umcs.workshop.game.Game;
import pl.umcs.workshop.image.Image;

@Entity
@Table(name = "groups")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Group {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name")
  private String name;

  // Relations
  @OneToMany(mappedBy = "group")
  private Set<Game> games;

  @ManyToMany
  @JoinColumn(name = "image_id")
  private Set<Image> images;
}
