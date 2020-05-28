package com.ynov.projet.Features.listener;


import com.ynov.projet.Features.Feature;
import com.ynov.projet.Features.PlayerData.PlayerInfo;
import com.ynov.projet.Main;
import net.citizensnpcs.api.event.NPCLeftClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.Controllable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;


public class CloneListener extends Feature {


    @EventHandler
    public void onCloneClick(NPCRightClickEvent e){
        NPC npc = e.getNPC();
        Player p = e.getClicker();
        PlayerInfo pInfo = PlayerInfo.getPlayerInfo(p);

        if(npc.getTrait(Controllable.class).isEnabled() && !pInfo.getPlayerClone().own(npc)){
            p.sendMessage(ChatColor.RED + "Vous ne pouvez pas controler un clone qui ne vous appartient pas !");
            Location loc = p.getLocation();

            if(npc.getEntity() instanceof Player) {
                Player playernpc = (Player)npc.getEntity();
                if (playernpc.getPassengers().isEmpty()) { //Si faux, le propriétaire du clone est déjà dessus, pas besoin d'eject
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plugin(), () -> {
                        playernpc.eject();
                        p.teleport(loc); //On retp le joueur à sa position initiale
                    }, 1L);
                }
            }
        }
    }

}