package util;

import bitzero.util.common.business.Debug;

import java.awt.*;

public class ArrayUtil {
    public static void fillArray(int[][] array, Point position, Point size, int value){
        for (int i=0 ;i< size.x; i++)
            for (int j=0;j <size.y; j++)
            {
                array[position.x+i][position.y+j] = value;
            }
    }
    public static void resetArray( int[][] array, int value){
        for (int i=0 ;i< array.length; i++)
        {
            for (int j=0; j <array[i].length; j++){
                array[i][j] = value;
            }
        }
    }
}
