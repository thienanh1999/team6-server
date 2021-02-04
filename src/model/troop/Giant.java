package model.troop;

import util.Constant;

import java.awt.geom.Point2D;

public class Giant extends Troop{
    public Giant(int id, int level, Point2D.Double position){
        super(id,level,position,"ARM_4");
        this.frameToPrepareAttack = 32;
        this.targetType = Constant.TARGET_DEFENSE;
    }
}
