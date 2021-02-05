package model;

import bitzero.util.common.business.Debug;
import model.log.*;
import model.troop.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import util.ArrayUtil;
import util.Constant;
import util.NumberUtil;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.*;

public class BattleController {
    private Integer opponentID;
    public int userID = 0;
    Integer maxTrophy = 50;
    int trophyGet = 0;
    Long gold = 0L; // max gold can get
    Long elixir = 0L; // max elixir can get
    Long darkElixir = 0L; // max darkElixir can get
    Long goldClaimed = 0L; // current gold got
    Long elixirClaimed = 0L; // current elixir got
    Long darkElixirClaimed = 0L; // current dark elixir got
    int troopCount = 0;
    Map<String, Integer> userTroop = new HashMap<>();

    HashMap<Integer, Entity> objectByID = new HashMap<>();
    ArrayList<Obstacle> obstacleList = new ArrayList<Obstacle>();
    ArrayList<StructureBattle> structureList = new ArrayList<StructureBattle>();
    ArrayList<Troop> troopList = new ArrayList<Troop>(); // List of created troop (used for update game state)
    ArrayList<DropTroop> inputs = new ArrayList<DropTroop>(); // List of dropTroop (get from client)
    Map<String, Integer> troopUsed = new HashMap<String, Integer>(); // Map of type & number of troop used (for responding to client)
    ArrayList<Log> logs = new ArrayList<Log>();
    ArrayList<Log> clientLogs = new ArrayList<Log>();


    // logic battle attribute
    private int totalHitPoint;
    private int totalHitPointMax;
    private boolean destroyHalf;
    private boolean townHallDestroy;
    private int star;

    // map to control troop
    int[][] mapId;
    int[][] mapWall;
    int[][] mapObstacles;
    int[][] mapIgnoreWall = null;
    long[] totalCapacity;
    private JSONObject mapInfo = null;
    public int numberOfStructures = 0;
    public int numberOfDefense = 0;
    private ArrayList<StructureBattle> listWallIsDestroyed;


    private EndGame endGame = null; // Update when game end
    private int endGameTime = -1;
    public Long gameTick = -1L; // Game Tick (count from 0 when start game)
    private static int milisecPerTick = Constant.GAME_TICK; // 100 tick each 5s
    private static int timeStep = Constant.TIME_STEP; // 5 second - same as client - send dropTroops each 5s


    public Long getGameTick() {
        return this.gameTick;
    }

    public void setGameTick(Long gameTick) {
        this.gameTick = gameTick;
    }

    public void setEndGameTime(int endGameTime) {
        this.endGameTime = endGameTime;
    }

    public BattleController() {
        Debug.warn("Create Battle Controller");
        this.troopUsed.put("ARM_1", 0);
        this.troopUsed.put("ARM_2", 0);
        this.troopUsed.put("ARM_3", 0);
        this.troopUsed.put("ARM_4", 0);
        this.troopUsed.put("ARM_6", 0);
        this.townHallDestroy = false;
        this.star = 0;
        this.destroyHalf=false;
        this.listWallIsDestroyed = new ArrayList<>();
    }

    private void battleLoop(ArrayList<DropTroop> dropTroops) {
        int topTroop = 0;
        int tick = timeStep / milisecPerTick;
        for (int i = 0; i < tick; i++) {

            this.gameTick += 1;
            if (this.gameTick == this.endGameTime) {
                this.endGame();
                break;
            }

            // notify troop if wall is destroyed
            if (this.listWallIsDestroyed.size() != 0) {
                for (Troop troop : this.troopList) {
                    troop.notifyWallIsDestroy(this.listWallIsDestroyed);
                }
                this.listWallIsDestroyed = new ArrayList<>();
            }
            // update troop
            for (Troop troop : this.troopList){
                troop.update();
            }

            // update structure
            for (StructureBattle structure : structureList) {
                if (!structure.isDestroyState()) {
                    structure.update();
                }
            }


            if (topTroop < dropTroops.size()) {
                DropTroop dropTroop = dropTroops.get(topTroop);
                if (gameTick >= dropTroop.getTime()) {
                    inputs.add(dropTroop);
                    Troop troop = createTroop(dropTroop);
                    troopList.add(troop);
//                    Debug.warn(this.gameTick, "\tDROP TROOP\t: id", troop.id, " type:", troop.getType(), " x:", troop.position.x, " y:", troop.position.y);
                    topTroop += 1;
                }
            }
            // TODO: update next game state & log events
        }
    }

