package com.ynov.projet.Features.data;

import com.ynov.projet.Features.PlayerData.PlayerConfig;
import com.ynov.projet.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerConfigDB {
    private DBManager data;

    PlayerConfigDB(DBManager data){
        this.data = data;
    }

    public void insertPlayerConfig(String uuid){
        try{
            PreparedStatement pst = data.getConnection().prepareStatement("INSERT INTO PlayerConfig(uuid, changechat, buildmode, vanish) VALUES(?,?,?,?)");
            pst.setString(1, uuid);
            pst.setBoolean(2, false);
            pst.setBoolean(3, false);
            pst.setBoolean(4, false);
            pst.executeUpdate();
            pst.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public boolean isInsert(String uuid){
        boolean insert = false;
        try {
            PreparedStatement pst = this.data.getConnection()
                    .prepareStatement("SELECT id FROM PlayerConfig WHERE uuid = ?");
            pst.setString(1, uuid);
            pst.executeQuery();
            ResultSet result = pst.getResultSet();
            insert = result.next();
            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return insert;
    }

    public void updatePlayerConfig(PlayerConfig pConfig){

        Bukkit.getScheduler().runTaskAsynchronously(Main.plugin(), new Runnable() {
            @Override
            public void run() {
                try {
                    PreparedStatement pst = data.getConnection().prepareStatement("UPDATE PlayerConfig SET changechat = ?, buildmode = ?, vanish = ? WHERE uuid = ?");

                    pst.setBoolean(1, pConfig.isChangechat());
                    pst.setBoolean(2,pConfig.isBuildmode());
                    pst.setBoolean(3, pConfig.isVanish());

                    pst.setString(4, pConfig.getUuid());

                    pst.executeUpdate();
                    pst.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
        });
    }

    public ResultSet getPlayerConfig(String uuid) {
        try {
            PreparedStatement pst = this.data.getConnection()
                    .prepareStatement("SELECT * FROM PlayerConfig WHERE uuid = ?");
            pst.setString(1, uuid);
            pst.executeQuery();
            ResultSet result = pst.getResultSet();
            if (result.next())
                return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public PlayerConfig loadConfig(Player p) {
        if (!isInsert(p.getUniqueId().toString())) {
            insertPlayerConfig(p.getUniqueId().toString());
        }

        try {
            ResultSet set = getPlayerConfig(p.getUniqueId().toString());
            boolean changechat = set.getBoolean("changechat");
            boolean buildmode = set.getBoolean("buildmode");
            boolean vanish = set.getBoolean("vanish");


            return new PlayerConfig(p, changechat, buildmode, vanish);

        } catch (SQLException e) {
            e.printStackTrace();
            Main.loadingList.remove(p.getName());
            p.kickPlayer(ChatColor.RED + "Une erreur est survenue pendant le chargement de vos donnéees !");
        }
        return null;
    }

    public PlayerConfig loadConfig(String uuid) {
        if (!isInsert(uuid)) {
            insertPlayerConfig(uuid);
        }

        try{
            ResultSet set = getPlayerConfig(uuid);
            boolean changechat = set.getBoolean("changechat");
            boolean buildmode = set.getBoolean("buildmode");
            boolean vanish = set.getBoolean("vanish");

            return new PlayerConfig(uuid, changechat, buildmode, vanish);

        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}
