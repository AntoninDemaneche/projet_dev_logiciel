package com.ynov.projet.utils;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class PlayerInfo {

    @Getter
    private Player player;

    @Getter
    String uuid;

    @Getter
    private int mana;

    @Getter
    private int maxMana;

    @Getter
    public static ArrayList<PlayerInfo> instanceList = new ArrayList<>();

    public PlayerInfo(Player p, int mana, int maxMana){
        this.player = p;
        this.uuid = p.getUniqueId().toString();
        this.mana = mana;
        this.maxMana = maxMana;
    }

    public PlayerInfo(String uuid, int mana, int maxMana){
        this.uuid = uuid;
        this.mana = mana;
        this.maxMana = maxMana;
    }

    public void addMana(int amount){
        int m = mana + amount;
        if(m>maxMana)
            mana = maxMana;
        else
            mana = m;

        player.setLevel(mana);
    }

    public void removeMana(int amount){
        int m = mana - amount;

        if(m<=0) {
            mana = 0;
            for(Player p : Bukkit.getOnlinePlayers()){
                if(p.isOp()){
                    p.sendMessage(player.getDisplayName() + ChatColor.YELLOW + " est tombé à 0 de chakra.");
                }
            }
        }else
            mana = m;

        player.setLevel(mana);
    }

    public void setMana(int amount){
        if(amount > maxMana)
            mana = maxMana;
        else
            mana = amount;

        player.setLevel(mana);
    }

    public static PlayerInfo getPlayerInfo(Player p){
        return instanceList.stream().filter(info -> info.getUuid().equals(p.getUniqueId().toString())).findFirst().orElse(null);
    }

    public void setMaxMana(int amount){
        this.maxMana = amount;
    }

    public void reset(){
        mana = 100;
        maxMana = 100;
    }
}
