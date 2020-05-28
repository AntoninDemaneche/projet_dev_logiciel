package com.ynov.projet.Features.PlayerData;

import com.ynov.projet.Features.Inventory.AbilityInventory;
import com.ynov.projet.Features.ability.Ability;
import com.ynov.projet.Features.skill.Skill;
import com.ynov.projet.Features.skill.SkillMastery;
import com.ynov.projet.Main;
import lombok.Getter;
import lombok.Setter;
import com.ynov.projet.Features.objectnum.Clan;
import com.ynov.projet.Features.objectnum.RPRank;
import com.ynov.projet.Features.objectnum.StyleCombat;
import com.ynov.projet.Features.objectnum.VoieNinja;
import com.ynov.projet.Features.objectnum.Teinte;
import com.ynov.projet.Features.objectnum.CouleurChakra;
import com.ynov.projet.Features.objectnum.Gender;
import com.ynov.projet.Features.objectnum.ChakraType;
import net.minecraft.server.v1_15_R1.ChatMessageType;
import net.minecraft.server.v1_15_R1.IChatBaseComponent;
import net.minecraft.server.v1_15_R1.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;




public class PlayerInfo {

    @Getter
    @Setter
    private Player player;

    @Getter
    private String uuid;

    @Getter
    @Setter
    private String id;

    @Getter
    @Setter
    private String apparenceprofil;

    @Getter
    private int mana;

    @Getter
    @Setter
    private int manamission;

    @Getter
    private int manamaze;
    @Getter
    @Setter
    private HashMap<Skill, SkillMastery> skills;

    @Getter
    private HashMap<Skill, Integer> rollBonus;

    @Getter
    private int maxMana;

    @Getter
    private Skill currentSkill;

    @Getter
    private RPRank rank;

    @Getter
    @Setter
    private ArrayList<Skill> favoriteList;

    @Getter
    @Setter
    Player target;

    @Getter
    PlayerClone playerClone;

    @Getter
    Clan clan;

    @Getter
    VoieNinja voieNinja;

    @Getter
    StyleCombat styleCombat;

    @Getter
    String chakraType;

    @Getter
    @Setter
    String appearance;

    @Getter
    @Setter
    int age;

    @Getter
    @Setter
    String attributClan;

    @Getter
    @Setter
    ArrayList<Ability> abilities;

    @Getter
    @Setter
    int points;

    @Getter
    @Setter
    String pointsAbilities;

    @Getter
    long delayPoints;

    @Getter
    @Setter
    int resistance;

    @Getter
    @Setter
    HashMap<String, Integer> resistancebonus;

    @Getter
    int ink;

    @Getter
    CouleurChakra couleurChakra;

    @Getter
    Teinte teinte;
    @Getter
    int transparence;

    @Getter
    @Setter
    String oldpos;

    @Getter
    @Setter
    Gender gender;

    @Getter
    Caract caract;

    @Getter
    int fuin_paper;

    @Getter
    int fuin_uzumaki;

    @Getter
    int fuin_lastday;

    @Getter
    @Setter
    int maskprofil;

    @Getter
    public static HashMap<String, PlayerInfo> instanceList = new HashMap<>();

