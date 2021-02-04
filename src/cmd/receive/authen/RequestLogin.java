package cmd.receive.authen;

import bitzero.server.extensions.data.BaseCmd;
import bitzero.server.extensions.data.DataCmd;

import java.nio.ByteBuffer;

public class RequestLogin extends BaseCmd {
    public String username = "";
    public int userId ;
    public RequestLogin(DataCmd dataCmd) {
        super(dataCmd);
    }
    
    @Override
    public void unpackData() {
        ByteBuffer bf = makeBuffer();
        try {
            username = readString(bf);
            userId = readInt(bf);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
