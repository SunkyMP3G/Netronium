package netro.type.units;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.content.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.blocks.environment.*;
import mindustry.world.meta.*;
import netro.content.*;
import netro.graphics.*;
import netro.ui.*;

import static mindustry.Vars.renderer;

public class NetroUnitType extends UnitType{
    public NetroUnitType(String name){
        super(name);
        researchCostMultiplier = 0f;
        lowAltitude = true;
        outlineColor = NetroPal.netroOutline;
    }

    ///Counts fall damage to show in stats for kamikaze units.
    public float fallDmgStat(){
        return Mathf.round(Mathf.pow(this.hitSize, 0.75f) * this.crashDamageMultiplier * 2.5f);
    }

    /// Same as normal, but on quicksand it will recolor the unit less.
    @Override
    public void applyColor(Unit unit){
        Draw.color();
        if(healFlash){
            Tmp.c1.set(Color.white).lerp(healColor, Mathf.clamp(unit.healTime - unit.hitTime));
        }
        Draw.mixcol(Tmp.c1, Math.max(unit.hitTime, !healFlash ? 0f : Mathf.clamp(unit.healTime)));

        if(unit.drownTime > 0 && unit.lastDrownFloor != null){
            if(unit.lastDrownFloor == NetroBlocks.quicksand){
                Draw.mixcol(Tmp.c1.set(unit.lastDrownFloor.mapColor).mul(0.83f), unit.drownTime * 0.5f);
            }else{
                Draw.mixcol(Tmp.c1.set(unit.lastDrownFloor.mapColor).mul(0.83f), unit.drownTime * 0.9f);
            }
        }
        //this is horribly scuffed.
        if(renderer != null && renderer.overlays != null){
            renderer.overlays.checkApplySelection(unit);
        }
    }
}