package com.ynov.projet.Features.commands.profil;

import com.ynov.projet.Features.PlayerData.PlayerInfo;
import com.ynov.projet.Main.Command;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Helliot on 16/08/2018
 */
public class ApparenceCommand extends Command {
    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        if(sender instanceof Player){
            Player p = (Player) sender;
            PlayerInfo pInfo = PlayerInfo.getPlayerInfo(p);

            if(split.length > 0){
                String message = "";
                for(String s : split){
                    message = message.concat(s + " ");
                }
                message = message.replace("&","§");
                pInfo.setAppearance(message);
                p.sendMessage(ChatColor.GREEN + "Votre apparence est maintenant: * " + ChatColor.translateAlternateColorCodes('&', message) + ChatColor.GREEN + " *");
            }else{
                String apparence = "" + pInfo.getAppearance();
                p.sendMessage(ChatColor.GREEN + "* " + ChatColor.translateAlternateColorCodes('&', apparence) + ChatColor.GREEN + " *");
            }
        }
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split)
    {
        return new ArrayList<>();
    }

}

