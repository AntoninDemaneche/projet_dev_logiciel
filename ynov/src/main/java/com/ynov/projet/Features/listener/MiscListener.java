package com.ynov.projet.Features.listener;

import com.ynov.projet.Features.Inventory.CasinoInventory;
import com.ynov.projet.Features.objectnum.Figurine;
import com.ynov.projet.Features.Feature;
import com.ynov.projet.Features.PlayerData.PlayerInfo;
import com.ynov.projet.Features.skill.Skill;
import com.ynov.projet.Features.skill.SkillMastery;
import com.ynov.projet.Features.utils.ItemUtil;
import com.ynov.projet.Main;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Score;

public class MiscListener extends Feature {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnPrivateMessage(PlayerCommandPreprocessEvent event) {
        System.out.println(event.getMessage());
        if(event.getMessage().startsWith("/w ") || event.getMessage().startsWith("/tell ") || event.getMessage().startsWith("/msg ")) {
            String[] command = event.getMessage().split(" ");
            if (command.length > 2) {
                Player p = Bukkit.getPlayer(command[1]);
                if (p != null) {
                    StringBuilder msg = new StringBuilder();
                    for (int i = 2; i < command.length; i++) {
                        msg.append(command[i]);
                        msg.append(" ");
                    }
                    event.getPlayer().sendMessage(ChatColor.GRAY+""+ChatColor.ITALIC+"Vous chuchotez à " + p.getName() + " : " + msg.toString());
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void OnHitBlock(PlayerInteractEvent event) {
        // Si c'est un clic gauche
        if(event.getAction() == Action.LEFT_CLICK_BLOCK) {
            if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.BARRIER) {
                Location locvase = (event.getClickedBlock().getLocation().add(0,-1,0));
                ItemFrame itemframe = getItemFrameAt(locvase);
                if(itemframe!= null) {
                    if(itemframe.getItem().getType() == Material.CLOCK) {
                        ItemStack vase = itemframe.getItem();
                        if(isVase(vase)) {
                            if(Bukkit.getScoreboardManager() != null) {
                                Score score = Bukkit.getScoreboardManager().getMainScoreboard().getObjective("vase").getScore("Veziah");
                                Location l = event.getClickedBlock().getLocation();
                                if (score.getScore() != 100000000) {
                                    if (l.getWorld() != null) {
                                        l.getWorld().spawnParticle(Particle.DAMAGE_INDICATOR, l.getX() + 0.5, l.getY() + 0.5, l.getZ() + 0.5, 5);
                                        l.getWorld().spawnParticle(Particle.SMOKE_NORMAL, l.getX() + 0.2, l.getY() + 0.2, l.getZ() + 0.2, 5);
                                        int value = Bukkit.getScoreboardManager().getMainScoreboard().getObjective("vase").getScore("Veziah").getScore();
                                        if(LimiteCoup(event.getPlayer())) {
                                            PlayerInfo playerInfo = PlayerInfo.getPlayerInfo(event.getPlayer());
                                            score.setScore(value + playerInfo.getCaract().getForce());
                                            switch (score.getScore()) {
                                                case 25000000:
                                                    itemframe.setItem(updateVase(vase, "vase1"));
                                                    // Un peu cassé
                                                    break;
                                                case 50000000:
                                                    itemframe.setItem(updateVase(vase, "vase2"));
                                                    // Un peu bcp cassé
                                                    break;
                                                case 75000000:
                                                    itemframe.setItem(updateVase(vase, "vase3"));
                                                    // Bcp cassé
                                                    break;
                                                case 100000000:
                                                    itemframe.setItem(updateVase(vase, "vase4"));
                                                    // CASsE
                                                    break;
                                            }
                                        }
                                    }
                                }
                                else {
                                    event.getPlayer().sendMessage(ChatColor.AQUA+"** Le vase se casse au prochain coup...");
                                    event.getPlayer().sendMessage(ChatColor.RED+"HRP : "+ ChatColor.GRAY+" Contactez Shikure en HRP.");
                                }
                            }
                        }
                    }
                }
            }
        }
        else if(event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.STONE_BUTTON){
            Location locclef = (event.getClickedBlock().getLocation().add(0,1,0));
            ItemFrame itemframe = getItemFrameAt(locclef);
            if(itemframe != null) {
                ItemStack itemStack = itemframe.getItem();
                if (itemStack.getType() == Material.CLOCK && ItemUtil.hasTag(itemStack, "seisan", "clef_or") && itemStack.hasItemMeta() && itemStack.getItemMeta() != null && itemStack.getItemMeta().hasDisplayName() && itemStack.getItemMeta().getDisplayName().equals("§6TENTEZ VOTRE CHANCE")) {
                    event.getPlayer().openInventory(CasinoInventory.getCasino(event.getPlayer()));
                    Listener.loccasino.put(event.getPlayer().getName(), locclef);
                }
            }
        }
    }

   /*
   @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void LogCopy(InventoryCreativeEvent event) {
        System.out.println("aaaa");
        if(event.getWhoClicked().getGameMode() == GameMode.CREATIVE) {
            if(event.getCurrentItem() == null) {
                return;
            }
                System.out.println("§cHRP : §7" + event.getWhoClicked().getName() + " a copié " + event.getCursor().getType().name());

            if(event.getCursor().getType() == Material.GOLD_NUGGET) {
                for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                    if(p.isOp()) {
                        p.sendMessage("§cHRP : "+event.getWhoClicked().getName() + "§7 a copié " + event.getCursor().getType().name());
                    }
                }
            }
        }
    }*/

    private boolean isVase(ItemStack vase) {
        boolean isVase = false;
            net.minecraft.server.v1_15_R1.ItemStack stack = CraftItemStack.asNMSCopy(vase);
            NBTTagCompound tag = stack.getTag() != null ? stack.getTag() : new NBTTagCompound();
            if(tag.hasKey("seisan") && tag.getString("seisan").contains("vase")) {
                // Blabla vérif du tag
                isVase = true;
        }
        return isVase;
    }

    private ItemStack updateVase(ItemStack vase, String newvase) {
        net.minecraft.server.v1_15_R1.ItemStack stack = CraftItemStack.asNMSCopy(vase);
        NBTTagCompound tag = stack.getTag() != null ? stack.getTag() : new NBTTagCompound();
        if(tag.hasKey("seisan") && tag.getString("seisan").contains("vase")) {
            // Blabla vérif du tag
            tag.setString("seisan", newvase);
        }
        stack.setTag(tag);
        return CraftItemStack.asBukkitCopy(stack);

    }

    private ItemFrame getItemFrameAt(Location l) {
        ItemFrame frame = null;
        if(l.getWorld() != null) {
            for (Entity entity : l.getWorld().getEntities()) {
                if(entity instanceof ItemFrame) {
                    if(entity.getLocation().getBlock().getLocation().equals(l)) {
                        frame = (ItemFrame)entity;
                    }
                }
            }
        }
        return frame;
    }

    private boolean LimiteCoup(Player player) {
        boolean test = false;

        if(!Listener.antimaccro.containsKey(player.getName())) {
            Listener.antimaccro.put(player.getName(), 1);
            test = true;
        }
        else {
            int nbcoup = Listener.antimaccro.get(player.getName());
            if(nbcoup <= 15) {
                test = true;
                Listener.antimaccro.put(player.getName(), nbcoup+1);
            }
        }

        if(test) Bukkit.getScheduler().runTaskLater(Main.plugin(), () -> Listener.antimaccro.put(player.getName(), Listener.antimaccro.get(player.getName())-1), 20);
        return test;
    }

    @EventHandler
    public void onExp(PlayerExpChangeEvent e){
        if(!Main.getManaRegenList().contains(e.getPlayer()))
            e.setAmount(0);
    }

    @EventHandler
    public void onDie(PlayerDeathEvent e){
        e.setDeathMessage("");
    }

    @EventHandler
    public void MountNinken(PlayerInteractEntityEvent e) {
        String name = e.getPlayer().getName();
        if(e.getHand() == EquipmentSlot.HAND) {
            return;
        }
        if(e.getRightClicked() instanceof Wolf) {
            if (Main.getIDninkenFromNamePlayer().containsKey(name)) {
                if(e.getRightClicked().getUniqueId().equals(Main.getIDninkenFromNamePlayer().get(name))) {
                    e.getPlayer().sendMessage("§b** Votre Ninken est trop petit pour le monter.");
                }
            }
        }
    }

    @EventHandler
    public void onLearnJutsu(PlayerInteractEvent e) {
        if (e.getHand() != null && e.getHand().equals(EquipmentSlot.HAND)) {
            if (e.getPlayer().isSneaking()) {
                if(e.getItem() != null && e.getItem().getType() == Material.BOOK) {
                    ItemStack stack = e.getItem();
                    if(ItemUtil.hasTag(stack, "jutsu", "learn")) {
                        PlayerInfo pInfo = PlayerInfo.getPlayerInfo(e.getPlayer());
                        Skill skill = Skill.getByRealName(stack.getItemMeta().getDisplayName());
                        if(skill != null) {
                            if (!pInfo.getSkills().containsKey(skill)) {
                                pInfo.updateSkill(skill, SkillMastery.UNLEARNED);
                                pInfo.getPlayer().getInventory().setItemInMainHand(null);
                            }
                            else {
                                e.getPlayer().sendMessage("§cHRP : §7Vous connaissez déjà la technique "+skill.getName());
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onOpenItem(PlayerInteractEvent e) {
        if(e.getHand() != null && e.getHand().equals(EquipmentSlot.HAND)) {
            if(e.getPlayer().isSneaking()) {
                if(e.getItem() != null && e.getItem().getType() == Material.PAPER) {
                    ItemStack stack = e.getItem();
                    if(ItemUtil.hasTag(stack, "seisan", "boite_sankamaisu")) {
                        e.setCancelled(true);
                        if(e.getPlayer().getInventory().firstEmpty() != -1) {
                            if (e.getItem().getAmount() > 1) {
                                e.getItem().setAmount(e.getItem().getAmount() - 1);
                                e.getPlayer().getInventory().setItemInMainHand(e.getItem());
                            } else {
                                e.getPlayer().getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                            }
                            e.getPlayer().getInventory().addItem(Figurine.getFromLuck());
                            e.getPlayer().updateInventory();
                        }
                        else {
                            e.getPlayer().sendMessage("§cHRP : §7Vous n'avez pas assez de place dans votre inventaire.");
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void commandPreprocess(PlayerCommandPreprocessEvent e) {
        String command = e.getMessage().split(" ")[0];
        command = command.substring(1);
        if(command.equalsIgnoreCase("reload")) {
            e.getPlayer().sendMessage(ChatColor.RED + "Le reload casse le plugin de Seisan, faites un /stop à la place");
            e.setCancelled(true);
        }else if(command.equalsIgnoreCase("help")){
            e.getPlayer().sendMessage(ChatColor.RED + "Bien tenté, mais non, pas de /help");
            e.setCancelled(true);
        }else if(command.equalsIgnoreCase("pl") || command.equalsIgnoreCase("plugin") || command.equalsIgnoreCase("plugins")){
            e.getPlayer().sendMessage("Plugins (1): " + ChatColor.GREEN + "Seisan");
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void consoleCommandPreprocess(ServerCommandEvent e) {
        if (e.getCommand().equalsIgnoreCase("reload")) {
            e.getSender().sendMessage(ChatColor.RED + "Le reload casse le plugin de Seisan, faites un /stop à la place");
            e.setCancelled(true);
        }
    }
}
