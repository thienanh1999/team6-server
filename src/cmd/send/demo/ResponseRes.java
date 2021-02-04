package cmd.send.demo;

import bitzero.server.extensions.data.BaseMsg;
import bitzero.util.common.business.Debug;
import cmd.CmdDefine;
import model.UserGame;

import java.nio.ByteBuffer;

public class ResponseRes extends BaseMsg {
    private int trophy;
    private int gold;
    private int elixir;
    private int g;
    private int darkElixir;
    private int builderNumber;
    public ResponseRes(short error, int gold,int g,int elixir,int builder,int darkelixir,int trophy) {
        super(CmdDefine.LOADRES, error);
        this.gold=gold;
        this.g=g;
        this.elixir=elixir;
        this.darkElixir=darkelixir;
        this.builderNumber=builder;
        this.trophy=trophy;
    }

    @Override
    public byte[] createData() {
        ByteBuffer bf = makeBuffer();
        bf.putInt(this.gold);
        bf.putInt(this.elixir);
        bf.putInt(this.g);
        bf.putInt(this.darkElixir);
        bf.putInt(this.builderNumber);
        bf.putInt(this.trophy);
        return packBuffer(bf);
    }
}
