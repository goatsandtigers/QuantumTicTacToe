package com.goatsandtigers.quantumtictactoe;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class BoardView extends View {
    
    private static final int MIN_TIME_IN_MILLIS_BETWEEN_TOUCHES = 250;
    private static final Paint BOARD_LINE_PAINT = new Paint();
    static {
        // Note: stroke width 1 doesn't always display on emulator
        BOARD_LINE_PAINT.setStrokeWidth(3);
    }
    private static final Paint SQUARE_CONTENTS_PAINT = buildSpookyMarksPaint();

    private BoardController controller;
    private long lastTouchEventTime;

    public BoardView(Context context, BoardController controller) {
        super(context);
        this.controller = controller;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getEventTime() - lastTouchEventTime < MIN_TIME_IN_MILLIS_BETWEEN_TOUCHES) {
            return true;
        }
        lastTouchEventTime = event.getEventTime();
        onEnterMove(event);
        invalidate();
        return true;
    }

    private void onEnterMove(MotionEvent event) {
        int cellX = (int) (event.getX() / (getWidth() / 3.0f));
        int cellY = (int) (event.getY() / (getHeight() / 3.0f));
        int squareIndex = (3 * cellY) + cellX;
        controller.onEnterMove(squareIndex);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        drawLines(canvas);
        drawEntanglements(canvas);
        for (int squareIndex = 0; squareIndex < 9; squareIndex++) {
            Rect bounds = getSquareBounds(squareIndex);
            drawSquare(canvas, bounds, squareIndex);
        }
    }

    private void drawLines(Canvas canvas) {
        float width = getWidth(), height = getHeight();
        canvas.drawLine(width / 3, 0, width / 3, height, BOARD_LINE_PAINT);
        canvas.drawLine(width * 2 / 3, 0, width * 2 / 3, height, BOARD_LINE_PAINT);
        canvas.drawLine(0, height / 3, width, height / 3, BOARD_LINE_PAINT);
        canvas.drawLine(0, height * 2 / 3, width, height * 2 / 3, BOARD_LINE_PAINT);
    }

    private Rect getSquareBounds(int squareIndex) {
        float x = squareIndex % 3;
        float y = squareIndex / 3;
        return new Rect((int) (getWidth() * x / 3),
                (int) (getHeight() * y / 3),
                (int) (getWidth() * (x + 1) / 3),
                (int) (getHeight() * (y + 1) / 3));
    }

    private void drawSquare(Canvas canvas, Rect bounds, int squareIndex) {
        if (controller.isSquareCollapsed(squareIndex)) {
            String collapsedState = controller.getCollapsedState(squareIndex);
            canvas.drawText(collapsedState, bounds.centerX(), bounds.centerY(), SQUARE_CONTENTS_PAINT);
        } else {
            List<SpookyMark> spookyMarks = controller.getSpookyMarks(squareIndex);
            for (SpookyMark spookyMark : spookyMarks) {
                Rect spookyMarkRect = getSpookyMarkBounds(bounds, spookyMark);
                canvas.drawText(spookyMark.getText(), spookyMarkRect.centerX(), spookyMarkRect.centerY(), SQUARE_CONTENTS_PAINT);
            }
        }
    }

    private Rect getSpookyMarkBounds(Rect parent, SpookyMark spookyMark) {
        int x = spookyMark.getTurn() % 3;
        int y = spookyMark.getTurn() / 3;
        return new Rect(parent.left + parent.width() * x / 3,
                parent.top + parent.height() * y / 3,
                parent.left + parent.width() * (x + 1) / 3,
                parent.top + parent.height() * (y + 1) / 3);
    }

    private void drawEntanglements(Canvas canvas) {
        for (Entanglement entanglement : controller.getEntanglements()) {
            int entanglementNumber = entanglement.getStartingSpookyMark().getTurn();
            for (Map.Entry<SpookyMark, Collection<SpookyMark>> entry : entanglement.getLinkedSpookyMarks().entrySet()) {
                SpookyMark start = entry.getKey();
                Point p1 = getSpookyMarkCenter(start);
                for (SpookyMark destination : entry.getValue()) {
                    Point p2 = getSpookyMarkCenter(destination);
                    drawEntanglementLine(canvas, p1, p2, entanglementNumber);
                }
            }
        }
    }

    private Point getSpookyMarkCenter(SpookyMark spookyMark) {
        int squareIndex = spookyMark.getSquareIndex();
        Rect squareRect = getSquareBounds(squareIndex);
        Rect spookyMarkBounds = getSpookyMarkBounds(squareRect, spookyMark);
        return new Point(spookyMarkBounds.centerX(), spookyMarkBounds.centerY());
    }

    private void drawEntanglementLine(Canvas canvas, Point p1, Point p2, int entanglementNumber) {
        float offset = entanglementNumber * 10;
        canvas.drawLine(p1.x + offset, p1.y - offset, p2.x + offset, p2.y - offset, getEntanglementColor(entanglementNumber));
    }

    private Paint getEntanglementColor(int entanglementNumber) {
        Paint paint = new Paint();
        if (entanglementNumber == 0) {
            paint.setColor(Color.RED);
        } else if (entanglementNumber == 1) {
            paint.setColor(Color.GREEN);
        } else if (entanglementNumber == 2) {
            paint.setColor(Color.BLUE);
        } else if (entanglementNumber == 3) {
            paint.setColor(Color.CYAN);
        } else if (entanglementNumber == 4) {
            paint.setColor(Color.MAGENTA);
        } else if (entanglementNumber == 5) {
            paint.setColor(Color.LTGRAY);
        } else {
            int r = (entanglementNumber * 32) % 256;
            int g = (entanglementNumber * 64) % 256;
            int b = (128 + (entanglementNumber * 32)) % 256;
            paint.setARGB(255, r, g, b);
        }
        paint.setStrokeWidth(5);
        return paint;
    }

    private static Paint buildSpookyMarksPaint() {
        Paint spookyMarksPaint = new Paint();
        spookyMarksPaint.setTextSize(60);
        return spookyMarksPaint;
    }
}
