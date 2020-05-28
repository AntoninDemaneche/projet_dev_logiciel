package com.ynov.projet.Features.Inventory;

import com.ynov.projet.Features.PlayerData.PlayerInfo;
import com.ynov.projet.Features.utils.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class TrainInventory {

    public static Inventory getFichePerso(PlayerInfo pInfo, Player holder){
        Inventory inv = Bukkit.createInventory(holder, 54, "§8Fiche personnage de " + pInfo.getPlayer().getDisplayName());


        ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        if(skullMeta != null) {
            skullMeta.setOwningPlayer(pInfo.getPlayer());
            skullMeta.setDisplayName("§6Fiche personnage de " + pInfo.getPlayer().getDisplayName());
            skullMeta.setLore(Arrays.asList("§7Sexe : §f"+pInfo.getGender().getName(),"§7Âge : §f" + pInfo.getAge(), "§7Rang : §f"+pInfo.getRank().displayName,"§7Nombre de dose d'encre : §f"+pInfo.getInk()));
            skull.setItemMeta(skullMeta);
        }


        /* Bordure */
        for(int i = 0; i < 54; i++) {
            inv.setItem(i, ItemUtil.createItemStack(Material.GRAY_STAINED_GLASS_PANE, 1, "§8-"));
            if(i > 8 && i%9 == 0 && i < 43) {
                i += 7;
            }
        }

        /* ITEM */
        String clan = pInfo.getClan().getName();
        String style = pInfo.getStyleCombat().getName();
        String voie = pInfo.getVoieNinja().getName();
        String chakra, chakramission, chakramaze, chakramax, chakrarank, chakracolor, chakralevel;
        String chakraresistance = "§7Résistance chakraïque : §f"+pInfo.getResistance();
        List<String> chakralore;
        if(pInfo.getPlayer().getName().equals(holder.getName()) || holder.isOp()) {
            chakra = "§7Nature de chakra : "+pInfo.getChakraList();
            chakramax = "§7Chakra : §f"+pInfo.getMana()+"§7/§f"+pInfo.getMaxMana();
            chakramission = "§7Chakra obtenu en mission : §f"+pInfo.getManamission()+"§7/§f200";
            chakramaze = "§7Chakra obtenu en [§g§6???§r§7] : §f"+pInfo.getManamaze()+"§7/§f300";
            chakrarank = "§7Chakra obtenu grâce à votre rang : §f"+pInfo.getRank().getChakraRank();
            chakracolor = "§7Couleur de chakra : "+pInfo.getCouleurChakra().getName() + " "+pInfo.getTeinte().getName();
            chakralevel = "§7Niveau de transparence du chakra : §f"+pInfo.getTransparence();
            chakralore = Arrays.asList(chakra, chakramax, chakramission, chakramaze, chakrarank, chakraresistance, chakracolor, chakralevel);
        }
        else {
            chakra = "§7Nature non perceptible.";
            chakramax = "§7Quantité de chakra non perceptible";
            chakralore = Arrays.asList(chakra, chakramax, chakraresistance);

        }

        List<String> clanlore;
        if(pInfo.getClan().getName().equals("Inuzuka")) {
            String name = pInfo.getAttributClan() == null ? "§7Sans nom de Ninken" : "§7Nom du Ninken : §6"+pInfo.getAttributClan();
            clanlore = Arrays.asList("§7"+clan,"§7Niveau : §f"+pInfo.getLvL(clan), name);
        }
        else if(clan.equals("Sabaku")) {
            String name = pInfo.getAttributClan() == null ? "§7Type de sable : §cNon défini." : "§7Type de sable : "+pInfo.getAttributClan();
            clanlore = Arrays.asList("§7"+clan, "§7Niveau : §f"+pInfo.getLvL(clan), name);
        }
        else {
            clanlore = Arrays.asList("§7"+clan,"§7Niveau : §f"+pInfo.getLvL(clan));
        }
        inv.setItem(12, skull);
        inv.setItem(14, ItemUtil.createItemStack(Material.BLUE_DYE, 1, "§6Informations sur le chakra", chakralore, "seisan", "chakra_icon"));
        inv.setItem(20, ItemUtil.createItemStack(Material.GOLDEN_SWORD, 1, "§6Style de combat", Arrays.asList("§7"+style,"§7Niveau : §f"+pInfo.getLvL(style))));
        inv.setItem(22, ItemUtil.createItemStack(Material.PAPER, 1, "§6Clan", clanlore, "seisan", pInfo.getClan().getTag()));
        if(pInfo.getVoieNinja().getId() == -1 || pInfo.getVoieNinja().getId() == 0) {
            inv.setItem(24, ItemUtil.createItemStack(Material.END_CRYSTAL, 1, "§6Voie ninja", Arrays.asList("§7"+voie,"§7Niveau : §f"+pInfo.getLvL(voie))));
        }
        else if(pInfo.getVoieNinja().getId() < 5){
            List<String> lore = Arrays.asList("§7"+voie,"§7Niveau : §f"+pInfo.getLvL(voie));
            if(pInfo.getVoieNinja().getId() == 1) {
                int lvl = pInfo.getLvL(pInfo.getVoieNinja().getName());
                if(lvl >= 4) {
                    lvl--;
                    lore = Arrays.asList("§7"+voie,
                            "§7Niveau : §f"+pInfo.getLvL(voie),
                            "§7Feuilles de Seji : §f"+pInfo.getFuin_paper(),
                            "§7Capacité de l'assembleur Uzumaki : §f"+pInfo.getFuin_uzumaki()+" §7/ §f"+lvl*lvl);
                }
            }
            inv.setItem(24, ItemUtil.createItemStack(Material.PAPER, 1, "§6Voie ninja", lore, "seisan", voie.toLowerCase()+"_scroll"));
        }
        else {
            inv.setItem(24, ItemUtil.createItemStack(Material.IRON_SWORD,1, "§6Second style de combat", Arrays.asList("§7"+voie,"§7Niveau : §f"+pInfo.getLvL(voie))));
        }
        inv.setItem(31, ItemUtil.createItemStack(Material.NETHER_STAR, 1, "§8Autres compétences", Arrays.asList("§7Cliquez pour davantage d'informations","§7(Ceci n'affichagera que les compétences autres)")));

        inv.setItem(37, ItemUtil.createItemStack(Material.RED_DYE, 1, "§4Force",Arrays.asList("§7Cliquez pour davantage d'informations","§7Niveau : §f"+pInfo.getLvL("Force")), "seisan", "force_icon"));
        inv.setItem(39, ItemUtil.createItemStack(Material.GREEN_DYE, 1, "§2Vitesse",Arrays.asList("§7Cliquez pour davantage d'informations","§7Niveau : §f"+pInfo.getLvL("Vitesse")), "seisan", "vitesse_icon"));
        inv.setItem(41, ItemUtil.createItemStack(Material.YELLOW_DYE, 1, "§ePerception de la vitesse",Arrays.asList("§7Cliquez pour davantage d'informations","§7Niveau : §f"+pInfo.getLvL("Perception de la vitesse")), "seisan", "perception_icon"));
        inv.setItem(43, ItemUtil.createItemStack(Material.BLUE_DYE, 1, "§9Instinct et expérience",Arrays.asList("§7Cliquez pour davantage d'informations","§7Niveau : §f"+pInfo.getLvL("Instinct et expérience")), "seisan", "instinct_icon"));

        return inv;
    }
}

