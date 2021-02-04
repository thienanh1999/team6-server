package model;

public class TypeQuantity {
    private String type;
    private int n;

    public TypeQuantity(String type, int n) {
        this.type = type;
        this.n = n;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getN() {
        return this.n;
    }

    public void setN(int n) {
        this.n = n;
    }
}
