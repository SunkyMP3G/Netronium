package netro;

import mindustry.mod.*;
import netro.content.*;
import netro.gen.*;

@SuppressWarnings("unused")
public class NetroniumTFSMod extends Mod{
    @Override
    public void loadContent(){
        EntityRegistry.register();
        NetroItems.load();
        NetroFluids.load();
        NetroUnits.load();
        NetroBlocks.load();
        NetroTechTree.load();
        NetroSoundControl.replace();
    }
}