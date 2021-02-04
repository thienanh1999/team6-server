package model;

import bitzero.core.P;
import bitzero.util.common.business.Debug;
import cmd.receive.demo.RequestIDPos;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapController {
    private ArrayList<Obstacle> obstacles;
    private Map<Integer, Structure> structures;
//    private Map<String, Troop> troops;
    private Map<String, Integer> totalTypeStructure;
    private int idTownHall;
    private int[][] map ;
    private int builder;
    private List<Brack> brackList;
    private Map<Integer,Integer> builderList;
//    public Structure getStructures() {
//        return
//    }


    public int[][] getMap() {
        return map;
    }

    public List<Brack> getBrackList() {
        return this.brackList;
    }

    public void setObstacles(ArrayList<Obstacle> obstacles) {
        this.obstacles = obstacles;
    }

    public void setStructures(Map<Integer, Structure> structures) {
        this.structures = structures;
    }

    public Map<Integer, Integer> getBuilderList() {
        return this.builderList;
    }

    public void setBuilderList(Map<Integer, Integer> builderList) {
        this.builderList = builderList;
    }

    public void setBrackList(List<Brack> brackList) {
        this.brackList = brackList;
    }

    public boolean checkPosition(Structure structure, Point pos) {
        Debug.warn("Check Position");
        Debug.warn("ID: " + structure.getId());
        int height = structure.getInt("height");
        int width = structure.getInt("width");
        for (int i = structure.getPos().x; i < pos.x + height; i++) {
            for (int j = structure.getPos().y; j < pos.y + width; j++) {
                if (map[i][j] != structure.getId() && map[i][j] != 0) return false;
            }
        }
        Debug.warn("Xay duoc ===========");
        return true;
    }

    public void add(Structure object, Integer id) {
//        this.builder--;
//        object.setLevel(1);
        // Debug.warn("Add Structure" + structures.size());
        structures.put(id, (Structure) object);
        // Debug.warn(structures.size());
        addLogicMap(object);
        try {
            int p = totalTypeStructure.get(object.getType());
            totalTypeStructure.put(object.getType(), p + 1);
        } catch (Exception e) {
            totalTypeStructure.put(object.getType(), 1);
        }
    }

    public void move(Structure s, RequestIDPos move) {
        removeLogicMap(s);//xoa vi tri hien tai cua s
////        Entity e = new Entity(s.getId(), s.getType(), s.getPos());
//        s.setPos(move.getPos());//update vi tri hien tai cua s
        s.setPos(move.getPos());
        addLogicMap(s);
        Debug.warn("Pos",s.getPos());
        this.getStructures().put(s.getId(), s);
//        updateMap1(s);
    }

    private void addLogicMap(Entity entity) {
        Point p = entity.getPos();
        int width = LoadConFig.getInstance().getInt(entity.getType(), 1, "width");
        int height = LoadConFig.getInstance().getInt(entity.getType(), 1, "height");
//        Debug.warn("Size", width, height);
        int id = entity.getId();
//        Debug.warn("Id " + id);
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                this.map[p.x + i][p.y + j] = id;
    }

    private void removeLogicMap(Entity entity) {
        Point p = entity.getPos();
        int width = LoadConFig.getInstance().getInt(entity.getType(), 1, "width");
        int height = LoadConFig.getInstance().getInt(entity.getType(), 1, "height");
        // Debug.warn("Size", width, height);
        int id = entity.getId();
        // Debug.warn("Id " + id);
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                this.map[p.x + i][p.y + j] = 0;
    }

    public MapController() {
        this.brackList= new ArrayList<>();
        this.obstacles = new ArrayList<Obstacle>();
        this.totalTypeStructure = new HashMap<String, Integer>();
        this.builderList= new HashMap<>();
//        troops = new HashMap<String, Troop>();
        map= new int[41][41];
        for (int i = 1; i < 58; i++) {
            JSONObject temp = null;
            try {
                temp = LoadConFig.getInstance().getJsonObject().getJSONObject("obs").getJSONObject(Integer.toString(i));
                Obstacle o = new Obstacle(i, temp.getString("type"), new Point(temp.getInt("posX"), temp.getInt("posY")));
                obstacles.add(o);
                this.addLogicMap(o);
            } catch (Exception e) {
                System.err.println(temp);
                e.printStackTrace();
            }
        }
        structures = new HashMap<Integer, Structure>();
        JSONObject temp = null;

        Structure s = new Structure(Structure.ID_TOWNHALL, "TOW_1", new Point(20, 19));
        this.add(s,s.getId());
        s = new Structure(Structure.ID_TOWNHALL + 1, "AMC_1", new Point(20, 24));
        this.add(s,s.getId());
        s = new Structure(Structure.ID_TOWNHALL + 2, "RES_1", new Point(25, 19));
        this.add(s,s.getId());
        s = new Structure(Structure.ID_TOWNHALL + 3, "BDH_1", new Point(18, 21));
        this.add(s,s.getId());
        this.getBuilderList().put(Structure.ID_TOWNHALL + 3,0);//them tho xay

        s = new Structure(Structure.ID_TOWNHALL + 4, "CLC_1", new Point(30, 22));
        this.add(s,s.getId());
        s = new Structure(Structure.ID_TOWNHALL + 5, "BAR_1", new Point(28, 28));//error
        s.setLevel(1);
        this.add(s,s.getId());
        this.getBrackList().add(new Brack(s));
//        Structure2 s1 = new Structure2(342,"RES_1",new Point(2,3));
//        this.add(s1,"1234");
    }

    public ArrayList<Obstacle> getObstacles() {
        return this.obstacles;
    }

    public Map<Integer, Structure> getStructures() {
        return this.structures;
    }

//    public Map<String, Troop> getTroops() {
//        return this.troops;
//    }

    public int getLevelTownHall() {
        Structure s = structures.get(this.idTownHall);
        return s.getLevel();
    }

    public int getIdTownHall() {
        return this.idTownHall;
    }

    public void setIdTownHall(int idTownHall) {
        this.idTownHall = idTownHall;
    }

    public int getNumberStructure(String type) {
//        int count=0;
//        this.structures.forEach((id, s) -> {
//            if (s.getType().equals(type)) ++count;
//        });
        if (this.totalTypeStructure.get(type) != null) return this.totalTypeStructure.get(type);
        else return 0;
    }

    public int getBuilder() {
        return this.builder;
    }

    public void setBuilder(int builder) {
        this.builder = builder;
    }
}
