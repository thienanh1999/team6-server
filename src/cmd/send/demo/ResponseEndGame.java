package cmd.send.demo;

import bitzero.server.extensions.data.BaseMsg;
import bitzero.util.common.business.Debug;
import cmd.CmdDefine;
import model.EndGame;

import java.nio.ByteBuffer;
import java.util.Map;

public class ResponseEndGame extends BaseMsg {
    EndGame endGame;

    public ResponseEndGame(short error, EndGame endGame) {
        super(CmdDefine.END_GAME, error);
        this.endGame = endGame;
    }

    @Override
    public byte[] createData() {
        ByteBuffer byteBuffer = makeBuffer();
        byteBuffer.putLong(this.endGame.getGold());
        byteBuffer.putLong(this.endGame.getElixir());
        byteBuffer.putLong(this.endGame.getDarkElixir());
        byteBuffer.putInt(this.endGame.getTrophy());

        // Troop Used
        Map<String, Integer> troops = this.endGame.getTroopUsed();
        byteBuffer.putInt(troops.size());
        troops.forEach((type, amount) -> {
            putStr(byteBuffer, type);
            byteBuffer.putInt(amount);
        });
        return packBuffer(byteBuffer);
    }
}