    public PlayerInfo(Player p, int mana, int manamission, int manamaze, RPRank rank, HashMap<Skill, SkillMastery> skills, HashMap<Skill, Integer> rollBonus, ArrayList<Skill> favoriteList, Clan clan , String chakraType, int age, String appearance, VoieNinja voieNinja, StyleCombat styleCombat, ArrayList<Ability> abilities, String appearanceprofil, String attributClan, int points, String pointsAbilities, long delayPoints, HashMap<String, Integer> resistancebonus, int ink, CouleurChakra couleurChakra, Teinte teinte, String oldpos, Gender gender, int fuin_paper, int fuin_uzumaki, int fuin_lastday, int maskprofil){
        this.player = p;
        this.uuid = p.getUniqueId().toString();
        this.mana = mana;
        this.rank = rank;
        this.skills = skills;
        this.rollBonus = rollBonus;
        this.favoriteList = favoriteList;
        this.clan = clan;
        this.chakraType = chakraType;
        this.age = age;
        this.appearance = appearance;
        this.voieNinja = voieNinja;
        this.styleCombat = styleCombat;
        this.abilities = abilities;
        this.manamission = manamission;
        this.manamaze = manamaze;
        this.playerClone = new PlayerClone(this);
        this.apparenceprofil = appearanceprofil;
        this.attributClan = attributClan;
        this.points = points;
        this.pointsAbilities = pointsAbilities;
        this.resistance = (rank.getId()-1)*5 + ((manamaze+manamission)/100) + sommeres(resistancebonus) + resbonus();
        this.resistancebonus = resistancebonus;
        this.ink = ink;
        if(getNextDimanche(LocalDateTime.now()) > delayPoints) {
            this.delayPoints = getNextDimanche(LocalDateTime.now());
            this.points++;
            if(this.points != 0) {
                player.sendMessage("§b**Un point de compétence vous a été attribué.");
            }
        }
        else {
            this.delayPoints = delayPoints;
        }
        this.couleurChakra = couleurChakra;
        this.teinte = teinte;
        this.maxMana = manamission + manamaze + rank.getChakraRank() + bonusChakra();
        this.oldpos = oldpos;
        this.gender = gender;
        this.fuin_paper = fuin_paper;
        this.fuin_uzumaki = fuin_uzumaki;
        this.fuin_lastday = fuin_lastday;
        this.maskprofil = maskprofil;

        p.setExp(0);
        p.setLevel(mana);

        if(!instanceList.containsKey(uuid)) instanceList.put(uuid,this);
        this.transparence = calculTransparence();
        ajoutInstinct();
        caract = new Caract(this.abilities);
        FuinjutsuUzumaki();
    }
    public PlayerInfo(String id, int mana, int manamission, int manamaze, RPRank rank, HashMap<Skill, SkillMastery> skills, HashMap<Skill, Integer> rollBonus, ArrayList<Skill> favoriteList, Clan clan, String chakraType, int age, String appearance, VoieNinja voieNinja, StyleCombat styleCombat, ArrayList<Ability> abilities, String appearanceprofil, String attributClan, int points, String pointsAbilities, long delayPoints,HashMap<String, Integer> resistancebonus, int ink, CouleurChakra couleurChakra, Teinte teinte, String oldpos, Gender gender, int fuin_paper, int fuin_uzumaki, int fuin_lastday, int maskprofil){
        this.id = id;
        this.mana = mana;
        this.manamission = manamission;
        this.manamaze = manamaze;
        this.rank = rank;
        this.skills = skills;
        this.rollBonus = rollBonus;
        this.favoriteList = favoriteList;
        this.clan = clan;
        this.chakraType = chakraType;
        this.age = age;
        this.appearance = appearance;
        this.voieNinja = voieNinja;
        this.styleCombat = styleCombat;
        this.abilities = abilities;
        this.apparenceprofil = appearanceprofil;
        this.attributClan = attributClan;
        this.points = points;
        this.pointsAbilities = pointsAbilities;
        this.delayPoints = delayPoints;
        this.resistance = (rank.getId()-1)*5 + ((manamaze+manamission)/100) + sommeres(resistancebonus);
        this.resistancebonus = resistancebonus;
        this.ink = ink;
        this.couleurChakra = couleurChakra;
        this.teinte = teinte;
        this.transparence = calculTransparence();
        this.maxMana = manamission + manamaze + rank.getChakraRank() + bonusChakra();
        this.oldpos = oldpos;
        this.gender = gender;
        this.fuin_paper = fuin_paper;
        this.fuin_uzumaki = fuin_uzumaki;
        this.fuin_lastday = fuin_lastday;
        this.maskprofil = maskprofil;
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
        favoriteList.remove(skill);
    }

    public void updateAbility(Ability ability) {
        if(!abilities.contains(ability)) {
            player.sendMessage(ChatColor.GRAY + "Vous avez acquis la compétence : " + ChatColor.GOLD + ability.getName());
            abilities.add(ability);
        }
        ajoutInstinct();
        caract = new Caract(this.abilities);
    }

