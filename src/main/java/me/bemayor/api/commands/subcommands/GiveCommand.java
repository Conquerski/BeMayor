package me.bemayor.api.commands.subcommands;

import io.github.bakedlibs.dough.common.CommonPatterns;
import io.github.bakedlibs.dough.common.PlayerList;
import me.bemayor.api.ApiManagement;
import me.bemayor.api.commands.SubCommand;
import me.bemayor.api.common.ChatUtils;
import me.bemayor.api.common.PlayerUtils;
import me.bemayor.api.customitem.CustomItemStack;

import me.bemayor.components.ComponentManagement;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class GiveCommand extends SubCommand {
    private static final String PLACEHOLDER_PLAYER = "%player%";
    private static final String PLACEHOLDER_ITEM = "%item%";
    private static final String PLACEHOLDER_AMOUNT = "%amount%";

    public GiveCommand(String description, ComponentManagement manager,List<String> itemIdListForTab){
        super("give",description+",格式为：{物品ID}_{物品数量(可缺省)}_{收货人名字(可缺省)}",false);
        this.setAction(e->{
            List<String> texts=e.getTexts();
            if(texts!=null && texts.size()>0) {
                CommandSender sender = e.getCommandSender();
                ApiManagement apiManager = manager.getPlugin().getApiManager();

                //获取要给与的玩家
                Player player;
                if (texts.size() > 2) {
                    //如果有写给与的玩家，则判断玩家在不在
                    String playerName=texts.get(2);
                    Optional<Player> p = PlayerList.findByName(playerName);
                    if (!p.isPresent()) {
                        apiManager.getChatMessages().sendMessage(sender, "messages.not-online", true, msg -> msg.replace(PLACEHOLDER_PLAYER, playerName));
                        return;
                    }
                    player = p.get();
                } else {
                    //如果没有写给与的玩家，则判断指令是不是由玩家发出
                    if (!(sender instanceof Player)) {
                        return;
                    }
                    player = (Player) sender;
                }

                //获取要给与的物品数量
                int amount = 1;
                if (texts.size() > 1) {
                    amount = parseAmount(texts.get(1));
                }

                //获取要给与的物品
                String id = texts.get(0);
                if (!apiManager.getCustomItemManager().hasId(id)) {
                    apiManager.getChatMessages().sendMessage(sender, "messages.invalid-item", true, msg -> msg.replace(PLACEHOLDER_ITEM, id));
                    return;
                }
                giveItem(sender, player, id, amount, apiManager);
            }
        });

        this.setTabFind(e->{
            List<String> texts=e.getTexts();
            if(texts!=null && texts.size()>0 &&  texts.size()<4) {
                if(texts.size()==1){
                    return itemIdListForTab;
                }else if(texts.size()==2){
                    return ChatUtils.amountListForTab;
                }else if(texts.size()==3){
                    return PlayerUtils.getPlayerNameListOnline();
                }
            }
            return ChatUtils.nullListForTab;
        });
    }

    private int parseAmount(String strAmount) {
        int amount = 1;
        if (CommonPatterns.NUMERIC.matcher(strAmount).matches()) {
            amount = Integer.parseInt(strAmount);
        }
        if(amount<1){amount = 1;}
        return amount;
    }
    private void giveItem(CommandSender sender, Player toPlayer, String id,int amount, ApiManagement apiManager) {
        CustomItemStack cItem=apiManager.getCustomItemManager().getTemplateById(id);
        apiManager.getChatMessages().sendMessage(toPlayer, "messages.given-item", true, msg -> msg.replace(PLACEHOLDER_ITEM, cItem.getName()).replace(PLACEHOLDER_AMOUNT, String.valueOf(amount)));
        Map<Integer, ItemStack> excess = toPlayer.getInventory().addItem(cItem.getCloneItemStack(amount));
        if (apiManager.getApiConfig().getBoolean("options.drop-excess-sf-give-items") && !excess.isEmpty()) {
            for (ItemStack is : excess.values()) {
                toPlayer.getWorld().dropItem(toPlayer.getLocation(), is);
            }
        }
        apiManager.getChatMessages().sendMessage(sender, "messages.give-item", true, msg -> msg.replace(PLACEHOLDER_PLAYER, toPlayer.getDisplayName()).replace(PLACEHOLDER_ITEM, cItem.getName()).replace(PLACEHOLDER_AMOUNT, String.valueOf(amount)));
    }


}

