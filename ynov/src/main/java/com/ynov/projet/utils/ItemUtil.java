package com.ynov.projet.utils;

import net.minecraft.server.v1_15_R1.NBTTagCompound;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Wool;

import java.util.List;

public class ItemUtil {
    public static ItemStack createItemStack(Material type, int amount){
        return new ItemStack(type, amount);
    }

    public static ItemStack createItemStack(Material type, int amount, String name){
        ItemStack item = new ItemStack(type);
        ItemMeta meta = item.getItemMeta();
        if(meta != null) {
            meta.setDisplayName(name);
            item.setItemMeta(meta);
        }

        return item;
    }

    public static ItemStack createItemStack(Material type, int amount, String name, List<String> lore){
        ItemStack item = new ItemStack(type, amount);
        ItemMeta meta = item.getItemMeta();
        if(meta != null) {
            meta.setDisplayName(name);
            meta.setLore(lore);
            item.setItemMeta(meta);
        }

        return item;
    }

    public static ItemStack createItemStack(Material type, int amount, String name, List<String> lore, String keytag, String valuetag){
        ItemStack item = new ItemStack(type, amount);
        ItemMeta meta = item.getItemMeta();
        if(meta != null) {
            meta.setDisplayName(name);
            meta.setLore(lore);
            item.setItemMeta(meta);
        }

        net.minecraft.server.v1_15_R1.ItemStack stack = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = stack.getTag() != null ? stack.getTag() : new NBTTagCompound();
        tag.setString(keytag, valuetag);
        stack.setTag(tag);
        item = CraftItemStack.asBukkitCopy(stack);
        return item;
    }

    public static ItemStack createRenamedWool(DyeColor color, int amount, String name){
        Wool wool = new Wool(color);
        ItemStack item = wool.toItemStack(amount);

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack addTag(ItemStack item, String keytag, String valuetag) {
        net.minecraft.server.v1_15_R1.ItemStack stack = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = stack.getTag() != null ? stack.getTag() : new NBTTagCompound();
        tag.setString(keytag, valuetag);
        stack.setTag(tag);
        item = CraftItemStack.asBukkitCopy(stack);
        return item;
    }

    public static boolean hasTag(ItemStack item, String keytag, String valuetag) {
        boolean test = false;
        net.minecraft.server.v1_15_R1.ItemStack stack = CraftItemStack.asNMSCopy(item);
        if(stack.hasTag() && stack.getTag() != null && stack.getTag().hasKey(keytag) && stack.getTag().getString(keytag).equals(valuetag)) {
            test = true;
        }
        return test;
    }

    public static String convertItemStackToJsonRegular(ItemStack itemStack) {
        // First we convert the item stack into an NMS itemstack
        net.minecraft.server.v1_15_R1.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(itemStack);
        net.minecraft.server.v1_15_R1.NBTTagCompound compound = new NBTTagCompound();

        return nmsItemStack.save(compound).toString();
    }
}
