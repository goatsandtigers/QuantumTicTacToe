package com.goatsandtigers.quantumtictactoe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BoardModel {

    public static final String CROSS = "X";
    public static final String NOUGHT = "O";

    int turn = 0;
    boolean firstSquareMarked;
    private List<SpookyMark>[] spookyMarkList = new List[9];
    {
        for (int i = 0; i < 9; i++) {
            spookyMarkList[i] = new ArrayList<>(8);
        }
    }
    private String[] collapsedState = new String[9];

    public BoardModel() {}

    public BoardModel(BoardModel model) {
        turn = model.turn;
        firstSquareMarked = model.firstSquareMarked;
        for (int i = 0; i < 9; i++) {
            spookyMarkList[i] = new ArrayList<>(model.spookyMarkList[i]);
            collapsedState[i] = model.collapsedState[i];
        }
    }

    public void onEnterMove(int squareIndex) {
        if (turn > 8) {
            return;
        }

        spookyMarkList[squareIndex].add(new SpookyMark(squareIndex, turn));
        if (!firstSquareMarked) {
            firstSquareMarked = true;
        } else {
            turn++;
            firstSquareMarked = false;
        }
    }

    public boolean isSquareCollapsed(int squareIndex) {
        return collapsedState[squareIndex] != null;
    }

    public String getCollapsedState(int squareIndex) {
        return collapsedState[squareIndex];
    }

    public List<SpookyMark> getSpookyMarks(int squareIndex) {
        return Collections.unmodifiableList(spookyMarkList[squareIndex]);
    }

    public List<Entanglement> getEntanglements() {
        List<Entanglement> result = new ArrayList<>();
        Collection<Integer> unparsedSquareIndices = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8));
        for (int turn = 0; turn < 9; turn++) {
            SpookyMark startingSpookyMark = null;
            for (Integer startingSquare : unparsedSquareIndices) {
                if (squareContainsTurn(startingSquare, turn)) {
                    startingSpookyMark = new SpookyMark(startingSquare, turn);
                    break;
                }
            }
            if (startingSpookyMark == null) {
                continue;
            }
            Entanglement entanglement = new Entanglement(startingSpookyMark);
            result.add(entanglement);
            List<SpookyMark> spookyMarksToParse = new ArrayList<>();
            spookyMarksToParse.add(startingSpookyMark);
            Collection<SpookyMark> parsedSpookyMarks = new HashSet<>();
            while (!spookyMarksToParse.isEmpty()) {
                SpookyMark currentSpookyMark = spookyMarksToParse.remove(0);
                if (parsedSpookyMarks.contains(currentSpookyMark)) {
                    continue;
                }
                parsedSpookyMarks.add(currentSpookyMark);
                unparsedSquareIndices.remove((Object) currentSpookyMark.getSquareIndex());
                SpookyMark correspondingSpookyMark = findCorrespondingSpookyMark(currentSpookyMark);
                if (correspondingSpookyMark != null && !parsedSpookyMarks.contains(correspondingSpookyMark)) {
                    entanglement.addStep(currentSpookyMark, correspondingSpookyMark);
                    spookyMarksToParse.add(correspondingSpookyMark);
                }
                for (SpookyMark linkedSpookyMark : spookyMarkList[currentSpookyMark.getSquareIndex()]) {
                    if (!currentSpookyMark.equals(linkedSpookyMark)) {
                        entanglement.addStep(currentSpookyMark, linkedSpookyMark);
                        spookyMarksToParse.add(linkedSpookyMark);
                    }
                }
            }
        }
        return result;
    }

    private boolean squareContainsTurn(int squareIndex, int turn) {
        for (SpookyMark spookyMark : spookyMarkList[squareIndex]) {
            if (spookyMark.getTurn() == turn) {
                return true;
            }
        }
        return false;
    }

    /**
     * Find a SpookyMark with the same turn number as the parameter.
     * @param spookyMark provides the turn number to match
     * @return corresponding SpookyMark, or null if the corresponding SpookyMark has not yet been placed on the board
     */
    private SpookyMark findCorrespondingSpookyMark(SpookyMark spookyMark) {
        for (int squareIndex = 0; squareIndex < 9; squareIndex++) {
            if (squareIndex == spookyMark.getSquareIndex()) {
                continue;
            }
            for (SpookyMark result : spookyMarkList[squareIndex]) {
                if (result.getTurn() == spookyMark.getTurn()) {
                    return result;
                }
            }
        }
        return null;
    }

    public void collapseToFirstState() {
        // TODO
    }
}
