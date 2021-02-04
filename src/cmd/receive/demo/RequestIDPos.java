package cmd.receive.demo;

import bitzero.core.P;
import bitzero.server.extensions.data.BaseCmd;
import bitzero.server.extensions.data.DataCmd;
import bitzero.util.common.business.Debug;

import java.awt.*;
import java.nio.ByteBuffer;


public class RequestIDPos extends BaseCmd {
    private int id;
    private Point pos;

    public RequestIDPos(DataCmd data) {
        super(data);
        unpackData();
    }

    @Override
    public void unpackData() {
        ByteBuffer bf = makeBuffer();
        try {
            int p = readInt(bf);
            this.id=p;
            int x,y;
            x=readInt(bf);
            y=readInt(bf);
            this.pos=new Point(x,y);
//            Debug.warn("move",this.id,this.pos.x,this.pos.y);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Point getPos() {
        return this.pos;
    }

    public void setPos(Point pos) {
        this.pos = pos;
    }
}
