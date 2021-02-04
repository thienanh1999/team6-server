package cmd.receive.user;

import java.nio.ByteBuffer;

import bitzero.server.extensions.data.BaseCmd;
import bitzero.server.extensions.data.DataCmd;

public class RequestPlayedGame extends BaseCmd {
    public long zingId;

    public RequestPlayedGame(DataCmd data) {
        super(data);

    }

    @Override
    public void unpackData() {
        ByteBuffer bf = makeBuffer();
        try {
            zingId = readLong(bf);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
