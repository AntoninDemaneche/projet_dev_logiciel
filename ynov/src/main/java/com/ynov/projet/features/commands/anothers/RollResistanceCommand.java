package com.ynov.projet.Features.commands.anothers;

import com.ynov.projet.Features.PlayerData.PlayerInfo;
import com.ynov.projet.Main.Command    ;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class RollResistanceCommand extends Command {
    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        if(split.length == 0) {
            sender.sendMessage("§cHRP : /rollresistance attaque");
            sender.sendMessage("§cHRP : /rollresistance defense");
            return;
        }
        if(!(sender instanceof Player)) {
            sender.sendMessage("§cHRP : §7Commande interdite depuis la console.");
            return;
        }
        int bonus = 0;
        if(sender.isOp() && split.length == 2) {
            if(StringUtils.isNumeric(split[1])) {
                bonus = Math.min(200, Math.max(0, Integer.parseInt(split[1])));
            }
        }
        int resultat = Commands.getRandom(sender.getName(), 1, 100);
        Player p = (Player)sender;
        PlayerInfo pInfo = PlayerInfo.getPlayerInfo(p);
        String type;
        resultat = resultat + bonus + pInfo.getResistance();
        switch (split[0]) {
            case "defense":
                type = "Défense";
                break;
            case "attaque":
                type = "Attaque";
                resultat += 40;
                break;
            default:
                return;
        }

        for (Entity target : p.getNearbyEntities(50, 50, 50)) {
            if (target instanceof Player) {
                target.sendMessage(ChatColor.GRAY + "[ROLL RESISTANCE CHAKRAIQUE - "+type+"] " + ChatColor.RESET + p.getDisplayName() + ChatColor.GRAY + " a tiré un " + resultat + ".*");
            }
        }
        System.out.println(ChatColor.GRAY + "[ROLL RESISTANCE CHAKRAIQUE - "+type+"] " + ChatColor.RESET + p.getDisplayName() + ChatColor.GRAY + " a tiré un " + resultat + ".*");
        p.sendMessage(ChatColor.GRAY + "[ROLL RESISTANCE CHAKRAIQUE - "+type+"] " + ChatColor.RESET + p.getDisplayName() + ChatColor.GRAY + " a tiré un " + resultat + ".*");
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split)
    {
        List<String> completion = new ArrayList();
        if(split.length == 1) {
            complete(completion, "attaque", split[0]);
            complete(completion, "defense", split[0]);
        }
        return completion;
    }

}
