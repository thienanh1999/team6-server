package service;
import bitzero.server.entities.User;
import bitzero.server.extensions.BaseClientRequestHandler;
import bitzero.server.extensions.data.DataCmd;
import bitzero.util.common.business.Debug;

import cmd.CmdDefine;
import cmd.receive.battle.RequestDropTroop;

import cmd.receive.battle.RequestEndGame;
import cmd.receive.battle.RequestSendLog;
import cmd.send.demo.ResponseEndGame;
import cmd.send.demo.ResponseOpponentInfo;
import com.google.gson.Gson;
import event.eventType.DemoEventType;
import model.BattleController;
import model.EndGame;
import model.UserGame;
import org.json.JSONException;
import org.json.JSONObject;
import util.Data;
import util.database.DataHandler;
import util.server.ServerUtil;

import java.util.HashMap;
import java.util.Map;

public class BattleHandler extends BaseClientRequestHandler{

    public static short DEMO_MULTI_IDS = 3000;
    public static String P = "v1";
//    private final Logger logger = LoggerFactory.getLogger("BattleHandler");
    private Map<Integer, BattleController> battleControllerMap = new HashMap<Integer, BattleController>();

    public BattleHandler() {
        super();
    }

    /**
     * this method automatically loaded when run the program
     * register new event, so the core will dispatch event type to this class
     */
    public void init() {
        getParentExtension().addEventListener(DemoEventType.LOGIN_SUCCESS, this);
    }

    @Override
    public void handleClientRequest(User user, DataCmd dataCmd) {
        try {
            switch (dataCmd.getId()) {
                case CmdDefine.FIND_MATCH:
                    processFindMatch(user);
                    break;
                case CmdDefine.POPULATE_USER:
                    Debug.warn("Populate User");
                    break;
                case CmdDefine.DROP_TROOP:
                    RequestDropTroop requestDropTroop = new RequestDropTroop(dataCmd);
                    processDropTroop(user, requestDropTroop);
                    break;
                case CmdDefine.SEND_LOG:
                    processSendLog(user, new RequestSendLog(dataCmd));
                    break;
                case CmdDefine.END_GAME:
                    RequestEndGame requestEndGame = new RequestEndGame(dataCmd);
                    processEndGame(user, requestEndGame);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.printStackTrace();
        }
    }

    // TODO: Find Opponent with current trophy
    private void processFindMatch(User user) {
        UserGame userGame = Data.getData(user.getId());
        try {
            userGame.updateTroop();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Debug.warn("Find Match", user.getId(), userGame.getTrophy());

        JSONObject jsonObject = null;
        try {
            String key = ServerUtil.getModelKeyName(userGame.getClass().getSimpleName(), user.getId()) + CmdDefine.NUMBER;
            jsonObject = new JSONObject(DataHandler.get(key).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        BattleController battleController = getBattleController(userGame);
        battleController.initMap(jsonObject);
        battleController.allocateResToStructure();
        battleController.setTroopCount(userGame.getTroop());
        Debug.warn("processFindMatch", battleController);
        JSONObject mapInfo = battleController.getMapJson();
        send(new ResponseOpponentInfo((short) 0, mapInfo), user);
    }

    private void processDropTroop(User user, RequestDropTroop requestDropTroop) {
        UserGame userGame = Data.getData(user.getId());

        BattleController battleController = getBattleController(userGame);
        battleController.dropTroop(requestDropTroop.getDropTroops());
        EndGame endGame = battleController.getEndResult();

        if (endGame != null) {
            send(new ResponseEndGame((short) 0, endGame), user);
        }
    }

    private void processSendLog(User user, RequestSendLog requestSendLog) {
        UserGame userGame = Data.getData(user.getId());
        Debug.warn("Send Logs", user.getId(), requestSendLog.getLogs().size());

        BattleController battleController = getBattleController(userGame);
        battleController.updateClientLogs(requestSendLog.getLogs());
    }

    private void processEndGame(User user, RequestEndGame requestEndGame) {
        UserGame userGame = Data.getData(user.getId());
        Debug.warn("End Game", user.getId());

        BattleController battleController = getBattleController(userGame);
        battleController.setEndGameTime(requestEndGame.getEndTime());
        battleController.dropTroop(requestEndGame.getDropTroops());
        EndGame endGame = battleController.getEndResult();
        Debug.warn(endGame.getTroopUsed().get("ARM_1"), endGame.getTroopUsed().get("ARM_2"),
                endGame.getTroopUsed().get("ARM_4"), endGame.getTroopUsed().get("ARM_6"));
        send(new ResponseEndGame((short) 0, endGame), user);
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

    private BattleController getBattleController(UserGame userGame) {
        BattleController battleController = battleControllerMap.get(userGame.getId());
        if (battleController == null || battleController.getEndResult() != null) {
            battleController = new BattleController();
            battleController.userID = userGame.getId();
            battleController.setOpponentID(userGame.getId());
            battleControllerMap.put(userGame.getId(), battleController);
        }
        return battleController;
    }
}
