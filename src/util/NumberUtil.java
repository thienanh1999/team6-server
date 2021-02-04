package util;

import bitzero.util.common.business.Debug;

import java.awt.*;
import java.awt.geom.Point2D;

public class NumberUtil {
    public static double getDistanceToRect(Point2D.Double position, Point min, Point max){
        double x = position.x;
        double y = position.y;

        if (min.x <= x && x <= max.x) {
            if (min.y <= y && y <= max.y) {
                return 0;
            } else {
                if (y > max.y) return y - max.y;
                else return min.y - y;
            }
        } else {
            if (min.y <= y && y <=max.y) {
                if (x > max.x) return x - max.x;
                else return min.x - x;
            } else {
                double dxmin = min.x - position.x;
                double dxmax = position.x - max.x;
                double dymin = min.y - position.y;
                double dymax = position.y - max.y;
                double dx = Math.max(dxmin, dxmax);
                double dy = Math.max(dymin, dymax);
                return Math.round(Math.sqrt(dx * dx + dy * dy)*1000f)/1000f;
            }
        }
    }

    public static double getEulerDistance(Point p1, Point2D.Double p2){
        double dx = p1.x - p2.x;
        double dy = p1.y - p2.y;
        return Math.round(dx * dx + dy * dy);
    }
}
