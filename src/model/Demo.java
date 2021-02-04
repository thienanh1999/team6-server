package model;

import util.database.DataModel;

import java.util.ArrayList;

public class Demo extends DataModel {
    private int id;
    private String name;
    private ArrayList<Integer> list;
    public Demo(){
        id=4;
        name="hello";
        list= new ArrayList<Integer>();
        list.add(123);
        list.add(1234);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Demo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
