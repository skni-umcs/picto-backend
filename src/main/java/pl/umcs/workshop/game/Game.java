package pl.umcs.workshop.game;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "games")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "user_1_number_of_images", nullable = false)
    private int userOneNumberOfImages;

    @Column(name = "user_2_number_of_images", nullable = false)
    private int userTwoNumberOfImages;

    @Column(name = "user_1_time", nullable = false)
    private double userOneTime;

    @Column(name = "user_2_time", nullable = false)
    private double userTwoTime;

    @Column(name = "symbol_groups_amount", nullable = false)
    private int symbolGroupsAmount;

    @Column(name = "symbols_in_group_amount", nullable = false)
    private int symbolsInGroupAmount;

    @Column(name = "correct_answer_points", nullable = false)
    private int correctAnswerPoints;

    @Column(name = "wrong_answer_points", nullable = false)
    private int wrongAnswerPoints;

    @Column(name = "topology_id", nullable = false)
    private int topologyId;

    @Column(name = "probability_of_edge_redrawing")
    private double probabilityOfEdgeRedrawing;

    @Column(name = "max_vertex_degree")
    private int maxVertexDegree;

    @Column(name = "create_date_time", nullable = false)
    private LocalDateTime createDateTime;

    @Column(name = "end_date_time")
    private LocalDateTime endDateTime;
}
