package pl.umcs.workshop.image;

import jakarta.persistence.*;
import pl.umcs.workshop.round.Round;
import pl.umcs.workshop.user.User;

@Entity
@Table(name = "images_users_rounds_relation")
public class ImageUserRoundRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relations
    @ManyToOne
    @JoinColumn(name = "round_id")
    private Round round;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "image_id")
    private Image image;
}
