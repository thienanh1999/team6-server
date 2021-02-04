package cmd.receive.demo;

import bitzero.server.extensions.data.BaseCmd;
import bitzero.server.extensions.data.DataCmd;
import bitzero.util.common.business.Debug;

import java.awt.*;
import java.nio.ByteBuffer;


public class RequestType_X_Y_Builder extends BaseCmd {
    private String type;
    private int x;
    private int y;
    private Point pos;
    private int builder;
//    private int k;

    public RequestType_X_Y_Builder(DataCmd data) {
        super(data);
        unpackData();
    }

    @Override
    public void unpackData() {
        // Debug.warn("unpackdata");
        ByteBuffer bf = makeBuffer();
        try {
            this.type = readString(bf);
//            Debug.warn(this.type);
            this.x=readInt(bf);
            this.y=readInt(bf);
            this.pos=new Point(this.x,this.y);
            this.builder=readInt(bf);
//            Debug.warn("Builder===="+this.builder);
//            Debug.warn(readInt(bf));
//            Debug.warn(readInt(bf));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getBuilder() {
        return this.builder;
    }

    public String getType() {
        return this.type;
    }

    public Point getPos() {
        return this.pos;
    }
}