    // logic battle


    public void handleWall() {
        int[][] mapWall = this.mapWall;
        for (int i=0; i< 42;i++){
            for (int j=0; j<42;j++){
                if (mapWall[i][j] !=-1){

                    StructureBattle wall = (StructureBattle) this.getEntityById(mapWall[i][j]);
                    int x = wall.getPos().x;
                    int y = wall.getPos().y;
                    int xMin = x - Constant.RANGE_NEIGHBOR;
                    int yMin = y - Constant.RANGE_NEIGHBOR;
                    int xMax = x + Constant.RANGE_NEIGHBOR;
                    int yMax = y + Constant.RANGE_NEIGHBOR;
                    if (xMin < 0) xMin = 0;
                    if (xMax >= 42) xMax = 41;
                    if (yMin < 0) yMin = 0;
                    if (yMax >= 42) xMax = 41;
                    for (int h=xMin; h<=xMax; h++){
                        for (int k=yMin; k<=yMax; k++){
                            if (mapWall[h][k] != -1){
                                StructureBattle neighborWall = (StructureBattle) this.getEntityById(mapWall[h][k]);
                                if (wall.getId()!=neighborWall.getId())
                                    wall.listNeighbor.add(neighborWall);
                            }
                        }
                    }
                }
            }
        }
    }

    public StructureBattle getTarget(Point2D.Double troopPosition, int targetType){
        int index = -1;
        double minDistance = 100000;
        if (this.numberOfStructures == 0) return null;
        for (int i = 0; i < this.structureList.size(); i++) {
            StructureBattle structure = this.structureList.get(i);
            if (!structure.isDestroyState() && structure.targetType >= targetType) {
                double distance = NumberUtil.getEulerDistance(structure.getPos(), troopPosition);
                if (distance < minDistance) {
                    index = i;
                    minDistance = distance;
                }
            }
        }
        if (index == -1 || index == this.structureList.size()) return null;
        return this.structureList.get(index);
    }

    public void noticeStructureIsDestroyed(int id) {
//        cc.log("Update Structure");
        StructureBattle structure = (StructureBattle) this.objectByID.get(id);
        if (structure.getType().equals(StructureBattle.TOWN_HALL) && !this.townHallDestroy) {
            this.star++;
            Debug.warn("Star up");
            this.townHallDestroy = true;
        }
        ArrayUtil.fillArray(this.mapId, structure.getPos(), structure.getSize(), -1);
        if (structure.getType().startsWith("WA")) {
            // save list wall is destroyed to notify troop
            this.listWallIsDestroyed.add(structure);
            ArrayUtil.fillArray(this.mapWall, structure.getPos(), structure.getSize(), -1);
        } else {
            this.numberOfStructures -= 1;
            this.totalHitPoint -= structure.getHitPointMax();

            if (this.totalHitPoint*1.0 / this.totalHitPointMax <= 0.5 && !this.destroyHalf) {
                this.destroyHalf = true;
                Debug.warn("Star up");
                this.star++;
            }
            if (structure.getType().startsWith("DEF")) {
                this.numberOfDefense -= 1;
            }
            ArrayUtil.fillArray(this.mapIgnoreWall, structure.getPos(), structure.getSize(), -1);
        }
        if (this.numberOfStructures == 0) {
            this.star++;
        }
    }

    public void updateResource(int gold, int elixir, int darkElixir) {
        this.gold -= gold;
        this.elixir -= elixir;
        this.darkElixir -= darkElixir;
//        if (this.getGameTick()%10==0)
//        Debug.warn(this.getGameTick(),"Res",this.gold,this.elixir,this.darkElixir);
        this.goldClaimed += gold;
        this.elixirClaimed += elixir;
        this.darkElixirClaimed += darkElixir;
    }

