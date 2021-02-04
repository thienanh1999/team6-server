package cmd.receive.demo;

import bitzero.server.extensions.data.BaseCmd;
import bitzero.server.extensions.data.DataCmd;

import java.awt.*;
import java.nio.ByteBuffer;


public class RequestCheat extends BaseCmd {
    private int g;
    private int gold;
    private int elixir;
    private int darkelixir;
    public RequestCheat(DataCmd data) {
        super(data);
        unpackData();
    }

    @Override
    public void unpackData() {
        ByteBuffer bf = makeBuffer();
        try {
            this.g = readInt(bf);
            this.gold = readInt(bf);
            this.elixir = readInt(bf);
            this.darkelixir= readInt(bf);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getElixir() {
        return elixir;
    }

    public void setElixir(int elixir) {
        this.elixir = elixir;
    }

    public int getDarkelixir() {
        return darkelixir;
    }

    public void setDarkelixir(int darkelixir) {
        this.darkelixir = darkelixir;
    }
}