    public void updateAbility(Ability ability, CommandSender sender) {
        char nb =  ability.getName().toCharArray()[ability.getName().length()-1];
        ArrayList<Ability> abilityArrayList = new ArrayList<>();
        abilityArrayList.add(ability);
        if(Character.isDigit(nb)) {
            int nombre = (nb - '0');
            int min = 0;
            /* Si c'est un style de combat */
            String test = ability.getType();
            if(nombre > 2  && StyleCombat.getIDFromName(test) != -2) {
                min = 2;
                abilityArrayList.add(Ability.getByPluginName("style_de_combat_1"));
                abilityArrayList.add(Ability.getByPluginName("style_de_combat_2"));
            }
            for(int i = nombre; i > min; i--) {
                char[] name = ability.getNameInPlugin().toCharArray();
                name[name.length-1] = Character.forDigit(i, 10);
                Ability abilityadd = Ability.getByPluginName(new String(name));
                if(abilityadd != null) {
                    abilityArrayList.add(abilityadd);
                }
            }
        }
        for(Ability ability1 : abilityArrayList) {
            if (!abilities.contains(ability1)) {
                player.sendMessage(ChatColor.GRAY + "Vous avez acquis la compétence : " + ChatColor.GOLD + ability1.getName());
                abilities.add(ability1);
                sender.sendMessage(player.getName() + " §7a acquis la compétence : " + ChatColor.GOLD + ability1.getName());
            }
        }
        ajoutInstinct();
        caract = new Caract(this.abilities);
    }

    public void removeAbility(Ability ability) {
        player.sendMessage(ChatColor.GRAY + "Vous avez perdu la compétence : " + ChatColor.GOLD + ability.getName());
        abilities.remove(ability);
    }

    public void removeAbility(Ability ability, CommandSender sender) {
        player.sendMessage(ChatColor.GRAY + "Vous avez perdu la compétence : " + ChatColor.GOLD + ability.getName());
        abilities.remove(ability);
        sender.sendMessage("§cHRP : §7Le joueur a perdu la compétence : "+ability.getName());
        caract = new Caract(this.abilities);
        ajoutInstinct();
    }

    public int getLvL(String type) {
        // Si c'est un style
        int lvl = 0;
        boolean style = false;
        if(StyleCombat.getIDFromName(type) != -2) {
            style = true;
        }
        for (Ability ability : abilities) {
            if (type.equals(ability.getType())) {
                if (ability.getLvl() > lvl) {
                    lvl = ability.getLvl();
                }
            }
            if(style && (ability.getType().contains("style_de_combat"))) {
                if(ability.getLvl() > lvl) {
                    lvl = ability.getLvl();
                }
            }
        }
        return lvl;

    }

    public SkillMastery getMastery(Skill skill){
        return skills.get(skill);
    }

    public static PlayerInfo getPlayerInfo(Player p){
        return instanceList.get(p.getUniqueId().toString());
    }

    public static void replacePlayerInfo(Player p, PlayerInfo pInfo) {
        instanceList.put(p.getUniqueId().toString(), pInfo);
    }

    public void setRank(RPRank rank){
        this.rank = rank;
        player.sendMessage(ChatColor.GRAY + "Votre rang est désormais: " + ChatColor.GOLD + rank.getDisplayName());
    }

    public void increaseBonus(Skill skill){
        int bonus = 0;

        if(rollBonus.containsKey(skill))
            bonus = rollBonus.get(skill);

        bonus = bonus + 1;

        if(bonus > skill.getLevel().getMaxBonus())
            bonus = skill.getLevel().getMaxBonus();

        rollBonus.put(skill, bonus);
    }

    public void setBonus(Skill skill, int bonus){
        rollBonus.put(skill, bonus);
    }

    public void addManaMax(int amount) {
        this.maxMana += amount;
    }

    public void removeManaMax(int amount) {
        this.maxMana -= amount;
    }

    public void destroy(){
        instanceList.remove(this.uuid);
    }

    public void setClan(Clan clan){
        player.sendMessage(ChatColor.GRAY + "Votre clan est désormais " + clan.getName());
        this.clan = clan;
    }

