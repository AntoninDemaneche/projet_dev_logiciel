package com.ynov.projet.Features.routines;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;

import static com.ynov.projet.Features.routines.Routines.*;

public class DayNight {

    public static World Seisan = Bukkit.getWorld("Seisan");
    protected static void SetTime() {
        if(Seisan != null) {
            timeMC++;
            if(timeMC > 24000) {
                timeMC = 0;
            }
            Seisan.setTime(timeMC);
        }
    }

    protected static void SetWeather() {
        int temps = random(0,13); /* Nombre de 0 à 13 (pour que le 13 = orage) */
        int time = -1;

        if(temps == 13) {
            if(!Seisan.isThundering()) {
                Seisan.setStorm(true); /* Pluie */
                Seisan.setThundering(true); /* Orage */
                time = 0;
            }
        }
        else if(temps > 10) {
            if(!Seisan.hasStorm() || Seisan.isThundering()) {
                Seisan.setThundering(false); /* Orage */
                Seisan.setStorm(true); /* Pluie */
                time = 1;
            }
        }
        else {
            if(Seisan.hasStorm() || Seisan.isThundering()) {
                Seisan.setThundering(false);
                Seisan.setStorm(false);
                time = 2;
            }
        }

        int phrase;
        String phrasetemps;
        if(time == 0)  {
            // Orage
            phrase = random(0, PhrasesORAGE.size()-1);
            phrasetemps = PhrasesORAGE.get(phrase);
        }
        else if(time == 1) {
            // Pluie
            phrase = random(0, PhrasesPLUIE.size()-1);
            phrasetemps = PhrasesPLUIE.get(phrase);
        }
        else {
            // Beau temps
            phrase = random(0, PhrasesSUN.size()-1);
            phrasetemps = PhrasesSUN.get(phrase);
        }
        if(time != -1) {
            Bukkit.getServer().broadcastMessage(ChatColor.AQUA + "** " + phrasetemps + " **");
        }
    }

    public static int random(int min, int max) {
        return min + (int)(Math.random() * ((max - min) + 1));
    }
}
