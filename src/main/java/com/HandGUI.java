package com;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.item.Item;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import me.onebone.economyapi.EconomyAPI;
import yxmingy.yupi.HandlerBase;
import yxmingy.yupi.ui.MultiOption;

import java.util.*;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;

public class HandGUI extends HandlerBase {
    private Config conf;

    public HandGUI(Config conf){
        this.conf = conf;
    }

    public void sendBack(Player player, String info){
        MultiOption ui = new MultiOption("全服助力活动");
        ui.setContent(info);
        ui.addButton("返回继续助力");
        ui.setHandler(new BackGUI(this.conf));
        ui.send(player);
    }

    @Override
    public void handle(String s, Player player) {
        HashMap<String, Object> data = (HashMap<String, Object>) this.conf.get("配置");
        HashMap<String, Integer> players = (HashMap<String, Integer>) this.conf.get("玩家数据");
        String name = player.getName();
        if(s.equals("null")){
            return;
        }else if(s.equals("1")){
            HashMap<String, Integer> sorted = players
                    .entrySet()
                    .stream()
                    .sorted(Collections.reverseOrder(comparingByValue()))
                    .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
            String txt = "§e====================助力排行榜====================\n";
            txt += " * §c只显示前50名的排行哟！努力上榜吧！\n";
            int ii = 1;
            boolean state = false;
            for(Map.Entry entry : sorted.entrySet()){
                if(ii > 50) break;
                if(entry.getKey().equals(name)){
                    txt += " * §a我的名字: " + name + " 排行: 第 §e" + ii + " §a名\n";
                    state = true;
                }
                ii++;
            }
            if(!state){
                txt += " * §c你暂未上榜！多助力吧！加油！\n";
            }
            txt += "§e====================助力排行榜====================\n";
            int i = 1;
            for(Map.Entry entry : sorted.entrySet()){
                if(i > 50) break;
                txt += "- §a第 §f" + i + " §a名: §b" + entry.getKey() + " §7- §e助力值: §6" + entry.getValue() + "\n";
                i++;
            }
            this.sendBack(player, txt);
        }else{

            double money = EconomyAPI.getInstance().myMoney(player);
            String type_money = (String) data.get("助力的金币");
            String type_item = (String) data.get("助力的物品");

            if(type_money != null){
                String[] args = type_money.split(":");
                int i = Integer.valueOf(args[0]);
                if (money >= i) {
                    EconomyAPI.getInstance().reduceMoney(player, i);
                    ((HashMap<String, Integer>)this.conf.get("玩家数据")).put(name, Integer.valueOf(players.get(name) + Integer.valueOf(args[1])));
                    ((HashMap<String, Integer>)this.conf.get("配置")).put("当前助力值", Integer.valueOf(String.valueOf(data.get("当前助力值"))) + Integer.valueOf(args[1]));
                    this.conf.save();
                    String txt = TextFormat.RED + "* §a恭喜 §f" + name + " §a助力成功！！§e获得 " + TextFormat.WHITE + args[1] + " §e助力值！";
                    Server.getInstance().broadcastMessage(txt);
                    this.sendBack(player, txt + "\n==点击下方按钮再次助力吧！==\n\n\n\n\n");
                }else{
                    player.sendMessage(TextFormat.RED + "* 助力失败！！你没有足够的金币，需要 " + i + " 金币");
                }
            }else if(type_item != null){
                String[] args = type_item.split(":");
                Item item = Item.get(Integer.valueOf(args[0]), Integer.valueOf(args[1]), Integer.valueOf(args[2]));
                if(player.getInventory().contains(item)){
                    player.getInventory().removeItem(item);
                    ((HashMap<String, Integer>)this.conf.get("玩家数据")).put(name, Integer.valueOf(players.get(name) + Integer.valueOf(args[3])));
                    ((HashMap<String, Integer>)this.conf.get("配置")).put("当前助力值", Integer.valueOf(String.valueOf(data.get("当前助力值"))) + Integer.valueOf(args[3]));
                    this.conf.save();
                    String txt = TextFormat.RED + "* §a恭喜 §f" + name + " §a助力成功！！§e获得 " + TextFormat.WHITE + args[3]  + " §e助力值！";
                    Server.getInstance().broadcastMessage(txt);
                    this.sendBack(player, txt + "\n==点击下方按钮再次助力吧！==\n\n\n\n\n");
                }else{
                    player.sendMessage(TextFormat.RED + "* 助力失败！你没有助力需要的物品！！");
                }
            }else{
                player.sendMessage(TextFormat.RED + "* 未设置助力物品，助力失败！！！");
            }
        }


    }
}