    public void setVoieNinja(VoieNinja voieNinja){
        player.sendMessage(ChatColor.GRAY + "Votre voie ninja est désormais " + voieNinja.getName());
        this.voieNinja = voieNinja;
    }

    public void setStyleCombat(StyleCombat styleCombat){
        player.sendMessage(ChatColor.GRAY + "Votre style de combat est désormais " + styleCombat.getName());
        this.styleCombat = styleCombat;
    }

    public void setInk(int ink) {
        if(ink < 0)
            ink = 0;
        player.sendMessage("§7Vous avez désormais §6"+ink+" §7doses d'encre.");
        this.ink = ink;
    }

    public void addChakraType(String type){
        if(type != null) {
            player.sendMessage(ChatColor.GRAY + "Votre nature de chakra est désormais composée du " + ChakraType.getPrettyName(type));
            chakraType = chakraType.concat(type+";");
        }
    }

    public void reset() {
        mana = 100;
        manamission = 0;
        manamaze = 0;
        currentSkill = null;
        skills.clear();
        rank = RPRank.STUDENT;
        rollBonus.clear();
        clan = Clan.INDEFINI;
        chakraType = "";
        age = 15;
        voieNinja = VoieNinja.INDEFINI;
        styleCombat = StyleCombat.INDEFINI;
        abilities.clear();
        updateAbility(Ability.getByPluginName("vitesse_1"));
        updateAbility(Ability.getByPluginName("force_1"));
        updateAbility(Ability.getByPluginName("perception_vitesse_3"));
        apparenceprofil = "";
        attributClan = "";
        points = 0;
        pointsAbilities = "";
        delayPoints = getNextDimanche(LocalDateTime.now());
        resistance = 0;
        ink = 0;
        resistancebonus.clear();
        player.kickPlayer("§6Réinitialisation de votre fiche personnage.");
    }

    public void removeChakraType(ChakraType type) {
        if(type != null) {
            String chakras = "";
            for(String chakra : chakraType.split(";")) {
                ChakraType chakratype = ChakraType.fromName(chakra);
                if(chakratype != null) {
                    if (chakratype.getName().equals(type.getName())) {
                        player.sendMessage(ChatColor.GRAY + "Votre nature de chakra n'est plus composée du " + type.getName());
                    } else {
                        chakras = chakras.concat(chakra+";");
                    }
                }
            }
            chakraType = chakras;
        }
    }

    public String getChakraList() {
        String s = "";
        for(String chakra : chakraType.split(";")) {
            if(chakra != null && !chakra.equals("")) {
                int nb = 0;
                String name = "ERREUR";
                if (chakra.contains("_")) {
                    nb = Integer.parseInt(chakra.split("_")[1]);
                    name = chakra.split("_")[0];
                }
                else {
                    name = chakra;
                }
                s = s.concat(Objects.requireNonNull(ChakraType.fromName(name)).getName() + " §6(-" + nb + "%)§7, ");
            }
        }
        if(s.length() == 0) {
            s = "§7Non défini";
        }
        else {
            s = s.substring(0, s.length()-2);
        }

        return s;
    }

    public boolean hasChakra(ChakraType type) {
        for(String chakra : chakraType.split(";")) {
            if(chakra.contains("_")) {
                chakra = chakra.split("_")[0];
            }
            if(chakra.equals(type.getName().substring(2))) {
                return true;
            }
        }
        return false;
    }

    public void updateChakra() {
        this.maxMana = manamission + manamaze + rank.getChakraRank() + bonusChakra();
    }

    public int getPointsToAbility(String name) {
        if(pointsAbilities != null && pointsAbilities.contains(";")) {
            String[] points = pointsAbilities.split(";");
            for (String ability : points) {
                String nameability = ability.split(",")[0];
                if (nameability.equals(name)) {
                    return Integer.parseInt(ability.split(",")[1]);
                }
            }
        }
        return 0;
    }

