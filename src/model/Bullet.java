package model;

import bitzero.util.common.business.Debug;

import java.awt.geom.Point2D;

public class Bullet {
    private int dame;
    private Point2D.Double position;
    private long time;
    private int target;

    public Bullet(int dame, Point2D.Double position, int time) {
        this.dame = dame;
        this.position = new Point2D.Double(position.x,position.y);
        this.time = time;
    }

    public void setTarget(int id) {
        this.target = id;
    }

    public void updateBullet(long time1) {
        this.time -= time1;
    }

    public int getDame() {
        return dame;
    }

    public void setDame(int dame) {
        this.dame = dame;
    }

    public Point2D.Double getPosition() {
        return position;
    }

    public void setPosition(Point2D.Double position) {
        this.position = position;
    }

    public double getTime() {
        return this.time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getTarget() {
        return target;
    }
}
