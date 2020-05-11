package com.ynov.projet.features.objectnum;

import lombok.Getter;

public enum  RPRank {

    NULL("null", "Erreur", -1, 0, 0),
    APPRENTI("apprenti", "Apprenti", 0, 1, 100),
    MAGICIEN("magicien", "Magicien", 1, 2, 200),
    ARCHIMAGE("archimage", "Archimage", 2, 3, 400);

    RPRank(String name, String displayName, int id, int maxDice, int chakraRank){
        this.name = name;
        this.displayName = displayName;
        this.id = id;
        this.maxDice = maxDice;
        this.manaRank = chakraRank;

    }

    @Getter
    public String name;
    @Getter
    public String displayName;
    @Getter
    public int id;
    @Getter
    public int maxDice;
    @Getter
    public int manaRank;

    public static RPRank getById(int id){
        for (RPRank rank : values()){
            if (rank.getId() == id){
                return rank;
            }
        }
        return RPRank.NULL;
    }

    public static RPRank getByName(String name){
        for (RPRank rank: values()){
            if (rank.getName().equals(name)){
                return rank;
            }
        }
        return RPRank.NULL;
    }
}
