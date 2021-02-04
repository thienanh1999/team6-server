package cmd.send.demo;

import bitzero.server.extensions.data.BaseMsg;

import bitzero.util.common.business.Debug;
import cmd.CmdDefine;
import model.Obstacle;
import model.UserGame;

import java.nio.ByteBuffer;

public class ResponseObstacle extends BaseMsg {
    private Obstacle obstacle;
    public ResponseObstacle(short error, Obstacle o) {
        super(CmdDefine.BUILD, error);
        this.obstacle=o;
    }

    @Override
    public byte[] createData() {
        ByteBuffer bf = makeBuffer();
        bf.putInt(obstacle.getId());
        putStr(bf,obstacle.getType());
        bf.putInt(obstacle.getPos().x);
        bf.putInt(obstacle.getPos().y);
        bf.putInt(obstacle.getState());
        bf.putInt(obstacle.getRemoveTime());
        return packBuffer(bf);
    }

}
