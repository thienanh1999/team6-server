package cmd.receive.user;

import java.nio.ByteBuffer;

import bitzero.server.extensions.data.BaseCmd;
import bitzero.server.extensions.data.DataCmd;

public class RequestWriteStatus extends BaseCmd {
    public String status;

    public RequestWriteStatus(DataCmd data) {
        super(data);

    }

    @Override
    public void unpackData() {
        ByteBuffer bf = makeBuffer();
        try {
            status = readString(bf);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
