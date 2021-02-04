package model;

import bitzero.util.common.business.Debug;
import com.google.gson.JsonParser;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;

public class LoadConFig {
    private JSONObject jsonObject;
    private JSONObject cloneUser;
    private static LoadConFig a = null;

    private LoadConFig() {
        // Load Config to jsonObject
        String list[] = {
                "conf/InitGame.json",
                "conf/BuilderHut.json",
                "conf/ArmyCamp.json",
                "conf/Laboratory.json",
                "conf/Barrack.json",
                "conf/Wall.json",
                "conf/TownHall.json",
                "conf/Storage.json",
                "conf/DefenceBase.json",
                "conf/Defence.json",
                "conf/Troop.json",
                "conf/TroopBase.json",
                "conf/Resource.json",
                "conf/Obstacle.json",
                "conf/ClanCastle.json"
        };

        String s = "";
        try {
            jsonObject = null;
            for (String str : list) {
                s = "";
                FileReader fr = new FileReader(str);
                BufferedReader br = new BufferedReader(fr);
                String p;
                while (true){
                    p=br.readLine();
                    if (p==null) break;
                    s+=p;
                }
                JSONObject jo = new JSONObject(s);
                if (jsonObject == null) jsonObject = jo;
                else
                    LoadConFig.deepMerge(jo, this.jsonObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Load clone User
//        String cloneUserPath = "res/Config/CloneUser.json";
//        try {
//            FileReader fileReader = new FileReader(cloneUserPath);
//            BufferedReader bufferedReader = new BufferedReader(fileReader);
//            this.cloneUser = new JSONObject(bufferedReader.readLine());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public JSONObject getCloneUser() {
        return cloneUser;
    }

    public static JSONObject deepMerge(JSONObject source, JSONObject target) {

        try {
            for (String key : JSONObject.getNames(source)) {
                Object value = source.get(key);
                if (!target.has(key)) {
                    // new value for "key":
                    target.put(key, value);
                } else {
                    // existing value for "key" - recursively deep merge:
                    if (value instanceof JSONObject) {
                        JSONObject valueJson = (JSONObject) value;
                        deepMerge(valueJson, target.getJSONObject(key));
                    } else {
                        target.put(key, value);
                    }
                }
            }
//            System.out.println(source);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return target;
    }

    public double getDouble(String type, int level, String p) {
        try {
            if (level == 0) return jsonObject.getJSONObject(type).getDouble(p);
            return jsonObject.getJSONObject(type).getJSONObject(Integer.toString(level)).getDouble(p);
        } catch (Exception e) {
//            e.printStackTrace();
            return 0;
        }
    }

    public int getInt(String type, int level, String p) {
        try {
            if (level == 0) return jsonObject.getJSONObject(type).getInt(p);
            return jsonObject.getJSONObject(type).getJSONObject(Integer.toString(level)).getInt(p);
        } catch (JSONException e) {
//            e.printStackTrace();
            return 0;
        }
    }

    public String getString(String type, int level, String p) {
        try {
            return jsonObject.getJSONObject(type).getJSONObject(Integer.toString(level)).getString(p);
        } catch (JSONException e) {
            return "ERROR";
        }
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public static LoadConFig getInstance() {
        if (a == null) a = new LoadConFig();
        return a;
    }

    public static void main(String[] args) {
        LoadConFig loadConFig = LoadConFig.getInstance();
        try {
            System.out.println(LoadConFig.getInstance().getInt("ARM_4", 0, "at"));
            System.out.println(LoadConFig.getInstance().getJsonObject().getJSONObject("DEF_1"));
        } catch (Exception e) {
            e.printStackTrace();
        }
//        System.err.println(loadConFig.getString("BDH_1", "1", "coin"));
//        try {
//            System.err.println(loadConFig.getJsonObject().getJSONObject("BDH_1"));
//            System.err.println(loadConFig.getInt("BDH_1",1,"buildTime"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        try{
//            System.out.println(loadConFig.jsonObject.getJSONObject("STO_1").getJSONObject("1"));
//        }catch (Exception e){
//            e.printStackTrace();
//        }
    }
}
