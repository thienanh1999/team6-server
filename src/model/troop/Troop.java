package model.troop;

import bitzero.util.common.business.Debug;
import model.BattleController;
import model.LoadConFig;
import model.StructureBattle;
import org.json.JSONException;
import util.ArrayUtil;
import util.Constant;
import util.MyPriorityQueue;
import util.NumberUtil;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;

public class Troop {
    private String type = null;
    // base attribute
    public int id;
    public int level;

    // attack attribute
    protected int hitPoint;
    protected int moveSpeed;
    protected int attackSpeed;
    protected double attackRange;
    protected int damePerAttack;
    protected int targetType;

    public Point2D.Double position;
    protected int state;
    public boolean isDied;
    protected boolean attacked = false;

    protected int waitingTime;
    protected int delayAttack;
    protected int defaultWaitingTime;

    protected ArrayList<Integer> path;
    protected StructureBattle target = null;
    protected StructureBattle majorTarget = null;

    protected int[][] open = null;
    protected Point[][] close = null;
    protected double deltaX;
    protected double deltaY;
    Point2D.Double finalPosition;
    protected int[][] mapId;
    protected int mapHeight;
    protected int mapWidth;
    protected ArrayList<StructureBattle> listWallIsDestroyed;
    // battle attribute
    protected BattleController battleController = null;
    // async animation
    protected int frameToUpdate;
    protected int frameToUpdateMove;
    protected int frameToUpdateAttack;
    protected int frameToPrepareAttack;
    protected int frameToAttack;

    public class Node {
        public Point position;
        public int cost;
        public Point prevPosition = null;

        public Node(Point position, int cost, Point prevPosition) {
            this.position = new Point(position.x, position.y);
            this.cost = cost;
            this.prevPosition = prevPosition;
        }
    }

    public Troop(int id, int level, Point2D.Double position, String type) {
        this.id = id;
        this.level = level;
        this.position = new Point2D.Double();
        this.position.x = position.x;
        this.position.y = position.y;
        this.type = type;
        this.state = Constant.TROOP_STATE.IDLE;
        this.path = new ArrayList<>();
        this.isDied = false;
        this.defaultWaitingTime = (this.id * this.id) % 20;
        this.waitingTime = this.defaultWaitingTime;
        this.listWallIsDestroyed = new ArrayList<>();
//        this.
        this.initAttribute();
    }

    private void initAttribute() {
        this.attackRange = LoadConFig.getInstance().getDouble(this.type, 0, "attackRange");
        this.hitPoint = LoadConFig.getInstance().getInt(this.type, this.level, "hitpoints");
        this.moveSpeed = LoadConFig.getInstance().getInt(this.type, 0, "moveSpeed");
        this.attackSpeed = LoadConFig.getInstance().getInt(this.type, 0, "attackSpeed");
        this.damePerAttack = LoadConFig.getInstance().getInt(this.type, this.level, "damagePerAttack");

        float timeMove = (10f / this.moveSpeed) / 2 * 1000;
        this.frameToUpdateMove = Math.round(timeMove / Constant.GAME_TICK);
        this.frameToUpdateAttack = Math.round(this.attackSpeed * 1000f / Constant.GAME_TICK);
    }

    public void update() {
        if (!this.isDied) {

            if (this.state == Constant.TROOP_STATE.IDLE) {
                this.waitingTime -= 1;
                if (this.waitingTime > 0) return;
                if (this.targetType == Constant.TARGET_DEFENSE && this.battleController.numberOfDefense == 0) {
                    this.targetType = Constant.TARGET_STRUCTURE;
                }
                this.majorTarget = this.battleController.getTarget(this.position, this.targetType);
                if (this.majorTarget == null) {
                    this.state = Constant.TROOP_STATE.DONE;
                    return;
                }
                this.target = this.majorTarget;
                this.path = this.getPathAndTarget();
                if (this.target.getType().startsWith("WA")) {
                    this.handleWallTarget();
                }
                this.target.target();
                // LOG DEBUG
                // TODO : LOG troop target structure
//                Debug.warn(battleController.gameTick, " TROOP id:", this.id, " type:", this.type, " target:",this.target.getId()," type:", this.target.getType()," hp:",this.target.hitPoint);
                this.state = Constant.TROOP_STATE.RUNNING;
                this.frameToUpdate = 0;
            } else if (this.state == Constant.TROOP_STATE.RUNNING) {
                this.updateMove();
            } else if (this.state == Constant.TROOP_STATE.ATTACKING) {
                this.updateAttack();
            }

        }

    }

