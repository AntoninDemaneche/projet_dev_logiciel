package com.ynov.projet.Features.objectnum;

import lombok.Getter;


public enum VoieNinja {

    NULL("Null", 0, "ERROR_404"),
    FUINJUTSU("Fuinjutsu", 1, "Fuinjutsu"),
    GENJUTSU("Genjutsu", 2, "Genjutsu"),
    NINJUTSU("Ninjutsu", 3, "Ninjutsu"),
    IRYOJUTSU("Iryojutsu", 4, "Iryojutsu"),
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
    KARI("Kari Sasori", 18, "Kari_Sasori"),
    KARATE("Karaté aquatique", 19, "Karate_aquatique"),
    SHORINJI("Shorinji Kempo", 20, "Shorijin_Kempo"),
    JUKEN("Juken", 21, "Juken"),
    HANGU("Hangu To Hugo", 22, "Hangu_To_Hugo"),
    IJITSU("Ijitsu", 23, "Ijitsu"),
    YASEIKEN("Yaseiken", 24, "Yaseiken"),
    INDEFINI("Non défini", -1, "NO_DEFINE");

    @Getter
    private String name;
    @Getter
    private int id;
    @Getter
    private String identifiant;

    VoieNinja(String name, int id, String identifiant){
        this.name = name;
        this.id = id;
        this.identifiant = identifiant;
    }

    public static VoieNinja getFromID(int id){
        for(VoieNinja voieNinja : values()){
            if(voieNinja.getId() == id){
                return voieNinja;
            }
        }
        return INDEFINI;
    }

    public static int getIDFromName(String name) {
        for(VoieNinja voieNinja : values()){
            if(voieNinja.getName().equals(name)){
                return voieNinja.getId();
            }
        }
        return -2;
    }

    public static VoieNinja getFromIdentifiant(String identifiant) {
        for(VoieNinja voieNinja : values()){
            if(voieNinja.getIdentifiant().equals(identifiant)){
                return voieNinja;
            }
        }
        return INDEFINI;
    }
}
