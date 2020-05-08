package com.ynov.projet;

import com.ynov.projet.data.DBManager;
import com.ynov.projet.listener.DataListener;
import com.ynov.projet.listener.PlayerJoin;
import com.ynov.projet.listener.PlayerRightClick;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.logging.Logger;

public final class Plugin extends JavaPlugin {

    @Getter
    private static Plugin instance;

    @Getter
    public static DBManager dbManager;

    @Getter
    private Logger spigotLogger;

    @Getter
    public static ArrayList<String> loadingList = new ArrayList<>();

    @Getter
    private static PluginManager pluginManager;

    @Getter
    @Setter
    private static boolean serverOpen;


    @Override
    public void onEnable() {
        System.out.println("---> Enabling Plugin <---");
        instance = this;
        spigotLogger = Bukkit.getLogger();
        pluginManager = Bukkit.getPluginManager();
        spigotLogger.info("Loading events...");
        Bukkit.getPluginManager().registerEvents(new PlayerRightClick(), this);
        pluginManager.registerEvents(new PlayerJoin(), this);
        pluginManager.registerEvents(new DataListener(), this);

        spigotLogger.info("Loading data...");
        dbManager = new DBManager(this);
        dbManager.getConnection();
        spigotLogger.info("---> Plugin enabled <---");
    }

    @Override
    public void onDisable() {
        spigotLogger.info("---> Disabling Plugin <---");
        spigotLogger.info("Saving data...");

        Bukkit.getScheduler().cancelTasks(this);
        spigotLogger.info("---> Plugin disabled <---");
    }
}
