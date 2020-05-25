package com.ynov.projet.features.data;

import com.ynov.projet.Main;
import com.ynov.projet.features.PlayerData.PlayerInfo;
import com.ynov.projet.features.objectnum.RPRank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

public class PlayerDB {

    private DBManager data;

    PlayerDB(DBManager data){
        this.data = data;
    }

    public void insertPlayer(String uuid){
        try{
            PreparedStatement pst = data.getConnection().prepareStatement("INSERT INTO PlayerInfo(uuid, mana, rank, age, disconnectTime) VALUES(?,?,?,?,?)");

            pst.setString(1, uuid); //UUID
            pst.setInt(2, 100); //Mana
            pst.setInt(3, 0);//Rank
            pst.setInt(4, 0);//Age
            pst.setInt(5, 0);//DisconnectTime
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
                    .prepareStatement("SELECT id FROM PlayerInfo WHERE uuid = ?");
            pst.setString(1, uuid);
            pst.executeQuery();
            ResultSet result = pst.getResultSet();
            insert = result.next();
            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return !insert;
    }

    public void updatePlayer(PlayerInfo pInfo){
        String uuid = pInfo.getUuid();

        Bukkit.getScheduler().runTaskAsynchronously(Main.plugin(), () -> {
            try {
                PreparedStatement pst = data.getConnection().prepareStatement("UPDATE PlayerInfo SET mana = ?, rank = ?, age = ?, disconnectTime = ? WHERE uuid = ?");

                pst.setInt(1, pInfo.getMana());
                pst.setInt(2, pInfo.getRank().getId());
                pst.setInt(3, pInfo.getAge());
                pst.setLong(4, System.currentTimeMillis());
                pst.setString(5, uuid);

                pst.executeUpdate();
                pst.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        });
    }

    public ResultSet getPlayerInfo(String uuid) {
        try {
            PreparedStatement pst = this.data.getConnection()
                    .prepareStatement("SELECT * FROM PlayerInfo WHERE uuid = ?");
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

    public void loadData(Player p){
        Main.loadingList.add(p.getName());
        p.sendMessage(ChatColor.DARK_GRAY + "Veuillez patienter pendant le chargement de vos données !");
        if(isInsert(p.getUniqueId().toString())) {
            insertPlayer(p.getUniqueId().toString());
        }


        Bukkit.getScheduler().runTaskAsynchronously(Main.plugin(), new Runnable() {
            @Override
            public void run() {
                ResultSet set = getPlayerInfo(p.getUniqueId().toString());
                try {

                    int mana = set.getInt("mana");
                    RPRank rank = RPRank.getById(set.getInt("rank"));
                    int age = set.getInt("age");
                    PlayerInfo pInfo = new PlayerInfo(p, mana, rank, age);

                    long disconnectTime = set.getLong("disconnectTime");
                    long timeDisconnected = System.currentTimeMillis() - disconnectTime;
                    long minutesDisconnected = TimeUnit.MILLISECONDS.toMinutes(timeDisconnected);
                    double manaRegenNumber = round(minutesDisconnected / 2.0, 0);

                    int manaAdd = pInfo.getMaxMana() / 100;
                    int totalMana = new Double(manaRegenNumber * manaAdd).intValue();
                    pInfo.addMana(totalMana);

                    Main.loadingList.remove(p.getName());
                    p.sendMessage(ChatColor.DARK_GRAY + "Vos données ont été chargées correctement ! \n"
                            + ChatColor.GRAY + "Bienvenue sur " + ChatColor.RED + "Serveur " + p.getDisplayName() + ChatColor.GRAY + ",\n" +
                            ChatColor.GRAY + "Votre rang est: " + ChatColor.GOLD + rank.getDisplayName() + "\n");
                }catch (Exception e){
                    e.printStackTrace();
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plugin(), new Runnable() {
                        @Override
                        public void run() {
                            Main.loadingList.remove(p.getName());
                            p.kickPlayer(ChatColor.RED + "Une erreur est survenue pendant le chargement de vos donnéees !");
                        }
                    });
                }
            }
        });
    }

    private PlayerInfo loadPlayerInfo(String name, ResultSet set) throws SQLException{
        int mana = set.getInt("mana");
        RPRank rank = RPRank.getById(set.getInt("rank"));
        int age = set.getInt("age");
        return new PlayerInfo(name, mana, rank, age);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
