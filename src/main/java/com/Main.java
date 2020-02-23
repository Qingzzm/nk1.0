package com;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import yxmingy.yupi.ui.MultiOption;

import java.util.*;

public class Main extends PluginBase implements Listener {

    public Config config;



    @Override
    public void onEnable() {
        getLogger().info("全服助力活动插件正在开启，作者：Qingzz， QQ: 1220887314");

        LinkedHashMap<String, Object> map = new LinkedHashMap<>();

        HashMap<String, Object> data = new HashMap<>();
        data.put("活动标题", "坑钱助力");
        data.put("当前助力值", 0);
        data.put("全服助力总值", 9999999);
        data.put("助力的金币", "1000:1");
        data.put("助力的物品", null);

        HashMap<String, Long> players = new HashMap<>();
        map.put("配置", data);
        map.put("玩家数据", players);

        this.config = new Config(this.getDataFolder() + "/Config.yml", Config.YAML, map);
    }

    @Override
    public void onDisable() {
        getLogger().info("全服助力插件已关闭，作者QQ: 1220887314");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            String cmd = command.getName();
            if(command.getName().equals("助力")){
                sendGUI(((Player)sender), this.config);
            }
        }
        return true;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        event.getPlayer().sendMessage("§l§e[广告] §c定制插件请联系QQ: §b1220887314");
    }

    public static void sendMessage(String info){
        Server.getInstance().broadcastMessage(info);
    }

    public static void sendGUI(Player player, Config conf) {
        sendGUI(player, conf, "");
    }

    public static void sendGUI(Player player, Config conf, String info){
        String name = player.getName();
        HashMap<String, Object> data = (HashMap<String, Object>) conf.get("配置");
        HashMap<String, Integer> players = (HashMap<String, Integer>) conf.get("玩家数据");
        if(!players.containsKey(name)){
            players.put(name, 0);
            ((HashMap<String, Integer>)conf.get("玩家数据")).put(name, Integer.valueOf(0));
            conf.save();
        }
        MultiOption ui = new MultiOption(TextFormat.YELLOW + "全服助力活动");
        ui.setContent(TextFormat.YELLOW + "全服助力活动 —— " + data.get("活动标题").toString()+ "\n\n"
                + TextFormat.YELLOW + "我的名字: " + TextFormat.WHITE + name + "\n\n"
                + TextFormat.YELLOW + "全服助力值: " + TextFormat.GREEN + data.get("当前助力值") + TextFormat.GRAY + " / " + TextFormat.RED + data.get("全服助力总值") + "\n\n"
                + TextFormat.YELLOW + "我的助力值: " + TextFormat.GREEN +  players.get(name) + "\n\n"
                + TextFormat.YELLOW + "活动正在火热进行中！来助力吧！！" + "\n"
                + TextFormat.WHITE + info);
        ui.addButton(TextFormat.YELLOW + "[我要助力]");
        ui.addButton(TextFormat.AQUA + "§l===助力排行榜===");
        ui.setHandler(new HandGUI(conf));
        ui.send(player);
    }

}
