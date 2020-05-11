package com.ynov.projet.features.listener;

import com.ynov.projet.Main;
import com.ynov.projet.features.data.DBManager;
import com.ynov.projet.features.Feature;
import com.ynov.projet.features.data.PlayerDB;
import com.ynov.projet.features.PlayerData.PlayerInfo;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static org.bukkit.Bukkit.getServer;

public class DataListener extends Feature {
    Main main = Main.plugin();

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        e.setJoinMessage("");
        if (Main.serverOpen){
            String command = "execute as " + e.getPlayer().getName() + " unless entity @s[team=CacheJoueurs] run team join CacheJoueurs @s";
            getServer().dispatchCommand(getServer().getConsoleSender(), command);

            if (PlayerInfo.getPlayerInfo(e.getPlayer()) == null){
                DBManager dbManager = Main.dbManager;
                PlayerDB playerDB = dbManager.getPlayerDB();

                playerDB.loadData(e.getPlayer());
            }
        }else{
            e.getPlayer().kickPlayer(ChatColor.GOLD + "Le plugin redémarre !");
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        e.setQuitMessage("");

        if (Main.dbManager != null){

        }

        PlayerInfo pInfo = PlayerInfo.getPlayerInfo(e.getPlayer());
        DBManager dbManager = Main.dbManager;
        PlayerDB playerDB = dbManager.getPlayerDB();

        playerDB.updatePlayer(pInfo);

        pInfo.destroy();
    }
}
