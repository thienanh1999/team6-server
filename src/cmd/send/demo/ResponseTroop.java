package cmd.send.demo;

import bitzero.server.extensions.data.BaseMsg;
import bitzero.util.common.business.Debug;
import cmd.CmdDefine;

import java.nio.ByteBuffer;
import java.util.Map;

public class ResponseTroop extends BaseMsg {
    private Map<String, Integer> troop;
    public ResponseTroop(short error, short cmd, Map<String, Integer> troop) {
        super(cmd, error);
        this.troop= troop;
    }

    @Override
    public byte[] createData() {
        ByteBuffer bf= makeBuffer();
        bf.putInt(this.troop.size());
//        Debug.warn(this.troop.size());
        this.troop.forEach((id, s) -> {
//            Debug.warn("Troop",id,s);
            putStr(bf,id);
            bf.putInt(s);
//            Debug.warn((new Date().getTime()/1000)-s.getTime());
        });
        return packBuffer(bf);
    }
}
