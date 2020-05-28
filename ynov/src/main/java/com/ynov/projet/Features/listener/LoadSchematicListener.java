package com.ynov.projet.Features.listener;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.ynov.projet.Features.Feature;
import com.ynov.projet.Main;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class LoadSchematicListener extends Feature {

    @EventHandler
    public void interact(PlayerInteractEvent e) throws IOException {
        Player p = e.getPlayer();
        if (p.getName().equals("WhiteSlimes")){
            if (p.getInventory().getItemInMainHand().getType().equals(Material.JUNGLE_SAPLING)){
                loadSchematic(p);
                p.sendMessage(ChatColor.GREEN + "Le schematic est load");
            }
        }
    }


    private void loadSchematic(Player p) throws IOException{
        Location location = p.getLocation();
        File schematic = new File(Main.plugin().getDataFolder() + File.separator + "/schematics/hisanaga_arbre_1.schem");

        ClipboardFormat format = ClipboardFormats.findByFile(schematic);
        try (ClipboardReader reader = format.getReader(new FileInputStream(schematic))){
            Clipboard clipboard = reader.read();

            try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(new BukkitWorld(location.getWorld()), -1)){
                Operation operation = new ClipboardHolder(clipboard)
                        .createPaste(editSession)
                        .to(BlockVector3.at(location.getX(), location.getY(), location.getZ()))
                        .ignoreAirBlocks(false)
                        .build();
                Operations.complete(operation);
            }
        } catch (FileNotFoundException | WorldEditException e){
            e.printStackTrace();
        }
    }

}
