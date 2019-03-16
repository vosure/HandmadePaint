package model;

import java.util.ArrayList;

public class PointHolder {
    ArrayList<Point> points = new ArrayList();

    public Point getPoint(int index) {
        return points.get(index);
    }

    public void addPoint(Point point){
        points.add(point);
    }

    public int getCount(){
        return points.size();
    }

    public void clearPoints(){
        points.clear();
    }

}
