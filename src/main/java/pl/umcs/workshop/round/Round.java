package pl.umcs.workshop.round;

import pl.umcs.workshop.user.User;

public class Round {
    private int id;
    private int gameId;
    private int userOneId;
    private int userTwoId;
    private int imageChosen;
    private int imageSelected;

    private User userOne;
    private User userTwo;

    public Round(int gameId, int userOneId, int userTwoId, int imageChosen) {
        this.gameId = gameId;
        this.userOneId = userOneId;
        this.userTwoId = userTwoId;
        this.imageChosen = imageChosen;
    }
}
