package cmd.send.demo;


import bitzero.server.extensions.data.BaseMsg;
import bitzero.util.common.business.Debug;
import cmd.CmdDefine;
import model.MapController;
import model.Obstacle;
import model.Structure;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class ResponseListEntity extends BaseMsg {
    private MapController map;
    private int countID;

    public ResponseListEntity(short error, MapController map, int countID) {
        super(CmdDefine.LOADSTRUCTURE, error);
        this.map = map;
        this.countID = countID;
    }

    @Override
    public byte[] createData() {
        ByteBuffer bf = makeBuffer();
        Map<Integer, Structure> m1 = map.getStructures();
        ArrayList<Obstacle> m2 = map.getObstacles();
        bf.putInt(m1.size()+m2.size());
        m1.forEach((id, s) -> {
            bf.putInt(s.getId());
            putStr(bf,s.getType());
            bf.putInt(s.getPos().x);
            bf.putInt(s.getPos().y);
            bf.putInt(s.getLevel());
            bf.putInt(s.getState());
            bf.putInt(s.getTime());
//            Debug.warn((new Date().getTime()/1000)-s.getTime());
            if (s.getId()==1000) Debug.warn("Dang xay",s.getState());
        });

        for (Obstacle o : m2) {
            bf.putInt(o.getId());
            putStr(bf,o.getType());
            bf.putInt(o.getPos().x);
            bf.putInt(o.getPos().y);
            bf.putInt(1);
            bf.putInt(o.getState());
            bf.putInt(o.getRemoveTime());
        }
        bf.putInt(this.countID);
        return  packBuffer(bf);
    }
}
