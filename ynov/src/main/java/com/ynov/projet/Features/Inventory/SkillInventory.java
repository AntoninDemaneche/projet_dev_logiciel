package com.ynov.projet.Features.Inventory;

import com.ynov.projet.Features.PlayerData.PlayerInfo;
import com.ynov.projet.Features.objectnum.ChakraType;
import com.ynov.projet.Features.skill.Skill;
import com.ynov.projet.Features.skill.SkillMastery;
import com.ynov.projet.Features.utils.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;


public class SkillInventory {

    public static Inventory getMainInventory(PlayerInfo pInfo, int page, String element){
        Inventory inv;
        if(element.equals("Fuinjutsu")) {
            inv = Bukkit.createInventory(pInfo.getPlayer(), 18, "§6Fuinjutsu : §7Choix du type de sceau");
            inv.setItem(2, ItemUtil.createItemStack(Material.LEVER, 1, "§6Jôken", Arrays.asList("§7Cliquez ici pour obtenir les informations", "§7sur les symboles activeurs (§6Jôken§7).")));
            inv.setItem(4, ItemUtil.createItemStack(Material.IRON_SWORD, 1, "§6Saishô", Arrays.asList("§7Cliquez ici pour obtenir les informations", "§7sur les symboles principaux (§6Saishô§7).")));
            inv.setItem(6, ItemUtil.createItemStack(Material.SHIELD, 1, "§6Kinaï", Arrays.asList("§7Cliquez ici pour obtenir les informations", "§7sur les symboles secondaires (§6Kinaï§7)")));
            inv.setItem(13, ItemUtil.createItemStack(Material.ARROW, 1, "§6Retour"));
        }
        else {
            inv = Bukkit.createInventory(pInfo.getPlayer(), 36, "§6Jutsu : " + element);

            ArrayList<Skill> skillList = new ArrayList<>(pInfo.getSkills().keySet());
            ArrayList<Skill> realSkillList = new ArrayList<>();
            /* Tri des jutsus en fonction de l'élément */
            for (Skill skill : skillList) {
                if (skill.getElement().equals(element)) {
                    realSkillList.add(skill);
                }
            }
            getListeSceauxJutsu(pInfo, page, inv, realSkillList);
            inv.setItem(35, ItemUtil.createItemStack(Material.ARROW, 1, "§6Retour"));
        }
        return inv;
    }

    public static Inventory getMainInventory(PlayerInfo pInfo, int page){
        Inventory inv = Bukkit.createInventory(pInfo.getPlayer(), 36, "§6Jutsu : §7Tous");

        ArrayList<Skill> skillList = new ArrayList<>(pInfo.getSkills().keySet());
        getListeSceauxJutsu(pInfo, page, inv, skillList);
        return inv;
    }


