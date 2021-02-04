package cmd.receive.demo;

import bitzero.server.extensions.data.BaseCmd;
import bitzero.server.extensions.data.DataCmd;

import java.nio.ByteBuffer;

public class RequestID_ID extends BaseCmd {
    private int struturesID;
    private int builderID;
    public RequestID_ID(DataCmd data) {
        super(data);
        unpackData();
    }

    public int getStruturesID() {
        return this.struturesID;
    }

    public void setStruturesID(int struturesID) {
        this.struturesID = struturesID;
    }

    public int getBuilderID() {
        return this.builderID;
    }

    public void setBuilderID(int builderID) {
        this.builderID = builderID;
    }

    @Override
    public void unpackData() {
        ByteBuffer bf =makeBuffer();
        this.struturesID=readInt(bf);
        this.builderID=readInt(bf);
    }
}
