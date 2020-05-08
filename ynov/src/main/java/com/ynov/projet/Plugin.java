package com.ynov.projet;

import com.ynov.projet.listener.PlayerJoin;
import com.ynov.projet.listener.PlayerRightClick;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public final class Plugin extends JavaPlugin {

    @Getter
    private static Plugin instance;

    @Getter
    public static ArrayList<String> loadingList = new ArrayList<>();

    @Override
    public void onEnable() {
        System.out.println("---> Enabling Plugin <---");
        Bukkit.getPluginManager().registerEvents(new PlayerJoin(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerRightClick(), this);
    }

    @Override
    public void onDisable() {

    }
}
