package com.ynov.projet.Features.commands.anothers;

import com.ynov.projet.Features.PlayerData.PlayerConfig;
import com.ynov.projet.Main;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class GmCommand extends Main.Command {
    @Override
    protected void myOnCommand(CommandSender sender, Command command, String label, String[] split) {
        Player p = (Player) sender;

        if ((sender.isOp()) || PlayerConfig.getPlayerConfig(p).isBuildmode() == true){
            switch (split[0]){
                case "0":
                    p.setGameMode(GameMode.SURVIVAL);
                    p.sendMessage(ChatColor.AQUA + "Tu es maintenant en survie flemmard !");
                    break;
                case "1":
                    p.setGameMode(GameMode.CREATIVE);
                    p.sendMessage(ChatColor.AQUA + "Tu es maintenant en créatif flemmard !");
                    break;
                case "2":
                    p.setGameMode(GameMode.ADVENTURE);
                    p.sendMessage(ChatColor.AQUA + "Tu es maintenant en adventure flemmard !");
                    break;
                case "3":
                    p.setGameMode(GameMode.SPECTATOR);
                    p.sendMessage(ChatColor.AQUA + "Tu es maintenant en spectateur flemmard !");
                    break;
            }
        }

    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, Command command, String label, String[] split) {
        return null;
    }
}
