package com.ynov.projet.Features.commands.anothers;

import com.ynov.projet.Features.PlayerData.PlayerInfo;
import com.ynov.projet.Main.Command;
import org.bukkit.ChatColor;

import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class BootsCommand extends Command {

    //final String BOOTS_TAG = ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + ChatColor.BOLD + "CHAKRA_BOOTS" + ChatColor.DARK_GRAY + "] ";

    @Override
    protected void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.getInventory().getBoots() != null) {
                if (!p.getInventory().getBoots().containsEnchantment(Enchantment.BINDING_CURSE)) {
                    //          sender.sendMessage(BOOTS_TAG + ChatColor.AQUA + "Tu actives tes bottes de chakra !");
                    ItemStack customBoots = p.getInventory().getBoots();
                    ItemMeta customB = customBoots.getItemMeta();
                    if(customB != null) {
                        customB.setDisplayName("§4Bottes de chakra");
                        customB.setUnbreakable(true);
                        customB.addEnchant(Enchantment.FROST_WALKER, 1, true);
                        customB.addEnchant(Enchantment.BINDING_CURSE, 1, true);
                        customB.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                        customBoots.setItemMeta(customB);
                    }
                    p.updateInventory();
                    PlayerInfo playerInfo = PlayerInfo.getPlayerInfo(p);
                    if(playerInfo.getClan().getId() == 18 && playerInfo.getLvL(playerInfo.getClan().getName()) >= 4) {
                        p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 999999, 14, true));
                    }
                    else {
                        p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 999999, playerInfo.getCaract().getForce(), true));
                    }
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999, playerInfo.getCaract().getVitesse(), true));
                } else {
                    //     sender.sendMessage(BOOTS_TAG + ChatColor.AQUA + "Tu désactives tes bottes de chakra !");
                    p.getInventory().getBoots().removeEnchantment(Enchantment.FROST_WALKER);
                    p.getInventory().getBoots().removeEnchantment(Enchantment.BINDING_CURSE);
                    p.removePotionEffect(PotionEffectType.JUMP);
                    p.removePotionEffect(PotionEffectType.SPEED);
                }
            } else {
                //   sender.sendMessage(BOOTS_TAG + ChatColor.DARK_RED + "Vous ne pouvez pas, il vous faut des bottes !");
                sender.sendMessage(ChatColor.RED + "HRP : " + ChatColor.GRAY + "Vous ne pouvez pas exécuter cette commande, il vous faut des bottes !");

            }
        }
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        return null;
    }
}
