package com.ynov.projet.features.commands.anothers;

import com.ynov.projet.features.Feature;
import org.bukkit.permissions.PermissionAttachment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class Commands extends Feature {
    public static ArrayList<String> param = new ArrayList<>();
    public static HashMap<UUID, PermissionAttachment> perms = new HashMap<>();
    public static Random random;

    @Override
    protected void doRegister(){
        random = new Random(System.nanoTime());
    }
}
