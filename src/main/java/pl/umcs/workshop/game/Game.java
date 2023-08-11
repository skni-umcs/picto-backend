package pl.umcs.workshop.game;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "games")
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

    @Column(name = "create_time", nullable = false)
    private LocalDate createTime;

    @Column(name = "end_time")
    private LocalDate endTime;

    public Game(int userOneNumberOfImages, int userTwoNumberOfImages, double userOneTime, double userTwoTime, int symbolGroupsAmount, int symbolsInGroupAmount, int correctAnswerPoints, int wrongAnswerPoints, int topologyId) {
        this.userOneNumberOfImages = userOneNumberOfImages;
        this.userTwoNumberOfImages = userTwoNumberOfImages;
        this.userOneTime = userOneTime;
        this.userTwoTime = userTwoTime;
        this.symbolGroupsAmount = symbolGroupsAmount;
        this.symbolsInGroupAmount = symbolsInGroupAmount;
        this.correctAnswerPoints = correctAnswerPoints;
        this.wrongAnswerPoints = wrongAnswerPoints;
        this.topologyId = topologyId;
        this.createTime = LocalDate.now();
    }

    public Game() {

    }

    public int getId() {
        return this.id;
    }

    public void setUserOneNumberOfImages(int numberOfImages) {
        this.userOneNumberOfImages = numberOfImages;
    }

    // TODO make a builder for this class
}
