package cmd.send.demo;

import bitzero.server.extensions.data.BaseMsg;
import bitzero.util.common.business.Debug;
import cmd.CmdDefine;

import java.nio.ByteBuffer;

public class ResponseInt extends BaseMsg {
    private int ok;
    private int id;

    public ResponseInt(short error,short cmd,int id) {
        super(cmd, error);
        this.ok = error;
        this.id=id;
    }

    @Override
    public byte[] createData() {
        ByteBuffer bf= makeBuffer();
//        Debug.warn("Get time",this.id);
        bf.putInt(this.ok);
        bf.putInt(this.id);
        return packBuffer(bf);
    }
}
