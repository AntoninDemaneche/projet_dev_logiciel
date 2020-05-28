package com.ynov.projet.Features.objectnum;

import lombok.Getter;


public enum Clan {

    NULL("Null", 0, "null", "NULL"),
    UCHIWA("Uchiwa", 1, "uchiwa_icon", "Uchiwa"),
    SENJU("Senju", 2, "senju_icon", "Senju"),
    HYUGA("Hyûga", 3, "hyuga_icon", "Hyuga"),
    ABURAME("Aburame", 4, "aburame_icon", "Aburame"),
    INUZUKA("Inuzuka", 5, "inuzuka_icon", "Inuzuka"),
    KAMIZURU("Kamizuru", 6, "kamizuru_icon", "Kamizuru"),
    HIKAI("Hikai", 7, "hikai_icon", "Hikai"),
    HOZUKI("Hozuki", 8, "hozuki_icon", "Hozuki"),
    HOSHIGAKI("Hoshigaki", 9, "hoshigaki_icon", "Hoshigaki"),
    KAGUYA("Kaguya", 10, "kaguya_icon", "Kaguya"),
    DENKI("Denki", 11, "denki_icon", "Denki"),
    CHINOIKE("Chinoike", 12, "chinoike_icon", "Chinoike"),
    BUNRAKU("Bunraku", 13, "bunraku_icon", "Bunraku"),
    SABAKU("Sabaku", 14, "sabaku_icon", "Sabaku"),
    KAMI("Kami", 15, "kami_icon", "Kami"),
    NINSHUSUMIE("Ninshu Sumie", 16, "ninshu_icon", "Ninshu_Sumie"),
    NINSHUYOSUGA("Ninshu Yosuga", 17, "ninshu_icon", "Ninsu_Yosuga"),
    ERMITECRAPAUD("Ermite Crapaud", 18, "ermite_icon", "Ermite_Crapaud"),
    ERMITESERPENT("Ermite Serpent", 19, "ermite_icon", "Ermite_Serpent"),
    ERMITELIMACE("Ermite Limace", 20, "ermite_icon", "Ermite_Limace"),
    ERMITELOUP("Ermite Loup", 21, "ermite_icon", "Ermite_Loup"),
    ERMITEPOULPE("Ermite Poulpe", 22, "ermite_icon", "Ermite_Poulpe"),
    ERMITERAT("Ermite Rat", 23, "ermite_icon", "Ermite_Rat"),
    ERMITEFAUCON("Ermite Faucon", 24, "ermite_icon", "Ermite_Faucon"),
    ERMITEARAIGNEE("Ermite Araignée", 25, "ermite_icon", "Ermite_Araignee"),
    ERMITETORTUE("Ermite Tortue", 26, "ermite_icon", "Ermite_Tortue"),
    ERMITESINGE("Ermite Singe", 27, "ermite_icon", "Ermite_Singe"),
    ERMITETAUREAU("Ermite Taureau", 28, "ermite_icon", "Ermite_Taureau"),
    ERMITELION("Ermite Lion", 29, "ermite_icon", "Ermite_Lion"),
    SPECIAL("Spécial", 30, "special_icon", "Special_comme_samere"),
    ONI("Oni", 31, "oni_icon", "Oni"),
    YAMANAKA("Yamanaka", 32, "yamanaka_icon", "Yamanaka"),
    NARA("Nara", 33, "nara_icon", "Nara"),
    AKIMICHI("Akimichi", 34, "akimichi_icon", "Akimichi"),
    UZUMAKI("Uzumaki", 35, "uzumaki_icon", "Uzumaki"),
    SARUTOBI("Sarutobi", 36, "sarutobi_icon", "Sarutobi"),
    DEI("Dei", 37, "dei_icon", "Dei"),
    DOKU("Doku", 38, "doku_icon", "Doku"),
    TAIYAN("Taiyan", 39, "taiyan_icon", "Taiyan"),
    SAMOURAI("Samourai", 40, "samourai_icon", "Samourai"),

    INDEFINI("Non défini", -1, "null", "null_null_null");

    @Getter
    private String name;
    @Getter
    private int id;
    @Getter
    private String tag;
    @Getter
    private String identifiant;

    Clan(String name, int id, String tag, String identifiant){
        this.name = name;
        this.id = id;
        this.tag = tag;
        this.identifiant = identifiant;
    }

    public static Clan getFromID(int id){
        for(Clan clan : values()){
            if(clan.getId() == id){
                return clan;
            }
        }
        return INDEFINI;
    }

    public static int getIDFromName(String name) {
        for(Clan clan : values()){
            if(clan.getName().equals(name)){
                return clan.getId();
            }
        }
        return -2;
    }

    public static Clan getFromIdentifiant(String identifiant) {
        for(Clan clan : values()){
            if(clan.getIdentifiant().equals(identifiant)){
                return clan;
            }
        }
        return INDEFINI;
    }
}
