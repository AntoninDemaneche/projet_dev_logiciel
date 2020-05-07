package com.ynov.projet;

import com.ynov.projet.listener.PlayerJoin;
import com.ynov.projet.listener.PlayerRightClick;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Plugin extends JavaPlugin {

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
