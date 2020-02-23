package com;

import cn.nukkit.Player;
import cn.nukkit.utils.Config;
import yxmingy.yupi.HandlerBase;

public class BackGUI extends HandlerBase {
    private Config conf;
    public BackGUI(Config conf) {
        this.conf = conf;
    }

    @Override
    public void handle(String s, Player player) {
        if(s.equals("null")){
            return;
        }
        Main.sendGUI(player, this.conf);
    }
}
