package cmd.receive.battle;

import bitzero.server.extensions.data.BaseCmd;
import bitzero.server.extensions.data.DataCmd;
import bitzero.util.common.business.Debug;
import model.log.*;

import java.awt.*;
import java.awt.geom.Point2D;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class RequestSendLog extends BaseCmd {
    private ArrayList<Log> logs = new ArrayList<Log>();
    ByteBuffer byteBuffer = null;

    public RequestSendLog(DataCmd data) {
        super(data);
        this.unpackData();
    }

    @Override
    public void unpackData() {
        byteBuffer = makeBuffer();
        int size = readInt(byteBuffer);
        for (int i = 0; i < size; i++) {
            int type = readInt(byteBuffer);
            long time = readInt(byteBuffer);
            switch (type) {
                case 1:
                    TroopLog troopLog = this.unpackTroop();
                    Log log = new DropTroopLog(time, troopLog);
                    this.logs.add(log);
                    break;
                case 5:
                    TroopLog troopLog5 = this.unpackTroop();
                    Log log5 = new TroopDestroyLog(time, troopLog5);
                    this.logs.add(log5);
                    break;
                case 6:
                    StructureLog structureLog6 = this.unpackStructure();
                    Log log6 = new StructureDestroyLog(time, structureLog6);
                    this.logs.add(log6);
                    break;
            }
        }
    }

    private TroopLog unpackTroop() {
        String tType = readString(byteBuffer);
        int id = readInt(byteBuffer);
        double hp = readInt(byteBuffer)/(double)10;
        double x = readInt(byteBuffer)/(double)10;
        double y = readInt(byteBuffer)/(double)10;
        TroopLog troopLog = new TroopLog(tType, id, hp, new Point2D.Double(x, y));
        return troopLog;
    }

    private StructureLog unpackStructure() {
        String type = readString(byteBuffer);
        int id = readInt(byteBuffer);
        double hp = readInt(byteBuffer)/(double)10;
        int x = readInt(byteBuffer);
        int y = readInt(byteBuffer);
        StructureLog structureLog = new StructureLog(type, id, hp, new Point(x, y));
        return structureLog;
    }

    public ArrayList<Log> getLogs() {
        return this.logs;
    }
}
