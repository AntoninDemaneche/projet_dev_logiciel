package com.ynov.projet.Features.commands.anothers;

import com.ynov.projet.Features.PlayerData.PlayerConfig;
import com.ynov.projet.Main.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CanishCommand extends Command{
    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[]split){

        if (!sender.isOp())
        {
            sender.sendMessage("§cHRP : §7Vous n'avez pas la permission.");
            return;
        }
        Player p = (Player)sender;
        PlayerConfig pConfig = PlayerConfig.getPlayerConfig(p);

        pConfig.setVanish(!pConfig.isVanish());
        Commands.playerVanish(pConfig);
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[]split) {
        return new ArrayList<>();
    }
}
