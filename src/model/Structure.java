package model;

import java.awt.*;
import java.util.Date;

public class Structure extends Entity{
    public static final int STATE_0 = 0;
    public static final int STATE_BUILD = 1;
    public static final int ID_TOWNHALL=900;
    private int state;
    private int time;
    private long[] res = new long[3];
    private long[] capacity = new long[3];
    public int getInt(String key){
        return LoadConFig.getInstance().getInt(this.getType(),this.getLevel(), key);
    }
    public String getString(String key){
        return LoadConFig.getInstance().getString(this.getType(),this.getLevel(),key);
    }
    public Structure(int id, String type, Point pos) {
        super(id, type, pos);
        this.time=(int) (new Date().getTime()/1000);
        this.state=0;
    }

    public void updateCapacity(int level) {
        switch (this.getType()) {
            case "STO_1":
                this.capacity[0] = LoadConFig.getInstance().getInt(getType(), level, "capacity");
                break;
            case "STO_2":
                this.capacity[1] = LoadConFig.getInstance().getInt(getType(), level, "capacity");
                break;
            case "STO_3":
                this.capacity[2] = LoadConFig.getInstance().getInt(getType(), level, "capacity");
                break;
            case "TOW_1":
                this.capacity[0] = LoadConFig.getInstance().getInt(getType(), level, "capacityGold");
                this.capacity[1] = LoadConFig.getInstance().getInt(getType(), level, "capacityElixir");
                this.capacity[2] = LoadConFig.getInstance().getInt(getType(), level, "capacityDarkElixir");
                break;
        }
    }

    public long[] getRes() {
        return res;
    }

    public long[] getCapacity() {
        return capacity;
    }

    public void setRes(long res, int id) {
        this.res[id] = res;
    }

    @Override
    public String toString() {
        return "Structure{" +
                "state=" + state +
                ", time=" + time +
                '}';
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
