package model.log;

import java.awt.*;
import java.awt.geom.Point2D;

public class TroopLog {
    public String type;
    public int id;
    public double hp;
    public Point2D.Double point;

    public TroopLog(String type, int id, double hp, Point2D.Double point) {
        this.type = type;
        this.hp = hp;
        double x = point.x;
        double y = point.y;
        this.point = new Point2D.Double(((int)(x*10))/(double)10, ((int)(y*10))/(double)10);
        this.id = id;
    }

    public String showLog() {
        return this.type + " " + this.id + " " + this.point.x + " " + this.point.y;
    }
}
