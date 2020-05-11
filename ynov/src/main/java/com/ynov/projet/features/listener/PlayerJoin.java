package com.ynov.projet.features.listener;

import com.ynov.projet.features.Feature;
import com.ynov.projet.features.PlayerData.PlayerInfo;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class PlayerJoin extends Feature {
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent e){
        e.setJoinMessage("");
        Player p = e.getPlayer();
        PlayerInfo pInfo = PlayerInfo.getPlayerInfo(p);

        ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        if (skullMeta != null){
            skullMeta.setOwningPlayer(e.getPlayer());
            skullMeta.setDisplayName("§4Profil de " + e.getPlayer().getDisplayName());
            skull.setItemMeta(skullMeta);
        }

        e.getPlayer().getInventory().addItem(skull);

        if (PlayerInfo.getPlayerInfo(e.getPlayer()) == null){

        }

    }
}
