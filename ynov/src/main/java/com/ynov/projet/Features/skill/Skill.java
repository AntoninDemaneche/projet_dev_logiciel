package com.ynov.projet.Features.skill;

import com.ynov.projet.Features.objectnum.Clan;
import com.ynov.projet.Features.objectnum.VoieNinja;
import com.ynov.projet.Features.utils.ItemUtil;
import lombok.Getter;
import com.ynov.projet.Features.PlayerData.PlayerInfo;
import com.ynov.projet.Main;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Skill{

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
    private String mudras;
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

    public Skill(String name, String nameInPlugin, int manaCost, boolean needMastery, SkillLevel level, String message, String lore, String mudras, ArrayList<String> commandList, Material itemType, boolean needTarget, boolean canBeFullMaster, String infosup, boolean skillVisibility){
        this.name = name;
        this.nameInPlugin = nameInPlugin;
        this.manaCost = manaCost;
        this.needMastery = needMastery;
        this.level = level;
        this.message = message;
        this.mudras = mudras;
        this.element = name.split("-")[0];
        this.infosup = infosup;
        this.skillVisibility = skillVisibility;
        if (this.element.startsWith("§")){
            this.element = this.element.substring(2);
        }
        this.element = this.element.substring(0, this.element.length()-1);
        if(VoieNinja.getIDFromName(element) < 5 && VoieNinja.getIDFromName(element) > 0) {
            this.item = ItemUtil.createItemStack(Material.PAPER, 1, name, Arrays.asList(lore.split(";")), "seisan", element.toLowerCase()+"_scroll");
        }
        else if(Clan.getIDFromName(element) != -2) {
            Clan clan = Clan.getFromID(Clan.getIDFromName(element));
            this.item = ItemUtil.createItemStack(Material.PAPER, 1, name, Arrays.asList(lore.split(";")), "seisan", clan.getTag());
        }
        else {
            this.item = ItemUtil.createItemStack(itemType, 1, name, Arrays.asList(lore.split(";")), "seisan", "rouleau_" + this.element.toLowerCase());
        }
        if(commandList != null)
            this.commandList = commandList;
        else
            this.commandList = new ArrayList<>();

        this.needTarget = needTarget;
        this.canBeFullMaster = canBeFullMaster;


        instanceList.add(this);
    }

    public void launch(PlayerInfo playerInfo, Object... objects){
        Player p = playerInfo.getPlayer();
        if(needTarget && (playerInfo.getTarget() == null || !playerInfo.getTarget().isOnline())) {
            p.sendMessage("§cHRP : §7Votre cible n'est pas connectée ou n'existe pas !");
            playerInfo.setCurrentSkill(null);
            return;
        }

        //Vérifications relatives au mana
        if(manaToTake(playerInfo) > playerInfo.getMana()) {
            playerInfo.getPlayer().sendMessage("§b** Vous n'avez pas assez de chakra. **");
            playerInfo.setCurrentSkill(null);
            return;
        }

        else if(playerInfo.getFuin_paper() == 0 && playerInfo.getInk() < playerInfo.getCurrentSkill().getInk(playerInfo)) {
            playerInfo.getPlayer().sendMessage("§cHRP : §7Votre personnage n'a pas assez d'encre pour sceller le symbole.");
            playerInfo.setCurrentSkill(null);
            return;
        }
        if(playerInfo.getCurrentSkill().getName().substring(2).startsWith("Fuinjutsu")) {
            if(!TrySceaux(playerInfo.getCurrentSkill(), playerInfo)) {
                playerInfo.getPlayer().sendMessage("§b** Vous n'avez pas réussi à sceller le symbole. **");
                playerInfo.setCurrentSkill(null);
                return;
            }
        }
        else if(!(playerInfo.getSkills().get(playerInfo.getCurrentSkill()) == SkillMastery.LEARNED || TryJutsu(playerInfo.getCurrentSkill(), playerInfo))) {
            playerInfo.setCurrentSkill(null);
            return;
        }

        //Suppression de l'encre
        if(playerInfo.getCurrentSkill().getName().substring(2).startsWith("Fuinjutsu")) {
            if(playerInfo.getFuin_paper() != 0) {
                playerInfo.usePaper();
            }
            else {
                playerInfo.setInk(playerInfo.getInk() - playerInfo.getCurrentSkill().getInk(playerInfo));
            }
        }
        //Envoi du message d'encadrement
        String message = "";
        if (playerInfo.getSkills().get(playerInfo.getCurrentSkill()) == SkillMastery.ONEHAND) {
            message += "§7[Une main] ";
        }
        message += "§c** " + p.getDisplayName() + " §créalise la technique "+name;
        int range = 25;
        takeMana(playerInfo);
        String newname = name;
        newname = newname.concat(" §f["+this.level.getCharName()+"]");


        TextComponent messagecomponent = new TextComponent(message);
        messagecomponent.setColor(net.md_5.bungee.api.ChatColor.getByChar(name.toCharArray()[1]));
        BaseComponent[] texte = new BaseComponent[]{
                new TextComponent(ItemUtil.convertItemStackToJsonRegular(ItemUtil.createItemStack(Material.BOOK, 1, newname, getLore(p)))) // The only element of the hover events basecomponents is the item json
        };

        affichejutsu(p, playerInfo, range, messagecomponent, texte, this.skillVisibility, this.needTarget, playerInfo.getTarget());
        //Lancement de la technique
        ArrayList<String> commandToRun = (ArrayList<String>) commandList.clone();
        runCommands(commandToRun, 0, p);

        playerInfo.setCurrentSkill(null);
    }

    public void takeMana(PlayerInfo playerInfo){
        int manaTaken = manaToTake(playerInfo);
        playerInfo.removeMana(manaTaken);
    }

    public int manaToTake(PlayerInfo pInfo){
        return pInfo.getManaToTake(element, manaCost);
    }

    public static Skill getByPluginName(String s){
        return instanceList.stream().filter(skill -> skill.nameInPlugin.equals(s)).findFirst().orElse(null);
    }

    public static Skill getByRealName(String s){
        return instanceList.stream().filter(skill -> ChatColor.stripColor(skill.name).equals(ChatColor.stripColor(s))).findFirst().orElse(null);
    }

    public void runCommands(ArrayList<String> commandsToRun, int position, Player p){
        for(int i = position; i<commandsToRun.size(); i++){
            String command = commandsToRun.get(i);
            if(command.startsWith("/")){
                String formatedCommand = formatCommand(command, p);
                if(p.isOnline()) {
                    Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), formatedCommand);
                }
            }else if(command.startsWith("delay")){
                if(isDouble(command.split(" ")[1])){
                    double delay = Double.parseDouble(command.split(" ")[1]);
                    runCommandsLater(commandsToRun, i +1, p, delay);
                    break;
                } else if(StringUtils.isNumeric(command.split(" ")[1])){
                    int delay = Integer.parseInt(command.split(" ")[1]);
                    runCommandsLater(commandsToRun, i + 1, p, delay);
                    break;
                }
            }else if(command.startsWith("checkworld")){
                if(command.split(" ").length > 1){
                    String worldName = command.split(" ")[1];
                    if(!p.getLocation().getWorld().getName().equalsIgnoreCase(worldName)){
                        p.sendMessage(ChatColor.RED + "Vous n'êtes pas dans le bon monde !");
                        break;
                    }
                }
            }
        }
    }

    public String formatCommand(String s, Player p){
        PlayerInfo pInfo = PlayerInfo.getPlayerInfo(p);
        String formated = "";
        formated += s.replace("%player%", p.getName());
        formated = formated.replace("%displayname%", p.getDisplayName());

        if(pInfo.getTarget() != null) {
            formated = formated.replace("%target%", pInfo.getTarget().getName());
            formated = formated.replace("%targetname%", pInfo.getTarget().getDisplayName());
        }
        formated = formated.replace("%lvl%", Integer.toString(pInfo.getMaxMana()));
        return formated.substring(1);
    }

    public String formatEncaMessage(String message, Player p){
        String formated = "";
        PlayerInfo pInfo = PlayerInfo.getPlayerInfo(p);

        formated += message.replace("%displayname%", p.getDisplayName()+"§7");

        if(pInfo.getTarget() != null)
            formated = formated.replace("%targetname%", pInfo.getTarget().getDisplayName()+"§7");

        if(pInfo.getGender().getId() == 1) {
            formated = formated.replace(" Il ", " Elle ");
            formated = formated.replace(" il ", " elle ");
            formated = formated.replace(" lui ", " elle ");
            formated = formated.replace(" Lui ", " Elle ");
        }
        else if (pInfo.getGender().getId() == 2) {
            formated = formated.replace(" il ", " iel ");
            formated = formated.replace(" Il ", " Iel ");
            formated = formated.replace(" lui ", " iel ");
            formated = formated.replace(" Lui ", " Iel ");
        }
        return formated;
    }

    public void runCommandsLater(ArrayList<String> commandsToRun, int position, Player p, int delay){
        Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
            @Override
            public void run() {
                runCommands(commandsToRun, position, p);
            }
        }, 20*delay); //Delay in seconds
    }

    public void runCommandsLater(ArrayList<String> commandsToRun, int position, Player p, double delay){
        Double decimalDelay = 20*delay; //20 game ticks = 1 sec
        int integerDelay = decimalDelay.intValue();
        Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
            @Override
            public void run() {
                runCommands(commandsToRun, position, p);
            }
        }, integerDelay); //Delay in seconds
    }

    public boolean isInt(String s){
        try{
            Integer.parseInt(s);
        }catch (NumberFormatException e){
            return false;
        }
        return true;
    }

    public boolean isDouble(String s){
        try{
            Double.parseDouble(s);
        }catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

    public boolean TryJutsuOneHand(Skill skill, PlayerInfo pInfo) {
        boolean success = true;
        Player p = pInfo.getPlayer();
        int bonus = pInfo.getRollBonus().get(skill) != null ? pInfo.getRollBonus().get(skill) : 0;
        switch (rollMasteryOneHand(skill.getLevel(), bonus, pInfo.getSkills().get(skill) == SkillMastery.ONEHAND)) {
            case MASTERED:
                if(pInfo.getMastery(skill) != SkillMastery.ONEHAND) {
                    pInfo.updateSkill(skill, SkillMastery.ONEHAND);
                }
                pInfo.setBonus(skill, 0);
                break;
            case SUCCESS:
                p.sendMessage(ChatColor.AQUA + "** Vous avez réussi à lancer la technique à une main, mais vous ne la maitrisez pas encore. **");
                pInfo.increaseBonus(skill);
                break;
            case FAIL:
                p.sendMessage(ChatColor.RED + "** Vous n'avez pas réussi à lancer la technique à une main.**");
                takeMana(pInfo);
                pInfo.increaseBonus(skill);
                success = false;
                break;

        }
        return success;
    }

    public boolean TryJutsu(Skill skill, PlayerInfo pInfo) {
        boolean success = true;
        Player p = pInfo.getPlayer();
        int bonus = pInfo.getRollBonus().get(skill) != null ? pInfo.getRollBonus().get(skill) : 0;
        switch (rollMastery(skill.getLevel(), bonus, pInfo.getSkills().get(skill) == SkillMastery.LEARNED  || pInfo.getSkills().get(skill) == SkillMastery.ONEHAND)) {
            case MASTERED:
                if (pInfo.getMastery(skill) == SkillMastery.UNLEARNED) {
                    pInfo.updateSkill(skill, SkillMastery.LEARNED);
                }
                pInfo.setBonus(skill, 0);
                break;
            case SUCCESS:
                p.sendMessage(ChatColor.AQUA + "** Vous avez réussi à lancer la technique, mais vous ne la maitrisez pas encore. **");
                pInfo.increaseBonus(skill);
                break;
            case FAIL:
                p.sendMessage(ChatColor.RED + "** Vous n'avez pas réussi à maîtriser la technique **");
                takeMana(pInfo);
                pInfo.increaseBonus(skill);
                success = false;
                break;
        }
        return success;
    }

    private boolean TrySceaux(Skill skill, PlayerInfo pInfo) {
        boolean success = true;
        Player p = pInfo.getPlayer();
        int bonus = pInfo.getRollBonus().get(skill) != null ? pInfo.getRollBonus().get(skill) : 0;
        switch (rollMastery(skill.getLevel(), bonus, pInfo.getSkills().get(skill) == SkillMastery.LEARNED)) {
            case MASTERED:
                if (pInfo.getMastery(skill) == SkillMastery.UNLEARNED) {
                    pInfo.updateSkill(skill, SkillMastery.LEARNED);
                }
                pInfo.setBonus(skill, 0);
                break;
            case SUCCESS:
            case FAIL:
                takeMana(pInfo);
                pInfo.increaseBonus(skill);
                success = false;
                break;
        }
        return success;
    }

    private MasteryRollResult rollMastery(SkillLevel level, int bonus, boolean alreadyLearned){
        Random r = new Random();
        int i = r.nextInt(99) + 1; //Roll de 1 à 100
        i = i + bonus; //Ajout de l'éventuel bonus

        if(alreadyLearned || i >= level.getCritRoll())
            return MasteryRollResult.MASTERED;
        if(i >= level.getRequiredRoll())
            return MasteryRollResult.SUCCESS;
        return MasteryRollResult.FAIL;
    }

    private MasteryRollResult rollMasteryOneHand(SkillLevel level, int bonus, boolean alreadyOneHand) {
        Random r = new Random();
        int i = r.nextInt(99)+1;
        i = i + bonus;
        if(alreadyOneHand || i >= level.getCritRollOneHand())
            return MasteryRollResult.MASTERED;
        if(i >= level.getRequiredRollOneHand())
            return MasteryRollResult.SUCCESS;
        return MasteryRollResult.FAIL;
    }

    public int getInk(PlayerInfo pInfo) {
        if(this.getName().substring(2).startsWith("Fuinjutsu")) {
            if (pInfo.getVoieNinja().getId() == 1) {
                return 1;
            } else {
                return 2;
            }
        }
        return 0;
    }

    public ArrayList<String> getLore(Player p) {
        ArrayList<String> lore = new ArrayList<>();
        int taille = message.length();
        int tailledef = taille;
        int divi = 1;
        while(tailledef > 50) {
            divi++;
            tailledef = taille/divi;
        }
        int borneinf = 0;
        for(int i = 0; i < divi; i++) {
            int bornesupp = tailledef * (i+1);
            while(bornesupp < message.length() && message.charAt(bornesupp) != ' ') {
                bornesupp++;
            }
            if(divi-1 == i) {
                bornesupp = taille;
            }
            while (message.substring(borneinf, bornesupp).startsWith(" ")) {
                borneinf++;
            }
            lore.add("§7"+formatEncaMessage(message.substring(borneinf, bornesupp),p));
            borneinf = bornesupp;
        }
        return lore;
    }
    private enum MasteryRollResult{
        FAIL,
        SUCCESS,
        MASTERED
    }

    public static void affichejutsu(Player p, PlayerInfo pInfo, int range, TextComponent messagecomponent, BaseComponent[] texte, boolean skillVisibility, boolean needTarget, Player target) {
        messagecomponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, texte));
        p.spigot().sendMessage(messagecomponent);

        for (Entity entity : pInfo.getPlayer().getNearbyEntities(range, range, range)) {
            if (entity instanceof Player) {
                if (skillVisibility || entity.isOp() || (needTarget && entity.getName().equals(target.getName()))) {
                    entity.spigot().sendMessage(messagecomponent);
                }
            }
        }
    }
}
