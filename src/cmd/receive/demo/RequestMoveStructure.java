package cmd.receive.demo;

import bitzero.server.extensions.data.BaseCmd;
import bitzero.server.extensions.data.DataCmd;

import java.awt.*;
import java.nio.ByteBuffer;


public class RequestMoveStructure extends BaseCmd {
    private int id;
    private Point pos;

    public RequestMoveStructure(DataCmd data) {
        super(data);
        unpackData();
    }

    @Override
    public void unpackData() {
        ByteBuffer bf = makeBuffer();
        try {
            id = readInt(bf);
            pos.x = readInt(bf);
            pos.y = readInt(bf);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getID() {
        return this.id;
    }

    public Point getPos() {
        return this.pos;
    }
}
