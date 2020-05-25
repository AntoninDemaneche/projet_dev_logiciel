package com.ynov.projet.features.commands.anothers;

import com.ynov.projet.Main;
import com.ynov.projet.features.Feature;
import com.ynov.projet.features.PlayerData.PlayerConfig;
import org.bukkit.Bukkit;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class Commands extends Feature {
    public static ArrayList<String> param = new ArrayList<>();
    public static HashMap<UUID, PermissionAttachment> perms = new HashMap<>();
    public static ArrayList<String> PlayerBuildTemp = new ArrayList<>();
    public static Random random;

    @Override
    protected void doRegister(){
        random = new Random(System.nanoTime());
        new BuildCommand().register();

        param.add("add");
        param.add("remove");
    }

    public static void playerVanish(PlayerConfig pConfig) {
        String s;
        if(pConfig.isVanish()) {
            Main.plugin().getServer().getOnlinePlayers().forEach((p) -> {
                if (!p.isOp())
                {
                    p.hidePlayer(Main.plugin(), pConfig.getPlayer());
                }
            });
            pConfig.getPlayer().sendMessage("§cHRP : §7Flouf, vous disparaissez.");
            pConfig.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 200000, 2, false, false));
            s = "[V] " + pConfig.getPlayer().getDisplayName();
        }
        else {
            pConfig.getPlayer().sendMessage("§cHRP : §7Pouf, vous apparaissez.");
            pConfig.getPlayer().removePotionEffect(PotionEffectType.NIGHT_VISION);
            Main.plugin().getServer().getOnlinePlayers().forEach((p) -> {
                p.showPlayer(Main.plugin(), pConfig.getPlayer());
            });
            s = pConfig.getPlayer().getDisplayName();
        }
        Bukkit.getScheduler().runTaskLater(Main.plugin(), () -> pConfig.getPlayer().setPlayerListName(s), 50);

    }
}
