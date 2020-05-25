package com.ynov.projet.features.data;

import com.ynov.projet.Main;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {

    private static Config cfg = new Config("database.yml");
    private static int dataId = -1;
    private Connection connection;
    private Main main;

    @Getter
    PlayerConfigDB playerConfigDB;
    @Getter
    PlayerDB playerDB;

    public DBManager(Main main){
        this.main = main;

        this.playerConfigDB = new PlayerConfigDB(this);
        this.playerDB = new PlayerDB(this);
    }

    public Connection getConnection() {
        if (!isConnected()) {
            disconnect();
            connect();
        }
        return this.connection;
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
            System.out.println("[Plugin] Une erreur de connection à la base de données est survenue.");
            Bukkit.getServer().shutdown();
            e.printStackTrace();
        }

        if(dataId == -1) {
            dataId = Bukkit.getScheduler().scheduleSyncRepeatingTask(main, new Runnable() {
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
        System.out.println("[Plugin] Refresh de la connexion à la base de données.");
        try {
            if (isConnected()) {
                disconnect();
            }
            connect();
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
