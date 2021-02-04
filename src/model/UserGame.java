package model;

import bitzero.core.P;
import bitzero.util.common.business.Debug;
import org.json.JSONException;
import util.database.DataModel;

import java.awt.*;
import java.util.*;
import java.util.List;

public class UserGame extends DataModel {
    private static UserGame a;
    private int countID;
    public static boolean empty;
    private int id;
    private int trophy;
    private String name;
    private int g;
    private int gold;
    private int elixir;
    private int darkElixir;
    private int exp;
    private int level;
    private int goldMax, elixirMax, darkElixirMax;
    private int idTownHall;
    private int builder;
    private int spaceTroop;
    private Map<String, Integer> troop;
    private Map<String, Integer> levelTroop;
    private MapController map;

    public UserGame(int id) {
        this.id = id;
        this.trophy = 500;
        this.idTownHall = 900;
        this.gold = 750;
        this.elixir = 750;
        this.g = 1000000;
        this.darkElixir = 750;
        this.map = new MapController();
        countID = 1000;
        this.builder = 1;//this.getMap().getNumber("BDH_1");
        this.getMaxRes();
        this.spaceTroop = 20;
        troop = new HashMap<>();
        levelTroop = new HashMap<>();
    }

    public void setTrophy(int trophy) {
        this.trophy = trophy;
    }

    public void setSpaceTroop(int spaceTroop) {
        this.spaceTroop = spaceTroop;
    }

    public Map<String, Integer> getTroop() {
        return this.troop;
    }

    public Map<String, Integer> getLevelTroop() {
        return this.levelTroop;
    }

    public void setLevelTroop(Map<String, Integer> levelTroop) {
        this.levelTroop = levelTroop;
    }

    public int getGoldMax() {
        return this.goldMax;
    }

    public int getElixirMax() {
        return this.elixirMax;
    }

    public int getDarkElixirMax() {
        return this.darkElixirMax;
    }

    public void getMaxRes() {
        this.goldMax = this.elixirMax = this.darkElixirMax = 0;
        Structure townHall = this.getMap().getStructures().get(idTownHall);
        this.goldMax = townHall.getInt("capacityGold");
        this.elixirMax = townHall.getInt("capacityElixir");
        Map<Integer, Structure> structures = this.getMap().getStructures();
        structures.forEach((id, val) -> {
            int p = val.getInt("capacity");
            if (val.getType().startsWith("STO")) {
                if (val.getString("type").equals("gold")) this.goldMax += p;
                else if (val.getString("type").equals("elixir")) this.elixirMax += p;
                this.darkElixir += p;
            }
        });
    }

    public void setCountID(int countID) {
        this.countID = countID;
    }

    public int getCountID() {
//        Debug.warn("get id ",this.countID);
////        this.countID=this.countID+1;
//        Debug.warn("id new",this.countID);
        return this.countID;
    }
//    public static UserGame getInstance() {
//        if (a == null) {
//            Debug.warn("User Empty");
//            UserGame.empty = true;
//            a = new UserGame();
//        } else
//            UserGame.empty = false;
//        return a;
//    }

    public boolean checkResource(Entity structure) {
        Debug.warn("Check res");
        int goldNeed, elixirNeed, darkElixirNeed, coinNeed;
        goldNeed = elixirNeed = darkElixirNeed = coinNeed = 0;
        if (structure.getInt("gold") > this.gold) goldNeed = structure.getInt("gold") - this.gold;
        if (structure.getInt("elixir") > this.elixir) elixirNeed = structure.getInt("elixir") - this.elixir;
        if (structure.getInt("darkElixir") > this.darkElixir)
            darkElixirNeed = structure.getInt("darkElixir") - this.darkElixir;
        if (structure.getInt("coin") > this.g) coinNeed = structure.getInt("coin") - this.g;
        int gConvert = convert(goldNeed, elixirNeed, darkElixirNeed);
        if (coinNeed == 0 && this.g >= gConvert) {
            Debug.warn("Du tai nguyen");
            return true;
        } else return false;
    }

