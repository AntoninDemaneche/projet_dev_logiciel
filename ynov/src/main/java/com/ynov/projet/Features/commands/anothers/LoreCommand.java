package com.ynov.projet.Features.commands.anothers;

import com.ynov.projet.Main.Command;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class LoreCommand extends Command
{
    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split)
    {
        Player player = (Player) sender;
        ItemStack item = player.getInventory().getItemInMainHand();
        if(item.getType()== Material.AIR)
        {
            sender.sendMessage(ChatColor.RED + "HRP : " +ChatColor.GRAY +"Votre main principale est vide et il n'y a donc aucun objet � lorer.");
            return;
        }
        if(split.length == 0) {
            sender.sendMessage(ChatColor.RED + "HRP :" + ChatColor.GRAY + " /lore add (texte) : ajouter du texte � la ligne suivante");
            sender.sendMessage(ChatColor.RED + "HRP :" + ChatColor.GRAY + " /lore remove (nbligne) : supprime le lore � la ligne 'nblignr'");
            sender.sendMessage(ChatColor.RED + "HRP :" + ChatColor.GRAY + " /lore clear : supprimer tout le lore.");
            return;
        }
        ItemMeta meta = item.getItemMeta();
        if(meta == null) {
            sender.sendMessage("§cHRP : §7Erreur dans l'objet. Donnez le à Isami pour consultation.");
            return;
        }

        if(!sender.isOp() && meta.hasDisplayName() && !meta.getDisplayName().endsWith("§4*")) {
            sender.sendMessage("§cHRP : §7L'item est déjà validé. Impossible de le modifier.");
            return;
        }
        List<String> lore = meta.hasLore()?meta.getLore():new ArrayList<>();
        if(lore == null) {
            sender.sendMessage("§cHRP : §7Erreur dans l'objet. Donnez le à Isami pour consultation.");
            return;
        }
        int taille = 0;
        for(int i = 1; i < split.length; i++) {
            taille += split[i].length();
        }
        if(taille < 50) {
            int i;
            switch (split[0]) {
                case "add":
                    if (split.length == 1) {
                        sender.sendMessage("Syntax : /lore add <Line>");
                        return;
                    }
                    String line = "";
                    for (i = 1; i != split.length; i++) {
                        line = line.concat(split[i]+" ");
                    }
                    line = line.substring(0, line.length()-1);
                    line = line.replace("&","§");
                    if(!sender.isOp()) {
                        line = line.replace("§k", "");
                        line = line.replace("§l", "");
                        line = line.replace("§m", "");
                        line = line.replace("§n", "");
                        line = line.replace("§o", "");
                    }
                    lore.add(line);
                    break;
                case "clear":
                    if (split.length >= 2) {
                        sender.sendMessage("Syntaxe : /lore clear");
                        return;
                    }
                    lore.clear();
                    break;
                case "edit":
                    if(split.length < 3) {
                        sender.sendMessage("Syntaxe : /lore edit <LineNumber> <text>");
                        return;
                    }
                    try {
                        i = Integer.parseInt(split[1]);
                    } catch (Exception e) {
                        sender.sendMessage(ChatColor.RED + "HRP :" + ChatColor.GRAY + " Ce n'est pas un nombre.");
                        return;
                    }
                    if (i < 1) {
                        sender.sendMessage(ChatColor.RED + "HRP :" + ChatColor.GRAY + " Il y a au moins une ligne");
                        return;
                    }
                    if (i >= lore.size() + 1) {
                        sender.sendMessage(ChatColor.RED + "HRP :" + ChatColor.GRAY + " L'objet ne possède pas de "+i+"eme lignes !");
                        return;
                    }
                    String newlore = "";
                    for (i = 2; i != split.length; i++) {
                        newlore = newlore.concat(split[i]+" ");
                    }
                    lore.set(Integer.parseInt(split[1]) - 1, newlore.substring(0, newlore.length()-1).replace("&", "§"));
                    break;
                case "remove":
                    if (split.length == 1) {
                        sender.sendMessage("Syntaxe : /lore remove <LineNumber>");
                        return;
                    }
                    try {
                        i = Integer.parseInt(split[1]);
                    } catch (Exception e) {
                        sender.sendMessage(ChatColor.RED + "HRP :" + ChatColor.GRAY + " Ce n'est pas un nombre.");
                        return;
                    }
                    if (i < 1) {
                        sender.sendMessage(ChatColor.RED + "HRP :" + ChatColor.GRAY + " Il y a au moins une ligne");
                        return;
                    }
                    if (i >= lore.size() + 1) {
                        sender.sendMessage(ChatColor.RED + "HRP :" + ChatColor.GRAY + " L'objet ne possède pas de "+i+"eme lignes !");
                        return;
                    }
                    lore.remove( Integer.parseInt(split[1]) - 1);
                    break;
                default: {
                    sender.sendMessage(ChatColor.RED + "HRP :" + ChatColor.GRAY + " /lore add (texte) : ajouter du texte à la ligne suivante");
                    sender.sendMessage(ChatColor.RED + "HRP :" + ChatColor.GRAY + " /lore remove (nbligne) : supprime le lore à la ligne 'nblignr'");
                    sender.sendMessage(ChatColor.RED + "HRP :" + ChatColor.GRAY + " /lore clear : supprimer tout le lore.");
                    return;
                }
            }
            if(!sender.isOp()) {
                meta.setDisplayName(meta.getDisplayName().concat("§4*"));
            }
            meta.setLore(lore);
            item.setItemMeta(meta);
            player.getInventory().setItemInMainHand(item);
            player.updateInventory();

        }
        else {
            sender.sendMessage(ChatColor.RED+"Cette ligne de description est trop longue, réduisez-en la taille, ou allez à la ligne.");
        }
    }
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split)
    {
        List<String> completion = new ArrayList();
        switch(split.length) {
            case 1:
                complete(completion, "add",split[0]);
                complete(completion, "remove",split[0]);
                complete(completion, "clear",split[0]);
                break;
        }
        return completion;
    }
}

