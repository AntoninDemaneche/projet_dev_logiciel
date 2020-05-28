package com.ynov.projet.Features.objectnum;

import lombok.Getter;
import org.apache.commons.lang.StringUtils;



public enum ChakraType {

    RAITON(1, "§eRaiton"),
    KATON(2, "§cKaton"),
    DOTON(3, "§6Doton"),
    SUITON(4, "§9Suiton"),
    FUTON(5, "§fFuton"),
    REZUTON(6, "§1Rezuton"),
    SHINTON(7, "§8Shinton"),
    SHOTON(8, "§dShoton"),
    HETON(9, "§7Heton"),
    HARITON(10, "§bHariton"),
    MOKUTON(11, "§2Mokuton"),
    VUUTON(12, "§7Vuuton"),
    YOTON(13, "§4Yôton"),
    HAITON(14, "§cHaiton"),
    BAKUTON(15, "§6Bakuton"),
    NENDOTON(16, "§6Nendoton"),
    MITSUTON(17, "§6Mitsuton"),
    HYUTON(18, "§3Hyôton"),
    RANTON(19, "§8Ranton"),
    FUUKATON(20, "§7Fuukaton"),
    JITON(21, "§fJiton"),
    NETON(22, "§7Neton"),
    DENTON(23, "§0Denton"),
    NULL(-1, "ERREUR");

    @Getter
    public int id;

    @Getter
    public String name;

    ChakraType(int id, String name) {
        this.id = id;
        this.name = name;
    }


    public static ChakraType fromName(String str){
        String[] namecomplete = new String[2];
        String name = str;
        if(str.contains("_")) {
            namecomplete = str.split("_");
            name = namecomplete[0];
        }
        for(ChakraType chakra : values()){
            if(chakra.getName().substring(2).equals(name)){
                if(str.contains("_")) {
                    if(StringUtils.isNumeric(namecomplete[1])) {
                        int nb = Integer.parseInt(namecomplete[1]);
                        if(nb%25 != 0 || nb < 0 || nb > 50) {
                            return NULL;
                        }
                    }
                }
                return chakra;
            }
        }
        return null;
    }

    public static String getPrettyName(String str) {
        if(str.contains("_")) {
            str = str.split("_")[0];
        }
        for(ChakraType chakra : values()) {
            if (chakra.getName().substring(2).equalsIgnoreCase(str)) {
                return chakra.getName();
            }
        }
        return "ERREUR";
    }

    public static ChakraType fromId(int id) {
        for(ChakraType type : values()) {
            if(type.getId() == id) {
                return type;
            }
        }
        return null;
    }
}
