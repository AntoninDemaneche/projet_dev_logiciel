package com.ynov.projet.Features.commands.anothers;

import com.ynov.projet.Features.PlayerData.PlayerConfig;
import com.ynov.projet.Main.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ChangeChatDeSeisanCommand extends Command {
    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        Player p = (Player) sender;
        PlayerConfig pConfig = PlayerConfig.getPlayerConfig(p);
        pConfig.setChangechat(!pConfig.isChangechat());
        if(pConfig.isChangechat()) {
            sender.sendMessage("§cHRP : §7Les italiques ne s'affichent désormais plus dans le chat.");
        }
        else {
            sender.sendMessage("§cHRP : §7Les italiques s'affichent désormais dans le chat.");
        }
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split)
    {
        return new ArrayList<>();
    }
}