    public Entity getEntityById(Integer id) {
        return this.objectByID.get(id);
    }

    private Boolean checkPosition(Point2D.Double tilePosition) {
        double x = tilePosition.x;
        double y = tilePosition.y;
        if (x < 0) tilePosition.x = 0;
        else if (x >= 42) tilePosition.x = 42 - 1;
        if (y < 0) tilePosition.y = 0;
        else if (y >= 42) tilePosition.y = 42 - 1;
        // check available drop position
        for (int i = 0; i < 8; i++) {
            Point direction = Constant.DELTA_POSITION.map.get(i);
            int x1 = (int) (tilePosition.x + direction.x);
            int y1 = (int) (tilePosition.y + direction.y);
            if (x >= 0 && y >= 0 && x < 42 && y < 42 && this.mapId[x1][y1] != -1 && this.mapObstacles[x1][y1] != 1) {
//                cc.log("cant drop troop on structure");
                return null;
            }
        }
        return true;
    }

    public StructureBattle getWallByPosition(Point position) {
        int id = this.mapWall[position.x][position.y];
        // cc.log("id walll ", id);
        // cc.log(this._objectByID.get(id));
        return (StructureBattle) this.objectByID.get(id);
    }


    public void setTroopCount(Map<String, Integer> troops) {
        int count = troops.getOrDefault("ARM_1", 0) + troops.getOrDefault("ARM_2", 0)
                + troops.getOrDefault("ARM_6", 0) + troops.getOrDefault("ARM_4", 0);
        userTroop = troops;
        this.troopCount = count;
    }

    // TODO: Create packet queue for resolving

