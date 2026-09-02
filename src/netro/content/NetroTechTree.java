package netro.content;

import mindustry.content.*;

import static mindustry.content.TechTree.*;

public class NetroTechTree{
    public static void load(){
        Planets.serpulo.techTree = nodeRoot("serpulo", NetroBlocks.coreShield, () -> {
            node(NetroBlocks.coreBarrier, () -> {
                node(NetroBlocks.coreDome, () -> {
                });
            });
        });
    }
}