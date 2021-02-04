package cmd.send.demo;

import bitzero.server.extensions.data.BaseMsg;
import cmd.CmdDefine;
import model.UserGame;

import java.nio.ByteBuffer;

public class ResponseCheat extends BaseMsg {
    private int gold;
    private int elixir;
    private int g;
    private int darkElixir;
    private int builderNumber;
    public ResponseCheat(short error,short cmd, int gold,int g,int elixir,int darkelixir) {
        super(cmd, error);
        this.gold=gold;
        this.g=g;
        this.elixir=elixir;
        this.darkElixir=darkelixir;

    }

    @Override
    public byte[] createData() {
        ByteBuffer bf = makeBuffer();
        bf.putInt(this.gold);
        bf.putInt(this.elixir);
        bf.putInt(this.g);
        bf.putInt(this.darkElixir);
        return packBuffer(bf);
    }
}
