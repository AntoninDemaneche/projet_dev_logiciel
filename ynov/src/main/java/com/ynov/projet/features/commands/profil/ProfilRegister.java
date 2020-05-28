package com.ynov.projet.Features.commands.profil;

import com.ynov.projet.Features.Feature;

public class ProfilRegister extends Feature {
    @Override
    protected void doRegister() {
        new AbilitiesCommand().register();
        new AgeCommand().register();
        new ApparenceCommand().register();
        new AProfilCommand().register();
        new ChakraCommand().register();
        new ColorChakraCommand().register();
        new CompetenceCommand().register();
        new EntrainementCommand().register();
        new FeuilleCommand().register();
        new GenreCommand().register();
        new LostCommand().register();
        new MaskProfilCommand().register();
        new ProfilCommand().register();
        new ProfilEditCommand().register();
        new ProfilMJCommand().register();
        new RankCommand().register();
        new ResistanceCommand().register();
    }
}
