package util;

import model.troop.Troop;

import java.util.ArrayList;

public class MyPriorityQueue<T extends Troop.Node> {
    public static class MyPair<A,B>{

        private A key;
        private B value;

        public A getKey() {
            return key;
        }

        public B getValue() {
            return value;
        }
        public MyPair(A a, B b){
            this.key = a;
            this.value = b;
        }

    }
    ArrayList<MyPair<T,Double>> data;
    public MyPriorityQueue(){
        this.data = new ArrayList<>();
    }

    public void push (T item, double f){
        // TODO
        this.data.add(new MyPair<T,Double>(item,f));
    }

    public T pop (){
        double cost = 1000000;
        int index = -1;
        T node = null;
        for (int i = 0; i < this.data.size(); i++) {
            if (this.data.get(i).getValue() < cost) {
                node = this.data.get(i).getKey();
                index = i;
                cost = this.data.get(i).getValue();
            }
        }
        this.data.remove(index);
        return node;
    }

    public boolean isEmpty(){
        return this.data.size() == 0;
    }

    public void update (T item, double f){
        for (int i = 0; i < this.data.size(); i++) {
            T node = this.data.get(i).getKey();
            if (node.position.x == item.position.x && node.position.y == item.position.y) {
                node.cost = item.cost;
                node.prevPosition = item.prevPosition;
                this.data.set(i,new MyPair<T,Double>(node,f));
                return;
            }
        }
    }
}
