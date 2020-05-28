package com.ynov.projet.Features.listener;

import com.ynov.projet.Features.Inventory.AbilityInventory;
import com.ynov.projet.Features.Inventory.CompetenceInventory;
import com.ynov.projet.Features.Inventory.TrainInventory;
import com.ynov.projet.Features.Feature;
import com.ynov.projet.Features.PlayerData.PlayerInfo;
import com.ynov.projet.Features.ability.Ability;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Objects;

//import net.minecraft.server.v1_14_R1.ChatClickable;
//import net.minecraft.server.v1_14_R1.ChatMessage;
//import net.minecraft.server.v1_14_R1.EnumChatFormat;
//import net.minecraft.server.v1_14_R1.IChatBaseComponent;
//import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;


public class TrainInventoryListener extends Feature {

    @EventHandler
    public void onInvClick(InventoryClickEvent e){
        if(e.getView().getTitle().equals("§cEntraînements")){
       /*     e.setCancelled(true);

            Player p = (Player) e.getWhoClicked();
            PlayerInfo pInfo = PlayerInfo.getPlayerInfo(p);
            switch (e.getSlot()){
                case 1:
                    p.openInventory(TrainInventory.getFichePerso(pInfo, p));
                    break;
                case 3:
                    if(!Main.hasTrainTicket(p.getName())) {
                        if(pInfo.getPlayerStats().getTrainDelay().get(TrainType.A) <= System.currentTimeMillis()) {
                            if (pInfo.getPlayerStats().getTrainToken().get(TrainType.A) > 0) {
                                p.openInventory(TrainInventory.getTrainInventory(pInfo.getPlayerStats(), TrainType.A));
                            } else {
                                p.sendMessage(ChatColor.RED + "Vous n'avez plus de jetons d'entraînement dans cette catégorie.");
                            }
                        }else{
                            Date date = new Date(pInfo.getPlayerStats().getTrainDelay().get(TrainType.A));
                            p.sendMessage(ChatColor.RED + "Votre prochain entraînement pourra être fait à partir de " + date.toString());
                        }
                    }else{
                        p.sendMessage(ChatColor.RED + "Vous avez déjà une demande d'entraînement en attente.");
                    }
                    break;
                case 4:
                    if(!Main.hasTrainTicket(p.getName())) {
                        if(pInfo.getPlayerStats().getTrainDelay().get(TrainType.B) <= System.currentTimeMillis()) {
                            p.openInventory(TrainInventory.getTrainInventory(pInfo.getPlayerStats(), TrainType.B));
                        }else{
                            Date date = new Date(pInfo.getPlayerStats().getTrainDelay().get(TrainType.B));
                            p.sendMessage(ChatColor.RED + "Votre prochain entraînement pourra être fait à partir de " + date.toString());
                        }
                    }else{
                        p.sendMessage(ChatColor.RED + "Vous avez déjà une demande d'entraînement en attente.");
                    }
                    break;
                case 5:
                    if(!Main.hasTrainTicket(p.getName())) {
                        if(pInfo.getPlayerStats().getTrainDelay().get(TrainType.C) <= System.currentTimeMillis()) {
                            if (pInfo.getPlayerStats().getTrainToken().get(TrainType.C) > 0) {
                                p.openInventory(TrainInventory.getTrainInventory(pInfo.getPlayerStats(), TrainType.C));
                            } else {
                                p.sendMessage(ChatColor.RED + "Vous n'avez plus de jetons d'entraînement dans cette catégorie.");
                            }
                        }else{
                            Date date = new Date(pInfo.getPlayerStats().getTrainDelay().get(TrainType.C));
                            p.sendMessage(ChatColor.RED + "Votre prochain entraînement pourra être fait à partir de " + date.toString());
                        }
                    }else{
                        p.sendMessage(ChatColor.RED + "Vous avez déjà une demande d'entraînement en attente.");
                    }
                    break;
            }*/
        }else if(e.getView().getTitle().startsWith("§8Fiche personnage de ")) {
            int slot = e.getSlot();
            Player p = (Player) e.getWhoClicked();
            e.setCancelled(true);
            ItemStack skull = e.getInventory().getItem(12);
            if (skull == null) {
                return;
            }
            SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
            if (skullMeta == null || skullMeta.getOwningPlayer() == null || skullMeta.getOwningPlayer().getName() == null) {
                return;
            }

            Player target = Bukkit.getPlayer(skullMeta.getOwningPlayer().getName());
            PlayerInfo pInfo = PlayerInfo.getPlayerInfo(target);
            if (slot == 12) {
                if (pInfo.getApparenceprofil() != null && !pInfo.getApparenceprofil().equals("")) {
                    PlayerInfo.getAppareanceBook(pInfo.getApparenceprofil().split(";"), p);
                } else {
                    p.closeInventory();
                    p.sendMessage("§cHRP : §7Ce joueur n'a pas d'apparence disponible sur son profil.");
                }
                return;
            }
            // 20 22 24
            if (slot == 20 && (p.isOp() || p.getName().equals(pInfo.getPlayer().getName()))) {
                p.openInventory(AbilityInventory.getAbilitiesInventory(pInfo, pInfo.getStyleCombat().getName(), p, 0));
                return;
            }
            if (slot == 22 && (p.isOp() || p.getName().equals(pInfo.getPlayer().getName()))) {
                p.openInventory(AbilityInventory.getAbilitiesInventory(pInfo, pInfo.getClan().getName(), p, 0));
                return;
            }
            if (slot == 24 && (p.isOp() || p.getName().equals(pInfo.getPlayer().getName()))) {
                p.openInventory(AbilityInventory.getAbilitiesInventory(pInfo, pInfo.getVoieNinja().getName(), p, 0));
                return;
            }
            if (slot == 31) {
                p.openInventory(AbilityInventory.getAbilitiesInventory(pInfo, "Autre", p, 0));
                return;
            }
            if (slot == 37) {
                p.openInventory(AbilityInventory.getAbilitiesInventory(pInfo, "Force", p, 0));
                return;
            }
            if (slot == 39) {
                p.openInventory(AbilityInventory.getAbilitiesInventory(pInfo, "Vitesse", p, 0));
                return;
            }
            if (slot == 41) {
                p.openInventory(AbilityInventory.getAbilitiesInventory(pInfo, "Perception de la vitesse", p, 0));
                return;
            }
            if (slot == 43) {
                p.openInventory(AbilityInventory.getAbilitiesInventory(pInfo, "Instinct et expérience", p, 0));
            }
        }
        else if(e.getView().getTitle().startsWith("§8Apprentissage de compétences")) {
            int slot = e.getSlot();
            Player p = (Player)e.getWhoClicked();
            e.setCancelled(true);
            PlayerInfo pInfo = PlayerInfo.getPlayerInfo(p);
            if (slot == 31) {
                p.openInventory(CompetenceInventory.getCompetenceType(pInfo, p, "Autre"));
                return;
            }
            if (slot == 37) {
                p.openInventory(CompetenceInventory.getCompetenceType(pInfo, p, "Force"));
                return;
            }
            if (slot == 39) {
                p.openInventory(CompetenceInventory.getCompetenceType(pInfo, p, "Vitesse"));
                return;
            }
            if (slot == 41) {
                p.openInventory(CompetenceInventory.getCompetenceType(pInfo, p, "Perception de la vitesse"));
                return;
            }
            if (slot == 43) {
                p.openInventory(CompetenceInventory.getCompetenceType(pInfo, p, "Instinct et expérience"));
            }

        }else if(e.getView().getTitle().startsWith("§8Apprentissage :")) {
            int slot = e.getSlot();
            Player p = (Player)e.getWhoClicked();
            e.setCancelled(true);
            ItemStack item = e.getInventory().getItem(slot);
            if(item != null) {
                if(item.getType() == Material.GREEN_WOOL) {
                    if (item.getItemMeta() != null) {
                        if (Ability.getByRealName(item.getItemMeta().getDisplayName()) != null) {
                            p.openInventory(CompetenceInventory.getConfirmationCompetence(PlayerInfo.getPlayerInfo(p), p, item.getItemMeta().getDisplayName()));
                        }
                    }
                }
                if(slot == 31) {
                    p.openInventory(CompetenceInventory.getCompetenceGenerale(PlayerInfo.getPlayerInfo(p), p));
                }
            }
        }
        else if (e.getView().getTitle().startsWith("§7Confirmation :")) {
            int slot = e.getSlot();
            Player p = (Player)e.getWhoClicked();
            PlayerInfo pInfo = PlayerInfo.getPlayerInfo(p);
            e.setCancelled(true);
            ItemStack item = e.getInventory().getItem(slot);
            if(item != null) {
                String name = e.getView().getTitle().substring(17);
                if(item.getType() == Material.GREEN_WOOL) {
                    Ability ability = Ability.getByRealName(name);
                    if(ability != null) {
                        if(!pInfo.getAbilities().contains(ability)) {
                            pInfo.setPoints(pInfo.getPoints() + pInfo.getPointsToAbility(ability.getNameInPlugin()) - ability.getPts());
                            pInfo.deletePointstoAbilities(ability);
                            pInfo.updateAbility(ability);
                            p.closeInventory();
                        }
                    }
                }
                else if(item.getType() == Material.BOOK) {
                    Ability ability = Ability.getByRealName(name);
                    if(ability != null) {
                        p.closeInventory();
                        AbilityInventory.openInBook(p, ability.getDescription().split(";"));
                    }
                }
                else {
                    p.sendMessage("§cVous avez annulé l'obtention de la compétence : §6"+name);
                    p.closeInventory();
                }
            }
        }
        else if (e.getView().getTitle().startsWith("§8Compétence ")) {
            int slot = e.getSlot();
            Player p = (Player)e.getWhoClicked();
            e.setCancelled(true);

            if(slot == 31) {
                ItemStack skull = e.getInventory().getItem(31);
                if(skull == null) {
                    return;
                }
                SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
                if(skullMeta == null || skullMeta.getOwningPlayer() == null || skullMeta.getOwningPlayer().getName() == null) {
                    return;
                }

                Player target = Bukkit.getPlayer(skullMeta.getOwningPlayer().getName());
                PlayerInfo pInfo = PlayerInfo.getPlayerInfo(target);
                p.openInventory(TrainInventory.getFichePerso(pInfo, p));
                return;
            }

            if((e.getInventory().getItem(30) != null && slot == 30) || (e.getInventory().getItem(32) != null && slot == 32)) {
                ItemStack skull = e.getInventory().getItem(31);
                if(skull == null) {
                    return;
                }
                SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
                if(skullMeta == null || skullMeta.getOwningPlayer() == null || skullMeta.getOwningPlayer().getName() == null || !skullMeta.hasLore()) {
                    return;
                }
                int page = Integer.parseInt(Objects.requireNonNull(skullMeta.getLore()).get(0).substring(skullMeta.getLore().get(0).length()-1));
                Player target = Bukkit.getPlayer(skullMeta.getOwningPlayer().getName());
                PlayerInfo pInfo = PlayerInfo.getPlayerInfo(target);
                String type =  e.getView().getTitle().replace("§8Compétence : ", "");
                if(slot == 30) {
                    page = page - 2;
                }
                p.openInventory(AbilityInventory.getAbilitiesInventory(pInfo, type, p, page));
                return;

            }

            ItemStack item = e.getInventory().getItem(slot);
            if(item != null && item.getItemMeta() != null) {
                AbilityInventory.getAbilityToBook(p, Ability.getByRealName(item.getItemMeta().getDisplayName()));
            }
        } /*  e.setCancelled(true);
            Player p = (Player) e.getWhoClicked();
            PlayerInfo pInfo = PlayerInfo.getPlayerInfo(p);

            String name = "§cEntraînements : ";
            String typeName = e.getView().getTitle().substring(name.length());
            TrainType type = TrainType.fromName(typeName);

            if(e.getSlot() == 8){
                p.openInventory(TrainInventory.getMainInventory(pInfo.getPlayerStats()));
                return;
            }
            if(type != null){
                if(type == TrainType.B){
                    switch (e.getSlot()){
                        case 2:
                            launchMediation(pInfo, 5);
                            break;
                        case 3:
                            launchMediation(pInfo, 10);
                            break;
                        case 4:
                            launchMediation(pInfo, 15);
                            break;
                        case 5:
                            launchMediation(pInfo, 30);
                            break;
                        case 6:
                            launchMediation(pInfo, 60);
                            break;
                    }
                }else{

                    ItemStack item = e.getInventory().getItem(e.getSlot());
                    if(item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName() && RPStat.fromName(item.getItemMeta().getDisplayName().substring(2)) != null);
                    RPStat stat = RPStat.fromName(item.getItemMeta().getDisplayName().substring(2));
                    p.closeInventory();


                    for(Player online : Bukkit.getOnlinePlayers()){
                        if(online.isOp()){
                            online.sendMessage(pInfo.getPlayer().getDisplayName() + ChatColor.DARK_AQUA + " effectue un entraînement pour la statistique " + ChatColor.AQUA + stat.getName());
                        }
                    }

                    int progress = 0;

                    Random r = new Random();
                    for(int i = 0; i< pInfo.getRank().getMaxDice(); i++){
                        int roll = r.nextInt(39) + 1;
                        p.sendMessage(ChatColor.GOLD + "[" + ChatColor.GRAY + roll + "/40" + ChatColor.GOLD + "]");
                        progress += roll;
                    }

                    progress += pInfo.getPlayerStats().getProgress().get(stat);

                    long delay = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(3) + TimeUnit.MINUTES.toMillis(30);
                    pInfo.getPlayerStats().getTrainDelay().put(type, delay);

                    if(progress >= pInfo.getPlayerStats().getNeededPointsForNextLevel(stat)){
                        int newLevel = pInfo.getPlayerStats().getStats().get(stat) + 1;
                        TrainTicket ticket = new TrainTicket(p.getName(), p.getUniqueId().toString(), p.getDisplayName(), newLevel, stat);
                        Main.getTrainTickets().add(ticket);

                        p.sendMessage(ChatColor.GREEN + "Votre montée de niveau doit désormais être validée par le staff !");
                        for(Player onlinePlayer : Bukkit.getOnlinePlayers()){
                            if(onlinePlayer.isOp()){
                                onlinePlayer.sendMessage(ChatColor.AQUA + "Une nouvelle demande d'entraînement a été ajoutée à la liste. " + ChatColor.DARK_AQUA + "(/train list pour la voir)");
                            }
                        }
                    }else{
                        pInfo.getPlayerStats().getProgress().put(stat, progress);
                        int pointsRemaining = pInfo.getPlayerStats().getNeededPointsForNextLevel(stat) - pInfo.getPlayerStats().getProgress().get(stat);
                        p.sendMessage(ChatColor.GREEN + "Il ne vous reste que " + ChatColor.GOLD + pointsRemaining + ChatColor.GREEN + " points à obtenir avant le prochain niveau !");
                    }
                }
            }
        }
    }

    private void launchMediation(PlayerInfo pInfo, int minutes){
        if(!Main.getMeditationList().containsKey(pInfo.getPlayer())) {
            if ((minutes + pInfo.getPlayerStats().getMinutesDone()) <= 60) {
                int taskID = Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        if (Main.getMeditationList().containsKey(pInfo.getPlayer())) {
                            Main.getMeditationList().remove(pInfo.getPlayer());

                            int progress = pInfo.getPlayerStats().getProgress().get(RPStat.CHAKRA) + minutes;

                            if (progress >= pInfo.getPlayerStats().getMeditTime()) {
                                pInfo.getPlayerStats().getProgress().put(RPStat.CHAKRA, 0);

                                Player p = pInfo.getPlayer();
                                int newLevel = pInfo.getPlayerStats().getStats().get(RPStat.CHAKRA) + 10;
                                TrainTicket ticket = new TrainTicket(p.getName(), p.getUniqueId().toString(), p.getDisplayName(), newLevel, RPStat.CHAKRA);
                                Main.getTrainTickets().add(ticket);

                                p.sendMessage(ChatColor.GREEN + "Votre montée de niveau doit désormais être validée par le staff !");
                                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                                    if (onlinePlayer.isOp()) {
                                        onlinePlayer.sendMessage(ChatColor.AQUA + "Une nouvelle demande d'entraînement a été ajoutée à la liste. " + ChatColor.DARK_AQUA + "(/train list pour la voir)");
                                    }
                                }
                            } else {
                                pInfo.getPlayerStats().getProgress().put(RPStat.CHAKRA, progress);
                                int minutesRemaining = pInfo.getPlayerStats().getMeditTime() - pInfo.getPlayerStats().getProgress().get(RPStat.CHAKRA);
                                pInfo.getPlayer().sendMessage(ChatColor.GREEN + "Il ne vous reste que " + ChatColor.GOLD + minutesRemaining + " minutes de méditation à effectuer avant le prochain niveau !");
                            }

                            //Calcul du délai
                            int minutesDone = pInfo.getPlayerStats().getMinutesDone() + minutes;

                            if (minutesDone >= 60) { //On met un délai d'une journée toutes les 1h de méditation, pour éviter le tryhard
                                pInfo.getPlayerStats().setMinutesDone(0);
                                long delay = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(21);
                                pInfo.getPlayerStats().getTrainDelay().put(TrainType.B, delay);
                            } else {
                                pInfo.getPlayerStats().setMinutesDone(minutesDone);
                            }
                        }
                    }
                }, 20 * 60 * minutes);

                Main.getMeditationList().put(pInfo.getPlayer(), taskID);


                for (Player online : Bukkit.getOnlinePlayers()) {
                    if (online.isOp()) {
                        online.sendMessage(pInfo.getPlayer().getDisplayName() + ChatColor.DARK_AQUA + " a lancé une séance de méditation de " + minutes + " minutes.");
                    }
                }
                pInfo.getPlayer().sendMessage(ChatColor.AQUA + "Vous avez lancé une séquence de méditation de " + minutes + " minutes, pour qu'elle soit comptabilisée veuillez ne pas vous déconnecter ou AFK !");
                pInfo.getPlayer().sendMessage(ChatColor.DARK_AQUA + "Il est NECESSAIRE de faire des actions avec le prefixe *, si vous ne le faites pas, c'est un motif de refus de votre méditation.");

                launchMeditationTrigger(pInfo);
            } else {
                pInfo.getPlayer().sendMessage(ChatColor.RED + "Un délai d'attente est mis toutes les heures de méditation cumulées, le temps que vous avez choisi dépasse l'heure, veuillez choisir un temps plus court.");
            }*/
    }
/*
    public static void launchMeditationTrigger(PlayerInfo pInfo){
        if(!Main.getMeditationTrigger().containsKey(pInfo.getPlayer())) {
            int taskid = Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
                @Override
                public void run() {
                    launchMeditationConfirm(pInfo);
                    Main.getMeditationTrigger().remove(pInfo.getPlayer());
                }
            }, 20 * 60 * 4); //4 minutes

            Main.getMeditationTrigger().put(pInfo.getPlayer(), taskid);
        }
    }

    public static void launchMeditationConfirm(PlayerInfo playerInfo){
        if (Main.getMeditationList().containsKey(playerInfo.getPlayer())) { //Si le joueur est toujours en train de méditer
            if(!Main.getMeditationConfirm().containsKey(playerInfo.getPlayer())) {
                int taskid = Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        if (Main.getMeditationList().containsKey(playerInfo.getPlayer()) && Main.getMeditationConfirm().containsKey(playerInfo.getPlayer())) {
                            //Si la condition est vérifiée, le joueur est considéré comme AFK pendant la méditation, et la méditation est annulée
                            Main.getMeditationConfirm().remove(playerInfo.getPlayer());

                            Bukkit.getScheduler().cancelTask(Main.getMeditationList().get(playerInfo.getPlayer()));
                            Main.getMeditationList().remove(playerInfo.getPlayer());

                            playerInfo.getPlayer().sendMessage(ChatColor.RED + "Vous avez été considéré comme AFK. Votre méditation ne sera pas comptabilisée");

                            for (Player online : Bukkit.getOnlinePlayers()) {
                                if (online.isOp()) {
                                    online.sendMessage(playerInfo.getPlayer().getDisplayName() + ChatColor.RED + " a AFK pendant sa méditation.");
                                }
                            }
                        }
                    }
                }, 20 * 45); //45 secondes après la demande

                Main.getMeditationConfirm().put(playerInfo.getPlayer(), taskid);
                playerInfo.getPlayer().sendMessage(ChatColor.DARK_RED + "Veuillez confirmer que vous n'êtes pas AFK avec la commande /train confirm ou en cliquant sur le message ci-dessous, vous avez 45 secondes");

                //IChatBaseComponent clickHereMessage = new ChatMessage("Cliquez Ici");
                //clickHereMessage.getChatModifier().setColor(EnumChatFormat.YELLOW);
                //clickHereMessage.getChatModifier().setBold(true);
                //clickHereMessage.getChatModifier().setChatClickable(new ChatClickable(ChatClickable.EnumClickAction.RUN_COMMAND, "/train confirm"));

                //((CraftPlayer) playerInfo.getPlayer()).getHandle().sendMessage(clickHereMessage);
            }
        }

    }
*/
}