    public void updateRes(Entity structure) {
        int goldNeed, elixirNeed, darkElixirNeed, coinNeed;
        goldNeed = elixirNeed = darkElixirNeed = coinNeed = 0;
        if (structure.getInt("gold") > this.gold) goldNeed = structure.getInt("gold") - this.gold;
        if (structure.getInt("elixir") > this.elixir) elixirNeed = structure.getInt("elixir") - this.elixir;
        if (structure.getInt("darkElixir") > this.darkElixir)
            darkElixirNeed = structure.getInt("darkElixir") - this.darkElixir;
        if (structure.getInt("coin") > this.g) coinNeed = structure.getInt("coin") - this.g;
        int gConvert = convert(goldNeed, elixirNeed, darkElixirNeed);
        if (gConvert > 0) {
            this.g -= gConvert;
        }
        if (goldNeed > 0) this.gold = 0;
        else this.gold -= structure.getInt("gold");
        if (darkElixirNeed > 0) this.darkElixir = 0;
        else this.darkElixir -= structure.getInt("darkElixir");
        if (elixirNeed > 0) this.elixir = 0;
        else this.elixir -= structure.getInt("elixir");
    }

    public int convert(int goldNeed, int elixirNeed, int darkElixirNeed) {
        Debug.warn("Convert G", goldNeed, elixirNeed, darkElixirNeed);
        if (goldNeed == 0 && elixirNeed == 0 && darkElixirNeed == 0) return 0;
        return 1;
    }

    public int convert(int time) {
        return 1;
    }

    public int getDarkElixir() {
        return this.darkElixir;
    }

    public void setDarkElixir(int darkElixir) {
        this.darkElixir = Math.min(darkElixir, this.darkElixirMax);
    }

    public MapController getMap() {
        return this.map;
    }

