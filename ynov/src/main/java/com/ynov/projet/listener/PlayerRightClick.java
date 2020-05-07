package com.ynov.projet.listener;

import com.ynov.projet.inventory.ProfilInventory;
import com.ynov.projet.utils.PlayerInfo;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class PlayerRightClick implements Listener {
    @EventHandler
    public void playerClickOnHead(PlayerInteractEvent e){
        Player p = e.getPlayer();
        PlayerInfo pInfo = PlayerInfo.getPlayerInfo(p);
        if (e.getHand() != null && e.getHand().equals(EquipmentSlot.HAND)){
            if (e.getItem() != null && e.getItem().getType() == Material.PLAYER_HEAD){
                if (e.getItem().getItemMeta().getDisplayName().equals("§4Profil de " + e.getPlayer().getDisplayName())){
                    p.openInventory(ProfilInventory.getProfileInventory(pInfo, p));
                }else {
                    p.sendMessage("test3");
                }
            }
        }
    }
}
