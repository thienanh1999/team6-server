package model;

import bitzero.util.common.business.Debug;

import java.awt.*;
import java.util.Date;

public class Storage extends StructureBattle {
    private double goldCollect;
    private double elixirCollect;
    private double darkElixirCollect;
    private double gold;
    private double elixir;
    private double darkElixir;

    public Storage(int id, String type, Point pos, int level) {
        super(id, type, pos, level);
        int capacity = this.getInt("capacity");
        this.resource = new int[]{0, 0, 0};
        this.capacity = new int[]{0, 0, 0};
        this.gold = this.elixir = this.darkElixir = 0;
        if (this.getType().startsWith("STO") || this.getType().startsWith("RES")) {
            if (this.getString("type").startsWith("gold")) this.capacity[0] = capacity;
            if (this.getString("type").startsWith("eli")) this.capacity[1] = capacity;
            if (this.getString("type").startsWith("dark")) this.capacity[2] = capacity;
        }
        if (this.getType().startsWith("TOW")) {
            this.capacity = new int[]{this.getInt("capacityGold"), this.getInt("capacityElixir"), this.getInt("capacityDarkElixir")};
        }
//        if (this.getType().startsWith("RES")) {
//            if (this.getString("type").startsWith("gold")) this.resource[0] = capacity;
//            if (this.getString("type").startsWith("eli")) this.resource[1] = capacity;
//            if (this.getString("type").startsWith("dark")) this.resource[2] = capacity;
//        }
    }


    public void updateRes(int dame) {
        double gold1 = dame * 1.0 / this.hitPointMax;
        double elixir1 = gold1;
        double darkElixir1 = gold1;
//        Debug.warn("gold1",gold1,dame,this.hitPointMax);
        if (this.getType().startsWith("TOW")) {
            gold1 =gold1* this.resource[0];
            elixir1 =elixir1* this.resource[1];
            darkElixir1 =darkElixir* this.resource[2];
        } else if (this.getType().equals(StructureBattle.GOLD_STORAGE)) {
            gold1 =gold1* this.resource[0];
            elixir1 = darkElixir1 = 0;
        } else if (this.getType().equals(StructureBattle.ELIXIR_STORAGE)) {
            elixir1 =gold1* this.resource[1];
            gold1 = darkElixir1 = 0;
        } else if (this.getType().equals(StructureBattle.DARK_ELIXIR_STORAGE)) {
            darkElixir1 =darkElixir* this.resource[2];
            gold1 = elixir1 = 0;
        } else {

            return;
        }
//        Debug.warn("Res collect",gold1,elixir1,darkElixir1);
        if (this.hitPoint <= 0) {
            gold1 = this.resource[0] - (int) (this.goldCollect);
            elixir1 = this.resource[1] - (int) (this.elixirCollect);
            darkElixir1 = this.resource[2] - (int) (this.darkElixirCollect);

        }
        this.goldCollect += gold1;
        this.darkElixirCollect += darkElixir1;
        this.elixirCollect += elixir1;

        this.gold += gold1;
        this.elixir += elixir1;
        this.darkElixir += darkElixir1;
//        Debug.warn("Res collect",this.gold,this.elixir,this.darkElixir);
        battleController.updateResource((int) (this.gold), (int) (this.elixir), (int) (this.darkElixir));
        this.gold -= (int) (this.gold);
        this.elixir -= (int) (this.elixir);
        this.darkElixir -= (int) (this.darkElixir);
//        Debug.warn("GameTick:", battleController.tickGame + "/Gold:" + gold + " Elixir " + elixir + " DarkElixir " + darkElixir);

    }

    public static void main(String[] args) {
        String p = "a";
        String b = "a";
        System.out.println(p.equals(b));
    }
}
