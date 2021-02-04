package cmd.receive.demo;

import bitzero.server.extensions.data.BaseCmd;
import bitzero.server.extensions.data.DataCmd;
import bitzero.util.common.business.CommonHandle;
import cmd.obj.demo.DemoDirection;

import java.nio.ByteBuffer;

public class RequestObject extends BaseCmd {
    private String name;
    private short num;
    public RequestObject(DataCmd dataCmd) {
        super(dataCmd);
        unpackData();
    }
    public void unpackData() {
        ByteBuffer bf = makeBuffer();
        try {
            name = readString(bf);
            num= readShort(bf);
        } catch (Exception e) {
            e.printStackTrace();
//            direction = DemoDirection.UP.getValue();
//            CommonHandle.writeErrLog(e);
        }

    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNum(short num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public short getNum() {
        return num;
    }

    @Override
    public String toString() {
        return "RequestObject{" +
                "name='" + name + '\'' +
                ", num=" + num +
                '}';
    }
}