    public static Inventory getSkillSelectInventory(Skill skill, PlayerInfo pInfo){
        Inventory inv = Bukkit.createInventory(pInfo.getPlayer(), 9, "§8Technique : " + skill.getName());

        SkillMastery mastery = pInfo.getMastery(skill);
        if(pInfo.getPlayer().isOp()) {
            inv.setItem(0, ItemUtil.createItemStack(Material.BOOK, 1, "§2Parchemin de la technique", Arrays.asList("§7Ce bouton permet de donner un parchemin", "§7pour le jutsu : "+skill.getName()), "seisan", "rouleau_2"));
        }
        inv.setItem(1, ItemUtil.createItemStack(Material.CLOCK, 1, "§6Mudras de la technique :", getMudras(skill.getMudras())));
        inv.setItem(2, skill.getItem());
        if(skill.getName().substring(2).startsWith("Fuinjutsu")) {
            int ink = skill.getInk(pInfo);
            inv.setItem(3, ItemUtil.createItemStack(Material.EXPERIENCE_BOTTLE, 1, "§6" + mastery.getName(), Arrays.asList("§7" + skill.getLevel().getName(), "§6Coût de scellemment: §7" + skill.manaToTake(pInfo), "§6Coût en encre : §7"+ink)));
        }
        else {
            inv.setItem(3, ItemUtil.createItemStack(Material.EXPERIENCE_BOTTLE, 1, "§6" + mastery.getName(), Arrays.asList("§7" + skill.getLevel().getName(), "§6Coût: §7" + skill.manaToTake(pInfo))));
        }
        if(mastery != SkillMastery.UNLEARNED) {
            inv.setItem(4, ItemUtil.createItemStack(Material.STONE_BUTTON, 1, "§aSélectionner cette technique"));
        }
        else {
            inv.setItem(4, ItemUtil.createItemStack(Material.STONE_BUTTON, 1, "§aSélectionner cette technique", Arrays.asList("§cAttention, il y a des risques d'échouer !")));
        }

        if(mastery == SkillMastery.UNLEARNED) {
            inv.setItem(5, ItemUtil.createItemStack(Material.ANVIL, 1, "§aLa technique n'est pas maîtrisée", Arrays.asList("§6Bonus: §7" + (pInfo.getRollBonus().get(skill) != null ? pInfo.getRollBonus().get(skill) : 0))));
        }else {
            if(pInfo.getVoieNinja().getName().contains("Ninjutsu") && pInfo.getLvL("Ninjutsu") >= 2 && !skill.getMudras().equals("none")) {
                if(mastery == SkillMastery.LEARNED) {
                    inv.setItem(5, ItemUtil.createItemStack(Material.ANVIL, 1, "§aLa technique n'est pas complètement maîtrisée.", Arrays.asList("§7Il vous est possible d'entraîner ce jutsu à une main.", "§cAttention, il y a des risques d'échouer !", "§6Bonus: §7" + (pInfo.getRollBonus().get(skill) != null ? pInfo.getRollBonus().get(skill) : 0))));
                }
                else {
                    inv.setItem(5, ItemUtil.createItemStack(Material.ANVIL, 1, "§cLa technique est maîtrisée à une seule main."));
                }
            }
            else {
                inv.setItem(5, ItemUtil.createItemStack(Material.ANVIL, 1, "§cLa technique est maîtrisée."));
            }
        }

        if(!pInfo.getFavoriteList().contains(skill))
            inv.setItem(6, ItemUtil.createRenamedWool(DyeColor.LIME, 1, "§2Ajouter aux favoris"));
        else
            inv.setItem(6, ItemUtil.createRenamedWool(DyeColor.RED, 1 ,"§4Retirer des favoris"));

        inv.setItem(7, ItemUtil.createItemStack(Material.ARROW, 1, "§6Retour au menu"));

        return inv;
    }

    public static Inventory getFavoriteInventory(PlayerInfo pInfo){
        Inventory inv = Bukkit.createInventory(pInfo.getPlayer(), 36, "§8Techniques favorites");

        ArrayList<Skill> favList = pInfo.getFavoriteList();

        for(Skill skill : favList){
            inv.addItem(skill.getItem());
        }

        inv.setItem(31, ItemUtil.createItemStack(Material.ARROW, 1,"§6Revenir au menu principal"));
        return inv;
    }

    private static ArrayList<String> getMudras(String mudras) {
        ArrayList<String> s = new ArrayList<>();
        if(mudras.equals("none")) {
            s.add("§bIl n'y a aucun mûdra pour ce jutsu.");
        }
        else {
            String[] tabmudra = mudras.split(", ");
            s.add("§bIl y a "+tabmudra.length+" mûdras pour ce jutsu.");
            for(String mudra : tabmudra) {
                s.add("§b- "+mudra);
            }
        }
        return s;
    }

