package com.goatsandtigers.quantumtictactoe;

public class EntanglementStep {

    private int squareIndex;
    private SpookyMark spookyMark;

    public EntanglementStep(int squareIndex, SpookyMark spookyMark) {
        this.squareIndex = squareIndex;
        this.spookyMark = spookyMark;
    }

    public int getSquareIndex() {
        return squareIndex;
    }

    public int getSpookyMarkTurn() {
        return spookyMark.getTurn();
    }

    public SpookyMark getSpookyMark() {
        return spookyMark;
    }
}
