package netro;

import arc.*;
import mindustry.mod.*;
import netro.content.*;
import netro.gen.*;
import netro.ui.*;


import static mindustry.Vars.ui;

@SuppressWarnings("unused")
public class NetroniumTFSMod extends Mod{
    @Override
    public void loadContent(){
        EntityRegistry.register();
        NetroItems.load();
        NetroStatuses.load();
        NetroFluids.load();
        NetroWeathers.load();
        NetroUnits.load();
        EntityRegistry.registerUnits();
        NetroBlocks.load();
    }

    @Override
    public void init(){
        Core.app.post(() -> {
            //No use right now.
            //ProtectBar protbar = new ProtectBar();
            //protbar.build(ui.hudGroup);

            //Replaces the campaign difficulty dialog.
            ui.campaignRules = new NetroCampRulesDialog();
        });
        //Enabling it sometimes causes doubled music. Fix it at some point.
        //NetroSoundControl.replace();
    }
}