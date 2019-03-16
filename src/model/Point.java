package model;

import javafx.scene.paint.Color;

public class Point {
    Color color;
    int x;
    int y;
    double pointSize;

    boolean isPointLast;

    public Point(Color color, int x, int y, double pointSize, boolean isPointLast) {
        this.color = color;
        this.x = x;
        this.y = y;
        this.pointSize = pointSize;
        this.isPointLast = isPointLast;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double getPointSize() {
        return pointSize;
    }

    public Color getColor() {
        return color;
    }

    public boolean isPointtLast() {
        return isPointLast;
    }
}
