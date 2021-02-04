package model.troop;

import util.Constant;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D;

public class Archer extends Troop {
    public Archer(int id, int level, Point2D.Double position){

        super(id,level,position,"ARM_2");
        this.frameToPrepareAttack = 24;
        this.targetType = Constant.TARGET_STRUCTURE;
    }
}
