package pl.umcs.workshop.round;

import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "game_id", nullable = false)
    private Long gameId;

    @Column(name = "generation", nullable = false)
    private int generation;

    @Column(name = "user_1_id", nullable = false)
    private Long userOneId;

    @Column(name = "user_2_id", nullable = false)
    private Long userTwoId;

    @Column(name = "user_1_answer_time")
    private double userOneAnswerTime;

    @Column(name = "user_2_answer_time")
    private double userTwoAnswerTime;

    @Column(name = "topic", nullable = false)
    private Long topic;

    @Column(name = "image_selected")
    private Long imageSelected;
}
