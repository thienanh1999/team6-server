package model.troop;

import util.Constant;

import java.awt.geom.Point2D;

public class Goblin extends Troop {
    public Goblin(int id, int level, Point2D.Double position){

        super(id,level,position,"ARM_3");
        this.frameToPrepareAttack = 24;
        this.targetType = Constant.TARGET_STRUCTURE;

    }
}
