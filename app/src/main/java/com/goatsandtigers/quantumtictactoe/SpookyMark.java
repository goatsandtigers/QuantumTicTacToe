package com.goatsandtigers.quantumtictactoe;

import androidx.annotation.Nullable;

public class SpookyMark {

    private final int squareIndex;
    private final int turn;

    public SpookyMark(int squareIndex, int turn) {
        this.squareIndex = squareIndex;
        this.turn = turn;
    }

    public int getTurn() {
        return turn;
    }

    public String getText() {
        String symbol = (turn & 1) == 0 ? BoardModel.CROSS : BoardModel.NOUGHT;
        return symbol + (turn + 1);
    }

    @Override
    public String toString() {
        return getText();
    }

    public int getSquareIndex() {
        return squareIndex;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof SpookyMark)) {
            return false;
        }
        SpookyMark other = (SpookyMark) obj;
        return other.squareIndex == squareIndex && other.turn == turn;
    }

    @Override
    public int hashCode() {
        return (squareIndex * 10) + turn;
    }
}
