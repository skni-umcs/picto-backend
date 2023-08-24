package pl.umcs.workshop.round;

import jakarta.persistence.*;
import lombok.*;
import pl.umcs.workshop.game.Game;
import pl.umcs.workshop.image.Image;
import pl.umcs.workshop.image.ImageUserRoundRelation;
import pl.umcs.workshop.user.User;

import java.util.Set;

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

//    @Column(name = "game_id", nullable = false)
//    private Long gameId;

    @Column(name = "generation", nullable = false)
    private int generation;

//    @Column(name = "user_1_id", nullable = false)
//    private Long userOneId;

//    @Column(name = "user_2_id", nullable = false)
//    private Long userTwoId;

    @Column(name = "user_1_answer_time")
    private double userOneAnswerTime;

    @Column(name = "user_2_answer_time")
    private double userTwoAnswerTime;

//    @Column(name = "topic", nullable = false)
//    private Long topic;

//    @Column(name = "image_selected")
//    private Long imageSelected;

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
