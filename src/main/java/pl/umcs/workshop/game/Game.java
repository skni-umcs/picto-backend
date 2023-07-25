package pl.umcs.workshop.game;

import pl.umcs.workshop.round.Round;

import java.util.List;

public class Game {
    private final int id;
    private int symbolGroups;

    private int symbolsInGroup;
    private int correctAnswerPoints;
    private int wrongAnswerPoints;
    private int topologyId;

    public Game(int id, int symbolGroups, int symbolsInGroup, int correctAnswerPoints, int wrongAnswerPoints, int topologyId) {
        this.id = id;
        this.symbolGroups = symbolGroups;
        this.symbolsInGroup = symbolsInGroup;
        this.correctAnswerPoints = correctAnswerPoints;
        this.wrongAnswerPoints = wrongAnswerPoints;
        this.topologyId = topologyId;
    }

    public int getId() {
        return id;
    }

    public int getSymbolGroups() {
        return symbolGroups;
    }

    public void setSymbolGroups(int symbolGroups) {
        this.symbolGroups = symbolGroups;
    }

    public int getSymbolsInGroup() {
        return symbolsInGroup;
    }

    public void setSymbolsInGroup(int symbolsInGroup) {
        this.symbolsInGroup = symbolsInGroup;
    }

    public int getCorrectAnswerPoints() {
        return correctAnswerPoints;
    }

    public void setCorrectAnswerPoints(int correctAnswerPoints) {
        this.correctAnswerPoints = correctAnswerPoints;
    }

    public int getWrongAnswerPoints() {
        return wrongAnswerPoints;
    }

    public void setWrongAnswerPoints(int wrongAnswerPoints) {
        this.wrongAnswerPoints = wrongAnswerPoints;
    }

    public int getTopologyId() {
        return topologyId;
    }

    public void setTopologyId(int topologyId) {
        this.topologyId = topologyId;
    }
}
