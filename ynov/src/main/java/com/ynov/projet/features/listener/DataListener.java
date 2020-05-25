package com.ynov.projet.features.listener;

import com.ynov.projet.Main;
import com.ynov.projet.features.PlayerData.PlayerConfig;
import com.ynov.projet.features.commands.anothers.BuildCommand;
import com.ynov.projet.features.commands.anothers.Commands;
import com.ynov.projet.features.data.DBManager;
import com.ynov.projet.features.Feature;
import com.ynov.projet.features.data.PlayerDB;
import com.ynov.projet.features.PlayerData.PlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.PermissionAttachment;

import static org.bukkit.Bukkit.getServer;
import static com.ynov.projet.Main.dbManager;
import static com.ynov.projet.Main.serverOpen;
import static com.ynov.projet.features.commands.anothers.Commands.PlayerBuildTemp;
import static com.ynov.projet.features.commands.anothers.Commands.perms;

public class DataListener extends Feature {

    Main main = Main.plugin();


    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        e.setJoinMessage("");
        if(serverOpen) {
            String command = "execute as " + e.getPlayer().getName() + " unless entity @s[team=CacheJoueurs] run team join CacheJoueurs @s";
            getServer().dispatchCommand(getServer().getConsoleSender(), command);

            if(PlayerInfo.getPlayerInfo(e.getPlayer()) == null) {
                DBManager dbManager = Main.dbManager;
                PlayerDB playerDB = dbManager.getPlayerDB();

                playerDB.loadData(e.getPlayer());
            }
        } else {
            e.getPlayer().kickPlayer(ChatColor.GOLD + "Seisan restart !");
        }
        if (PlayerConfig.getPlayerConfig(e.getPlayer()) == null) {

            Main.dbManager.getPlayerConfigDB().loadConfig(e.getPlayer());
            PermissionAttachment attachment = e.getPlayer().addAttachment(main);
            perms.put(e.getPlayer().getUniqueId(), attachment);
            BuildCommand.newPermissionBuildMode(PlayerConfig.getPlayerConfig(e.getPlayer()));
        }
        if(PlayerConfig.getPlayerConfig(e.getPlayer()).isBuildmode()) {
            e.getPlayer().setPlayerListName("§7[Builder] "+e.getPlayer().getDisplayName());
        }
        Player player = e.getPlayer();
        if (!player.isOp())
        {
            Main.plugin().getServer().getOnlinePlayers().forEach((p) -> {
                if(PlayerConfig.getPlayerConfig(p) != null) {
                    if (PlayerConfig.getPlayerConfig(p).isVanish())
                        player.hidePlayer(Main.plugin(), p);
                }
                else {
                    retryNextTime(player, p);
                }
            });
        }
        else {
            if (PlayerConfig.getPlayerConfig(player).isVanish()) {
                Bukkit.getScheduler().runTaskLater(Main.plugin(), () -> Commands.playerVanish(PlayerConfig.getPlayerConfig(player)), 50);
            }
        }
    }


    private void retryNextTime(Player player, Player p) {
        if(PlayerConfig.getPlayerConfig(p) == null) {
            Bukkit.getScheduler().runTaskLater(Main.plugin(), () -> retryNextTime(player, p), 20);
        }
        else {
            if (PlayerConfig.getPlayerConfig(p).isVanish())
                player.hidePlayer(Main.plugin(), p);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        e.setQuitMessage("");

        if(dbManager != null) {
            PlayerConfig pConfig = PlayerConfig.getPlayerConfig(e.getPlayer());
            Main.dbManager.getPlayerConfigDB().updatePlayerConfig(pConfig);
            Player p = e.getPlayer();
            p.removeAttachment(perms.get(p.getUniqueId()));
            pConfig.destroy();
            if(PlayerBuildTemp.contains(e.getPlayer().getName())) {
                e.getPlayer().setGameMode(GameMode.SURVIVAL);
                PlayerBuildTemp.remove(e.getPlayer().getName());
            }
            e.setQuitMessage("");

            PlayerInfo pInfo = PlayerInfo.getPlayerInfo(e.getPlayer());


            DBManager dbManager = Main.dbManager;
            PlayerDB playerDB = dbManager.getPlayerDB();


            playerDB.updatePlayer(pInfo);

            pInfo.destroy();
        }

    }
}
