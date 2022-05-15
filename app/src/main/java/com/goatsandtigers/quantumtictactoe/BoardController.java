package com.goatsandtigers.quantumtictactoe;

import java.util.List;

public interface BoardController {

    public void onEnterMove(int squareIndex);

    public boolean isSquareCollapsed(int squareIndex);

    public String getCollapsedState(int squareIndex);

    public List<SpookyMark> getSpookyMarks(int squareIndex);

    public List<Entanglement> getEntanglements();

    BoardModel cloneModel();
}
