package com.ynov.projet.listener;

import com.ynov.projet.Plugin;
import com.ynov.projet.data.DBManager;
import com.ynov.projet.data.PlayerDB;
import com.ynov.projet.utils.PlayerInfo;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class DataListener implements Listener {
    Plugin plugin = Plugin.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent e){
        e.setJoinMessage("");
        if (Plugin.isServerOpen()){
            String command = "execute as " + e.getPlayer().getName() + " unless entity @s[team=CacheJoueurs] run team join CacheJoueurs @s";
            plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), command);

            if (PlayerInfo.getPlayerInfo(e.getPlayer()) == null){
                DBManager dbManager = Plugin.getDbManager();
                PlayerDB playerDB = dbManager.getPlayerDB();

                playerDB.loadData(e.getPlayer());
            }
        }else{
            e.getPlayer().kickPlayer(ChatColor.GOLD + "Le plugin redémarre !");
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onQuit(PlayerQuitEvent e){
        e.setQuitMessage("");

        PlayerInfo pInfo = PlayerInfo.getPlayerInfo(e.getPlayer());
        DBManager dbManager = Plugin.getDbManager();
        PlayerDB playerDB = dbManager.getPlayerDB();

        playerDB.updatePlayer(pInfo);

        pInfo.destroy();
    }
}
