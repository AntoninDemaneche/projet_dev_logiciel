package com.ynov.projet.Features.objectnum;

import lombok.Getter;


public enum StyleCombat {

    NULL("Aucun style", 0, "Aucun_Style_puesamerquoi"),
    KARI("Kari Sasori", 1, "Kari_Sasori"),
    KARATE("Karaté aquatique", 2, "Karete_aquatique"),
    SHORINJI("Shorinji Kempo", 3, "Shorinji_Kempo"),
    JUKEN("Juken", 4, "Juken"),
    PORTES("Portes & respirations célestes", 5, "Portes_&_respirations_celestes"),
    EPEISTES("Épéistes de la brume", 6, "Epeistes_de_la_brume"),
    SHUGUREJUTSU("Shugurejutsu", 7, "Shugurejutsu"),
    MULTICLONAGE("Multiclonage", 8, "Multiclonage"),
    KATA("Kata Ichi Ryu", 9, "Kata_Ichi_Ryu"),
    NODO("Nodo no Oyayubi", 10, "Nodo_no_Oyayubi"),
    TESSEN("Tessen", 11, "Tessen"),
    KYUJUTSU("Kyujutsu", 12, "Kyujutsu"),
    BOJIEI("Bô Jiei", 13, "Bo_Jiei"),
    SHURYO("Shuryo Sasori", 14, "Shuryo_Sasori"),
    TECHDELOMBRE("50 techniques de l'ombre", 15, "50_techniques_de_l_ombre"),
    GRIFFE("Griffe du loup blanc", 16, "Griffe_du_loup_blanc"),
    HANKAGE("Han Kage", 17, "Han_Kage"),
    HANGU("Hangu To Hugo", 18, "Hangu_to_hugo"),
    IJITSU("Ijitsu", 19, "Ijitsu"),
    YASEIKEN("Yaseiken", 20, "Yaseiken"),
    INDEFINI("Style non défini", -1, "null");

    @Getter
    private String name;
    @Getter
    private int id;
    @Getter
    private String identifiant;

    StyleCombat(String name, int id, String identifiant){
        this.name = name;
        this.id = id;
        this.identifiant = identifiant;
    }

    public static StyleCombat getFromID(int id){
        for(StyleCombat styleCombat : values()){
            if(styleCombat.getId() == id){
                return styleCombat;
            }
        }
        return INDEFINI;
    }

    public static int getIDFromName(String name) {
        for(StyleCombat styleCombat : values()){
            if(styleCombat.getName().equals(name)){
                return styleCombat.getId();
            }
        }
        return -2;
    }

    public static StyleCombat getFromIdentifiant(String identifiant) {
        for(StyleCombat styleCombat : values()){
            if(styleCombat.getIdentifiant().equals(identifiant)){
                return styleCombat;
            }
        }
        return INDEFINI;
    }
}

