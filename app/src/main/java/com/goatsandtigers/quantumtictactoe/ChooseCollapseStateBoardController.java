package com.goatsandtigers.quantumtictactoe;

import java.util.List;

public class ChooseCollapseStateBoardController implements BoardController {

    private BoardModel model;

    public ChooseCollapseStateBoardController(BoardModel model) {
        this.model = new BoardModel(model);
    }

    @Override
    public void onEnterMove(int squareIndex) {}

    @Override
    public boolean isSquareCollapsed(int squareIndex) {
        return model.isSquareCollapsed(squareIndex);
    }

    @Override
    public String getCollapsedState(int squareIndex) {
        return model.getCollapsedState(squareIndex);
    }

    @Override
    public List<SpookyMark> getSpookyMarks(int squareIndex) {
        return model.getSpookyMarks(squareIndex);
    }

    @Override
    public List<Entanglement> getEntanglements() {
        return model.getEntanglements();
    }

    @Override
    public BoardModel cloneModel() {
        throw new IllegalStateException();
    }
}
