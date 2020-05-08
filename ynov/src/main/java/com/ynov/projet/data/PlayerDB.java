package com.ynov.projet.data;

import com.ynov.projet.Plugin;
import com.ynov.projet.utils.PlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.security.PublicKey;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerDB {
    private DBManager data;

    PlayerDB(DBManager data){
        this.data = data;
    }

    public void insertPlayer(String uuid){
        try{
            PreparedStatement pst = data.getConnection().prepareStatement("INSERT INTO PlayerInfo(uuid, mana) VALUES (?, ?)");

            pst.setString(1, uuid);
            pst.setInt(2, 100);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public boolean isInsert(String uuid){
        boolean insert = false;
        try {
            PreparedStatement pst = this.data.getConnection()
                    .prepareStatement("SELECT id FROM PlayerInfo WHERE uuid = ?");
            pst.setString(1, uuid);
            pst.executeQuery();
            ResultSet result = pst.getResultSet();
            insert = result.next();
            pst.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return !insert;
    }

    public void updatePlayer(PlayerInfo pInfo){
        String uuid = pInfo.getUuid();

        Bukkit.getScheduler().runTaskAsynchronously(Plugin.getInstance(), () -> {
            try{
                PreparedStatement pst = data.getConnection().prepareStatement("UPDATE PlayerInfo SET mana = ? WHERE uuid = ?");
                pst.setInt(1, pInfo.getMana());
            }catch (SQLException e){
                e.printStackTrace();
            }
        });
    }

    public ResultSet getPlayerInfo(String uuid){
        try{
            PreparedStatement pst = this.data.getConnection()
                    .prepareStatement("SELECT * FROM PlayerInfo WHERE uuid = ?");
            pst.setString(1, uuid);
            pst.executeQuery();
            ResultSet result = pst.getResultSet();
            if (result.next())
                return result;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public void loadData(Player p){
        Plugin.getLoadingList().add(p.getName());
        p.sendMessage(ChatColor.DARK_GRAY + "Veuillez patienter pendant le chargement de vos données !");
        if (isInsert(p.getUniqueId().toString())){
            insertPlayer(p.getUniqueId().toString());
        }

        Bukkit.getScheduler().runTaskAsynchronously(Plugin.getInstance(), new Runnable() {
            @Override
            public void run() {
                ResultSet set = getPlayerInfo(p.getUniqueId().toString());
                try{
                    int mana = set.getInt("mana");

                    PlayerInfo pInfo = new PlayerInfo(p, mana);

                    Plugin.getLoadingList().remove(p.getName());
                    p.sendMessage(ChatColor.DARK_GRAY + "Vos données ont été chargées correctement ! \n"
                            + ChatColor.GRAY + "Bienvenue sur " + ChatColor.RED + "Projet " + p.getDisplayName() + ChatColor.GRAY + ".\n");
                }catch (Exception e){
                    e.printStackTrace();
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.getInstance(), new Runnable() {
                        @Override
                        public void run() {
                            Plugin.getLoadingList().remove(p.getName());
                            p.kickPlayer(ChatColor.RED + "Une erreur est survenue pendant le chargement de vos données !");
                        }
                    });
                }
            }
        });
    }

    public PlayerInfo loadData(String uuid){
        if (isInsert(uuid)){
            insertPlayer(uuid);
        }

        ResultSet set = getPlayerInfo(uuid);
        try {
            int mana = set.getInt("mana");
            return new PlayerInfo(uuid, mana);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
