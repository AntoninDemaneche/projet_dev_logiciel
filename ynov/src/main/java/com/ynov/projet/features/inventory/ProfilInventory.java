package com.ynov.projet.features.inventory;

import com.ynov.projet.features.utils.ItemUtil;
import com.ynov.projet.features.PlayerData.PlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProfilInventory {
    public static Inventory getProfileInventory(PlayerInfo pInfo, Player p){
        Inventory inv = Bukkit.createInventory(p, 54, "§8Profil de : " + p.getDisplayName());
        /* Bordure */
        for (int i = 0; i < 54; i++) {
            inv.setItem(i, ItemUtil.createItemStack(Material.GRAY_STAINED_GLASS_PANE, 1, "§8-"));
            if (i > 8 && i % 9 == 0 && i < 43) {
                i += 7;
            }
        }

        String mana, manamax, manarank;
        List<String> manalore;
        if (pInfo.getPlayer().getName().equals(p.getName()) || p.isOp()){
            manamax = "§7Mana : §f" + pInfo.getMana() + "§7/§f" + pInfo.getMaxMana();
            manarank = "§7Mana obtenu grâce à votre rang : §f" + pInfo.getRank().getManaRank();
            manalore = Arrays.asList(manamax, manarank);
        }else {
            mana = "§7Mana non perceptible.";
            manamax = "§7Quantité de mana non perceptible ";
            manalore = Arrays.asList(mana, manamax);
        }

        inv.setItem(14, ItemUtil.createItemStack(Material.BLUE_DYE, 1, "§6Informations sur le mana", manalore));

        ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        if (skullMeta != null){
            skullMeta.setOwningPlayer(p.getPlayer());
            skullMeta.setDisplayName(ChatColor.GOLD + "Profil de §4" + p.getDisplayName());
            skullMeta.setLore(Arrays.asList("§7Rang : §f" + pInfo.getRank().getDisplayName()));
            skull.setItemMeta(skullMeta);
        }
        inv.setItem(12, skull);

        return inv;
    }
}
