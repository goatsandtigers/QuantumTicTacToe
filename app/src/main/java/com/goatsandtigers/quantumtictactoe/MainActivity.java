package com.goatsandtigers.quantumtictactoe;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

public class MainActivity extends AppCompatActivity implements BoardController {

    private BoardModel model;
    private BoardView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        newGame();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.new_game) {
            confirmNewGame();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void confirmNewGame() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.app_name)
                .setMessage("New game?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        newGame();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void newGame() {
        view = new BoardView(this, this);
        model = new BoardModel();
        setContentView(view);
    }

    public void onEnterMove(int squareIndex) {
        model.onEnterMove(squareIndex);
        for (Entanglement entanglement : model.getEntanglements()) {
            if (entanglement.isCyclic()) {
                chooseCollapseState();
            }
        }
    }

    private void chooseCollapseState() {
        ChooseCollapseStateView chooseCollapseStateView = new ChooseCollapseStateView(view.getContext(), this);
        new AlertDialog.Builder(this)
                .setTitle("Congratulations!")
                .setMessage("Please enter your nickname to see the best times for this puzzle.")
                .setView(chooseCollapseStateView)
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel, null).show();
    }

    public boolean isSquareCollapsed(int squareIndex) {
        return model.isSquareCollapsed(squareIndex);
    }

    public String getCollapsedState(int squareIndex) {
        return model.getCollapsedState(squareIndex);
    }

    public List<SpookyMark> getSpookyMarks(int squareIndex) {
        return model.getSpookyMarks(squareIndex);
    }

    public List<Entanglement> getEntanglements() {
        return model.getEntanglements();
    }

    @Override
    public BoardModel cloneModel() {
        return new BoardModel(model);
    }
}