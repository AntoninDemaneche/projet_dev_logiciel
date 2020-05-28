package com.ynov.projet.Features.commands.profil;

import com.ynov.projet.Features.PlayerData.PlayerInfo;
import com.ynov.projet.Main;
import com.ynov.projet.Main.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ProfilMJCommand extends Command {
    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        if(split.length == 2) {
            if(split[0].equals("save")) {
                    PlayerInfo playerInfo = PlayerInfo.getPlayerInfo((Player) sender);
                    PlayerInfo profil = playerInfo.clone(split[1]);

                    if(Main.getFicheMJ().containsKey(split[1])) {
                        sender.sendMessage("§cHRP : §7La fiche personnage de "+split[1]+" a été mis à jour.");
                    }
                    else {
                        sender.sendMessage("§cHRP : §7La fiche personnage de "+split[1]+" a été créé.");
                    }
                    Main.getFicheMJ().put(split[1], profil);
            }
            else if(split[0].equals("load")) {
                if(!Main.getFicheMJ().containsKey(split[1])) {
                    sender.sendMessage("§cHRP : §7La fiche n'est pas dispo");
                    return;
                }
                PlayerInfo fiche = Main.getFicheMJ().get(split[1]);
                PlayerInfo profil = fiche.clone((Player)sender);
                PlayerInfo.replacePlayerInfo((Player)sender, profil);
                sender.sendMessage("§cHRP : §7Chargement de la fiche personnage de "+split[1]);
            }
        }
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split)
    {
        List<String> completion = new ArrayList();
        if(split.length == 1) {
            complete(completion, "save", split[0]);
            complete(completion, "load", split[0]);
        }
        if(split.length == 2) {
            for(String test : Main.getFicheMJ().keySet()) {
                complete(completion, test, split[1]);
            }
        }
        return completion;
    }
}