    private void updateMove() {
        this.frameToUpdate -= 1;
        if (this.frameToUpdate <= 0) {
            // check if wall is destroy
            if (this.target.destroyState || this.majorTarget.destroyState) {
                // LOG DEBUG
//                Debug.warn(battleController.gameTick, " TROOP id:", this.id, " type:", this.type, " target is destroyed, find new target");
                this.resetIdleState();
                return;
            }

            // check if wall is destroy
            if (this.listWallIsDestroyed.size() != 0) {
                for (int i = 0; i < this.listWallIsDestroyed.size(); i++) {
                    double distance = NumberUtil.getEulerDistance(this.listWallIsDestroyed.get(i).getPos(), this.position);
                    if (distance < 5) {
                        this.target.stopTarget();
                        // LOG DEBUG
//                        Debug.warn(battleController.gameTick, " TROOP id:", this.id, " type:", this.type, " wall is destroyed, find new path");
                        this.listWallIsDestroyed = new ArrayList<>();
                        this.resetIdleState();
                        return;
                    }
                }
                this.listWallIsDestroyed = new ArrayList<>();
            }

            // continue move
            if (this.path.size() == 0) {
                this.state = Constant.TROOP_STATE.ATTACKING;
                this.frameToUpdate = 0;
                return;
            } else {
                int direction = this.path.get(this.path.size() - 1);
                this.path.remove(this.path.size() - 1);
                this.deltaX = Math.round(Constant.DELTA_POSITION.map.get(direction).x * 1f / this.frameToUpdateMove / 2f * 10000f) / 10000f;
                this.deltaY = Math.round(Constant.DELTA_POSITION.map.get(direction).y * 1f / this.frameToUpdateMove / 2f * 10000f) / 10000f;
                this.finalPosition = new Point2D.Double();
                this.finalPosition.x = this.position.x + Constant.DELTA_POSITION.map.get(direction).x / 2f;
                this.finalPosition.y = this.position.y + Constant.DELTA_POSITION.map.get(direction).x / 2f;

            }

            this.frameToUpdate = this.frameToUpdateMove;
        }
        this.position.x += this.deltaX;
        this.position.y += this.deltaY;

    }

    private void resetIdleState() {
        this.state = Constant.TROOP_STATE.IDLE;
        this.waitingTime = this.defaultWaitingTime;
    }

    private void updateAttack() {
        // if target is destroyed, find other target
        if (this.target.destroyState || this.majorTarget.destroyState) {
            this.resetIdleState();
//            Debug.warn(battleController.gameTick, " TROOP id:", this.id, " type:", this.type, " target is destroyed, find new target");
            return;
        }

        // check wall is destroyed
        if (this.target.getType().equals(StructureBattle.WALL) && this.listWallIsDestroyed.size() != 0) {
            for (int i = 0; i < this.listWallIsDestroyed.size(); i++) {
                double distance = NumberUtil.getEulerDistance(this.listWallIsDestroyed.get(i).getPos(), this.position);
                if (distance < 5) {
//                    this.target.stopTarget();
                    this.listWallIsDestroyed = new ArrayList<>();
                    this.resetIdleState();
                    // LOG DEBUG
//                    Debug.warn(battleController.gameTick, " TROOP id:", this.id, " type:", this.type, " wall is destroyed, find new path");
                    return;
                }
            }
            this.listWallIsDestroyed = new ArrayList<>();
        }

        this.frameToUpdate -= 1;
        if (this.frameToUpdate <= 0) {
            this.attacked = true;
            this.frameToAttack = this.frameToPrepareAttack;

            this.frameToUpdate = this.frameToUpdateAttack;
        }

        // attack
        this.frameToAttack -= 1;
        if (this.attacked && this.frameToAttack <= 0) {
            this.target.updateHitPoint(this.damePerAttack);
            // LOG DEBUG
//            Debug.warn(battleController.gameTick, " TROOP id:", this.id, " type:", this.type, " attacked target:", this.target.getType()," hp: ",this.target.hitPoint);
            this.attacked = false;
        }
    }

