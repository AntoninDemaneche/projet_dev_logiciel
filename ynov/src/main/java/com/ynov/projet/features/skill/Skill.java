package com.ynov.projet.features.skill;

import com.ynov.projet.Main;
import com.ynov.projet.features.utils.ItemUtil;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;

public class Skill {
    private static Main main = Main.plugin();

    @Getter
    private String name; //Nom donné proprement

    @Getter
    private String nameInPlugin; //Nom sans espace pour les commandes (Ex: Mur de boue devient MurBoue)

    @Getter
    private int manaCost;

    @Getter
    private boolean needMastery;

    @Getter
    private SkillLevel level;

    @Getter
    private ItemStack item; //Item représentatif de la technique

    @Getter
    private String message;

    @Getter
    private ArrayList<String> commandList;

    @Getter
    private boolean needTarget;

    @Getter
    private boolean canBeFullMaster;

    @Getter
    private String element;

    @Getter
    private String infosup;

    @Getter
    private boolean skillVisibility;

    @Getter
    private static ArrayList<Skill> instanceList = new ArrayList<>();

    public Skill(String name, String nameInPlugin, int manaCost, boolean needMastery, SkillLevel level, String message, String lore, ArrayList<String> commandList, Material itemType, boolean needTarget, boolean canBeFullMaster, String infosup, boolean skillVisibility){
        this.name = name;
        this.nameInPlugin = nameInPlugin;
        this.manaCost = manaCost;
        this.needMastery = needMastery;
        this.level = level;
        this.message = message;
        this.element = name.split("-")[0];
        this.infosup = infosup;
        this.skillVisibility = skillVisibility;
        if (this.element.startsWith("§")){
            this.element = this.element.substring(2);
        }
        this.element = this.element.substring(0, this.element.length()-1);
        if(VoieNinja.getIDFromName(element) < 5 && VoieNinja.getIDFromName(element) > 0) {
            this.item = ItemUtil.createItemStack(Material.PAPER, 1, name, Arrays.asList(lore.split(";")), "seisan", element.toLowerCase()+"scroll");
        }
        else if(Clan.getIDFromName(element) != -2) {
            Clan clan = Clan.getFromID(Clan.getIDFromName(element));
            this.item = ItemUtil.createItemStack(Material.PAPER, 1, name, Arrays.asList(lore.split(";")), "seisan", clan.getTag());
        }
        else {
            this.item = ItemUtil.createItemStack(itemType, 1, name, Arrays.asList(lore.split(";")), "seisan", "rouleau" + this.element.toLowerCase());
        }
        if(commandList != null)
            this.commandList = commandList;
        else
            this.commandList = new ArrayList<>();

        this.needTarget = needTarget;
        this.canBeFullMaster = canBeFullMaster;


        instanceList.add(this);
    }


}