    public void deletePointstoAbilities(Ability ability) {
        if(this.pointsAbilities != null) {
            String list = "";
            for(String s : pointsAbilities.split(";")) {
                if(s != null && !s.equals(" ") && !s.equals("") && !s.contains(ability.getNameInPlugin())) {
                    list = list.concat(s+";");
                }
            }
            this.pointsAbilities = list;
        }

    }

    public int getPointsUsed() {
        int nb = 0;
        for(Ability ability : abilities) {
            //1ère sécurité

            if(ability.getPts() >= 0 && formatageGivenAbilities(ability.getGivenAbilities())) {
                nb += ability.getPts();
            }
        }

        // Seconde sécurité
        if(nb < 0) {
            nb = 0;
        }
        return nb;
    }

    private boolean formatageGivenAbilities(String givenAbilities) {
        if(givenAbilities != null && !givenAbilities.equals("")) {
            for (String ability : givenAbilities.split(";")) {
                Ability ab = Ability.getByPluginName(ability);
                if(ab != null && abilities.contains(ab)) {
                    return false;
                }
            }
        }
        return true;
    }

    public void addgiveAbilities(String giveAbilities) {
        if(giveAbilities != null && !giveAbilities.equals("")) {
            for (String ability : giveAbilities.split(";")) {
                Ability ab = Ability.getByPluginName(ability);
                if(ab != null && !abilities.contains(ab)) {
                    abilities.add(ab);
                }
            }
        }
    }

    public void removegiveAbilities(String giveAbilities) {
        if(giveAbilities != null && !giveAbilities.equals("")) {
            for (String ability : giveAbilities.split(";")) {
                Ability ab = Ability.getByPluginName(ability);
                if(ab != null) {
                    abilities.remove(ab);
                }
            }
        }
    }

    public boolean hasReqAbilities(String Reqabilities) {
        if(!Reqabilities.equals("none")) {
            for (String ability : Reqabilities.split(";")) {
                if (!this.abilities.contains(Ability.getByPluginName(ability))) {
                    return false;
                }
            }
        }
        return true;
    }

    public int incrementePointsAbility(Ability ability, int val) {
        String list = "";
        int i = 0;
        if(this.pointsAbilities != null && this.pointsAbilities.contains(";")) {
            for (String key : this.pointsAbilities.split(";")) {
                if (!key.contains(ability.getNameInPlugin())) {
                    list = list.concat(key+";");
                } else {
                    i = Integer.parseInt(key.split(",")[1]);
                }
            }
        }
        i = i + val;
        list = list.concat(ability.getNameInPlugin()+","+i+";");
        if(val == 1) {
            this.player.sendMessage("§b** Vos entraînements ont porté leur fruit. La compétence §6"+ability.getName()+" §bvous est un peu plus accessible.");
        }
        else {
            this.player.sendMessage("§b** Vos entrainements n'ont servi à rien. OU UN MJ VOUS A PRANK PTDR");
        }
        this.pointsAbilities = list;
        return i;
    }

    public static long getNextDimanche(LocalDateTime currentTime) {
        // Si c'est après le 4h du mat / dimanche
        LocalDateTime toDateTime = LocalDateTime.of(2019, 10, 6, 4, 0, 0);

        if(currentTime.getDayOfWeek() == DayOfWeek.SUNDAY && currentTime.getHour() >= 4) {
            currentTime = currentTime.plusDays(7);
        }
        else {
            int nbjour = currentTime.getDayOfWeek().getValue();
            currentTime = currentTime.plusDays(7-nbjour);
        }

        currentTime = currentTime.withHour(4).withMinute(0).withSecond(0);
        return toDateTime.until(currentTime, ChronoUnit.WEEKS);

    }

    public static int getLastDay(LocalDateTime currentTime) {
        // Si c'est après le 4h du mat / dimanche
        // Si c'est après le 4h du mat / 20 mars
        LocalDateTime toDateTime = LocalDateTime.of(2020, 3, 20, 4, 0, 0);
        currentTime = currentTime.withHour(4).withMinute(0).withSecond(0);
        return (int) toDateTime.until(currentTime, ChronoUnit.DAYS);
    }

    public static void getAppareanceBook(String[] desc, Player p) {
        AbilityInventory.openInBook(p, desc);
    }

