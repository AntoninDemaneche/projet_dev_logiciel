package com.ynov.projet.features.listener;

import com.ynov.projet.features.Feature;
import com.ynov.projet.features.inventory.ProfilInventory;
import com.ynov.projet.features.PlayerData.PlayerInfo;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class PlayerRightClick extends Feature {
    @EventHandler
    public void playerClickOnHead(PlayerInteractEvent e){
        Player p = e.getPlayer();
        PlayerInfo pInfo = PlayerInfo.getPlayerInfo(p);
        if (e.getHand() != null && e.getHand().equals(EquipmentSlot.HAND)){
            if (e.getItem() != null && e.getItem().getType() == Material.PLAYER_HEAD){
                if (e.getItem().getItemMeta().getDisplayName().equals("§4Profil de " + e.getPlayer().getDisplayName())){
                    p.openInventory(ProfilInventory.getProfileInventory(pInfo, p));
                }
            }
        }
    }
}
