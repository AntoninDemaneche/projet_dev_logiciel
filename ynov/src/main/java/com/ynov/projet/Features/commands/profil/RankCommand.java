package com.ynov.projet.Features.commands.profil;


import com.ynov.projet.Features.objectnum.RPRank;
import com.ynov.projet.Features.PlayerData.PlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import com.ynov.projet.Main.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class RankCommand extends Command {
    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        if(split.length == 2) {
            Player p = sender.getServer().getPlayer(split[1]);
            switch (split[0]) {
                case "promote":
                    if (p != null) {
                        PlayerInfo pInfo = PlayerInfo.getPlayerInfo(p);
                        RPRank rank = RPRank.getById(pInfo.getRank().getId() + 1);

                        if (rank != RPRank.NULL) {
                            pInfo.setRank(rank);
                            pInfo.setMaxMana(rank.getChakraRank() + pInfo.getManamaze() + pInfo.getManamission() + pInfo.bonusChakra());
                            sender.sendMessage(ChatColor.GREEN + "Le rang de " + p.getDisplayName() + ChatColor.GREEN + " est désormais " + ChatColor.GOLD + rank.getDisplayName());
                        } else {
                            sender.sendMessage(ChatColor.RED + "Ce joueur ne peut être promu à un plus haut grade");
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "Ce joueur n'est pas connecté !");
                    }
                    break;
                case "demote":
                    if (p != null) {
                        PlayerInfo pInfo = PlayerInfo.getPlayerInfo(p);
                        RPRank rank = RPRank.getById(pInfo.getRank().getId() - 1);

                        if (rank != RPRank.NULL) {
                            pInfo.setRank(rank);
                            pInfo.setMaxMana(rank.getChakraRank() + pInfo.getManamaze() + pInfo.getManamission() + pInfo.bonusChakra());
                            sender.sendMessage(ChatColor.GREEN + "Le rang de " + p.getDisplayName() + ChatColor.GREEN + " est désormais " + ChatColor.GOLD + rank.getDisplayName());
                        } else {
                            sender.sendMessage(ChatColor.RED + "Ce joueur ne peut être rétrogradé à un plus bas grade");
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "Ce joueur n'est pas connecté !");
                    }
                    break;
            }
        }
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split)
    {
        List<String> completion = new ArrayList();
        if(split.length == 1) {
            complete(completion, "promote", split[0]);
            complete(completion, "demmote", split[0]);
        }
        else if (split.length == 2) {
            for(Player p : Bukkit.getOnlinePlayers()) {
                complete(completion, p.getName(), split[1]);
            }
        }
        return completion;
    }


    private String getRankList() {
        String s = "";
        for(RPRank rank : RPRank.values()){
            s = s.concat(rank.getName() + ", " + "\n");
        }
        return s;
    }
}
