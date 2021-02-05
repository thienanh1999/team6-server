package util;

import bitzero.util.common.business.Debug;

import java.awt.*;
import java.util.HashMap;

public class Constant {
    public static final int GAME_TICK = 25;
    public static final int TIME_STEP = 5000;

    // TROOP
    public static final String WARRIOR = "ARM_1";
    public static final String ARCHER = "ARM_2";
    public static final String GIANT = "ARM_4";
    public static final String GOBLIN = "ARM_3";
    public static final String FLYING_BOOM = "ARM_6";
    public static final String[] troopTypeList = {"ARM_1", "ARM_2","ARM_3", "ARM_4", "ARM_6"};

    public static final int TARGET_WALL = 0;
    public static final int TARGET_STRUCTURE = 1;
    public static final int TARGET_DEFENSE = 2;

    public static final int RANGE_NEIGHBOR = 3;

    public static final class TROOP_STATE {
        public static final int IDLE = 0;
        public static final int RUNNING = 1;
        public static final int ATTACKING = 2;
        public static final int DONE = 3;

    }

    public static final class DIRECTION {
        public static final int S = 1;
        public static final int SW = 2;
        public static final int W = 3;
        public static final int NW = 4;
        public static final int N = 5;
        public static final int NE = 6;
        public static final int E = 7;
        public static final int SE = 0;
    }

    public static final Point[] DELTA = {
            new Point(0, 1),
            new Point(-1, 0),
            new Point(0, -1),
            new Point(1, 0),
            new Point(1, 1),
            new Point(-1, 1),
            new Point(-1, -1),
            new Point(1, -1),
    };

    public static final class DELTA_POSITION {
        public static final HashMap<Integer, Point> map = new HashMap<>();

        static {
            map.put(DIRECTION.SE, new Point(1, 1));
            map.put(DIRECTION.S, new Point(0, 1));
            map.put(DIRECTION.SW, new Point(-1, 1));
            map.put(DIRECTION.W, new Point(-1, 0));
            map.put(DIRECTION.NW, new Point(-1, -1));
            map.put(DIRECTION.N, new Point(0, -1));
            map.put(DIRECTION.NE, new Point(1, -1));
            map.put(DIRECTION.E, new Point(1, 0));
        }
    }

    public static final class DIR {
        public static final int[][] dir = new int[5][5];
        static{
            Debug.warn("static _____________");
            dir[2][2] = DIRECTION.SE;
            dir[1][2] = DIRECTION.S;
            dir[0][2] = DIRECTION.SW;
            dir[0][1] = DIRECTION.W;
            dir[0][0] = DIRECTION.NW;
            dir[1][0] = DIRECTION.N;
            dir[2][0] = DIRECTION.NE;
            dir[2][1] = DIRECTION.E;
        }
    }


}
