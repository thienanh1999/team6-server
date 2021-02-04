package cmd.send.demo;

import bitzero.server.extensions.data.BaseMsg;
import bitzero.util.common.business.Debug;
import cmd.CmdDefine;
import model.Brack;
import model.TypeQuantity;

import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ResponseBarack extends BaseMsg {
    private List<Brack> brackList;

    public ResponseBarack(short error, short cmd, List<Brack> brackList) {
        super(cmd, error);
        this.brackList = brackList;
    }

    @Override
    public byte[] createData() {
        ByteBuffer bf = makeBuffer();
        bf.putInt(this.brackList.size());//so luong barrack
        Debug.warn("Pack barrack", this.brackList.size());
        Iterator<Brack> it = this.brackList.iterator();
        if (this.brackList.size() != 0)
            while (it.hasNext()) {
                Brack barrack = it.next();
                bf.putInt(barrack.getId());
                bf.putInt(barrack.getTimeStart());
                bf.putInt(barrack.getTimeFirstTroop());
                bf.putInt(barrack.getListTrain().size());//size queue
                for (TypeQuantity troop : barrack.getListTrain()) {
                    putStr(bf, troop.getType());
                    bf.putInt(troop.getN());
                }
//            bf.putInt(barrack.getSpace());
            }
        return packBuffer(bf);
    }
}
