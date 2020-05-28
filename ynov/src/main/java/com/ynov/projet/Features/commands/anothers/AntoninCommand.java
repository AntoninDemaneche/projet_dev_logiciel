package com.ynov.projet.Features.commands.anothers;

import com.ynov.projet.Main.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

import static com.ynov.projet.Features.commands.anothers.Commands.getRandom;
import static com.ynov.projet.Features.commands.anothers.Commands.phraseantonin;

public class AntoninCommand extends Command {
    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        sender.sendMessage("§7Antonin vous chuchote : §i"+phraseantonin.get(getRandom(sender.getName(),0, phraseantonin.size()-1)));
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        return new ArrayList<>();
    }
}
