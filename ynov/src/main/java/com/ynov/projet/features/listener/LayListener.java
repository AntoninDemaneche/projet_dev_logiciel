package com.ynov.projet.Features.listener;

import com.ynov.projet.Features.Feature;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class LayListener extends Feature {
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void removeArmorStandSit(PlayerMoveEvent event) {
        if(event.getPlayer().getScoreboardTags().contains("lay")) {
            if (event.getTo().getX() != event.getFrom().getX() || event.getTo().getY() != event.getFrom().getY() || event.getTo().getZ() != event.getFrom().getZ()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void PlayerQuitListener(PlayerQuitEvent event)
    {
        event.getPlayer().getScoreboardTags().remove("lay");
    }

}
