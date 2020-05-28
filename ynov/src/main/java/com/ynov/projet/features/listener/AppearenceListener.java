/*
 * Copyright 404Team (c) 2018. For all uses ask 404Team for approuval before.
 */
package com.ynov.projet.Features.listener;

import com.ynov.projet.Features.objectnum.Figurine;
import com.ynov.projet.Features.Feature;
import com.ynov.projet.Features.PlayerData.PlayerInfo;
import com.ynov.projet.Features.utils.ItemUtil;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.ArrayList;


public class AppearenceListener extends Feature {

    @EventHandler
    public void onClick(PlayerInteractAtEntityEvent e){
        if(e.getRightClicked() instanceof Player && e.getHand().equals(EquipmentSlot.HAND)){
            Player target = (Player) e.getRightClicked();
            PlayerInfo targetInfo = PlayerInfo.getPlayerInfo(target);
            if(targetInfo != null && targetInfo.getAppearance() != null && targetInfo.getAppearance().length() > 0){
                String message = ChatColor.GREEN + "*Vous observez " + target.getDisplayName() + "§2. *";
                if(Figurine.random(0,1000) == 500) {
                    message = ChatColor.GREEN + "*Vous stalkez " + target.getDisplayName() + "§2. *";
                }
                String newname = "§7Apparence de : "+target.getDisplayName();
                TextComponent messagecomponent = new TextComponent(message);
                messagecomponent.setColor(net.md_5.bungee.api.ChatColor.getByChar(target.getDisplayName().toCharArray()[1]));
                BaseComponent[] texte = new BaseComponent[]{
                        new TextComponent(ItemUtil.convertItemStackToJsonRegular(ItemUtil.createItemStack(Material.BOOK, 1, newname, getApparence(targetInfo.getAppearance())))) // The only element of the hover events basecomponents is the item json
                };
                messagecomponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, texte));
                messagecomponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/aprofil see "+target.getName()));
                e.getPlayer().spigot().sendMessage(messagecomponent);
            }
        }
    }

    public ArrayList<String> getApparence(String message) {
        message = message.replace("&", "§");
        ArrayList<String> lore = new ArrayList<>();
        int taille = message.length();
        int tailledef = taille;
        int divi = 1;
        while(tailledef > 50) {
            divi++;
            tailledef = taille/divi;
        }
        int borneinf = 0;
        String color = "§7";
        for(int i = 0; i < divi; i++) {
            int bornesupp = tailledef * (i+1);
            while(bornesupp < message.length() && message.charAt(bornesupp) != ' ') {
                bornesupp++;
            }
            if(divi-1 == i) {
                bornesupp = taille;
            }
            while (message.substring(borneinf, bornesupp).startsWith(" ")) {
                borneinf++;
            }
            String partmsg = message.substring(borneinf, bornesupp);
            lore.add(color.concat(partmsg));
            if(partmsg.contains("§")) {
                String[] test = partmsg.split("§");
                color = "§"+test[test.length-1].charAt(0);
            }
            borneinf = bornesupp;
        }
        return lore;
    }
}
