package model.troop;

import util.Constant;

import java.awt.*;
import java.awt.geom.Point2D;

public class FlyingBoom extends Troop {
    public FlyingBoom(int id, int level, Point2D.Double position){
        super(id,level,position,"ARM_6");
        this.frameToPrepareAttack = 40;
        this.targetType = Constant.TARGET_DEFENSE;
    }

    @Override
    protected Boolean checkAvailablePosition(Point position, int[][] mapId) {
        int x = position.x;
        int y = position.y;
        return x >= 0 && x < this.mapHeight && y >= 0 && y < this.mapWidth;
    }
}