    public void setMap(MapController map) {
        this.map = map;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTrophy() {
        return this.trophy;
    }

    public void changeTrophy(int amount) {
        this.trophy += amount;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getG() {
        return this.g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getGold() {
        return this.gold;
    }

    public void setGold(int gold) {
        this.gold = Math.min(gold, this.goldMax);
    }

    public int getElixir() {
        return this.elixir;
    }

    public void setElixir(int elixir) {
        this.elixir = Math.min(elixir, this.elixirMax);
    }

    public int getExp() {
        return this.exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel() {
        this.level = level;
    }

    public int getBuilder() {
        return this.builder;
    }

    public void setBuilder(int builder) {
        this.builder = builder;
    }

    @Override
    public String toString() {
        return "UserGame{" +
                "id=" + id +
                ", name=" + name +
                ", g=" + g +
                ", gold=" + gold +
                ", elixir=" + elixir +
                ", exp=" + exp +
                ", level=" + level +
                ", map=" + map.getStructures().size() +
                '}';
    }

    public boolean checkBuilder() {
        Debug.warn("CheckBuilder", this.builder > 0);
        return this.builder > 0;
    }

    public boolean checkTownHall(Structure s) {
        if (s.getType().equals("BDH_1")) return true;
        Structure tow1 = this.getMap().getStructures().get(idTownHall);
        Debug.warn("Check townhall");
        return (this.getMap().getNumberStructure(s.getType()) < tow1.getInt(s.getType()));
    }

    public int getLevelTownHall() {
        Debug.warn("Check townhall");
        Structure tow1 = this.getMap().getStructures().get(idTownHall);
        return tow1.getLevel();
    }

    public void updateTroop() throws JSONException {
//        Debug.warn("Update Troop");
//        int space = this.getSpaceTroop();
        List<Brack> brackList = this.getMap().getBrackList();
        int timeNow = (int) (new Date().getTime() / 1000);
//        System.out.println(timeNow);
        int timeBrack;
        boolean kt = true;
        int endTimeTroop = 0, troopSpaceMin = 0;
        TypeQuantity troopTrain = null;
        while (kt) {
            int idBrackTrain = -1;
            endTimeTroop = timeNow;
            for (int i = 0; i < brackList.size(); i++) {
                Brack brack = brackList.get(i);
                timeBrack = brack.getTimeStart();
                if (brack.getListTrain().size() == 0) continue;
                int p = timeNow - timeBrack;
                ArrayList<TypeQuantity> listTrain = brack.getListTrain();
                TypeQuantity troop = listTrain.get(0);
                if (troop.getN() == 0) {
                    listTrain.remove(0);
                    troop = listTrain.get(0);
                }
                if (!listTrain.isEmpty()) {
                    int troopSpace = LoadConFig.getInstance().getJsonObject().getJSONObject(troop.getType()).getInt("housingSpace");
                    int timeTrainTroop = brack.getTimeFirstTroop();
//                    Debug.warn("time",brack.getTimeStart(),timeNow);
                    if ((timeTrainTroop + timeBrack < endTimeTroop || idBrackTrain == -1) && this.getMap().getStructures().get(brack.getId()).getState() == Structure.STATE_0
                    &&brack.getTimeStart()<timeNow) {
                        endTimeTroop = timeBrack + timeTrainTroop;
                        idBrackTrain = i;
                        troopTrain = troop;
                        troopSpaceMin = troopSpace;
                    }
                }
            }
//            Debug.warn("idBarrack",idBrackTrain);
            if (idBrackTrain != -1 && brackList.get(idBrackTrain).getTimeStart() <= timeNow && brackList.get(idBrackTrain).getListTrain().size() != 0) {
                Brack brack = brackList.get(idBrackTrain);
                ArrayList<TypeQuantity> listTrain = brack.getListTrain();
//                Debug.warn("Size List Train", listTrain.size());
//                Debug.warn("Time Now",timeNow,"End Time Troop",endTimeTroop,timeNow-endTimeTroop);
                if (timeNow >= endTimeTroop) {
//                    Debug.warn("Train Troop");
                    brack.setTimeStart(endTimeTroop);
                    if (troopSpaceMin <= this.spaceTroop) { // du space
//                        Debug.warn("Troop Space", troopSpaceMin);
                        try {
//                            Debug.warn("Troop", troopTrain.getType(), troopTrain.getN());
                            this.spaceTroop -= troopSpaceMin;
                            troopTrain.setN(troopTrain.getN() - 1);//giam luong linh
                            if (troopTrain.getN() == 0) listTrain.remove(0);//remove troop =0
//                            Debug.warn("Update ", troopTrain.getType(), troopTrain.getN());
                            brack.setSpace(brack.getSpace() + troopSpaceMin);
                            this.troop.put(troopTrain.getType(), this.troop.get(troopTrain.getType()) + 1);//add troop
                        } catch (Exception e) {
//                            Debug.warn("First Troop");
                            this.troop.put(troopTrain.getType(), 1);
//                            this.spaceTroop -= troopSpaceMin;
                        }
//                        Debug.warn("Add troop", troopTrain.getType(), this.troop.get(troopTrain.getType()));
                        TypeQuantity troop = null;
                        if (listTrain.size() != 0) troop = listTrain.get(0);
                        if (troop != null && troop.getN() == 0) {
                            listTrain.remove(0);
                            troop = listTrain.get(0);

                        }
                        if (troop != null)
                            brack.setTimeFirstTroop(LoadConFig.getInstance().getJsonObject().getJSONObject(troop.getType()).getInt("trainingTime"));//set timefirstroop
//                        System.out.println(troopTrain.getType() + " " + idBrackTrain);

                    } else {
//                        Debug.warn("Troop ");
                        brack.setTimeFirstTroop(0);
                        brack.setTimeStart(timeNow);
                    }
                } else {
//                    Debug.warn("Update Barrack====", endTimeTroop - timeNow);
                    brack.setTimeFirstTroop(endTimeTroop - timeNow);
                    brack.setTimeStart(timeNow);
                }

            } else kt = false;
        }
    }

    public static void main(String[] args) throws JSONException {
        UserGame userGame = new UserGame(123);
        Brack brack = new Brack(new Structure(123, "BAR_1", new Point(3, 4)));
        int timeNow = (int) (new Date().getTime() / 1000);
        userGame.getMap().getStructures().put(123, new Structure(123, "BAR_1", new Point(3, 4)));


        brack.setTimeStart((int) ((new Date().getTime()) / 1000 - (40)));
        System.out.println(timeNow - brack.getTimeStart());
        brack.setSpace(brack.getSpace()-5);
        brack.getListTrain().add(new TypeQuantity("ARM_1", 5));
//        brack.getListTrain().add(new TypeQuantity("ARM_2", 5));
        brack.setTimeFirstTroop(LoadConFig.getInstance().getJsonObject().getJSONObject(brack.getListTrain().get(0).getType()).getInt("trainingTime")-10);
        userGame.getMap().getBrackList().add(brack);

        Brack brack2 = new Brack(new Structure(193, "BAR_1", new Point(3, 4)));
        brack2.setTimeStart((int) ((new Date().getTime()) / 1000 - (40)));
        brack2.getListTrain().add(new TypeQuantity("ARM_1", 5));
        brack2.setSpace(brack2.getSpace()-5);
//        brack2.getListTrain().add(new TypeQuantity("ARM_4", 5));
        brack2.setTimeFirstTroop(LoadConFig.getInstance().getJsonObject().getJSONObject(brack2.getListTrain().get(0).getType()).getInt("trainingTime")-11);
        userGame.getMap().getStructures().put(193, new Structure(123, "BAR_1", new Point(3, 4)));
        userGame.getMap().getBrackList().add(brack2);
        System.out.println(timeNow - brack2.getTimeStart());
        userGame.spaceTroop=5;//set space
        userGame.updateTroop();

        System.out.println("Barack:" + brack.getTimeFirstTroop() + " " + brack.getListTrain().size());
        System.out.println("Time first troop " + brack.getTimeFirstTroop());
        Iterator<TypeQuantity> it = brack.getListTrain().iterator();
        while (it.hasNext()) {
            TypeQuantity troop = it.next();
            System.out.println("Troop:" + troop.getType() + " " + troop.getN());
        }
        System.out.println("Space " + brack.getSpace());
//
        System.out.println("Barack2:" + brack2.getTimeFirstTroop() + " " + brack2.getListTrain().size());
        System.out.println("Time first troop " + brack2.getTimeFirstTroop());
        it = brack2.getListTrain().iterator();
        while (it.hasNext()) {
            TypeQuantity troop = it.next();
            System.out.println("Troop:" + troop.getType() + " " + troop.getN());
        }
        System.out.println("Space " + brack2.getSpace());
        userGame.getTroop().forEach((id, n) -> {
            Debug.warn("Troo", id, n);
        });
        System.out.println("Barack2:" + brack2.getTimeFirstTroop());
        System.out.println("Space Troop:"+userGame.getSpaceTroop());
    }

    public int getSpaceTroop() {//demo
        return this.spaceTroop;
//        return 2;
    }

    public void updateResTroop(String type, String typeTroop, int levelTroop) {
        Debug.warn("UpdateResTroop", type, typeTroop, levelTroop);
        int elixirNeed = LoadConFig.getInstance().getInt(typeTroop, levelTroop, "trainingElixir");
        int darkElixirNeed = LoadConFig.getInstance().getInt(typeTroop, levelTroop, "researchDarkElixir");
        Debug.warn("Resoure", elixirNeed, darkElixirNeed);
        Debug.warn("elixirOld", this.elixir);
        if (type.equals("delete")) {
            this.darkElixir += darkElixirNeed;
            this.elixir += elixirNeed;
            if (this.darkElixir > this.darkElixirMax) this.darkElixir = this.darkElixirMax;
            if (this.elixir > this.elixirMax) this.elixir = this.elixirMax;
        } else {

            if (elixirNeed > this.elixir) {
                elixirNeed -= this.elixir;
                this.elixir = 0;
            } else {
                this.elixir -= elixirNeed;
                elixirNeed = 0;
                Debug.warn("Tru elixir", this.elixir, elixirNeed);
            }
            if (darkElixirNeed > this.darkElixir) {
                darkElixirNeed -= this.elixir;
                this.darkElixir = 0;
            } else {
                this.darkElixir -= darkElixirNeed;
                darkElixirNeed = 0;
            }
            this.g -= this.convert(0, elixirNeed, darkElixirNeed);
        }
        Debug.warn("elixr new", this.elixir);
    }

    public boolean checkResourceTroop(String type, Integer level) {
        Debug.warn("checkResource", type, level);
        int elixirNeed = LoadConFig.getInstance().getInt(type, level, "trainingElixir");
        int darkElixirNeed = LoadConFig.getInstance().getInt(type, level, "researchDarkElixir");
        Debug.warn("troop", elixirNeed, darkElixirNeed);
        Debug.warn("Resouce", this.elixir, this.darkElixir, this.g);
        if (elixirNeed > this.elixir) elixirNeed -= this.elixir;
        else elixirNeed = 0;
        if (darkElixirNeed > this.darkElixir) darkElixirNeed -= this.darkElixir;
        else darkElixirNeed = 0;
        return this.g > convert(0, elixirNeed, darkElixirNeed);
    }

    public void updateStructure() {
//        Debug.warn("Update Map");
        ArrayList<Integer> listBuild = new ArrayList<>();
        int timeStamp = (int) (new Date().getTime() / 1000);
//        Debug.warn("timeStamp", (int) (new Date().getTime() / 1000));
        Map<Integer, Structure> structures = this.getMap().getStructures();
        structures.forEach((id, s) -> {
            if (timeStamp - s.getTime() >= s.getInt("buildTime") && s.getState() == Structure.STATE_BUILD) {
                this.builder++;
                listBuild.add(s.getId());
                s.setState(Structure.STATE_0);
                if (s.getType().startsWith("RES")) {
                    s.setTime(s.getTime() + s.getInt("buildTime"));
                }
                if (s.getType().startsWith("TOW")) {// townhall

                    if (s.getLevel() != 1) {
                        s.setLevel(s.getLevel() - 1);
                        this.goldMax -= s.getInt("capacityGold");
                        this.elixirMax -= s.getInt("capacityElixir");
                        this.darkElixirMax -= s.getInt("capacityDarkElixir");
                        s.setLevel(s.getLevel() + 1);
                    }
                    this.goldMax += s.getInt("capacityGold");
                    this.elixirMax += s.getInt("capacityElixir");
                    this.darkElixirMax += s.getInt("capacityDarkElixir");
                }

                if (s.getType().startsWith("STO")) {
                    if (s.getString("type").equals("gold")) {
                        if (s.getLevel() != 1) {
                            s.setLevel(s.getLevel() - 1);
                            this.goldMax -= s.getInt("capacity");
                            s.setLevel(s.getLevel() + 1);
                        }
                        this.goldMax += s.getInt("capacity");
                    }

                    if (s.getString("type").equals("elixir")) {
                        if (s.getLevel() != 1) {
                            s.setLevel(s.getLevel() - 1);
                            this.elixirMax -= s.getInt("capacity");
                            s.setLevel(s.getLevel() + 1);
                        }
                        this.elixirMax += s.getInt("capacity");
                    }

                    if (s.getString("type").equals("darkElixir")) {
                        if (s.getLevel() != 1) {
                            s.setLevel(s.getLevel() - 1);
                            this.darkElixirMax -= s.getInt("capacity");
                            s.setLevel(s.getLevel() + 1);
                        }
                        this.darkElixirMax += s.getInt("capacity");
                    }
                }

                if (s.getType().startsWith("AMC")) {
                    if (s.getLevel() != 1) {
                        s.setLevel(s.getLevel() - 1);
                        this.spaceTroop -= s.getInt("capacity");
                        s.setLevel(s.getLevel() + 1);
                    }
                    this.spaceTroop += s.getInt("capacity");
                }

                if (s.getType().startsWith("BDH")) {
                    Debug.warn("Builder");
                    this.builder++;
                    Debug.warn("slbuilder", this.getBuilder());
                    this.getMap().getBuilderList().put(s.getId(), 0);
                }
                if (s.getType().startsWith("BAR")) {
                    List<Brack> barrackList = this.getMap().getBrackList();
                    if (s.getLevel() == 1) barrackList.add(new Brack(s));
                    else
                        for (Brack brack : barrackList) {
                            if (brack.getId() == s.getId()) {
                                brack.setTimeStart(s.getTime()+s.getInt("buildTime"));
                                s.setLevel(s.getLevel() - 1);
                                int spaceOld = s.getInt("queueLength");
                                s.setLevel(s.getLevel() + 1);
                                int spaceNew = s.getInt("queueLength");
                                brack.setSpace(brack.getSpace() + spaceNew - spaceOld);
                            }
                        }
                }
                structures.put(id, s);//check
            }
        });
        int i = 0;
        ArrayList<Obstacle> obstacles = this.getMap().getObstacles();
        while (i < obstacles.size()) {
            Obstacle o = obstacles.get(i);
            if (timeStamp - o.getRemoveTime() > o.getInt("buildTime") && o.getState() == Structure.STATE_BUILD) {

                this.builder++;
                obstacles.remove(i);
                this.elixir += o.getInt("rewardElixir");
                this.darkElixir += o.getInt("rewardDarkElixir");
                listBuild.add(o.getId());
            } else i++;
        }
//        Debug.warn("Builder", this.builder);
        Debug.warn(listBuild.size());
        Map<Integer, Integer> builderList = this.getMap().getBuilderList();
        for (int j = 0; j < listBuild.size(); j++) {
            int finalJ = j;
            builderList.forEach((idBuilder, idStructure) -> {
                Debug.warn(listBuild.get(finalJ), idStructure);
                Debug.warn(idStructure.equals(listBuild.get(finalJ)));
                if (idStructure.equals(listBuild.get(finalJ))) {
                    builderList.put(idBuilder, 0);
//                    Debug.warn(idBuilder);
                }
            });
        }
    }
//    public void set(JSONObject jsonObject) {
//        Gson gson = new Gson();
//        this = gson.fromJson(jsonObject,UserGame.class);
//    }
}
