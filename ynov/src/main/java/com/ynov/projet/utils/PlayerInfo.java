package com.ynov.projet.utils;

import lombok.Getter;
import org.bukkit.entity.Player;

public class PlayerInfo {

    @Getter
    private Player player;

    @Getter
    String uuid;

    @Getter
    private int mana;

    @Getter
    private int maxMana;

}
