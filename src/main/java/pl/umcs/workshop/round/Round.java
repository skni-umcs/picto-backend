package pl.umcs.workshop.round;

import jakarta.persistence.*;

@Entity
@Table(name = "rounds")
public class Round {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "game_id", nullable = false)
    private int gameId;

    @Column(name = "generation", nullable = false)
    private int generation;

    @Column(name = "user_1_id", nullable = false)
    private int userOneId;

    @Column(name = "user_2_id", nullable = false)
    private int userTwoId;

    @Column(name = "user_1_answer_time", nullable = false)
    private double userOneAnswerTime;

    @Column(name = "user_2_answer_time", nullable = false)
    private double userTwoAnswerTime;

    @Column(name = "topic", nullable = false)
    private int topic;

    @Column(name = "image_selected", nullable = false)
    private int imageSelected;

    // TODO make builder instead of whatever this is
    public Round(int gameId, int generation, int userOneId, int userTwoId, double userOneAnswerTime, double userTwoAnswerTime, int topic, int imageSelected) {
        this.gameId = gameId;
        this.generation = generation;
        this.userOneId = userOneId;
        this.userTwoId = userTwoId;
        this.userOneAnswerTime = userOneAnswerTime;
        this.userTwoAnswerTime = userTwoAnswerTime;
        this.topic = topic;
        this.imageSelected = imageSelected;
    }

    public Round() {

    }

    public int getId() {
        return this.id;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }
}
