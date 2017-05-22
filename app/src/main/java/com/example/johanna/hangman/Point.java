package com.example.johanna.hangman;

/**
 * This class saves the real positions on Canvas related to screen resolution.
 * Created by Johanna on 09.01.2017.
 */

public class Point {
    private float x;
    private float y;
    private float width;
    private float height;
    private float xFactor;
    private float yFactor;

    public Point(HangmanView view, float x, float y) {

        width = view.getDisplyWidth();
        height = view.getDisplayHeight();
        y = y - 100;
        if (y < 0) {
            y = 0;
        }
        this.x = (float) x * width / 1080;
        this.y = (float) y * height / 1536;

    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
