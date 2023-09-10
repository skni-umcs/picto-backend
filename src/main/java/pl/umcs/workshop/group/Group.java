package pl.umcs.workshop.group;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.Set;
import lombok.*;
import pl.umcs.workshop.game.Game;
import pl.umcs.workshop.image.Image;
import pl.umcs.workshop.symbol.Symbol;

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
//  @JsonManagedReference(value = "group-games-reference")
  @JsonIgnoreProperties({""})
  private Set<Game> games;

  @ManyToMany
  @JoinColumn(name = "image_id")
//  @JsonBackReference(value = "group-images-reference")
  @JsonIgnoreProperties({"imageUserRoundRelations", "topics", "imagesSelected", "groups"})
  private Set<Image> images;

  @OneToMany
  @JoinColumn(name = "symbol_id")
  @JsonBackReference(value = "symbols-groups-id")
  private Set<Symbol> symbols;
}
