package service;

import bitzero.engine.sessions.ISession;
import bitzero.server.core.IBZEvent;
import bitzero.server.entities.User;
import bitzero.server.extensions.BaseClientRequestHandler;
import bitzero.server.extensions.data.DataCmd;

import bitzero.util.common.business.Debug;
import cmd.CmdDefine;

import cmd.receive.demo.*;

import cmd.send.demo.*;

import java.awt.Point;
import java.util.*;

import com.google.gson.Gson;
import event.eventType.DemoEventParam;
import event.eventType.DemoEventType;
import model.*;

import model.troop.Troop;
import org.apache.commons.lang.exception.ExceptionUtils;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Data;
import util.database.DataHandler;
import util.server.ServerUtil;

public class DemoHandler extends BaseClientRequestHandler {

    public static short DEMO_MULTI_IDS = 2000;
    public static String P = "v1";
    /**
     * log4j level
     * ALL < DEBUG < INFO < WARN < ERROR < FATAL < OFF
     */

    private final Logger logger = LoggerFactory.getLogger("DemoHandler");

    public DemoHandler() {
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
    /**
     * this method handle all client requests with cmdId in range [1000:2999]
     *
     */
    public void handleClientRequest(User user, DataCmd dataCmd) {
        //TODO: Nghe goi tin gui den
        try {
//            Logger.
//            dataCmd.setId();
            switch (dataCmd.getId()) {
                // get username
                case CmdDefine.GETTIME:
                    processGetTime(user);
                    break;
                // set username
                case CmdDefine.SET_NAME:
                    RequestSetName set = new RequestSetName(dataCmd);
//                    processSetName(set, user);
                    break;
                case CmdDefine.MOVE:
                    RequestMove move = new RequestMove(dataCmd);
//                    processMove(user, move);
                    break;
                case CmdDefine.LOADSTRUCTURE:
                    Debug.warn("load map");
//                    RequestObject object = new RequestObject(dataCmd);
                    RequestID load = new RequestID(dataCmd);
                    processLoadStructure(user, load);
                    break;
                case CmdDefine.LOADRES:
                    Debug.warn("load res");
                    RequestID load2 = new RequestID(dataCmd);
                    processLoadRes(user, load2);
                    break;
                case CmdDefine.LOADTROOP:
                    RequestID loadtroop = new RequestID(dataCmd);
                    processLoadTroop(user, loadtroop);
                    break;
                case CmdDefine.LOADBARACK:
                    RequestID loadbarack = new RequestID(dataCmd);
                    processLoadBarack(user, loadbarack);
                    break;
                case CmdDefine.BUILD:
                    RequestType_X_Y_Builder build = new RequestType_X_Y_Builder(dataCmd);
                    processBuildObject(user, build);
                    break;
                case CmdDefine.MOVESTRUCTURE:
                    RequestIDPos moveStructure = new RequestIDPos(dataCmd);
                    processMoveStructure(user, moveStructure);
                    break;
                case CmdDefine.CANCEL:
                    RequestID_ID cancel = new RequestID_ID(dataCmd);
                    processCancel(user, cancel);
                    break;
                case CmdDefine.CHEAT:
                    RequestCheat convertG = new RequestCheat(dataCmd);
                    processCheat(user, convertG);
                    break;
                case CmdDefine.UPGRADEFAST:
                    RequestID_ID upgradeFast = new RequestID_ID(dataCmd);
                    processUpgradeFast(user, upgradeFast);
                    break;
                case CmdDefine.DELETE:
                    RequestID_ID deleteObstacle = new RequestID_ID(dataCmd);
                    processDeleteObstacle(user, deleteObstacle);
                    break;
                case CmdDefine.UPGRADE:
                    RequestID_ID upgrade = new RequestID_ID(dataCmd);
                    processUpgrade(user, upgrade);
                    break;
                case CmdDefine.COLLECT:
                    RequestID collect = new RequestID(dataCmd);
                    processCollect(user, collect);
                    break;
                case CmdDefine.TRAINTROOP:
                    RequestIDType traintroop = new RequestIDType(dataCmd);
                    processTrainTroop(user, traintroop);
                    break;
                case CmdDefine.DELETETRAINTROOP:
                    RequestIDType deletetraintroop = new RequestIDType(dataCmd);
                    processDeleteTrainTroop(user, deletetraintroop);
                    break;
                case CmdDefine.FASTTRAIN:
                    RequestID fasttrain = new RequestID(dataCmd);
                    processFastTrain(user, fasttrain);
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("DEMO HANDLER EXCEPTION " + e.getMessage());
            logger.warn(ExceptionUtils.getStackTrace(e));
        }

    }

    private void processFastTrain(User user, RequestID fasttrain) {
        Debug.warn("Fast Train");
        try {
            UserGame userGame = Data.getData(user.getId());
            Debug.warn("space",userGame.getSpaceTroop());
            userGame.updateStructure();
            userGame.updateTroop();
            List<Brack> barrackList = userGame.getMap().getBrackList();
            for (Brack b : barrackList) {
                if (b.getId() == fasttrain.getID()) {
                    int timeTrain = b.getTimeFirstTroop()- Troop.get(b.getListTrain().get(0).getType(),"housingSpace");
                    int quantity = 0;
                    for (TypeQuantity troop : b.getListTrain()) {
                        int timeTrainTroop = Troop.get(troop.getType(),"trainingTime");
                        int troopSpace=Troop.get(troop.getType(),"housingSpace");
                        Debug.warn("Troop",troop.getN(),troopSpace);
                        quantity += (troopSpace*troop.getN());
                        timeTrain+=(timeTrainTroop*troop.getN());
                        timeTrain+=Troop.get(troop.getType(),"trainingTime");
                    }
                    int space=userGame.getSpaceTroop();
                    int res=userGame.convert(timeTrain);
                    if (space<quantity){
                        Debug.warn("Space",space,quantity);
                        logger.error("Khong du space");
                        send(new ResponseConfirm(DemoError.ERROR.getValue(), CmdDefine.FASTTRAIN),user);
                    }else if (res>userGame.getG()){
                        logger.error("Khong du tai nguyen");
                        send(new ResponseConfirm(DemoError.ERROR.getValue(), CmdDefine.FASTTRAIN),user);
                    }else{
                        userGame.setG(userGame.getG()-res);
                        b.setTimeStart(0);//fix
                        send(new ResponseConfirm(DemoError.SUCCESS.getValue(), CmdDefine.FASTTRAIN),user);
                    }
                }
            }
            userGame.saveModel(user.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processGetTime(User user) {
        int time = (int) (new Date().getTime() / 1000);
//        Debug.warn(time);
        send(new ResponseInt(DemoError.SUCCESS.getValue(), CmdDefine.GETTIME, time), user);
    }

    private void processLoadTroop(User user, RequestID loadtroop) {
        Debug.warn("Load Troop");
        UserGame userGame = Data.getData(user.getId());
        try {
            userGame.updateTroop();
            userGame.saveModel(user.getId());
            // userGame.getTro();
        } catch (Exception e) {
            e.printStackTrace();
        }
        send(new ResponseTroop(DemoError.SUCCESS.getValue(), CmdDefine.LOADTROOP, userGame.getTroop()), user);
    }

    private void processLoadBarack(User user, RequestID listbarack) {
        Debug.warn("Load Barack");
        UserGame userGame = Data.getData(user.getId());
        userGame.updateStructure();
        try {
            userGame.updateTroop();
            userGame.saveModel(user.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        send(new ResponseBarack(DemoError.SUCCESS.getValue(), CmdDefine.LOADBARACK, userGame.getMap().getBrackList()), user);
    }

    private void processDeleteTrainTroop(User user, RequestIDType deletetraintroop) {
        Debug.warn("Delete Train Troop");
        UserGame userGame = Data.getData(user.getId());
        int timeNow = (int) new Date().getTime() / 1000;
        Structure s = userGame.getMap().getStructures().get(deletetraintroop.getID());
        try {
            userGame.updateTroop();
            int typeTroopBrack = getTypeTroop(s.getString("unlockedUnit"));
            int typeTroop = getTypeTroop(deletetraintroop.getType());
            List<Brack> bracklist = userGame.getMap().getBrackList();
            if (typeTroop <= typeTroopBrack) {//troop valid
                for (int i = 0; i < bracklist.size(); i++) {
                    Brack brack = bracklist.get(i);
                    if (brack.getId() == deletetraintroop.getID()) {// correct brack
                        Iterator<TypeQuantity> it = brack.getListTrain().iterator();
                        boolean kt = false;
//                        Debug.warn(it);
                        for (int j = 0; j < brack.getListTrain().size(); j++) {
                            // find troop in list
                            TypeQuantity typeQuantity = brack.getListTrain().get(j);

                            if (typeQuantity.getType().equals(deletetraintroop.getType())) {// correct troop
                                Debug.warn("Tru Linh");
                                int troopSpace = LoadConFig.getInstance().getJsonObject().getJSONObject(typeQuantity.getType()).getInt("housingSpace");
                                typeQuantity.setN(typeQuantity.getN() - 1);
//                                brack.setSpace(brack.getSpace() - troopSpace);
                                brack.setSpace(brack.getSpace() + troopSpace);
                                userGame.updateResTroop("delete", deletetraintroop.getType(), 1);
                                if (typeQuantity.getN() == 0) {
                                    brack.getListTrain().remove(typeQuantity);
                                }
                            }
                        }
                        if (brack.getListTrain().size() != 0 && brack.getListTrain().get(0).getN() == 0) {// first troop =0
                            brack.getListTrain().remove(0);
                            brack.setTimeStart(timeNow);
                            if (brack.getListTrain().size() != 0) {
                                int troopSpace = LoadConFig.getInstance().getJsonObject().getJSONObject(brack.getListTrain().get(0).getType()).getInt("housingSpace");
                                brack.setTimeFirstTroop(troopSpace);
                            }
                        }
                        send(new ResponseConfirm(DemoError.SUCCESS.getValue(), CmdDefine.DELETETRAINTROOP), user);
                        userGame.saveModel(user.getId());
                    }
                }
            } else
                send(new ResponseConfirm(DemoError.ERROR.getValue(), CmdDefine.DELETETRAINTROOP), user);//troop chua the train
        } catch (Exception e) {
            e.printStackTrace();
            send(new ResponseConfirm(DemoError.ERROR.getValue(), CmdDefine.DELETETRAINTROOP), user);
        }
    }

    private void processTrainTroop(User user, RequestIDType traintroop) {
        Debug.warn("===========================");
        Debug.warn("TrainTroop");
        Debug.warn(traintroop.getID(), traintroop.getType());
        UserGame userGame = Data.getData(user.getId());
        Structure s = userGame.getMap().getStructures().get(traintroop.getID());
        userGame.updateStructure();
        try {
            userGame.updateTroop();
            int troopSpace = LoadConFig.getInstance().getJsonObject().getJSONObject(traintroop.getType()).getInt("housingSpace");
            int typeTroopBrack = getTypeTroop(s.getString("unlockedUnit"));
            int typeTroop = getTypeTroop(traintroop.getType());
            List<Brack> bracklist = userGame.getMap().getBrackList();
            if (!userGame.checkResourceTroop(traintroop.getType(), 1)) {
                Debug.warn("KHong du tai nguyen");
                send(new ResponseConfirm(DemoError.ERROR.getValue(), CmdDefine.TRAINTROOP), user);//ko du tai nguyen
            } else if (s.getState() == Structure.STATE_BUILD) {
                Debug.warn("Nha linh dang xay");
                send(new ResponseConfirm(DemoError.ERROR.getValue(), CmdDefine.TRAINTROOP), user);
            } else if (typeTroop <= typeTroopBrack) {//troop valid
//                Debug.warn("Check valid troop");
//                Debug.warn("ID barrack train:", traintroop.getID());
//                Debug.warn("So luong barrack", bracklist.size());
                for (int i = 0; i < bracklist.size(); i++) {
                    Brack brack = bracklist.get(i);
//                    Debug.warn("ID barrack ", brack.getId());
                    if (brack.getId() == traintroop.getID() && brack.getSpace() < troopSpace) {//check space
                        Debug.warn("Khong du sapce");
                        send(new ResponseConfirm(DemoError.ERROR.getValue(), CmdDefine.TRAINTROOP), user);//qua lengthqueue
                        break;
                    }
                    Debug.warn("Barrack space", brack.getSpace());
                    if (brack.getId() == traintroop.getID()) {// correct brack
                        Debug.warn("Correct Barrack");
                        Iterator<TypeQuantity> it = brack.getListTrain().iterator();
                        boolean kt = false;
                        while (it.hasNext()) {// find troop in list
                            TypeQuantity typeQuantity = it.next();
                            if (typeQuantity.getType().equals(traintroop.getType())) {
                                Debug.warn("Ton tai linh");
                                kt = true;
                                Debug.warn("Troop train");
                                typeQuantity.setN(typeQuantity.getN() + 1);
                                brack.setSpace(brack.getSpace() - troopSpace);
                                userGame.updateResTroop("train", traintroop.getType(), 1);
                            }
                        }
                        if (brack.getListTrain().size() == 0) {// first troop
                            Debug.warn("First troop");
                            brack.getListTrain().add(new TypeQuantity(traintroop.getType(), 1));
                            brack.setTimeStart((int) (new Date().getTime() / 1000));
                            brack.setTimeFirstTroop(LoadConFig.getInstance().getJsonObject().getJSONObject(traintroop.getType()).getInt("trainingTime"));
                            brack.setSpace(brack.getSpace() - troopSpace);
                            userGame.updateResTroop("train", traintroop.getType(), 1);
                        } else if (!kt) {
                            Debug.warn("Add new type troop");
                            brack.getListTrain().add(new TypeQuantity(traintroop.getType(), 1));
                            brack.setSpace(brack.getSpace() - troopSpace);
                            userGame.updateResTroop("train", traintroop.getType(), 1);
                        }
                        send(new ResponseConfirm(DemoError.SUCCESS.getValue(), CmdDefine.TRAINTROOP), user);
                        userGame.saveModel(user.getId());
                    }
                }
            } else send(new ResponseConfirm(DemoError.ERROR.getValue(), CmdDefine.TRAINTROOP), user);//troop ko hop le
        } catch (Exception e) {
            e.printStackTrace();
            send(new ResponseConfirm(DemoError.ERROR.getValue(), CmdDefine.TRAINTROOP), user);
        }
    }

    private int getTypeTroop(String unlockedUnit) {
        String[] p = unlockedUnit.split("ARM_");
        Debug.warn(unlockedUnit, p[1]);
        return Integer.parseInt(p[1]);
    }

    private void processCollect(User user, RequestID collect) {
        Debug.warn("Collect");
        UserGame userGame = Data.getData(user.getId());
        userGame.updateStructure();
        Structure s = userGame.getMap().getStructures().get(collect.getID());
        int productivity = 0;
        try {
            String typeRes = s.getString("type");
            int time = (int) (new Date().getTime() / 1000);
            int timeGet = time - s.getTime();
            //TODO test collect
//            productivity = 10000;
            productivity = s.getInt("productivity") * timeGet / 3600;
            if (productivity > s.getInt("capacity")) productivity = s.getInt("capacity");
            Debug.warn("Product", productivity);
            int ResType = -1;
            if (typeRes.equals("ERROR")) send(new ResponseConfirm(DemoError.ERROR.getValue(), CmdDefine.COLLECT), user);
            else {
                if (productivity > 0) s.setTime(time);
                if (typeRes.equals("gold")) {
                    userGame.setGold(userGame.getGold() + productivity);
                    productivity = userGame.getGold();
                    ResType = 0;
                }
                if (typeRes.equals("elixir")) {
                    userGame.setElixir(userGame.getElixir() + productivity);
                    productivity = userGame.getElixir();
                    ResType = 1;
                }
                if (typeRes.equals("darkElixir")) {
                    userGame.setDarkElixir(userGame.getDarkElixir() + productivity);
                    productivity = userGame.getDarkElixir();
                    ResType = 2;
                }
                Debug.warn("res===", productivity);
                userGame.getMap().getStructures().put(collect.getID(), s);
                if (s.getState() == Structure.STATE_0)
                    send(new ResponseConfirm_Vol_Type(DemoError.SUCCESS.getValue(), CmdDefine.COLLECT, productivity, ResType), user);
                else send(new ResponseConfirm(DemoError.ERROR.getValue(), CmdDefine.COLLECT), user);
            }
            userGame.saveModel(user.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processUpgradeFast(User user, RequestID_ID upgradeFast) {
        Debug.warn("UpgradeFast");
        Debug.warn(upgradeFast.getBuilderID(), upgradeFast.getStruturesID());
        UserGame userGame = Data.getData(user.getId());
        Map<Integer, Structure> structures = userGame.getMap().getStructures();
        boolean obstacle = false;
        ArrayList<Obstacle> obtacles = userGame.getMap().getObstacles();
        int p = 0;
        try {
            Structure s = structures.get(upgradeFast.getStruturesID());
            p = s.getTime();
        } catch (Exception e) {
            obstacle = true;
            for (Obstacle o : obtacles) {
                if (o.getId() == upgradeFast.getStruturesID()) {
                    p = o.getRemoveTime();
                }
            }
        }
        Debug.warn("Obstacle", obstacle);

        int timeNow = (int) (new Date().getTime() / 1000);
        int g = userGame.convert(p - timeNow);
        if (!(userGame.getMap().getBuilderList().get(upgradeFast.getBuilderID()) == upgradeFast.getStruturesID())) {
            Debug.warn("Sai tho xay");
            send(new ResponseConfirm(DemoError.ERROR.getValue(), CmdDefine.UPGRADEFAST), user);//sai tho xay
        } else if (userGame.getG() > g) {
            userGame.setG(userGame.getG() - g);
            try {
                if (obstacle) {
                    Debug.warn("Delete obstacle");
                    for (int i = 0; i < obtacles.size(); i++) {
                        Obstacle o = obtacles.get(i);
                        if (o.getId() == upgradeFast.getStruturesID()) {
                            obtacles.remove(i);
                            userGame.setBuilder(userGame.getBuilder() + 1);
                            userGame.getMap().getBuilderList().put(upgradeFast.getBuilderID(), 0);
                            break;
                        }
                    }
                } else {
                    Structure s = structures.get(upgradeFast.getStruturesID());
//                    s.setState(0);
                    s.setTime(timeNow - s.getInt("buildTime"));
//                    Debug.warn("ADD 11",s.getInt("buildTime"),s.getId(),s.getState(),timeNow-s.getTime());
                }
//                Debug.warn();
//                userGame.setBuilder(userGame.getBuilder() + 1);
//                userGame.getMap().getBuilderList().put(upgradeFast.getBuilderID(), 0);
//                Debug.warn("Update structure");
                userGame.updateStructure();
                userGame.saveModel(user.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
            send(new ResponseConfirm(DemoError.SUCCESS.getValue(), CmdDefine.UPGRADEFAST), user);
        } else {
            Debug.warn("Khong du tai nguyen");
            send(new ResponseConfirm(DemoError.ERROR.getValue(), CmdDefine.UPGRADEFAST), user);//ko du g
        }
    }

    private void processCheat(User user, RequestCheat cheat) {
        Debug.warn("Cheat", cheat.getG(), cheat.getGold(), cheat.getElixir(), cheat.getDarkelixir());
        UserGame userGame = Data.getData(user.getId());
        userGame.setG(cheat.getG());
        userGame.setElixir(cheat.getElixir());
        userGame.setDarkElixir(cheat.getDarkelixir());
        userGame.setGold(cheat.getGold());
        send(new ResponseConfirm(DemoError.SUCCESS.getValue(), CmdDefine.CHEAT), user);
        try {
            userGame.saveModel(user.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processLoadRes(User user, RequestID id) {
//        id.setID(123);
        id.setID(user.getId());
        Debug.warn("Show Res");
        UserGame userGame = null;
//        Debug.warn("Empty"+UserGame.empty);
        userGame = Data.getData(user.getId());
        // Debug.warn("res", userGame.getGold(), userGame.getG(), userGame.getElixir());
        send(new ResponseRes(
                        (short) 0,
                        userGame.getGold(),
                        userGame.getG(),
                        userGame.getElixir(),
                        userGame.getBuilder(),
                        userGame.getDarkElixir(),
                        userGame.getTrophy()),
                user);
    }

    private void processDeleteObstacle(User user, RequestID_ID delete) {
        Debug.warn("Delete");
        int id = delete.getStruturesID();
        UserGame userGame = Data.getData(user.getId());
        userGame.updateStructure();
//        Debug.warn("Builder", userGame.getBuilder());

        Obstacle obstacle = null;
        for (Obstacle o : userGame.getMap().getObstacles()) {
            if (o.getId() == id) {
                obstacle = o;
            }
        }
        if (userGame.getMap().getBuilderList().get(delete.getBuilderID()) != 0) {

            RequestID_ID upgradeFast = new RequestID_ID(new DataCmd(new byte[]{}));
            upgradeFast.setBuilderID(delete.getBuilderID());
            upgradeFast.setStruturesID(userGame.getMap().getBuilderList().get(delete.getBuilderID()));
            Debug.warn("Upgrade Fast", delete.getBuilderID(), userGame.getMap().getBuilderList().get(delete.getBuilderID()));
            processUpgradeFast(user, upgradeFast);
            userGame = Data.getData(user.getId());
            userGame.updateStructure();
        }
//        Debug.warn("Builder", userGame.getBuilder());
        if (obstacle != null) {
            if (userGame.checkResource(obstacle) && userGame.checkBuilder() && obstacle.getState() == Structure.STATE_0) {
                userGame.updateRes(obstacle);
                Debug.warn("Set time", (int) (new Date().getTime() / 1000));
                obstacle.setRemoveTime((int) (new Date().getTime() / 1000));
                for (Obstacle o : userGame.getMap().getObstacles()) {
                    if (o.getId() == obstacle.getId()) o.setRemoveTime(obstacle.getRemoveTime());
                }
                userGame.setBuilder(userGame.getBuilder() - 1);
                userGame.getMap().getBuilderList().put(delete.getBuilderID(), delete.getStruturesID());
                try {
                    userGame.saveModel(user.getId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                send(new ResponseConfirm(DemoError.SUCCESS.getValue(), CmdDefine.DELETE), user);
            } else send(new ResponseConfirm(DemoError.ERROR.getValue(), CmdDefine.DELETE), user);
        } else
            send(new ResponseConfirm(DemoError.ERROR.getValue(), CmdDefine.DELETE), user);
    }

    private void processUpgrade(User user, RequestID_ID upgrade) {
        Debug.warn("Upgrade");
        UserGame userGame = Data.getData(user.getId());
        userGame.updateStructure();

        Structure s = userGame.getMap().getStructures().get(upgrade.getStruturesID());
//        if (s!=null&&s.)
        s.setLevel(s.getLevel() + 1);
        if (userGame.getMap().getBuilderList().get(upgrade.getBuilderID()) != 0) {
            RequestID_ID upgradeFast = new RequestID_ID(new DataCmd(new byte[]{}));
            upgradeFast.setBuilderID(upgrade.getBuilderID());
            upgradeFast.setStruturesID(userGame.getMap().getBuilderList().get(upgrade.getBuilderID()));
            Debug.warn("Upgrade Fast", upgrade.getBuilderID(), userGame.getMap().getBuilderList().get(upgrade.getBuilderID()));
            processUpgradeFast(user, upgradeFast);
            userGame = Data.getData(user.getId());
            userGame.updateStructure();
        }
        if (userGame.getMap().getBuilderList().get(upgrade.getBuilderID()) != 0) {
            Debug.warn("Tho xay khong hop le");
            send(new ResponseConfirm(DemoError.ERROR.getValue(), CmdDefine.UPGRADE), user);
        }// tho xay ko hop le
        if (s != null || !s.getType().equals("BDH_1"))
            if ((s.getState() & 1) == Structure.STATE_0
                    && userGame.getLevelTownHall() >= s.getInt("townHallLevelRequired")
                    && userGame.checkResource(s)
                    && userGame.checkBuilder()) {

                s.setState(Structure.STATE_BUILD);
                s.setTime((int) (new Date().getTime() / 1000));
//                s.setLevel(s.getLevel()+1);
                userGame.getMap().getStructures().put(s.getId(), s);
                userGame.setBuilder(userGame.getBuilder() - 1);
                userGame.getMap().getBuilderList().put(upgrade.getBuilderID(), upgrade.getStruturesID());
                userGame.updateRes(s);
                send(new ResponseConfirm(DemoError.SUCCESS.getValue(), CmdDefine.UPGRADE), user);
                try {
                    userGame.saveModel(user.getId());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else send(new ResponseConfirm(DemoError.ERROR.getValue(), CmdDefine.UPGRADE), user);
    }

    private void processCancel(User user, RequestID_ID cancel) {
        Debug.warn("Cancel build");
        UserGame userGame = Data.getData(user.getId());
        Structure s = userGame.getMap().getStructures().remove(cancel.getStruturesID());
        userGame.updateStructure();
        if (s != null && s.getState() == Structure.STATE_BUILD) {
            userGame.setGold(userGame.getGold() + s.getInt("gold") / 2);
            userGame.setElixir(userGame.getElixir() + s.getInt("elixir") / 2);
            userGame.setDarkElixir(userGame.getElixir() + s.getInt("darkElixir") / 2);
            if (s.getLevel() > 1) {
                userGame.setBuilder(userGame.getBuilder() + 1);
                s.setState(Structure.STATE_0);
                s.setLevel(s.getLevel() - 1);
                userGame.getMap().getStructures().put(s.getId(), s);
                userGame.setBuilder(userGame.getBuilder() + 1);
                userGame.getMap().getBuilderList().put(cancel.getBuilderID(), 0);
//                userGame.
            }
            try {
                userGame.saveModel(user.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
            send(new ResponseConfirm(DemoError.SUCCESS.getValue(), CmdDefine.CANCEL), user);
        } else send(new ResponseConfirm(DemoError.ERROR.getValue(), CmdDefine.CANCEL), user);
    }

    private void processMoveStructure(User user, RequestIDPos moveStructure) {
        Debug.warn("MoveS");
        UserGame userGame = Data.getData(user.getId());
        Debug.warn(moveStructure.getId(), moveStructure.getPos());
//        MapController mapController = userGame.getMap();
        Structure s = userGame.getMap().getStructures().get(moveStructure.getId());
        if (s != null && userGame.getMap().checkPosition(s, moveStructure.getPos())) {
            userGame.getMap().move(s, moveStructure);
            try {
                userGame.saveModel(user.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
//            if (mapController.move(s,moveStructure))
            send(new ResponseConfirm(DemoError.SUCCESS.getValue(), CmdDefine.MOVESTRUCTURE), user);
            ;
        } else
            send(new ResponseConfirm(DemoError.ERROR.getValue(), CmdDefine.MOVESTRUCTURE), user);
    }
//    private void

    /**
     * events will be dispatch here
     */
//    @Override
    public void handleServerEvent(IBZEvent ibzevent) {
        logger.warn("login Success");
        if (ibzevent.getType() == DemoEventType.LOGIN_SUCCESS) {
            this.processUserLoginSuccess((User) ibzevent.getParameter(DemoEventParam.USER), (String) ibzevent.getParameter(DemoEventParam.NAME));
        }
    }

    private UserGame getData(int userID) {
        UserGame userGame = new UserGame(userID);
        try {
            Gson gson = new Gson();
            String key = ServerUtil.getModelKeyName(userGame.getClass().getSimpleName(), userID) + CmdDefine.NUMBER;
            JSONObject jsonObject = new JSONObject(DataHandler.get(key).toString());
//            Debug.warn(jsonObject);
            userGame = gson.fromJson(String.valueOf(jsonObject), UserGame.class);
            userGame.getCountID();
        } catch (Exception e) {
            e.printStackTrace();

        }
        return userGame;
    }

    private void processBuildObject(User user, RequestType_X_Y_Builder build) {
        Debug.warn("Build");
        Debug.warn("thong so", build.getBuilder(), build.getPos(), build.getType());
        UserGame userGame = Data.getData(user.getId());

        userGame.updateStructure();

        // Debug.warn(userGame.toString());
        Debug.warn("Size=" + userGame.getMap().getStructures().size());
        int id = userGame.getCountID();
        id++;
        userGame.setCountID(id);
        Point pos = build.getPos();
        String type = build.getType();
        int level = 1;
        if (type.equals("BDH_1")) level = userGame.getMap().getNumberStructure("BDH_1") + 1;
        Structure s = new Structure(id, type, pos);
        Debug.warn("Id", id, s.getId());
        try {
//            Debug.warn("Builder Old:", userGame.getBuilder());
            s.setLevel(level);
            s.setState(1);
            s.setTime((int) (new Date().getTime() / 1000));
//            Debug.warn("Builder "+build);
            Debug.warn("builder===", userGame.getBuilder());
            if (userGame.getMap().getBuilderList().get(build.getBuilder()) != 0) {
                Debug.warn("Giai phong linh", build.getBuilder());
                RequestID_ID upgradeFast = new RequestID_ID(new DataCmd(new byte[]{}));
                upgradeFast.setBuilderID(build.getBuilder());
                upgradeFast.setStruturesID(userGame.getMap().getBuilderList().get(build.getBuilder()));
                Debug.warn("Upgrade Fast", build.getBuilder(), userGame.getMap().getBuilderList().get(build.getBuilder()));
                processUpgradeFast(user, upgradeFast);
            }
            userGame = Data.getData(user.getId());
            Debug.warn("Builder New", userGame.getBuilder());
            userGame.updateStructure();
            userGame.setCountID(id);
            if (userGame.checkResource(s) && userGame.getMap().checkPosition(s, build.getPos())
                    && userGame.checkTownHall(s) && userGame.checkBuilder() && userGame.getMap().getBuilderList().get(build.getBuilder()) == 0) {
//                Debug.warn("=====Xay===" + id);
                userGame.setBuilder(userGame.getBuilder() - 1);
                userGame.getMap().add(s, id);
                userGame.getMap().getBuilderList().put(build.getBuilder(), id);
                userGame.updateRes(s);
                Debug.warn("Build send", s.getId());
                send(new ResponseBuild(DemoError.SUCCESS.getValue(), CmdDefine.BUILD, s.getId(), build.getBuilder(), build.getPos().x, build.getPos().y), user);

//                Debug.warn(userGame.getGold(), userGame.getElixir(), userGame.getDarkElixir());
//                Debug.warn("Size=" + userGame.getMap().getStructures().size());
//                Debug.warn("Save Model");
                Debug.warn("countid", userGame.getCountID());
                userGame.saveModel(user.getId());
            } else
                send(new ResponseBuild(DemoError.ERROR.getValue(), CmdDefine.BUILD, s.getId(), build.getBuilder(), build.getPos().x, build.getPos().y), user);

        } catch (
                Exception e) {
            e.printStackTrace();
        }

    }

    private void processLoadStructure(User user, RequestID load) {
//        load.setID(123);
        load.setID(user.getId());
        Debug.warn("Show Structure");
        UserGame userGame = null;
//        Debug.warn("Empty"+UserGame.empty);
        userGame = Data.getData(user.getId());
        userGame.updateStructure();
        Debug.warn("Save model");
        try {
            userGame.updateTroop();
            userGame.saveModel(user.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
//        Structure2 s= (Structure2) userGame.getMap().getStructures().get("1234");
//        Debug.warn("=========Test==========" + s.toString());
        Debug.warn("Size=" + userGame.getMap().getStructures().size());
        MapController map = userGame.getMap();
        Map<Integer, Structure> m1 = map.getStructures();
        Debug.warn("Send");
        send(new ResponseListEntity((short) 0, map, userGame.getCountID()), user);

    }


    private void processUserLoginSuccess(User user, String name) {
        /**
         * process event
         */
        logger.warn("processUserLoginSuccess, name = " + name);
    }

    public enum DemoError {
        SUCCESS((short) 0),
        NOT_ENOUGH_RESOURCE((short) 10),
        NO_BUILDER((short) 11),
        ERROR((short) 1),
        PLAYERINFO_NULL((short) 2),
        EXCEPTION((short) 3);

        private final short value;

        private DemoError(short value) {
            this.value = value;
        }

        public short getValue() {
            return this.value;
        }
    }

    public static void main(String[] args) {
        UserGame userGame = new UserGame(23);
        ISession iSession = null;
        User user = new User(iSession);
        user.setId(23);
        DemoHandler demoHandler = new DemoHandler();
    }
}
