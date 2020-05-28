package com.ynov.projet.Features.commands.profil;

import com.ynov.projet.Features.PlayerData.PlayerInfo;
import com.ynov.projet.Main.Command;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class AgeCommand extends Command {
    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        if(sender.isOp()){
            if(split.length == 2){
                Player target = sender.getServer().getPlayer(split[0]);
                if(target != null){
                    if(StringUtils.isNumeric(split[1])){
                        int age = Integer.parseInt(split[1]);
                        PlayerInfo tInfo = PlayerInfo.getPlayerInfo(target);
                        tInfo.setAge(age);
                        target.sendMessage(ChatColor.GRAY + "Vous avez maintenant " + age + " ans");
                        sender.sendMessage(target.getDisplayName() + ChatColor.GRAY + " a maintenant " + age + " ans");
                    }else{
                        sender.sendMessage(ChatColor.RED + "L'age doit être un nombre positif");
                    }
                }else{
                    sender.sendMessage(ChatColor.RED + "Ce joueur n'est pas connecté !");
                }
            }else{
                sender.sendMessage(ChatColor.RED + "Syntaxe: /age (joueur) (age)");
            }
        }else{
            sender.sendMessage(ChatColor.RED + "Vous n'avez pas la permission !");
        }
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split)
    {
        return new ArrayList<>();
    }
}
