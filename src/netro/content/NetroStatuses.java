package netro.content;

import mindustry.graphics.*;
import mindustry.type.*;
import netro.graphics.*;

public class NetroStatuses{
    public static StatusEffect
    phomaxiteChase, hardBuff;

    public static void load(){
        //Increases movement speed of abominations when they target something.
        phomaxiteChase = new StatusEffect("phomaxite-chase"){{
            color = NetroPal.phomaxiticGreen;

            speedMultiplier = 1.8f;
        }};
        //Visual status indicating that this unit has special abilities when on hard+ difficulty.
        hardBuff = new StatusEffect("hard-buff"){{
            color = Pal.negativeStat;

            effect = NetroFx.hardBuff;
            effectChance = 0.1f;
        }};
    }
}