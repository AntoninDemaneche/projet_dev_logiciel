package com.ynov.projet.data;

import com.ynov.projet.Plugin;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {
    private static Config cfg = new Config("database.yml");
    private static int dataId = -1;
    private Connection connection;
    private Plugin plugin;

    @Getter
    PlayerDB playerDB;

    public DBManager(Plugin plugin){
        this.plugin = plugin;
        this.playerDB = new PlayerDB(this);
    }

    public Connection getConnection(){
        if (isConnected()){
            return this.connection;
        }else {
            disconnect();
            connect();
            return this.connection;
        }
    }

    public void connect(){
        try {
            String host = cfg.getString("host");
            int port = cfg.getInt("port");
            String user = cfg.getString("user");
            String password = cfg.getString("password");
            String database = cfg.getString("database");
            String sqlhost = "jdbc:mysql://" + host + ":" + port + "/" + database + "?verifyServerCertificate=false&useSSL=true";

            this.connection = DriverManager.getConnection(sqlhost, user, password);
        } catch (SQLException e) {
            System.out.println("[SeisanPlugin] Une erreur de connection à la base de données est survenue.");
            Bukkit.getServer().shutdown();
            e.printStackTrace();
        }

        if(dataId == -1) {
            dataId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
                @Override
                public void run() {
                    refreshConnection();
                }
            }, 10 * 60 * 20L, 10L);
        }
    }

    public boolean isConnected(){
        try {
            return (this.connection != null) && (!this.connection.isClosed());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void refreshConnection(){
        System.out.println("[SeisanPlugin] Refresh de la connection à la base de données.");
        try {
            if (isConnected()) {
                disconnect();
                connect();
            } else {
                connect();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Bukkit.getServer().shutdown();
        }
    }

    public void disconnect(){
        if (isConnected()) {
            try {
                this.connection.close();
                Bukkit.getScheduler().cancelTask(dataId);
                dataId = -1;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
