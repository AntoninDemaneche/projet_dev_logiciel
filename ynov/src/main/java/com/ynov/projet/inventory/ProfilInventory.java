package com.ynov.projet.inventory;

import com.ynov.projet.utils.ItemUtil;
import com.ynov.projet.utils.PlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Arrays;

public class ProfilInventory {
    public static Inventory getProfileInventory(PlayerInfo pInfo, Player p){
        Inventory inv = Bukkit.createInventory(p, 54, "§8Apprentissage de compétences");
        /* Bordure */
        for (int i = 0; i < 54; i++) {
            inv.setItem(i, ItemUtil.createItemStack(Material.GRAY_STAINED_GLASS_PANE, 1, "§8-"));
            if (i > 8 && i % 9 == 0 && i < 43) {
                i += 7;
            }
        }

        ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        if (skullMeta != null){
            skullMeta.setOwningPlayer(p.getPlayer());
            skullMeta.setDisplayName("§fProfil de " + p.getDisplayName());
            skull.setItemMeta(skullMeta);
        }
        inv.setItem(13, skull);

        return inv;
    }
}