    private ArrayList<Integer> getPathAndTarget() {
        ArrayList<Integer> pathIgnoreWall = this.findPathToTarget(true, 10000);
        if (pathIgnoreWall == null) return null;
        Point direction;
        Point position = new Point((int) Math.round(this.position.x * 2), (int) Math.round(this.position.y * 2));
        int[][] mapId = battleController.getMapId();
        int index = -1;
        StructureBattle wallTarget = null;
        for (int i = pathIgnoreWall.size() - 1; i >= 0; i--) {
            direction = Constant.DELTA_POSITION.map.get(pathIgnoreWall.get(i));
            position.x += direction.x;
            position.y += direction.y;
            if (!this.checkAvailablePosition(position, mapId)) {
                Point wallPosition = new Point(
                        (int) Math.floor(position.x / 2f), (int) Math.floor(position.y / 2f)
                );
                wallTarget = battleController.getWallByPosition(wallPosition);
                index = i;
                break;
            }

        }
        if (wallTarget == null) {
            return pathIgnoreWall;
        } else {
            ArrayList<Integer> path = this.findPathToTarget(false, pathIgnoreWall.size() + 10);
            int pathLength;
            if (path == null) pathLength = 1000000;
            else pathLength = path.size();
            // DEBUG
            Debug.warn(battleController.gameTick, " TROOP id:", this.id, " type:", this.type, " choose path",pathLength,pathIgnoreWall.size());
            if (pathLength > pathIgnoreWall.size() + 10) {
                this.target = wallTarget;
                ArrayList<Integer> resultPath = new ArrayList<>();
                for (int i = index + 1; i < pathIgnoreWall.size(); i++) {
                    resultPath.add(pathIgnoreWall.get(i));
                }
                return resultPath;
            } else {
                return path;
            }
        }
    }

    private Point getRandomTargetPoint(StructureBattle target){
        Point targetPoint = new Point(Math.round(target.getPos().x * 2f), Math.round(target.getPos().y * 2f));
        int sizeX = target.getSize().x*2;
        int sizeY = target.getSize().y*2;
        int x = (this.id*this.id)%sizeX;
        int y = (this.id*7) % sizeY;
        return new Point(targetPoint.x+x, targetPoint.y+y);
    }

    ArrayList<Integer> findPathToTarget(Boolean ignoreWall, int ceil) {
        Point targetPoint = this.getRandomTargetPoint(this.target);
        // init mapId, mapWall
        if (!ignoreWall) {
            this.mapId = battleController.getMapId();
        } else {
            this.mapId = battleController.getMapIdIgnoreWal();
        }

        this.mapHeight = this.mapId.length * 2;
        this.mapWidth = this.mapId[0].length * 2;

        // A-star : open, close
        if (this.open == null) this.open = new int[this.mapHeight][this.mapWidth];
        if (this.close == null) this.close = new Point[this.mapHeight][this.mapWidth];
        ArrayUtil.resetArray(this.open, -1);
        // reset array close
        for (Point[] points : this.close) {
            Arrays.fill(points, null);
        }

        MyPriorityQueue<Node> queue = new MyPriorityQueue<>();
        Node startNode = new Node(new Point((int) Math.round(this.position.x * 2f), (int) Math.round(this.position.y * 2f)), 0, new Point(-1,-1));
        queue.push(startNode, 0);
        this.open[startNode.position.x][startNode.position.y] = 0;

        // A-start loop
        int ii = 0;
        while (!queue.isEmpty()) {
            ii+=1;
            Node node = queue.pop();
//            Debug.warn("visit node ",node.position.x,",",node.position.y);
            this.close[node.position.x][node.position.y] = node.prevPosition;
            this.open[node.position.x][node.position.y] = -1;
            if (this.checkTarget(node.position)) {
                return this.generatePath(node.position);
            }
            for (Point direction : Constant.DELTA) {
                Point newPosition = new Point(node.position.x + direction.x, node.position.y + direction.y);
                int cost = node.cost + 1;
                if (cost < ceil && this.checkAvailablePosition(newPosition, this.mapId)) {
                    double deltaX = targetPoint.x - newPosition.x * 1f;
                    double deltaY = targetPoint.y - newPosition.y * 1f;
                    double f = Math.round((cost + deltaX * deltaX + deltaY * deltaY) * 100f) / 100f;
                    if (this.close[newPosition.x][newPosition.y] != null) {
                        continue;
                    }
                    Node newNode = new Node(newPosition, cost, node.position);
                    if (this.open[newPosition.x][newPosition.y] == -1) {
                        queue.push(newNode, f);
                        this.open[newPosition.x][newPosition.y] = cost;
                    } else if (this.open[newPosition.x][newPosition.y] > cost) {
                        queue.update(newNode, f);
                        this.open[newPosition.x][newPosition.y] = cost;
                    }
                }
            }
        }
        Debug.warn("null path ");
        return null;
    }

