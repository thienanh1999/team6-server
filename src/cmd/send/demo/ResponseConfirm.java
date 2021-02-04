package cmd.send.demo;

import bitzero.server.extensions.data.BaseMsg;
import cmd.CmdDefine;

import java.nio.ByteBuffer;

public class ResponseConfirm extends BaseMsg {
    private int ok;

    public ResponseConfirm(short error,short cmd) {
        super(cmd, error);
        this.ok = error;
    }

    @Override
    public byte[] createData() {
        ByteBuffer bf= makeBuffer();
        bf.putInt(ok);
        return packBuffer(bf);
    }
}
