package com.goatsandtigers.quantumtictactoe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Entanglement {

    private final SpookyMark startingSpookyMark;
    private final Map<SpookyMark, Collection<SpookyMark>> linkedSpookyMarks = new HashMap<>();

    public Entanglement(SpookyMark startingSpookyMark) {
        this.startingSpookyMark = startingSpookyMark;
    }

    public void addStep(SpookyMark start, SpookyMark destination) {
        if (!linkedSpookyMarks.containsKey(start)) {
            linkedSpookyMarks.put(start, new ArrayList<>());
        }
        linkedSpookyMarks.get(start).add(destination);
    }

    public Map<SpookyMark, Collection<SpookyMark>> getLinkedSpookyMarks() {
        return Collections.unmodifiableMap(linkedSpookyMarks);
    }

    public SpookyMark getStartingSpookyMark() {
        return startingSpookyMark;
    }

    public boolean isCyclic() {
        for (int startingSquareIndex : Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9)) {
            if (isCyclicRecursive(-1, startingSquareIndex, new ArrayList<>())) {
                return true;
            }
        }
        return false;
    }

    private boolean isCyclicRecursive(int previousSquareIndex, int currentSquareIndex, Collection<Integer> visitedSquareIndices) {
        visitedSquareIndices.add(currentSquareIndex);
        Collection<Integer> connectedSquareIndices = findSquaresIndicesConnectedToSquareIndex(currentSquareIndex);
        for (Integer connectedSquareIndex : connectedSquareIndices) {
            if (connectedSquareIndex == previousSquareIndex) {
                continue;
            }
            if (visitedSquareIndices.contains(connectedSquareIndex)) {
                return true;
            } else {
                if (isCyclicRecursive(currentSquareIndex, connectedSquareIndex, new ArrayList<>(visitedSquareIndices))) {
                    return true;
                }
            }
        }
        return false;
    }

    private Collection<SpookyMark> flatMapSpookyMarks() {
        Collection<SpookyMark> result = new ArrayList<>();
        for (Collection<SpookyMark> spookyMarks : linkedSpookyMarks.values()) {
            result.addAll(spookyMarks);
        }
        return result;
    }

    private Collection<Integer> findSquaresIndicesConnectedToSquareIndex(int squareIndex) {
        Collection<Integer> result = new ArrayList<>();
        for (SpookyMark spookyMark : flatMapSpookyMarks()) {
            if (spookyMark.getSquareIndex() == squareIndex) {
                SpookyMark otherSpookyMarkForTurn = findOtherSpookyMarkForTurn(spookyMark);
                if (otherSpookyMarkForTurn != null) {
                    result.add(otherSpookyMarkForTurn.getSquareIndex());
                }
            }
        }
        return result;
    }

    private SpookyMark findOtherSpookyMarkForTurn(SpookyMark spookyMark) {
        for (SpookyMark otherSpookyMark : flatMapSpookyMarks()) {
            if (otherSpookyMark.getSquareIndex() != spookyMark.getSquareIndex() &&
                    otherSpookyMark.getTurn() == spookyMark.getTurn()) {
                return otherSpookyMark;
            }
        }
        return null;
    }
}