    private Boolean checkTarget(Point position) {
        Point2D.Double pos = new Point2D.Double(position.x / 2f, position.y / 2f);
        Point min = this.target.getPos();
        Point max = new Point(min.x + this.target.getSize().x, min.y + this.target.getSize().y);
        double distance = NumberUtil.getDistanceToRect(pos, min, max);
//        Debug.warn("check target ",this.target.getPos().x, this.target.getPos().y," position: ", position.x,position.y,distance);
        return distance < this.attackRange;
    }

    protected Boolean checkAvailablePosition(Point position, int[][] mapId) {
        int x = position.x;
        int y = position.y;
        if (x >= 0 && x < this.mapHeight && y >= 0 && y < this.mapWidth) {
            int posX = (int) Math.floor(position.x / 2f);
            int posY = (int) Math.floor(position.y / 2f);
            // empty tile
            if (mapId[posX][posY] == -1) return true;
            // center tile contain structure
            if (x % 2 == 1 && y % 2 == 1) return false;
            int x2 = posX - 1;
            int y2 = posY - 1;
            if (x2 >= 0 && y2 >= 0) {
                if (x % 2 == 0 && y % 2 == 1 && mapId[posX][posY] == mapId[posX - 1][posY]) return false;
                if (x % 2 == 1 && y % 2 == 0 && mapId[posX][posY] == mapId[posX][posY - 1]) return false;
                if (mapId[x2][y2] != mapId[posX][posY]) return true;
            } else {
                return true;
            }
        }
        return false;
    }

    private void handleWallTarget() {
        ArrayList<StructureBattle> listNeighbor = this.target.listNeighbor;
        int maxCount = this.target.countTroop;
        StructureBattle newTarget = null;
        for (StructureBattle structureBattle : listNeighbor) {
            if (!structureBattle.destroyState && structureBattle.countTroop > maxCount) {
                maxCount = structureBattle.countTroop;
                newTarget = structureBattle;
            }
        }
        if (newTarget != null) {
            this.target = newTarget;
            this.path = this.findPathToTarget(false, 10000);
            // LOG DEBUG
//            Debug.warn(battleController.gameTick, " TROOP id:", this.id, " type:", this.type, " change wall target vs id:", this.target.getId());
        }
    }

    public void notifyWallIsDestroy(ArrayList<StructureBattle> listWall) {
        this.listWallIsDestroyed.addAll(listWall);
    }

    private ArrayList<Integer> generatePath(Point targetPosition) {
        ArrayList<Integer> resultPath = new ArrayList<>();
        Point position = new Point(targetPosition.x, targetPosition.y);
        Point previous = this.close[position.x][position.y];
        while (previous != null) {
            if (previous.x == -1) break;
            int deltaX = position.x - previous.x;
            int deltaY = position.y - previous.y;
            position = previous;
            previous = this.close[position.x][position.y];
            resultPath.add(Constant.DIR.dir[deltaX+1][deltaY+1]);
        }
        return resultPath;
    }


    public static int get(String type, String s) {
        try {
            return LoadConFig.getInstance().getJsonObject().getJSONObject(type).getInt(s);
        } catch (JSONException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public String getType() {
        return this.type;
    }

    public boolean isDied() {
        return this.isDied;
    }

    public int getId() {
        return this.id;
    }

    public void setBattleController(BattleController controller){
        this.battleController = controller;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDied(boolean died) {
        this.isDied = died;
    }

    public void updateHitPoint(int dame) {
        if (this.isDied) return;
        this.hitPoint -= dame;
        if (this.hitPoint <= 0) {
            battleController.logDestroyTroop(this);
            this.isDied = true;
            this.state = Constant.TROOP_STATE.DONE;
        }
    }

}
