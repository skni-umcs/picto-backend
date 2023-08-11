package pl.umcs.workshop.game;

import jakarta.persistence.*;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;

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

    // TODO fix this later
    @Column(name = "create_date", nullable = false)
//    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "create_time", nullable = false)
//    @Column(name = "create_time")
    private Time createTime;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "end_time")
    private Time endTime;

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

        LocalDateTime localDateTime = LocalDateTime.now();
        this.createDate = Date.valueOf(localDateTime.toLocalDate());
        this.createTime = Time.valueOf(localDateTime.toLocalTime());
        this.endDate = null;
        this.endTime = null;

//        System.out.println(this.createDate + " " + this.createTime);
    }

    public Game(int id, int userOneNumberOfImages, int userTwoNumberOfImages, double userOneTime, double userTwoTime, int symbolGroupsAmount, int symbolsInGroupAmount, int correctAnswerPoints, int wrongAnswerPoints, int topologyId) {
        this.id = id;
        this.userOneNumberOfImages = userOneNumberOfImages;
        this.userTwoNumberOfImages = userTwoNumberOfImages;
        this.userOneTime = userOneTime;
        this.userTwoTime = userTwoTime;
        this.symbolGroupsAmount = symbolGroupsAmount;
        this.symbolsInGroupAmount = symbolsInGroupAmount;
        this.correctAnswerPoints = correctAnswerPoints;
        this.wrongAnswerPoints = wrongAnswerPoints;
        this.topologyId = topologyId;

        LocalDateTime localDateTime = LocalDateTime.now();
        this.createDate = Date.valueOf(localDateTime.toLocalDate());
        this.createTime = Time.valueOf(localDateTime.toLocalTime());
        this.endDate = null;
        this.endTime = null;

//        System.out.println(this.createDate + " " + this.createTime);
    }

    public Game() {

    }

    public int getId() {
        return this.id;
    }

    public void setUserOneNumberOfImages(int numberOfImages) {
        this.userOneNumberOfImages = numberOfImages;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public Time getCreateTime() {
        return createTime;
    }

    // TODO make a builder for this class
}
