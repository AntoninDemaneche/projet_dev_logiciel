package com.ynov.projet.Features.listener;

import com.ynov.projet.Features.Inventory.SkillInventory;
import com.ynov.projet.Features.Feature;
import com.ynov.projet.Features.PlayerData.PlayerInfo;
import com.ynov.projet.Features.skill.Skill;
import com.ynov.projet.Features.skill.SkillMastery;
import com.ynov.projet.Features.utils.ItemUtil;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Optional;

import static com.ynov.projet.Features.commands.others.ParcheminCommand.GiveParchemin;


public class SkillInventoryListener extends Feature {


    @EventHandler
    public void onInvClick(InventoryClickEvent e){
        if(e.getClickedInventory() != null && (e.getView().getTitle().startsWith("§6Jutsu : ") || e.getView().getTitle().startsWith("§6Sceaux : "))){
            if(e.getWhoClicked() instanceof Player) {
                Player p = (Player) e.getWhoClicked();
                PlayerInfo pInfo = PlayerInfo.getPlayerInfo(p);
                Inventory inv = e.getClickedInventory();
                e.setCancelled(true);
                int slot = e.getSlot();
                if (slot == 35 && inv.getItem(slot) != null) {
                    p.openInventory(SkillInventory.getElementInventory(pInfo));
                    return;
                }
                if (slot == 30 && inv.getItem(slot) != null) {
                    int actualPage = Integer.parseInt(inv.getItem(31).getItemMeta().getDisplayName().substring(7));
                    if(!e.getView().getTitle().equals("§6Jutsu : §7Tous")) {
                        p.openInventory(SkillInventory.getMainInventory(pInfo, actualPage - 1, e.getView().getTitle().substring(10)));
                    }
                    else {
                        p.openInventory(SkillInventory.getMainInventory(pInfo, actualPage - 1));

                    }
                    return;
                }

                if (slot == 32 && inv.getItem(slot) != null) {
                    int actualPage = Integer.parseInt(inv.getItem(31).getItemMeta().getDisplayName().substring(7));
                    if(!e.getView().getTitle().equals("§6Jutsu : §7Tous")) {
                        p.openInventory(SkillInventory.getMainInventory(pInfo, actualPage + 1, e.getView().getTitle().substring(10)));
                    }
                    else {
                        p.openInventory(SkillInventory.getMainInventory(pInfo, actualPage + 1));
                    }
                    return;
                }


                if(inv.getItem(slot) != null){
                    if(e.getClickedInventory().getType() == InventoryType.CHEST) {
                        String skillName = inv.getItem(slot).getItemMeta().getDisplayName();
                        Skill skill = Skill.getByRealName(skillName);
                        if (skill != null) {
                            p.openInventory(SkillInventory.getSkillSelectInventory(skill, pInfo));
                        }
                    }
                }
            }
        }else if(e.getClickedInventory() != null && e.getView().getTitle().startsWith("§8Technique : ")) {
            if (e.getWhoClicked() instanceof Player) {
                e.setCancelled(true);
                Player p = (Player) e.getWhoClicked();

                /* Optional ...  = if(PlayerInfo.getPlayerInfo(p) != null) */
                Optional.ofNullable(PlayerInfo.getPlayerInfo(p)).ifPresent(pInfo -> {
                    Inventory inv = e.getClickedInventory();

                    ItemStack itemskillname = inv.getItem(2);
                    if (itemskillname == null) {
                        return;
                    }
                    ItemMeta im = itemskillname.getItemMeta();
                    if (im == null) {
                        return;
                    }
                    String skillName = im.getDisplayName();
                    /* Optional ... = if(Skill.getByRealName(skillname) != null) */
                    Optional.ofNullable(Skill.getByRealName(skillName)).ifPresent(skill -> {
                        int slot = e.getSlot();
                        switch (slot) {
                            case 0:
                                if (pInfo.getPlayer().isOp()) {
                                    GiveParchemin(skill, pInfo.getPlayer());
                                }
                            case 2:
                                if (skill.getInfosup() == null) {
                                    p.sendMessage("§cHRP : §7Cette compétence n'a pas encore de description détaillée. Patience !");
                                } else {
                                    PlayerInfo.getAppareanceBook(skill.getInfosup().replace("%displayname%", p.getDisplayName()).split(";"), p);
                                }
                                break;
                            case 3:
                                break;
                            case 4:
                                if (inv.getItem(slot).getType() == Material.STONE_BUTTON) {
                                    pInfo.setCurrentSkill(skill);
                                }
                                break;
                            case 5:
                                if (pInfo.getVoieNinja().getName().contains("Ninjutsu") && pInfo.getLvL("Ninjutsu") >= 2) {
                                    if (pInfo.getMastery(skill) == SkillMastery.LEARNED && !skill.getMudras().equals("none")) {
                                        if (pInfo.getMana() >= skill.manaToTake(pInfo)) {
                                            if (skill.TryJutsuOneHand(skill, pInfo)) {
                                                skill.takeMana(pInfo);


                                                String message = "§7[Une main] §c** " + p.getDisplayName() + " §créalise la technique "+skillName;
                                                int range = 25;

                                                TextComponent messagecomponent = new TextComponent(message);
                                                messagecomponent.setColor(net.md_5.bungee.api.ChatColor.RED);

                                                BaseComponent[] texte = new BaseComponent[]{
                                                        new TextComponent(ItemUtil.convertItemStackToJsonRegular(ItemUtil.createItemStack(Material.BOOK, 1, skillName, skill.getLore(p)))) // The only element of the hover events basecomponents is the item json
                                                };

                                                Skill.affichejutsu(p, pInfo, range, messagecomponent, texte, skill.isSkillVisibility(), skill.isNeedTarget(), pInfo.getTarget());

                                                //Lancement de la technique
                                                ArrayList<String> commandToRun = skill.getCommandList();
                                                skill.runCommands(commandToRun, 0, p);
                                                p.closeInventory();
                                            }
                                        } else {
                                            p.sendMessage("§cVous n'avez pas assez de chakra pour lancer la technique. (§b" + pInfo.getMana() + "/" + skill.getManaCost());
                                        }
                                    }
                                }
                                /*
                                    Le roll bonus est affiché ici.
                                */
                                break;
                            case 6:
                                if (inv.getItem(slot) != null && inv.getItem(slot).hasItemMeta() && inv.getItem(slot).getItemMeta().hasDisplayName() &&
                                        inv.getItem(slot).getItemMeta().getDisplayName().startsWith("§2Ajouter")) {
                                    if (pInfo.getFavoriteList().size() < 27) {
                                        pInfo.getFavoriteList().add(skill);
                                    } else {
                                        p.sendMessage(ChatColor.RED + "Vous ne pouvez pas avoir plus de 27 favoris");
                                    }
                                } else if (inv.getItem(slot) != null && inv.getItem(slot).hasItemMeta() && inv.getItem(slot).getItemMeta().hasDisplayName() &&
                                        inv.getItem(slot).getItemMeta().getDisplayName().startsWith("§4Retirer")) {
                                    pInfo.getFavoriteList().remove(skill);
                                }
                                p.openInventory(SkillInventory.getSkillSelectInventory(skill, pInfo));
                                break;
                            case 7:
                                p.openInventory(SkillInventory.getElementInventory(pInfo));
                                break;

                            default:
                                break;
                        }
                    });


                });
            }
        }else if(e.getClickedInventory() != null && e.getView().getTitle().equals("§6Fuinjutsu : §7Choix du type de sceau")) {
            if(e.getWhoClicked() instanceof Player) {
                Player p = (Player) e.getWhoClicked();
                PlayerInfo pInfo = PlayerInfo.getPlayerInfo(p);
                Inventory inv = e.getClickedInventory();
                e.setCancelled(true);

                int slot = e.getSlot();
                ItemStack item = inv.getItem(slot);
                if(item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
                    return;
                }
                if(slot == 2 || slot == 4 || slot == 6) {
                    p.openInventory(SkillInventory.getSceaux(pInfo, item.getItemMeta().getDisplayName().substring(2), 0));
                    return;
                }
                if(slot == 13) {
                    p.openInventory(SkillInventory.getElementInventory(pInfo));
                }
            }
        }
        else if(e.getClickedInventory() != null && e.getView().getTitle().equals("§8Techniques favorites")){
            if(e.getWhoClicked() instanceof Player) {
                Player p = (Player) e.getWhoClicked();
                PlayerInfo pInfo = PlayerInfo.getPlayerInfo(p);
                Inventory inv = e.getClickedInventory();
                e.setCancelled(true);

                int slot = e.getSlot();

                if(slot == 31 && inv.getItem(slot) != null) {
                    p.openInventory(SkillInventory.getElementInventory(pInfo));
                    return;
                }

                if (inv.getItem(slot) != null) {
                    if(e.getClickedInventory().getType() == InventoryType.CHEST) {
                        String skillName = inv.getItem(slot).getItemMeta().getDisplayName();
                        Skill skill = Skill.getByRealName(skillName);
                        if (skill != null) {
                            p.openInventory(SkillInventory.getSkillSelectInventory(skill, pInfo));
                        }
                    }
                }
            }
        }else if (e.getClickedInventory() != null && e.getView().getTitle().equals("§6Eléments")){
            if (e.getWhoClicked() instanceof Player){
                Player p = (Player) e.getWhoClicked();
                PlayerInfo pInfo = PlayerInfo.getPlayerInfo(p);
                Inventory inv = e.getClickedInventory();
                e.setCancelled(true);

                int slot = e.getSlot();
                ItemStack item = inv.getItem(slot);

                if(slot == 37 && inv.getItem(slot) != null){
                    if(!pInfo.getFavoriteList().isEmpty()) {
                        p.openInventory(SkillInventory.getFavoriteInventory(pInfo));
                    }else{
                        p.sendMessage(ChatColor.RED + "Vous n'avez aucune technique favorite !");
                    }
                    return;
                }

                if(slot == 49 && inv.getItem(slot) != null) {
                  p.openInventory(SkillInventory.getMainInventory(pInfo, 0));
                  return;
                }
                if (item != null && item.getType() != Material.GRAY_STAINED_GLASS_PANE){
                    if(e.getClickedInventory().getType() == InventoryType.CHEST) {
                        String skillName = item.getItemMeta().getDisplayName();
                        if(skillName.startsWith("§")) {
                            skillName = skillName.substring(2);
                        }
                        /* Pour ne pas ouvrir l'inventaire en conséquence */
                        if (SkillInventory.GetJutsuType(pInfo.getSkills()).contains(skillName)) {
                            p.openInventory(SkillInventory.getMainInventory(pInfo, 0, skillName));
                        }
                        else {
                            p.sendMessage("§cVous n'avez pas de jutsu dans cette catégorie.");
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInvDrag(InventoryDragEvent e){
        if(e.getView().getTitle().startsWith("§6Eléments") || e.getView().getTitle().startsWith("§6Jutsu : ") || e.getView().getTitle().equals("§6Fuinjutsu : §7Choix du type de sceau")){
            e.setCancelled(true);
        }
    }
}
