package com.ynov.projet.Features.commands.anothers;

import com.ynov.projet.Features.PlayerData.PlayerConfig;
import com.ynov.projet.Features.utils.ItemUtil;
import com.ynov.projet.Main;
import com.ynov.projet.Main.Command;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class BuildCommand extends Command {
    @Override

    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        if(split.length == 2) {
            Player target = Bukkit.getPlayer(split[1]);
            if(target == null ) {
                sender.sendMessage("§7Le joueur n'est pas connecté.");
                return;
            }
            switch (split[0]) {
                case "definitif":
                    PlayerConfig pConfig = PlayerConfig.getPlayerConfig(target);
                    pConfig.setBuildmode(!pConfig.isBuildmode());
                    if (pConfig.isBuildmode()) {
                        sender.sendMessage(target.getDisplayName() + " §7est désormais en mode build.");
                        target.setPlayerListName("§7[Builder] "+target.getDisplayName());
                        target.sendMessage("§cHRP : §7Vous êtes désormais en mode builder. Veuillez prendre en considération vos règles sur discord.");
                    } else {
                        sender.sendMessage(target.getDisplayName() + " §7n'est désormais plus en mode build.");
                        target.setPlayerListName(target.getDisplayName());
                        target.sendMessage("§cHRP : §7Vous n'êtes plus en mode builder. Merci de votre contribution pour Seisan !");
                    }
                    newPermissionBuildMode(pConfig);
                    break;
                case "tempo":
                    /* On lui retire */
                    if(Commands.PlayerBuildTemp.contains(target.getName())) {
                        Commands.PlayerBuildTemp.remove(target.getName());
                        target.setGameMode(GameMode.SURVIVAL);
                        target.getInventory().clear();
                        target.sendMessage("§cHRP : §7Vous pouvez récupérer votre coffre en toute sécurité.");
                        target.setPlayerListName(target.getDisplayName());
                    }
                    /* On lui remet */
                    else {
                        target.setPlayerListName("§7[Builder_TMP]"+target.getDisplayName());
                        Commands.PlayerBuildTemp.add(target.getName());
                        target.setGameMode(GameMode.CREATIVE);
                        generateChest(target);
                        target.sendMessage("§4IMPORTANT : §7Ne prenez pas votre inventaire avant de ne plus avoir le build mode temporaire ! (ou déco reco)");

                    }
                    break;
            }
        }
        else {
            sender.sendMessage("§cUsage : §7/build tempo|definitif (joueur)");
        }
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split)
    {
        List<String> completion = new ArrayList();
        switch (split.length) {
            case 1:
                complete(completion, "tempo", split[0]);
                complete(completion, "definitif", split[0]);
                break;
            case 2:
                for(Player player : Main.plugin().getServer().getOnlinePlayers())
                {
                    complete(completion, player.getName(), split[1]);
                }
                break;
        }
        return completion;    }

    public static void newPermissionBuildMode(PlayerConfig pConfig) {
        Commands.perms.get(pConfig.getPlayer().getUniqueId()).setPermission("minecraft.command.me", false);
        // C'est là pour l'auto complete
        pConfig.getPlayer().updateCommands();
        if(!pConfig.getPlayer().isOp()) {
            if (pConfig.isBuildmode()) {
                Commands.perms.get(pConfig.getPlayer().getUniqueId()).setPermission("worldedit.*", true);
                Commands.perms.get(pConfig.getPlayer().getUniqueId()).setPermission("minecraft.command.gamemode", true);
                Commands.perms.get(pConfig.getPlayer().getUniqueId()).setPermission("minecraft.command.effect", true);
                Commands.perms.get(pConfig.getPlayer().getUniqueId()).setPermission("multiverse.*", true);
                Commands.perms.get(pConfig.getPlayer().getUniqueId()).setPermission("clickwarp.*", true);
                Commands.perms.get(pConfig.getPlayer().getUniqueId()).setPermission("minecraft.command.give", true);
                Commands.perms.get(pConfig.getPlayer().getUniqueId()).setPermission("minecraft.command.teleport", true);
                Commands.perms.get(pConfig.getPlayer().getUniqueId()).setPermission("voxelsniper.*", true);
                Commands.perms.get(pConfig.getPlayer().getUniqueId()).setPermission("voxelsniper.sniper", true);
                Commands.perms.get(pConfig.getPlayer().getUniqueId()).setPermission("voxelsniper.ignorelimitations", true);
                Commands.perms.get(pConfig.getPlayer().getUniqueId()).setPermission("voxelsniper.goto", true);
                Commands.perms.get(pConfig.getPlayer().getUniqueId()).setPermission("voxelsniper.brush.*", true);

            } else {
                Commands.perms.get(pConfig.getPlayer().getUniqueId()).setPermission("worldedit.*", false);
                Commands.perms.get(pConfig.getPlayer().getUniqueId()).setPermission("minecraft.command.gamemode", false);
                Commands.perms.get(pConfig.getPlayer().getUniqueId()).setPermission("minecraft.command.effect", false);
                Commands.perms.get(pConfig.getPlayer().getUniqueId()).setPermission("multiverse.*", false);
                Commands.perms.get(pConfig.getPlayer().getUniqueId()).setPermission("clickwarp.*", false);
                Commands.perms.get(pConfig.getPlayer().getUniqueId()).setPermission("minecraft.command.give", false);
                Commands.perms.get(pConfig.getPlayer().getUniqueId()).setPermission("minecraft.command.teleport", false);
                Commands.perms.get(pConfig.getPlayer().getUniqueId()).setPermission("voxelsniper.*", false);
                Commands.perms.get(pConfig.getPlayer().getUniqueId()).setPermission("voxelsniper.sniper", false);
                Commands.perms.get(pConfig.getPlayer().getUniqueId()).setPermission("voxelsniper.ignorelimitations", false);
                Commands.perms.get(pConfig.getPlayer().getUniqueId()).setPermission("voxelsniper.goto", false);
                Commands.perms.get(pConfig.getPlayer().getUniqueId()).setPermission("voxelsniper.brush.*", false);
            }
        }
        // C'est là pour l'auto complete
        pConfig.getPlayer().updateCommands();
    }


    private void generateChest(Player p){
        org.bukkit.block.data.type.Chest chestData1;
        org.bukkit.block.data.type.Chest chestData2;
        Location loc = p.getLocation();
        Location loc2 = loc.clone();
        loc2.add(-1,0,0);
        Block block1 = loc.getBlock();
        Block block2 = loc2.getBlock();
        block1.breakNaturally();
        block2.breakNaturally();

        block1.setType(Material.CHEST);
        block2.setType(Material.CHEST);

        Chest chest1 = (Chest) block1.getState();
        Chest chest2 = (Chest) block2.getState();

        chestData1 = (org.bukkit.block.data.type.Chest) chest1.getBlockData();
        chestData2 = (org.bukkit.block.data.type.Chest) chest2.getBlockData();

        chestData1.setType(org.bukkit.block.data.type.Chest.Type.RIGHT);
        block1.setBlockData(chestData1, true);
        chestData2.setType(org.bukkit.block.data.type.Chest.Type.LEFT);
        block2.setBlockData(chestData2, true);

        //Updating the chest
        Chest chest = (org.bukkit.block.Chest) block1.getState();
        chest.setCustomName("§7Coffre de "+p.getName());
        chest.update();

        chest1.getBlockInventory().setItem(0, ItemUtil.createItemStack(Material.TRIPWIRE_HOOK, 1, p.getName()));

        int k = 9;
        for(int i = 0; i<9; i++){
            if(p.getInventory().getItem(i) != null) {
                chest1.getBlockInventory().setItem(k,p.getInventory().getItem(i));
            }else{
                chest1.getBlockInventory().setItem(k,new ItemStack(Material.AIR));
            }
            k++;
        }

        /* Si il a activé ses bottes de chakra */
        if(p.getInventory().getBoots() != null && p.getInventory().getBoots().containsEnchantment(Enchantment.BINDING_CURSE)) {
            p.getInventory().getBoots().removeEnchantment(Enchantment.FROST_WALKER);
            p.getInventory().getBoots().removeEnchantment(Enchantment.BINDING_CURSE);
            p.removePotionEffect(PotionEffectType.JUMP);
            p.removePotionEffect(PotionEffectType.SPEED);
        }

        for (int i = 100; i < 104; i++) {
            if(p.getInventory().getItem(i) != null) {
                chest1.getBlockInventory().setItem(k,p.getInventory().getItem(i));
            }else{
                chest1.getBlockInventory().setItem(k,new ItemStack(Material.AIR));
            }
            k++;
        }
        chest1.getBlockInventory().setItem(k, p.getInventory().getItemInOffHand());

        k= 0;
        for(int i = 9; i<p.getInventory().getContents().length && k<27; i++){
            if(p.getInventory().getContents()[i] != null) {
                chest2.getBlockInventory().setItem(k,p.getInventory().getContents()[i]);
            }else{
                chest2.getBlockInventory().setItem(k,new ItemStack(Material.AIR));
            }
            k++;
        }


        p.getInventory().clear();
        p.getInventory().addItem(ItemUtil.createItemStack(Material.STONE_BRICKS, 1));
        p.getInventory().addItem(ItemUtil.createItemStack(Material.OAK_PLANKS, 1));
    }
}
