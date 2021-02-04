package model;

import java.awt.*;

public class Obstacle extends Entity {
    private int removeTime;
    private int state;

    public Obstacle(int id, String type, Point pos) {

        super(id, type, pos);
        this.setLevel(1);
        this.state=0;
    }

    public int getRemoveTime() {
        return removeTime;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setRemoveTime(int removeTime) {
        this.removeTime = removeTime;
    }
}
