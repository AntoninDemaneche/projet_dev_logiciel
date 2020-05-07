package com.ynov.projet.listener;

import com.ynov.projet.utils.PlayerInfo;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Arrays;

public class PlayerJoin implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent e){
        e.setJoinMessage("");
        Player p = e.getPlayer();
        PlayerInfo pInfo = PlayerInfo.getPlayerInfo(p);

        ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        if (skullMeta != null){
            skullMeta.setOwningPlayer(pInfo.getPlayer());
            skullMeta.setDisplayName("§fProfil de " + pInfo.getPlayer().getDisplayName());
            skull.setItemMeta(skullMeta);
        }

        e.getPlayer().getInventory().addItem(skull);

        if (PlayerInfo.getPlayerInfo(e.getPlayer()) == null){

        }

    }
}
