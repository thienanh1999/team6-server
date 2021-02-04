package util;

import cmd.CmdDefine;
import com.google.gson.Gson;
import model.UserGame;
import org.json.JSONObject;
import util.database.DataHandler;
import util.server.ServerUtil;

public class Data {
    public static UserGame getData(int userID){
        UserGame userGame = new UserGame(userID);
        try {
            Gson gson = new Gson();
            String key = ServerUtil.getModelKeyName(userGame.getClass().getSimpleName(), userID) + CmdDefine.NUMBER;
            JSONObject jsonObject = new JSONObject(DataHandler.get(key).toString());
            userGame = gson.fromJson(String.valueOf(jsonObject), UserGame.class);
            userGame.updateStructure();
            userGame.updateTroop();
        } catch (Exception e) {
            e.printStackTrace();

        }
        return userGame;
    }
}
