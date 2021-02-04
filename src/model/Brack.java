package model;


import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

public class Brack {
    private int id;
    private int timeStart;
    private ArrayList<TypeQuantity> listTrain;
    private int timeFirstTroop;// neu con troop co the ra la 1, con troop khong the ra la 0
    private int space;
    public Brack(Structure s){
        this.id=s.getId();
        listTrain= new ArrayList<>();
        timeStart= (int) (new Date().getTime()/1000);
        timeFirstTroop=1;
        this.space=s.getInt("queueLength");
    }

    public int getSpace() {
        return this.space;
    }

    public void setSpace(int space) {
        this.space = space;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTimeFirstTroop() {
        return this.timeFirstTroop;
    }

    public void setTimeFirstTroop(int timeFirstTroop) {
        this.timeFirstTroop = timeFirstTroop;
    }

    public int getTimeStart() {
        return this.timeStart;
    }

    public void setTimeStart(int timeStart) {
        this.timeStart = timeStart;
    }

    public ArrayList<TypeQuantity> getListTrain() {
        return this.listTrain;
    }

    public void setListTrain(ArrayList<TypeQuantity> listTrain) {
        this.listTrain = listTrain;
    }
}
