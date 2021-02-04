package cmd.send.demo;

import bitzero.server.extensions.data.BaseMsg;
import cmd.CmdDefine;

import java.awt.*;

public class ResponseLogin extends BaseMsg {
    public ResponseLogin(short error) {
        super(CmdDefine.CUSTOM_LOGIN, error);
    }
}
