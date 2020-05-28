package com.ynov.projet.Features.commands.others;

import com.ynov.projet.Features.Feature;

public class OthersCommandRegister extends Feature {
    @Override
    protected void doRegister() {
        new CloneCommand().register();
        new GiveChakraCommand().register();
        new GiveItemCommand().register();
        new InvseeCommand().register();
        new NinkenCommand().register();
        new ParcheminCommand().register();
        new ReloadYMLCommand().register();
        new JutsuCommand().register();
        new StealChakraCommand().register();
        new StopCommand().register();
        new TargetCommand().register();
        new TPBackCommand().register();
        new TransferCommand().register();
        new WhoIsCommand().register();
    }
}
