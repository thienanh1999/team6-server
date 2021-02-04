package cmd.send.demo;

import bitzero.server.extensions.data.BaseMsg;
import bitzero.util.common.business.Debug;
import cmd.CmdDefine;

import java.nio.ByteBuffer;

public class ResponseConfirm_Vol_Type extends BaseMsg {
    private int ok;
    private int id;
    private int type;
    public ResponseConfirm_Vol_Type(short error, short cmd, int id, int type) {
        super(cmd, error);
        this.ok = error;
        this.id=id;
        this.type=type;
    }

    @Override
    public byte[] createData() {
        ByteBuffer bf= makeBuffer();
        bf.putInt(this.ok);
        bf.putInt(this.id);
        bf.putInt(this.type);
        Debug.warn("confirm",this.ok,this.id,this.type);
        return packBuffer(bf);
    }
}
