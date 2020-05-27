package com.ynov.projet.features.PlayerData;

import com.ynov.projet.Main;
import com.ynov.projet.features.objectnum.RPRank;
import com.ynov.projet.features.skill.Skill;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayerInfo {

    @Getter
    @Setter
    private HashMap<Skill, SkillMastery> skills;

    @Getter
    private Skill currentSkill;

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
    @Setter
    int age;

    @Getter
    public static HashMap<String, PlayerInfo> instanceList = new HashMap<>();

    public PlayerInfo(Player p, int mana, RPRank rank, int age, HashMap<Skill, SkillMastery> skills ){
        this.player = p;
        this.uuid = p.getUniqueId().toString();
        this.rank = rank;
        this.mana = mana;
        this.age = age;
        this.skills = skills;
        this.maxMana = 100 + rank.getManaRank();

        p.setExp(0);
        p.setLevel(mana);
        if (!instanceList.containsKey(uuid)) instanceList.put(uuid, this);
    }

    public PlayerInfo(String id, int mana, RPRank rank, int age, HashMap<Skill, SkillMastery> skills  ){
        this.id = id;
        this.mana = mana;
        this.rank = rank;
        this.age = age;
        this.skills = skills;
        this.maxMana = 100 + rank.getManaRank();
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

    public void updateSkill(Skill skill, SkillMastery mastery){
        if(!skills.containsKey(skill))
            player.sendMessage(ChatColor.GRAY + "Vous avez appris la technique: " + ChatColor.GOLD + skill.getName());
        else
            player.sendMessage(ChatColor.GRAY + "Votre technique " + ChatColor.GOLD + skill.getName() + ChatColor.GRAY + " est désormais " + ChatColor.GOLD + mastery.getName());
        skills.put(skill, mastery);
    }

    public void removeSkill(Skill skill){
        player.sendMessage(ChatColor.GRAY + "Vous avez oublié la technique: " + ChatColor.GOLD + skill.getName());
        skills.remove(skill);
    }

    public SkillMastery getMastery(Skill skill){
        return skills.get(skill);
    }

    public void setCurrentSkill(Skill currentSkill) {
        if (Main.getCurrentSelectSkill().containsKey(player.getName())) {
            Bukkit.getScheduler().cancelTask(Main.getCurrentSelectSkill().get(player.getName()));
            Main.getCurrentSelectSkill().remove(player.getName());
        }
        if(currentSkill == null) {
            this.currentSkill = null;
        }
        else {
            this.currentSkill = currentSkill;
            showCurrentSkill();
        }
    }

    private void showCurrentSkill() {
        int i = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.plugin(), new Runnable() {
            int time = 0;
            @Override
            public void run() {
                if(time < 120) {
                    sendActionBar(player, "§6** La technique choisie est " + currentSkill.getName() + " §7("+getMastery(currentSkill).getName()+"§7)");
                    time += 2;
                }
                else {
                    currentSkill = null;
                }
            }
        }, 0, 20 * 2);
        Main.getCurrentSelectSkill().put(player.getName(), i);
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
