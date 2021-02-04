package model.log;

import java.awt.*;
import java.awt.geom.Point2D;

public class StructureLog {
    String type;
    int id;
    double hp;
    Point point;

    public StructureLog(String type, int id, double hp, Point point) {
        this.type = type;
        this.hp = hp;
        this.point = point;
        this.id = id;
    }

    public String showLog() {
        return this.type + " " + this.id + " " + this.point.x + " " + this.point.y;
    }
}
