package com.ynov.projet.features.commands.profil;

import com.ynov.projet.features.Feature;

public class ProfilRegister extends Feature {
    @Override
    protected void doRegister(){
        new LostCommand().register();
    }
}
