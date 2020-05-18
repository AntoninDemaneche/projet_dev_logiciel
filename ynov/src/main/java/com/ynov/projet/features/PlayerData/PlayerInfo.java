package com.ynov.projet.features.PlayerData;

import com.ynov.projet.features.objectnum.RPRank;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayerInfo {

    @Getter
    private Player player;

    @Getter
    String uuid;

    @Getter
    @Setter
    private String id;

    @Getter
    private int mana;

    @Getter
    private int maxMana;

    @Getter
    private RPRank rank;

    @Getter
    public static HashMap<String, PlayerInfo> instanceList = new HashMap<>();

    public PlayerInfo(Player p, int mana, RPRank rank){
        this.player = p;
        this.uuid = p.getUniqueId().toString();
        this.rank = rank;
        this.mana = mana;
        this.maxMana = mana + rank.getManaRank();

        p.setExp(0);
        p.setLevel(mana);
        if (!instanceList.containsKey(uuid)) instanceList.put(uuid, this);
    }

    public PlayerInfo(String id, int mana, RPRank rank){
        this.id = id;
        this.mana = mana;
        this.rank = rank;
        this.maxMana = mana + rank.getManaRank();
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
                    p.sendMessage(player.getDisplayName() + ChatColor.YELLOW + " est tombé à 0 de mana.");
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
        return instanceList.get(p.getUniqueId().toString());
    }

    public static void replacePlayerInfo(Player p, PlayerInfo pInfo) {
        instanceList.put(p.getUniqueId().toString(), pInfo);
    }

    public void setMaxMana(int amount){
        this.maxMana = amount;
    }

    public void addManaMax(int amount) {
        this.maxMana += amount;
    }

    public void removeManaMax(int amount) {
        this.maxMana -= amount;
    }

    public void reset(){
        mana = 100;
        rank = RPRank.APPRENTI;
    }

    public void setRank(RPRank rank){
        this.rank = rank;
        player.sendMessage(ChatColor.GRAY + "Votre rang est désormais: " + ChatColor.GOLD + rank.getDisplayName());
    }

    public void destroy(){
        instanceList.remove(this.uuid);
    }
}