    public static Inventory getElementInventory(PlayerInfo pInfo){

        Inventory inv = Bukkit.createInventory(pInfo.getPlayer(), 54, "§6Eléments");
        int i;

        for(i = 0; i < 54; i++) {
            inv.setItem(i, ItemUtil.createItemStack(Material.GRAY_STAINED_GLASS_PANE, 1, "§8-"));
            if(i > 8 && i%9 == 0 && i < 43) {
                i += 7;
            }
        }

        HashMap<Skill, SkillMastery> skills = pInfo.getSkills();
        ArrayList<String> typeJutsu = GetJutsuType(skills);

        inv.setItem(10, ItemUtil.createItemStack(Material.PAPER, 1, "§6"+pInfo.getClan().getName(), Arrays.asList("§7Cliquez ici pour obtenir les jutsus", "§7relatifs à votre clan."), "seisan", pInfo.getClan().getTag()));

        if(typeJutsu.contains("Ninpo")) {
            inv.setItem(11, ItemUtil.createItemStack(Material.DRAGON_BREATH, 1, "§7Ninpo"));
        }
        if(!pInfo.getVoieNinja().getName().equals("Ninjutsu")) {
            if(pInfo.getVoieNinja().getId() < 5) {
                inv.setItem(19, ItemUtil.createItemStack(Material.PAPER, 1, "§6" + pInfo.getVoieNinja().getName(), Arrays.asList("§7Cliquez ici pour obtenir les jutsus", "§7relatifs à votre voie ninja."), "seisan", pInfo.getVoieNinja().getName().toLowerCase() + "_scroll"));
            }
            else {
                inv.setItem(19, ItemUtil.createItemStack(Material.IRON_SWORD, 1, "§6" + pInfo.getVoieNinja().getName(), Arrays.asList("§7Cliquez ici pour obtenir les jutsus", "§7relatifs à votre second style de combat.")));
            }
        }
        else {
            inv.setItem(19, ItemUtil.createItemStack(Material.BARRIER, 1, "§7Indisponible dans la voie du ninjutsu."));
        }
        inv.setItem(28, ItemUtil.createItemStack(Material.GOLDEN_SWORD, 1, "§6"+pInfo.getStyleCombat().getName(), Arrays.asList("§7Cliquez ici pour obtenir les jutsus", "§7relatifs à votre style de combat.")));
        inv.setItem(37, ItemUtil.createItemStack(Material.DIAMOND, 1, "§6Favoris"));
        inv.setItem(49, ItemUtil.createItemStack(Material.BOOK, 1, "§6Liste complète des jutsus", Arrays.asList("§7Cliquez ici pour retourner sur l'ancien", "§7affichage des jutsus de votre personnage."), "seisan", "rouleau_1"));
        i = 12;
        for(int idChakra = 1; idChakra < 24; idChakra++) {
            ChakraType chakraType = ChakraType.fromId(idChakra);
            if(chakraType != null) {
                if(typeJutsu.contains(chakraType.getName().substring(2))) {
                    inv.setItem(i, ItemUtil.createItemStack(Material.DRAGON_BREATH, 1, chakraType.getName(), Collections.singletonList("§7Vos techniques " + chakraType.getName()), "seisan", chakraType.getName().substring(2).toLowerCase()));
                    i++;
                }
            }
            if(i%9 == 8) {
                i += 3;
            }
        }
        return inv;
    }

    public static ArrayList<String> GetJutsuType(HashMap<Skill, SkillMastery> skills) {
        ArrayList<String> listtype = new ArrayList<>();
        for(Skill s: skills.keySet()) {
            String nature = s.getElement();
            if(!listtype.contains(nature)) {
                listtype.add(nature);
            }
        }
        return listtype;
    }

    public static Inventory getSceaux(PlayerInfo pInfo, String type, int page) {
        Inventory inv = Bukkit.createInventory(pInfo.getPlayer(), 36,"§6Sceaux : "+type);
        ArrayList<Skill> listsceaux = getTypeSceaux(pInfo.getSkills().keySet(), type);
        getListeSceauxJutsu(pInfo, page, inv, listsceaux);
        inv.setItem(35, ItemUtil.createItemStack(Material.ARROW, 1, "§6Retour"));

        return inv;
    }

    private static void getListeSceauxJutsu(PlayerInfo p, int page, Inventory inv, ArrayList<Skill> listsceaux) {
        listsceaux.sort(Comparator.comparing(Skill::getName));

        for (int i = page * 27; (i < listsceaux.size() && i < page * 27 + 27); i++) {
            ItemStack newitem = new ItemStack(listsceaux.get(i).getItem());
            ItemMeta meta = newitem.getItemMeta();
            assert meta != null;
            List<String> lore = meta.getLore();
            assert lore != null;
            String skillName = meta.getDisplayName();
            Skill skill = Skill.getByRealName(skillName);
            lore.add("§7Coût chakra : §f"+ skill.manaToTake(p));
            lore.add("§6"+p.getMastery(skill).getName());
            meta.setLore(lore);
            newitem.setItemMeta(meta);
            inv.addItem(newitem);
        }

        if (page > 0)
            inv.setItem(30, ItemUtil.createItemStack(Material.ARROW, 1, "§6Page précédente"));

        if (page * 27 + 27 < listsceaux.size())
            inv.setItem(32, ItemUtil.createItemStack(Material.ARROW, 1, "§6Page suivante"));

        inv.setItem(31, ItemUtil.createItemStack(Material.STONE_BUTTON, 1, "§6Page " + page));
        inv.setItem(27, ItemUtil.createItemStack(Material.DIAMOND, 1, "§6Favoris"));
    }

    private static ArrayList<Skill> getTypeSceaux(Set<Skill> list, String type) {
        ArrayList<Skill> sceaux = new ArrayList<>();
        for (Skill skill : list) {
            if(skill.getName().split("-").length >= 2) {
                if (skill.getName().split("-")[1].contains(type)) {
                    sceaux.add(skill);
                }
            }
        }
        return sceaux;
    }
}
