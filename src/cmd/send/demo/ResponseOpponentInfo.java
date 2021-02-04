package cmd.send.demo;

import bitzero.server.extensions.data.BaseMsg;
import cmd.CmdDefine;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import bitzero.util.common.business.Debug;
import util.Constant;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;

public class ResponseOpponentInfo extends BaseMsg {
    private JSONObject info;

    public ResponseOpponentInfo(short error, JSONObject info) {
        super(CmdDefine.FIND_MATCH, error);
        this.info = info;
    }

    @Override
    public byte[] createData() {
        ByteBuffer byteBuffer = makeBuffer();
        try {
            // Opponent Info
            byteBuffer.putInt(info.getInt("id"));
            byteBuffer.putInt(50);
            byteBuffer.putLong(info.getLong("gold"));
            byteBuffer.putLong(info.getLong("elixir"));
            byteBuffer.putLong(info.getLong("darkElixir"));

            // Map Info
            JSONObject structures = info.getJSONObject("map").getJSONObject("structures");
            JSONArray obstacles = info.getJSONObject("map").getJSONArray("obstacles");
            byteBuffer.putInt(structures.length() + obstacles.length()); // total object

            for (int i = 0; i < obstacles.length(); i++) {
                JSONObject obstacle = obstacles.getJSONObject(i);
                byteBuffer.putInt(obstacle.getInt("id"));
                putStr(byteBuffer,obstacle.getString("type"));
                JSONObject position = obstacle.getJSONObject("pos");
                byteBuffer.putInt(position.getInt("x"));
                byteBuffer.putInt(position.getInt("y"));
                byteBuffer.putInt(obstacle.getInt("level"));
                byteBuffer.putInt(obstacle.getInt("state"));
                byteBuffer.putInt(obstacle.getInt("removeTime"));
            }

            Iterator iterator = structures.keys();
            while (iterator.hasNext()) {
                JSONObject structure = structures.getJSONObject((String) iterator.next());
                byteBuffer.putInt(structure.getInt("id"));
                putStr(byteBuffer, structure.getString("type"));
                JSONObject position = structure.getJSONObject("pos");
                byteBuffer.putInt(position.getInt("x"));
                byteBuffer.putInt(position.getInt("y"));
                byteBuffer.putInt(structure.getInt("level"));
                byteBuffer.putInt(structure.getInt("state"));
                byteBuffer.putInt(structure.getInt("time"));
                if (structure.getString("type").equals("WAL_1"))
                    continue;
                long gold = structure.getLong("gold");
                long elixir = structure.getLong("elixir");
                long darkElixir = structure.getLong("darkElixir");
                byteBuffer.putLong(gold);
                byteBuffer.putLong(elixir);
                byteBuffer.putLong(darkElixir);
            }

            // Troop Info
            JSONObject troops = info.getJSONObject("troops");
            byteBuffer.putInt(Constant.troopTypeList.length);
            for (String troopType : Constant.troopTypeList) {
                putStr(byteBuffer, troopType);
                byteBuffer.putInt(troops.getInt(troopType));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return packBuffer(byteBuffer);
    }
}
