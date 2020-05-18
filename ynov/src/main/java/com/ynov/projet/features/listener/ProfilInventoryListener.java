package com.ynov.projet.features.listener;

import com.ynov.projet.features.Feature;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ProfilInventoryListener extends Feature {
    @EventHandler
    public void onInvClick(InventoryClickEvent e){
        Player p = (Player) e.getWhoClicked();
        if (e.getView().getTitle().equals("§8Profil de : " + p.getDisplayName())){
            int slot = e.getSlot();
            e.setCancelled(true);
        }
    }
}