    public void initMap(JSONObject info) {
        this.mapInfo = info;
        mapId = new int[42][42];
        ArrayUtil.resetArray(mapId, -1);
        try {
            // Opponent Info
            opponentID = info.getInt("id");
            // TODO: Calculate maxTrophy
            maxTrophy = 50;
            gold = info.getLong("gold");
            elixir = info.getLong("elixir");
            darkElixir = info.getLong("darkElixir");

            // Obstacles
            JSONObject structures = info.getJSONObject("map").getJSONObject("structures");
            JSONArray obstacles = info.getJSONObject("map").getJSONArray("obstacles");

            this.mapObstacles = new int[42][42];
            ArrayUtil.resetArray(this.mapObstacles, -1);
            for (int i = 0; i < obstacles.length(); i++) {
                JSONObject item = obstacles.getJSONObject(i);
                Obstacle obstacle = new Obstacle(
                        item.getInt("id"),
                        item.getString("type"),
                        new Point(item.getJSONObject("pos").getInt("x"), item.getJSONObject("pos").getInt("y")));
                obstacleList.add(obstacle);
                this.objectByID.put(obstacle.getId(), obstacle);
                this.updateMapId(obstacle);
                ArrayUtil.fillArray(this.mapObstacles, obstacle.getPos(), obstacle.getSize(), 1);
            }

            // Structures
            this.mapWall = new int[42][42];
            ArrayUtil.resetArray(this.mapWall, -1);
            Iterator iterator = structures.keys();
            while (iterator.hasNext()) {
                JSONObject item = structures.getJSONObject((String) iterator.next());
                StructureBattle structure = new StructureBattle(
                        item.getInt("id"),
                        item.getString("type"),
                        new Point(item.getJSONObject("pos").getInt("x"), item.getJSONObject("pos").getInt("y")),
                        item.getInt("level")
                );
                if (structure.getType().startsWith("DEF"))
                    structure = new Defense(structure.getId(), structure.getType(), structure.getPos(), structure.getLevel());
                if (structure.getType().startsWith("TOW") || structure.getType().startsWith("STO") || structure.getType().startsWith("RES"))
                    structure = new Storage(structure.getId(), structure.getType(), structure.getPos(), structure.getLevel());
                structure.setBattleController(this);
                this.updateMapId(structure);
                this.objectByID.put(structure.getId(), structure);
                if (structure.getType().startsWith("WA")) {
                    this.mapWall[structure.getPos().x][structure.getPos().y] = structure.getId();
                    this.mapId[structure.getPos().x][structure.getPos().y]= -2;
                } else {
                    structureList.add(structure);
                    this.totalHitPoint+=structure.getInt("hitpoints");
                    this.numberOfStructures += 1;
                    if (structure.targetType == Constant.TARGET_DEFENSE) {
                        this.numberOfDefense += 1;
                    }
                }
            }
            this.totalHitPointMax=this.totalHitPoint;
            // handle wall
            this.handleWall();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void allocateResToStructure() {
        long gold1 = this.gold;
        long elixir1 = this.elixir;
        long darkelixir1 = this.darkElixir;
        Debug.warn("ResAll", this.gold, this.elixir, this.darkElixir);
        long goldMax = gold1;
        long elixirMax = elixir1;
        long darkElixirMax = darkelixir1;
        this.totalCapacity = new long[]{0, 0, 0};
        for (StructureBattle structure : structureList) {
            totalCapacity[0] += structure.getCapacity()[0];
            totalCapacity[1] += structure.getCapacity()[1];
            totalCapacity[2] += structure.getCapacity()[2];
        }
        for (StructureBattle structure : structureList) {
            if (structure.getType().equals(StructureBattle.GOLD_STORAGE)) {
                int p = (int) (goldMax * (structure.getCapacity()[0] * 1.0 / totalCapacity[0]));
                structure.setResource(new int[]{p, 0, 0});
                gold1 -= p;
            }
            if (structure.getType().equals(StructureBattle.ELIXIR_STORAGE)) {
                int p = (int) (elixirMax * (structure.getCapacity()[1] * 1.0 / totalCapacity[1]));
                structure.setResource(new int[]{0, p, 0});
                elixir1 -= p;
            }
            if (structure.getType().equals(StructureBattle.DARK_ELIXIR_STORAGE)) {
                int p = (int) (darkElixirMax * (structure.getCapacity()[2] * 1.0 / totalCapacity[2]));
                structure.setResource(new int[]{0, 0, p});
                darkelixir1 -= p;
            }

        }
        for (StructureBattle structure : structureList) {
            if (structure.getType().equals(StructureBattle.TOWN_HALL)) {
                structure.setResource(new int[]{(int) (gold1), (int) (elixir1), (int) (darkelixir1)});
                Debug.warn("Town");
            }
        }
    }

    ;

    private void updateMapId(Entity entity) {
        int width = LoadConFig.getInstance().getInt(entity.getType(), 1, "width");
        int height = LoadConFig.getInstance().getInt(entity.getType(), 1, "height");
        for (int x = entity.getPos().x; x < entity.getPos().x + width; x++) {
            for (int y = entity.getPos().y; y < entity.getPos().y + height; y++) {
                this.mapId[x][y] = entity.getId();
            }
        }
    }

    private void startGame() {
        Debug.warn("Start Game");
        gameTick = -1L;
    }


    Troop createTroop(DropTroop dropTroop) {
        int amount = troopUsed.get(dropTroop.getType());
        troopUsed.put(dropTroop.getType(), amount + 1);

        Troop troop = null;
        int id = this.troopList.size();
        switch (dropTroop.getType()) {
            case Constant.WARRIOR:
                troop = new Warrior(id, 1, dropTroop.getPoint());
                break;
            case Constant.ARCHER:
                troop = new Archer(id, 1, dropTroop.getPoint());
                break;
            case Constant.GIANT:
                troop = new Giant(id, 1, dropTroop.getPoint());
                break;
            case Constant.FLYING_BOOM:
                troop = new FlyingBoom(id, 1, dropTroop.getPoint());
                break;
            case Constant.GOBLIN:
                troop = new Goblin(id,1,dropTroop.getPoint());
                break;
        }

        if (troop != null) troop.setBattleController(this);

        DropTroopLog dropTroopLog = new DropTroopLog(dropTroop.getTime(), new TroopLog(
                dropTroop.getType(),
                dropTroop.getId(),
                100,
                dropTroop.getPoint()
        ));
        logs.add(dropTroopLog);
        return troop;
    }

    public void dropTroop(ArrayList<DropTroop> dropTroops) {
        if (inputs.size() == 0 && dropTroops.size() > 0) {
            startGame();
            this.gameTick = -1l;
        }
        battleLoop(dropTroops);
        // TODO: get user total troop
        int totalTroop = 10;
        if (inputs.size() >= totalTroop) {
            // TODO: Endgame and send response
        }
    }

    public EndGame getEndResult() {
        return this.endGame;
    }


    public void compareLog() {
        int length = Math.min(this.logs.size(), this.clientLogs.size());
        Boolean allMatch = true;
        for (int i = 0; i < length; i++) {
            Log client = this.clientLogs.get(i);
            Log server = this.logs.get(i);
            String clientLog = client.showLog();
            String serverLog = server.showLog();
            if (!clientLog.equals(serverLog)){
                allMatch = false;
                Debug.warn("NOT MATCH", clientLog, serverLog);
            }
        }
        if (!allMatch) {
            Debug.warn("Fully Synchronized!!!");
        }
    }

    public void updateClientLogs(ArrayList<Log> logs) {
        this.clientLogs.addAll(logs);
        this.compareLog();
    }

    public EndGame endGame() {
        Debug.warn("End Game At", this.gameTick, this.star);
        this.showLogs(this.logs);
        EndGame endGame = new EndGame(this.userID, goldClaimed, elixirClaimed, darkElixirClaimed, this.maxTrophy*this.star/3, troopUsed);
        this.endGame = endGame;
        return endGame;
    }

    public Integer getOpponentID() {
        return opponentID;
    }

    public void setOpponentID(Integer opponentID) {
        this.opponentID = opponentID;
    }

    public int[][] getMapIdIgnoreWal(){
        if (this.mapIgnoreWall == null){
            this.mapIgnoreWall = new int[42][42];
            for (int i=0;i <42;i++)
                for (int j=0; j<42;j++){
                    if (this.mapId[i][j] == -2) this.mapIgnoreWall[i][j] = -1;
                    else this.mapIgnoreWall[i][j] = this.mapId[i][j];
                }
        }
        return this.mapIgnoreWall;
    }

    public int[][] getMapWall() {
        return this.mapWall;
    }

    public int[][] getMapId() {
        return this.mapId;
    }

    public JSONObject getMapJson() {
        for (StructureBattle structure : this.structureList) {
            try {
                // Res to Structure
                int[] res = structure.getResource();
                this.mapInfo.getJSONObject("map").getJSONObject("structures").getJSONObject(String.valueOf(structure.getId()))
                        .put("gold", res[0]);
                this.mapInfo.getJSONObject("map").getJSONObject("structures").getJSONObject(String.valueOf(structure.getId()))
                        .put("elixir", res[1]);
                this.mapInfo.getJSONObject("map").getJSONObject("structures").getJSONObject(String.valueOf(structure.getId()))
                        .put("darkElixir", res[2]);

                // Troop List
                JSONObject troops = new JSONObject();
                for (String troopType : Constant.troopTypeList) {
                    troops.put(troopType, userTroop.getOrDefault(troopType, 0));
                }
                mapInfo.put("troops", troops);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return this.mapInfo;
    }

    private void showLogs(ArrayList<Log> logs) {
        for (Log log: logs) {
            Debug.warn(log.showLog());
        }
    }

    public void logDestroyTroop(Troop troop) {
        TroopLog troopLog = new TroopLog(troop.getType(), troop.getId(), 0, troop.position);
        TroopDestroyLog troopDestroyLog = new TroopDestroyLog(this.gameTick, troopLog);
        this.logs.add(troopDestroyLog);
    }

    public void logDestroyStructure(StructureBattle structureBattle) {
        StructureLog structureLog = new StructureLog(structureBattle.getType(), structureBattle.getId(), 0, structureBattle.getPos());
        StructureDestroyLog structureDestroyLog = new StructureDestroyLog(this.gameTick, structureLog);
        this.logs.add(structureDestroyLog);
    }
}
