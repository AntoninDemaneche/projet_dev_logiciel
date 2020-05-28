/*
 * Copyright 404Team (c) 2018. For all uses ask 404Team for approuval before.
 */
package com.ynov.projet.Features.commands.others;

import com.ynov.projet.Features.ability.AbilityLoader;
import com.ynov.projet.Features.skill.SkillLoader;
import com.ynov.projet.Main.Command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class ReloadYMLCommand extends Command {
    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        if(split.length == 1) {
            switch (split[0]) {
                case "jutsu":
                    if(SkillLoader.checkConfig()) {
                        SkillLoader.reloadAllSkills(sender);
                    }
                    else {
                        sender.sendMessage("§4ATTENTION, JUTSU NON VALIDE. VOIR CONSOLE POUR DETAILS;");
                    }
                    break;
                case "ability":
                    if(AbilityLoader.checkConfig()) {
                        AbilityLoader.reloadAllAbility(sender);
                    }
                    else {
                        sender.sendMessage("§4ATTENTION, ABILITY NON VALIDE. VOIR CONSOLE POUR DETAILS;");
                    }
                    break;
                default:
                    sender.sendMessage(ChatColor.RED+"/reloadjutsu justu|ability");
            }
        }
        else {
            sender.sendMessage(ChatColor.RED+"/reloadyml justu");
            sender.sendMessage(ChatColor.RED+"/reloadyml ability");
        }
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split)
    {
        return new ArrayList<>();
    }
}
