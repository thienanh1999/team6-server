package cmd.send.demo;

import bitzero.server.extensions.data.BaseMsg;
import bitzero.util.common.business.Debug;
import cmd.CmdDefine;

import java.nio.ByteBuffer;

public class ResponseBuild extends BaseMsg {
    private int ok;
    private int id;
    private int builder;
    private int x;
    private int y;

    public ResponseBuild(short error, short cmd, int id,int builder,int x,int y) {
        super(cmd, error);
        this.ok = error;
        this.id=id;
        this.builder=builder;
        Debug.warn("builder:",builder);
        this.x=x;
        this.y=y;
    }

    @Override
    public byte[] createData() {
        ByteBuffer bf= makeBuffer();
        bf.putInt(this.ok);
        bf.putInt(this.id);
        bf.putInt(this.builder);
        bf.putInt(this.x);
        bf.putInt(this.y);
        return packBuffer(bf);
    }
}
