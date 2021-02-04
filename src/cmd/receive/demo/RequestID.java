package cmd.receive.demo;

import bitzero.server.extensions.data.BaseCmd;
import bitzero.server.extensions.data.DataCmd;

import java.nio.ByteBuffer;

public class RequestID extends BaseCmd {
    private int ID;
    public RequestID(DataCmd data) {
        super(data);
        unpackData();
    }

    public int getID() {
        return this.ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    @Override
    public void unpackData() {
        ByteBuffer bf =makeBuffer();
        this.ID=readInt(bf);
    }
}
