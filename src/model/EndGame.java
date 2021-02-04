package model;

import bitzero.util.common.business.Debug;
import cmd.CmdDefine;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;
import util.Data;
import util.database.DataHandler;
import util.server.ServerUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class EndGame {
    private int userID;
    private Long gold = 0L;
    private Long elixir = 0L;
    private Long darkElixir = 0L;
    private int trophy = 0;
    private Map<String, Integer> troopUsed;

    public EndGame(int userID, Long gold, Long elixir, Long darkElixir, int trophy, Map<String, Integer> troopUsed) {
        this.userID = userID;
        Debug.warn("Update Battle", userID);
        this.gold = gold;
        this.elixir = elixir;
        this.darkElixir = darkElixir;
        this.trophy = trophy;
        this.troopUsed = troopUsed;
        this.updateData();
    }

    public void updateData() {
        UserGame userGame = Data.getData(this.userID);
        try {
            userGame.updateTroop();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        userGame.setGold((int) (userGame.getGold() + this.gold));
        userGame.setElixir((int) (userGame.getElixir() + this.elixir));
        userGame.setDarkElixir((int) (userGame.getDarkElixir() + this.gold));
        userGame.setTrophy((int) (userGame.getTrophy() + this.trophy));
        Map<String, Integer> troop = userGame.getTroop();
        this.troopUsed.forEach((type, n) -> {
            Debug.warn("Troop",type,n);
            if (n != 0) {
                troop.put(type, troop.get(type) - n);
                userGame.setSpaceTroop(userGame.getSpaceTroop()+n*LoadConFig.getInstance().getInt(type,0,"housingSpace"));
            }
        });
        try {
            userGame.saveModel(userID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private UserGame getData(int userID) {
        UserGame userGame = new UserGame(userID);
        try {
            Gson gson = new Gson();
            String key = ServerUtil.getModelKeyName(userGame.getClass().getSimpleName(), userID) + CmdDefine.NUMBER;
            JSONObject jsonObject = new JSONObject(DataHandler.get(key).toString());
            userGame = gson.fromJson(String.valueOf(jsonObject), UserGame.class);
            userGame.getCountID();
        } catch (Exception e) {
            e.printStackTrace();

        }
        return userGame;
    }

    public Long getGold() {
        return gold;
    }

    public void setGold(Long gold) {
        this.gold = gold;
    }

    public Long getElixir() {
        return elixir;
    }

    public void setElixir(Long elixir) {
        this.elixir = elixir;
    }

    public Long getDarkElixir() {
        return darkElixir;
    }

    public void setDarkElixir(Long darkElixir) {
        this.darkElixir = darkElixir;
    }

    public int getTrophy() {
        return trophy;
    }

    public void setTrophy(int trophy) {
        this.trophy = trophy;
    }

    public Map<String, Integer> getTroopUsed() {
        return troopUsed;
    }

    public void setTroopUsed(Map<String, Integer> troopUsed) {
        this.troopUsed = troopUsed;
    }

}
