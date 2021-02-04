package model;

import java.awt.*;

public class Entity {
    private int id;
    private String type;
    private Point pos;
    private int level;
    private Point size;
    public Entity(int id, String type, Point pos) {
        this.id = id;
        this.type = type;
        this.pos = pos;
        this.level=1;
        int width = LoadConFig.getInstance().getInt(this.getType(), 1, "width");
        int height = LoadConFig.getInstance().getInt(this.getType(), 1, "height");
        this.size = new Point();
        this.size.x = width;
        this.size.y = height;
    }

    public int getId() {
        return this.id;

    }
    public int getInt(String key){
        return LoadConFig.getInstance().getInt(this.getType(),this.getLevel(), key);
    }
    public String getString(String key){
        return LoadConFig.getInstance().getString(this.getType(),this.getLevel(),key);
    }
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Point getPos() {
        return pos;
    }

    public void setPos(Point pos) {
        this.pos = pos;
    }

    public Point getSize(){
        return this.size;
    }
}
