package com.ynov.projet.features.commands.profil;

import com.ynov.projet.Main;
import com.ynov.projet.features.PlayerData.PlayerInfo;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import com.ynov.projet.Main.Command;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class LostCommand extends Command {

    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        if (split.length == 1){
            Player target = (Player) sender;
            PlayerInfo targetInfo = PlayerInfo.getPlayerInfo(target);
            if (StringUtils.isNumeric(split[0])){
                int amout = Integer.parseInt(split[0]);
                targetInfo.removeMana(amout);
                sender.sendMessage(ChatColor.BLUE + "Tu perds " + ChatColor.RED + amout + ChatColor.BLUE + " de mana !");
            }else {
                sender.sendMessage(ChatColor.RED + "La commande c'est /lost <quantité> !");
            }
        }
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        return new ArrayList<>();
    }
}