    private int sommeres(HashMap<String, Integer> res) {
        AtomicInteger total = new AtomicInteger();
        res.forEach((string, somme) -> total.set(total.get() + somme));
        return total.get();
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

    public int resbonus() {
        int bonus = 0;
        Ability ability = Ability.getByPluginName("conviction");
        if(ability != null && abilities.contains(ability)) {
            bonus += 7;
        }
        if(clan.getName().contains("Ermite") && getLvL(clan.getName()) >= 10) {
            bonus += 10;
        }
        if(clan.getName().contains("Ninshu") && getLvL(clan.getName()) >= 10) {
            bonus += 10;
        }
        if(clan.getName().contains("Uchiwa") && getLvL(clan.getName()) >= 8) {
            bonus += 5;
        }
        if(clan.getName().contains("Hozuki") && getLvL(clan.getName()) >= 10) {
            bonus += 5;
        }
        return bonus;
    }
    public void sendActionBar(Player player, String message) {
        CraftPlayer p = (CraftPlayer) player;
        IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}");
        PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, ChatMessageType.GAME_INFO);
        p.getHandle().playerConnection.sendPacket(ppoc);
    }

    private void ajoutInstinct() {
        Ability instinct_1 = Ability.getByPluginName("instinct_&_experience_1");
        Ability instinct_2 = Ability.getByPluginName("instinct_&_experience_2");
        Ability instinct_3 = Ability.getByPluginName("instinct_&_experience_3");
        Ability instinct_4 = Ability.getByPluginName("instinct_&_experience_4");
        int nbpoints = getPointsUsed() + getPoints();
        if(getLvL(styleCombat.getName()) >= 3) {
            if(!abilities.contains(instinct_1)) {
                abilities.add(instinct_1);
                player.sendMessage(ChatColor.GRAY + "Vous avez acquis la compétence : " + ChatColor.GOLD + instinct_1.getName());
            }
        }
        else {
            abilities.remove(instinct_1);
        }
        if(nbpoints >= 60) {
            if(!abilities.contains(instinct_4)) {
                abilities.add(instinct_4);
                player.sendMessage(ChatColor.GRAY + "Vous avez acquis la compétence : " + ChatColor.GOLD + instinct_4.getName());
            }
        }
        else {
            abilities.remove(instinct_4);
        }
        if(nbpoints >= 40) {
            if(!abilities.contains(instinct_3)) {
                abilities.add(instinct_3);
                player.sendMessage(ChatColor.GRAY + "Vous avez acquis la compétence : " + ChatColor.GOLD + instinct_3.getName());
            }
        }
        else {
            abilities.remove(instinct_3);
        }
        if(nbpoints >= 20) {
            if(!abilities.contains(instinct_2)) {
                abilities.add(instinct_2);
                player.sendMessage(ChatColor.GRAY + "Vous avez acquis la compétence : " + ChatColor.GOLD + instinct_2.getName());
            }
        }
        else {
            abilities.remove(instinct_2);
        }
        updateChakra();
    }

    public void setCouleurChakra(CouleurChakra couleurChakra) {
        this.couleurChakra = couleurChakra;
        player.sendMessage("§b** La couleur de votre chakra est désormais : "+this.couleurChakra.getName());
    }

    public void setTeinte(Teinte teinte) {
        this.teinte = teinte;
        player.sendMessage("§b** La teinte de votre chakra est désormais : "+this.teinte.getName());
    }

    public int bonusChakra() {
        int bonus = 0;
        bonus += Math.min(100, 25 * getLvL("Instinct et expérience"));
        if(age >= 17) {
            bonus += 25;
        }
        if(age >= 19) {
            bonus += 25;
        }
        if(age >= 21) {
            bonus += 25;
        }
        if(age >= 23) {
            bonus += 25;
        }
        if(age >= 35) {
            bonus +=100;
        }
        if(age >= 45) {
            bonus += 200;
        }
        if(getLvL(styleCombat.getName()) >= 4) {
            bonus += 25;
        }
        if(getLvL(styleCombat.getName()) >= 5) {
            bonus += 25;
        }
        if(getLvL(styleCombat.getName()) >= 6) {
            bonus += 50;
        }
        return bonus;
    }

    private int calculTransparence() {
        int lvl = 0;
        if(this.maxMana >= 500) {
            lvl++;
        }
        if(this.maxMana >= 1000) {
            lvl++;
        }
        if(getLvL(styleCombat.getName()) >= 6 && getLvL(clan.getName()) >= 6 && getLvL(voieNinja.getName()) >= 6) {
            lvl++;
        }
        if(getLvL(styleCombat.getName()) == 10 || getLvL(clan.getName()) == 10 || getLvL(voieNinja.getName()) == 10) {
            lvl++;
        }
        if(getLvL(styleCombat.getName()) >= 10 && getLvL(clan.getName()) >= 10 && getLvL(voieNinja.getName()) >= 10) {
            lvl++;
        }
        if(getLvL("Force") == 4 && getLvL("Vitesse") == 3) {
            lvl++;
        }
        if(getLvL("Instinct et expérience") == 5) {
            lvl++;
        }
        if(this.resistance >= 100) {
            lvl++;
        }
        return lvl;
    }

    public int getManaToTake(String element, int manaCost) {
        for(String chakratype : chakraType.split(";")) {
            if(chakratype.contains("_")) {
                String chakra = chakratype.split("_")[0];
                int prct = Integer.parseInt(chakratype.split("_")[1]);
                if(chakra.equals(element)) {
                    return manaCost - manaCost * prct/100;
                }
            }
        }
        return manaCost;
    }

    private void FuinjutsuUzumaki() {
        // Si il est + que lvl 4 en Fuin
        int lvl = getLvL(this.voieNinja.getName());
        if(lvl >= 4 && this.voieNinja.getId() == 1) {
            // Si c 1 autre jour
            int lastday = getLastDay(LocalDateTime.now());
            if(fuin_lastday < lastday) {
                lvl = lvl - 1;
                fuin_uzumaki += lvl * (lastday - this.fuin_lastday);
                fuin_lastday = lastday;
                fuin_uzumaki = Math.min(lvl*lvl, fuin_uzumaki);
            }
            
        }
    }

    public void addPaperUzumaki() {
        this.fuin_paper += this.fuin_uzumaki;
        this.fuin_uzumaki = 0;
    }

    public void usePaper() {
        this.fuin_paper--;
        player.sendMessage("§b** Vous avez désormais "+this.fuin_paper+" feuilles de Seju.");
    }

    public PlayerInfo clone(String id) {
        return new PlayerInfo(id, this.mana, this.manamission, this.manamaze, this.rank, (HashMap<Skill, SkillMastery>)this.skills.clone(), (HashMap<Skill, Integer>)this.rollBonus.clone(), (ArrayList<Skill>)this.favoriteList.clone(), this.clan, this.chakraType, this.age, this.appearance, this.voieNinja, this.styleCombat, (ArrayList<Ability>)this.abilities.clone(), this.apparenceprofil, this.attributClan, this.points, this.pointsAbilities, this.delayPoints,(HashMap<String, Integer>)resistancebonus.clone(), this.ink,this.couleurChakra, this.teinte, this.oldpos, this.gender, this.fuin_paper, this.fuin_uzumaki, this.fuin_lastday, this.maskprofil);
    }

    public PlayerInfo clone(Player p) {
        return new PlayerInfo(p, this.mana, this.manamission, this.manamaze, this.rank, (HashMap<Skill, SkillMastery>) this.skills.clone(), (HashMap<Skill, Integer>) this.rollBonus.clone(), (ArrayList<Skill>) this.favoriteList.clone(), this.clan, this.chakraType, this.age, this.appearance, this.voieNinja, this.styleCombat, (ArrayList<Ability>) this.abilities.clone(), this.apparenceprofil, this.attributClan, this.points, this.pointsAbilities, this.delayPoints, (HashMap<String, Integer>) resistancebonus.clone(), this.ink, this.couleurChakra, this.teinte, this.oldpos, this.gender, this.fuin_paper, this.fuin_uzumaki, this.fuin_lastday, this.maskprofil);
    }
}