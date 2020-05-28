package com.ynov.projet.Features.commands.others;

import com.ynov.projet.Features.skill.Skill;
import com.ynov.projet.Features.utils.ItemUtil;
import org.bukkit.Material;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import com.ynov.projet.Main.Command;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParcheminCommand extends Command {
    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        if(sender instanceof Player) {
            if (sender.isOp()) {
                if (split.length != 0) {
                    String nom_jutsu = split[0];
                    Skill skill = Skill.getByPluginName(nom_jutsu);
                    if (skill != null) {
                        GiveParchemin(skill, (Player)sender);
                        sender.sendMessage("§4HRP : §7 Le parchemin pour le jutsu "+skill.getName()+" a été ajouté à votre inventaire.");
                    } else {
                        sender.sendMessage("§4HRP : §7Le nom du jutsu est incorrect. Veuillez vérifier l'écriture de ce dernier.");
                    }
                } else {
                    sender.sendMessage("§4Usage : §7/parchemin (nom_du_jutsu)");
                }
            } else {
                sender.sendMessage("§4HRP : §7Vous n'avez pas la permission pour exécuter cette commande.");
            }
        }
        else {
            sender.sendMessage("Seul les joueurs peuvent utiliser la commande.");
        }
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split)
    {
        return new ArrayList<>();
    }


    public static void GiveParchemin(Skill skill, Player player) {
        ItemStack parchemin = ItemUtil.createItemStack(Material.BOOK, 1, skill.getName(), Arrays.asList("Un parchemin pour apprendre le jutsu :", skill.getName()), "seisan", "rouleau_"+skill.getElement().toLowerCase());
        parchemin = ItemUtil.addTag(parchemin, "jutsu", "learn");
        player.getInventory().addItem(parchemin);
    }
}

