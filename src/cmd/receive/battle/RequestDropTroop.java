package cmd.receive.battle;

import bitzero.server.extensions.data.BaseCmd;
import bitzero.server.extensions.data.DataCmd;
import model.DropTroop;

import java.awt.geom.Point2D;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class RequestDropTroop extends BaseCmd {
    private ArrayList<DropTroop> dropTroops = new ArrayList<DropTroop>();

    public RequestDropTroop(DataCmd data) {
        super(data);
        unpackData();
    }

    @Override
    public void unpackData() {
        ByteBuffer byteBuffer = makeBuffer();
        int size = readInt(byteBuffer);
        for (int i = 0; i < size; i++) { // type - id - x - y - time
            String type = readString(byteBuffer);
            int id = readInt(byteBuffer);
            int x = readInt(byteBuffer);
            int y = readInt(byteBuffer);
            int time = readInt(byteBuffer);
            DropTroop dropTroop = new DropTroop(type, id, new Point2D.Double(x/10f, y/10f), time);
            dropTroops.add(dropTroop);
        }
    }

    public ArrayList<DropTroop> getDropTroops() {
        return dropTroops;
    }
}
