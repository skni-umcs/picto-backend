package pl.umcs.workshop.round;

import jakarta.persistence.*;
import java.util.Set;
import lombok.*;
import pl.umcs.workshop.game.Game;
import pl.umcs.workshop.image.Image;
import pl.umcs.workshop.relation.ImageUserRoundRelation;
import pl.umcs.workshop.user.User;

@Entity
@Table(name = "rounds")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Round {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "generation", nullable = false)
    private int generation;

    @Column(name = "user_1_answer_time")
    private double userOneAnswerTime;

    @Column(name = "user_2_answer_time")
    private double userTwoAnswerTime;

    // Relations
    @OneToMany(mappedBy = "round")
    private Set<ImageUserRoundRelation> imageUserRoundRelations;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @ManyToOne
    @JoinColumn(name = "user_1_id")
    private User userOne;

    @ManyToOne
    @JoinColumn(name = "user_2_id")
    private User userTwo;

    @ManyToOne
    @JoinColumn(name = "topic")
    private Image topic;

    @ManyToOne
    @JoinColumn(name = "image_selected")
    private Image imageSelected;
}
