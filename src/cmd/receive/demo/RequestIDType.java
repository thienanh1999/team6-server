package cmd.receive.demo;

import bitzero.server.extensions.data.BaseCmd;
import bitzero.server.extensions.data.DataCmd;
import bitzero.util.common.business.Debug;

import java.nio.ByteBuffer;

public class RequestIDType extends BaseCmd {
    private int ID;
    private String type;
    public RequestIDType(DataCmd data) {
        super(data);
        unpackData();
    }

    public int getID() {
        return this.ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public void unpackData() {
        ByteBuffer bf =makeBuffer();
        int p=readInt(bf);
//        Debug.warn("Id barrack",p);
        this.ID=p;
        this.type=readString(bf);

    }
}
