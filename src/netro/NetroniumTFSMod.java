package netro;

import arc.*;
import mindustry.mod.*;
import netro.content.*;
import netro.gen.*;
import netro.type.*;
import netro.ui.*;

import static mindustry.Vars.ui;

@SuppressWarnings("unused")
public class NetroniumTFSMod extends Mod{
    public ProtectBar protbar;
    @Override
    public void loadContent(){
        EntityRegistry.register();
        NetroItems.load();
        NetroFluids.load();
        NetroBullets.load();
        NetroWeathers.load();
        NetroUnits.load();
        NetroBlocks.load();
        //NetroSoundControl.replace();
    }

    @Override
    public void init(){
        Core.app.post(() -> {
            protbar = new ProtectBar();
            protbar.build(ui.hudGroup);
        });
    }
}