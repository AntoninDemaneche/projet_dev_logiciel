package com.ynov.projet.Features.commands.profil;


import com.ynov.projet.Features.objectnum.ChakraType;
import com.ynov.projet.Features.PlayerData.PlayerInfo;
import com.ynov.projet.Features.utils.ItemUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import com.ynov.projet.Main.Command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChakraCommand extends Command {
    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        if (split.length == 3) {
            Player target = Bukkit.getPlayer(split[2]);
            if (target != null) {
                PlayerInfo targetInfo = PlayerInfo.getPlayerInfo(target);
                if (sender.isOp()) {
                    ChakraType type;
                    switch (split[0]) {
                        case "addtype":
                            type = ChakraType.fromName(split[1]);
                            if (type != null ) {
                                if(type == ChakraType.NULL) {
                                    sender.sendMessage("§cHRP : §7La nature n'existe pas ou le nombre suivant n'est pas 25 ou 50");
                                    return;
                                }
                                if(!targetInfo.hasChakra(type)) {
                                    targetInfo.addChakraType(split[1]);
                                    sender.sendMessage(ChatColor.GREEN + "La nature de chakra de " + target.getDisplayName() + ChatColor.GREEN + " est désormais composée du " + type.getName());
                                }
                                else {
                                    sender.sendMessage("§4OH ZEBI IL A DEJA LA NATURE TU FAIS QUOI WOLLEH ?");
                                }
                            } else {
                                sendHelpList(sender);
                            }
                            break;
                        case "removetype":
                            type = ChakraType.fromName(split[1]);
                            if (type != null) {
                                if(targetInfo.hasChakra(type)) {
                                    targetInfo.removeChakraType(type);
                                    sender.sendMessage(ChatColor.GREEN + "La nature de chakra de " + target.getDisplayName() + ChatColor.GREEN + " n'est désormais plus composée du " + type.getName());
                                }
                                else {
                                    sender.sendMessage("§2Eh... Il n'a pas la nature... tu ne peux pas lui retirer...");
                                }
                            } else {
                                sendHelpList(sender);
                            }
                            break;
                        case "add":
                            if (StringUtils.isNumeric(split[1])) {
                                int amount = Integer.parseInt(split[1]);
                                targetInfo.addMana(amount);
                                sender.sendMessage(target.getDisplayName() + ChatColor.GREEN + " a gagné " + ChatColor.GOLD + amount + ChatColor.GREEN + " point(s) de mana.");
                            } else {
                                sendHelpList(sender);
                            }
                            break;

                        case "remove":
                            if (StringUtils.isNumeric(split[1])) {
                                int amount = Integer.parseInt(split[1]);
                                targetInfo.removeMana(amount);
                                sender.sendMessage(target.getDisplayName() + ChatColor.GREEN + " a perdu " + ChatColor.GOLD + amount + ChatColor.GREEN + " point(s) de mana.");
                            } else {
                                sendHelpList(sender);
                            }
                            break;
                        case "setmax":
                            if (StringUtils.isNumeric(split[1])) {
                                int amount = Integer.parseInt(split[1]);
                                if (amount % 10 == 0) {
                                    targetInfo.setMaxMana(amount);
                                    sender.sendMessage(target.getDisplayName() + ChatColor.GREEN + " a désormais " + ChatColor.GOLD + amount + ChatColor.GREEN + " de chakra max.");
                                } else {
                                    sender.sendMessage(ChatColor.RED + "Le nombre que vous donnez doit être un multiple de 10");
                                }
                            } else {
                                sendHelpList(sender);
                            }
                            break;
                        case "set":
                            if (StringUtils.isNumeric(split[1])) {
                                int amount = Integer.parseInt(split[1]);
                                targetInfo.setMana(amount);
                                sender.sendMessage(target.getDisplayName() + ChatColor.GREEN + " a désormais " + ChatColor.GOLD + amount + ChatColor.GREEN + " point(s) de mana.");
                            } else {
                                sendHelpList(sender);
                            }
                            break;
                        case "mission":
                            if ("get".equals(split[1])) {
                                Player p = Bukkit.getServer().getPlayer(split[2]);
                                if (p != null) {
                                    PlayerInfo playerInfo = PlayerInfo.getPlayerInfo(p);
                                    sender.sendMessage("§cHRP : " + p.getDisplayName() + "§7 a obtenu §6" + playerInfo.getManamission() + " §7chakra en récompense de mission.");
                                } else {
                                    sender.sendMessage("§cHRP : §7Le joueur n'est pas connecté.");
                                }
                            } else {
                                sendHelpList(sender);
                            }
                            break;
                        default:
                            sendHelpList(sender);
                    }
                }
            }else {
                sender.sendMessage(ChatColor.RED + "Le joueur n'est pas connecté !");
            }
        } else if (split.length == 2) {
            if (sender.isOp()) {
                Player target = sender.getServer().getPlayer(split[1]);
                if (target != null) {
                    PlayerInfo targetInfo = PlayerInfo.getPlayerInfo(target);
                    if ("info".equals(split[0])) {
                        sender.sendMessage(target.getDisplayName() + ChatColor.GRAY + ": §6" + targetInfo.getMana() + "§7/§6" + targetInfo.getMaxMana());
                    } else {
                        sendHelpList(sender);
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "Le joueur n'est pas connecté !");
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Vous n'avez pas la permission !");
            }
        }else if(split.length == 1){
            if ("roll".equals(split[0])) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    PlayerInfo pInfo = PlayerInfo.getPlayerInfo(p);

                    if (pInfo.getChakraType() == null || pInfo.getChakraType().equals("§7Non défini") || pInfo.getChakraType().equals("")) {
                        if (p.getEquipment().getItemInMainHand().equals(ItemUtil.createItemStack(Material.PAPER, 1, "§6Papier à Chakra"))) {
                            Random r = new Random();
                            int i = r.nextInt(4) + 1;
                            String encaMessage = "";
                            switch (i) {
                                case 1:
                                    pInfo.addChakraType(ChakraType.RAITON.name);
                                    encaMessage = "§c** Le papier se froisse **";
                                    break;
                                case 2:
                                    pInfo.addChakraType(ChakraType.DOTON.name);
                                    encaMessage = "§c** Le papier se salit avant de s'effriter **";
                                    break;
                                case 3:
                                    pInfo.addChakraType(ChakraType.KATON.name);
                                    encaMessage = "§c** Le papier s'enflamme **";
                                    break;
                                case 4:
                                    pInfo.addChakraType(ChakraType.FUTON.name);
                                    encaMessage = "§c** Le papier se coupe en deux **";
                                    break;
                                case 5:
                                    pInfo.addChakraType(ChakraType.SUITON.name);
                                    encaMessage = "§c** Le papier devient humide **";
                                    break;
                            }

                            p.sendMessage(encaMessage);
                            for (Entity e : p.getNearbyEntities(20, 20, 20)) {
                                if (e instanceof Player) {
                                    e.sendMessage(encaMessage);
                                }
                            }
                        } else {
                            p.sendMessage(ChatColor.RED + "Vous devez avoir un papier à chakra en main");
                        }
                    } else {
                        p.sendMessage(ChatColor.RED + "Votre nature de chakra est déjà définie.");
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "Player command only");
                }
            } else {
                sendHelpList(sender);
            }

        } else if(split.length == 4) {
            if(split[0].equals("mission")) {
                switch (split[1]) {
                    case "add":
                        Player p = Bukkit.getServer().getPlayer(split[2]);
                        if(p != null) {
                            if (StringUtils.isNumeric(split[3])) {
                                PlayerInfo playerInfo = PlayerInfo.getPlayerInfo(p);
                                int amount = Integer.parseInt(split[3]);
                                int newamount = amount + playerInfo.getManamission();
                                if(newamount <= 200) {
                                    playerInfo.setManamission(newamount);
                                    playerInfo.addManaMax(amount);
                                    sender.sendMessage(p.getDisplayName()+"§7 a désormais §6"+newamount+"§7/200 de chakra de mission.");
                                }
                                else {
                                    sender.sendMessage("§cHRP : §7Le chakra obtenable en mission ne peut exceder 200. (§6"+newamount+"§7/§6200§7)");
                                }
                            }
                            else {
                                sender.sendMessage("§cHRP : §6"+split[3]+" §7n'est pas un chiffre");
                            }
                        }
                        else {
                            sender.sendMessage("§cHRP : §7Le joueur n'est pas connecté");
                        }
                        break;
                    case "remove":
                        Player pl = Bukkit.getServer().getPlayer(split[2]);
                        if(pl != null) {
                            if (StringUtils.isNumeric(split[3])) {
                                PlayerInfo playerInfo = PlayerInfo.getPlayerInfo(pl);
                                int amount = Integer.parseInt(split[3]);
                                int newamount = playerInfo.getManamission() - amount;
                                if(newamount >= 0) {
                                    playerInfo.setManamission(newamount);
                                    playerInfo.removeManaMax(amount);
                                    sender.sendMessage(pl.getDisplayName()+"§7 a désormais §6"+newamount+"§7/200 de chakra de mission.");
                                }
                                else {
                                    sender.sendMessage("§cHRP : §7Le chakra obtenable en mission ne peut descendre sous 0. (§6"+newamount+"§7/§6200§7)");
                                }
                            }
                            else {
                                sender.sendMessage("§cHRP : §6"+split[3]+" §7n'est pas un chiffre");
                            }
                        }
                        else {
                            sender.sendMessage("§cHRP : §7Le joueur n'est pas connecté");
                        }
                        break;
                    default:
                        sendHelpList(sender);
                }
            }
            else {
                sendHelpList(sender);
            }
        }
        else {
            sendHelpList(sender);
        }
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split)
    {
        List<String> completion = new ArrayList();
        switch (split.length) {
            case 1 :
                complete(completion, "roll", split[0]);
                if(sender.isOp()) {
                    complete(completion, "add", split[0]);
                    complete(completion, "remove", split[0]);
                    complete(completion, "setmax", split[0]);
                    complete(completion, "set", split[0]);
                    complete(completion, "addtype", split[0]);
                    complete(completion, "removetype", split[0]);
                    complete(completion, "mission", split[0]);
                }
                break;
            case 2 :
                if(sender.isOp()) {
                    if(split[0].equals("mission")) {
                        complete(completion, "add", split[1]);
                        complete(completion, "remove", split[1]);
                        complete(completion, "get", split[1]);
                    }
                }
                break;
            case 3 :
                if(sender.isOp()) {
                    if(split[0].equals("mission")) {
                        if(split[1].equals("add") || split[1].equals("remove") || split[1].equals("get")) {
                            for(Player p : Bukkit.getOnlinePlayers()) {
                                complete(completion, p.getName(), split[2]);
                            }
                        }
                    }
                }
        }
        return completion;
    }

    private void sendHelpList(CommandSender sender) {
        ArrayList<String> helpList = new ArrayList<>();

        helpList.add("§6/chakra §eroll §8- Permet de découvrir sa nature première de chakra");
        if (sender.isOp()) {
            helpList.add("§6/chakra §eadd §7(nombre) (joueur) §8- Permet d'ajouter du chakra à un joueur (Ne peut dépasser le maximum)");
            helpList.add("§6/chakra §eremove §7(nombre) (joueur) §8- Permet de retirer du chakra à un joueur (Ne peut arriver en dessous de 0)");
            helpList.add("§6/chakra §esetmax §7(nombre) (joueur) §8- Permet de changer le chakra maximal d'un joueur");
            helpList.add("§6/chakra §eset §7(nombre) (joueur) §8- Permet de mettre le chakra d'un joueur à une valeur précise (Peut dépasser le maximum)");
            helpList.add("§6/chakra §einfo §7(joueur) §8- Permet d'avoir les infos de chakra d'un joueur");
            helpList.add("§6/chakra §eaddtype §7(type) (joueur) §8- Permet d'ajouter une nature de chakra d'un joueur");
            helpList.add("§6/chakra §eremovetype §7(type) (joueur) §8- Permet de retirer une nature de chakra d'un joueur");
            helpList.add("§6/chakra §emission §7add (joueur) (nb)§8- Permet d'ajouter du chakra de mission.");
            helpList.add("§6/chakra §emission §7remove (joueur) (nb) §8- Permet de retirer du chakra de mission.");
            helpList.add("§6/chakra §emission §7get (joueur) §8- Permet de connaître le chakra de mission.");
        }

        for(String s : helpList){
            sender.sendMessage(s);
        }
    }
}