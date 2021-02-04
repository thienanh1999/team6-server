package util;

import bitzero.util.common.business.Debug;

public class Calculate {
    public static double calculateRange(double x1,double y1,double x2,double y2){
//        Debug.warn("thong so",x1,y1,x2,y2,Math.sqrt((x1-x2) * (x1-x2) + (y1-y2) * (y1-y2)));
        return Math.sqrt((x1-x2) * (x1-x2) + (y1-y2) * (y1-y2));
    }
}
