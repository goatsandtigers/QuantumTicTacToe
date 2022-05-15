package com.goatsandtigers.quantumtictactoe;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.widget.LinearLayout;

public class ChooseCollapseStateView extends LinearLayout {

    private BoardController boardController1;
    private BoardController boardController2;
    private BoardView boardView1;
    private BoardView boardView2;

    public ChooseCollapseStateView(Context context, BoardController boardController) {
        super(context);
        setOrientation(VERTICAL);
        BoardModel boardModel1 = collapseToFirstPossibleState(boardController.cloneModel());
        boardController1 = new ChooseCollapseStateBoardController(boardModel1);
        boardView1 = new BoardView(context, boardController1);
        addView(boardView1);
        boardController2 = new ChooseCollapseStateBoardController(boardController.cloneModel());
        boardView2 = new BoardView(context, boardController2);
        addView(boardView2);
    }

    private BoardModel collapseToFirstPossibleState(BoardModel model) {
        model.collapseToFirstState();
        return model;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (boardView1 != null) {
            int quarterOfScreenHeight = getScreenHeight() / 4;
            boardView1.getLayoutParams().height = quarterOfScreenHeight;
            boardView2.getLayoutParams().height = quarterOfScreenHeight;
        }
    }

    private int getScreenHeight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }
}
