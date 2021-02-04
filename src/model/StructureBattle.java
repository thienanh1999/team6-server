package model;

import bitzero.util.common.business.Debug;
import model.troop.Troop;
import util.Calculate;
import util.Constant;

import util.Constant;

import java.awt.*;
import java.util.ArrayList;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Date;

public class StructureBattle extends Entity {
    public static final int STATE_0 = 0;
    public static final int STATE_BUILD = 1;
    public static final int ID_TOWNHALL = 900;
    public static final int DAME = 0;
    public static final int X = 1;
    public static final int Y = 2;
    public static final int TIMEBULLET = 3;
    public static final int TARGET = 4;
    public static final String TOWN_HALL="TOW_1";
    public static final String ARMY_CAMP="AMC_1";
    public static final String BARRACK="BAR_1";
    public static final String BUILDER_HUT="BDH_1";
    public static final String CLAN_CASTLE="CLC_1";
    public static final String LABORATORY="LAB_1";
    public static final String GOLD_MINE="RES_1";
    public static final String ELIXIR_MINE="RES_2";
    public static final String DARK_ELIXIR_MINE="RES_3";
    public static final String GOLD_STORAGE="STO_1";
    public static final String ELIXIR_STORAGE="STO_2";
    public static final String DARK_ELIXIR_STORAGE="STO_3";
    public static final String WALL="WAL_1";
    // DEFENSE
    public static final String ARCHER_TOWER="DEF_2";
    public static final String CANON="DEF_1";
    public static final String MORTAR="DEF_3";
    public static final String AIR_DEFENSE="DEF_5";


    public int[] capacity;
    public int[] resource;
    private int state;
    private int time;
    public boolean destroyState;
    public int hitPoint;
    public int hitPointMax;
    public BattleController battleController;
    public Point size;
    public int targetType;
    public ArrayList<StructureBattle> listNeighbor;
    public int countTroop = 0;

    public int getInt(String key) {
        return LoadConFig.getInstance().getInt(this.getType(), this.getLevel(), key);
    }
    public double getDouble(String key,boolean b) {
        return LoadConFig.getInstance().getDouble(this.getType(), 0, key);
    }
    public int getInt(String key, boolean check) {
        return LoadConFig.getInstance().getInt(this.getType(), 0, key);
    }
    public String getString(String key){
        return LoadConFig.getInstance().getString(this.getType(),this.getLevel(),key);
    }

    public StructureBattle(int id, String type, Point pos,int level) {
        super(id, type, pos);
        this.resource= new int[]{0, 0, 0};
        this.capacity= new int[]{0, 0, 0};
        this.setLevel(level);
        int height = this.getInt("height");
        int width = this.getInt("width");
        this.size=new Point(width,height);
        this.destroyState = false;
        this.time = (int) (new Date().getTime() / 1000);
        this.state = 0;
        this.loadConfig();

        if (this.getType().startsWith("D")){
            this.targetType = Constant.TARGET_DEFENSE;
        }
        else if (this.getType().startsWith("W")){
            this.targetType = Constant.TARGET_WALL;
            this.listNeighbor = new ArrayList<>();
        }
        else{
            this.targetType = Constant.TARGET_STRUCTURE;
        }
        this.updateCapacity(level);
    }
    public void setResource(int gold, int elixir, int darkElixir) {
        this.resource = new int[]{gold, elixir, darkElixir};
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
    public void loadConfig() {
        this.hitPoint=this.getInt("hitpoints");
        this.hitPointMax=this.getInt("hitpoints");
    }

    public int getHitPointMax() {
        return this.hitPointMax;
    }

    public Point getSize() {
        return this.size;
    }

    public BattleController getBattleController() {
        return this.battleController;
    }

    public void setBattleController(BattleController battleController) {
        this.battleController = battleController;
    }

    public boolean isDestroyState() {
        return this.destroyState;
    }

    public void setDestroyState(boolean destroyState) {
        this.destroyState = destroyState;
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

    public void update() {

    }

    public int[] getCapacity() {
        return this.capacity;
    }

    public void setCapacity(int[] capacity) {
        this.capacity = capacity;
    }

    public int[] getResource() {
        return this.resource;
    }

    public void setResource(int[] resource) {
        this.resource = resource;
    }

    public void updateHitPoint(int dame) {
        if (this.destroyState) return;
        this.hitPoint -= dame;
        if (this.hitPoint <= 0) {
            this.destroyState = true;
            this.battleController.logDestroyStructure(this);
//            Debug.warn("GameTick:", battleController.getGameTick(), "Structure Destroy:" + this.getType(),this.getId());
            battleController.noticeStructureIsDestroyed(this.getId());
        }
        this.updateRes(dame);
    }

    public void updateRes(int dame) {
    }

    public static void main(String[] args) {
    }
    public void target(){
        this.countTroop += 1;
    }

    public void stopTarget(){
        this.countTroop -= 1;
    }
}
