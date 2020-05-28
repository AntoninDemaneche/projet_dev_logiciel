package com.ynov.projet.Features.objectnum;

import lombok.Getter;


public enum RPRank {

    NULL("null", "Erreur", -1, 0, 0),
    STUDENT("etudiant", "Étudiant", 0, 1,100),
    GENIN("genin", "Genin", 1, 2,200),
    CHUUNIN("chuunin", "Chuunin", 2, 3,200),
    JUUNIN("juunin", "Juunin", 3, 4,200),
    RYOJI("ryoji", "Ryoji", 4, 5,200);



    RPRank(String name, String displayName, int id, int maxDice, int chakraRank){
        this.name = name;
        this.displayName = displayName;
        this.id = id;
        this.maxDice = maxDice;
        this.chakraRank = chakraRank;

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
    public int chakraRank;

    public static RPRank getById(int id){
        for(RPRank rank : values()){
            if(rank.getId() == id){
                return rank;
            }
        }
        return RPRank.NULL;
    }

    public static RPRank getByName(String name){
        for(RPRank rank : values()){
            if(rank.getName().equals(name)){
                return rank;
            }
        }
        return RPRank.NULL;
    }
}