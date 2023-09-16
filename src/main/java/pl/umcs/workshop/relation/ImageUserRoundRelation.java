package pl.umcs.workshop.relation;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import pl.umcs.workshop.image.Image;
import pl.umcs.workshop.round.Round;
import pl.umcs.workshop.user.User;

@Entity
@Table(name = "images_users_rounds_relations")
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class ImageUserRoundRelation {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // Relations
  @ManyToOne
  @JoinColumn(name = "round_id")
  @JsonBackReference(value = "round-image-user-round-reference")
  private Round round;

  @ManyToOne
  @JoinColumn(name = "user_id")
  @JsonBackReference(value = "round-image-user-user-reference")
  private User user;

  @ManyToOne
  @JoinColumn(name = "image_id")
  @JsonBackReference(value = "round-image-user-image-reference")
  private Image image;
}
