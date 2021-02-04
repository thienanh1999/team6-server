package model;

import bitzero.util.common.business.Debug;

import java.awt.*;
import java.awt.geom.Point2D;

public class DropTroop {
    private String type;
    private Point2D.Double point;
    private int time;
    private int id;

    public DropTroop(String type, int id, Point2D.Double point, int time) {
        this.type = type;
        this.point = point;
        this.time = time;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Point2D.Double getPoint() {
        return this.point;
    }

    public void setPoint(Point2D.Double point) {
        this.point = point;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
