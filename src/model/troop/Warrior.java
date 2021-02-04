package model.troop;

import util.Constant;

import java.awt.geom.Point2D;


public class Warrior extends Troop {
    public Warrior(int id, int level, Point2D.Double position){

        super(id,level,position,"ARM_1");
        this.frameToPrepareAttack = 32;
        this.targetType = Constant.TARGET_STRUCTURE;

//        Debug.warn("warrior");
//        Debug.warn(this.frameToUpdateMove);
//        Debug.warn(this.frameToUpdateAttack);
//        Debug.warn(this.frameToRepairAttack);
    }
}
