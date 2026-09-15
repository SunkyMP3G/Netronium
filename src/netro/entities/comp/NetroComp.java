package netro.entities.comp;

import arc.*;
import arc.math.*;
import arc.util.*;
import ent.anno.Annotations.*;
import mindustry.content.*;
import mindustry.game.*;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.blocks.environment.*;
import netro.content.*;
import netro.type.*;

import static mindustry.Vars.*;

@SuppressWarnings("unused")
@EntityComponent
abstract class NetroComp implements Unitc{
    @Import UnitType type;
    @Import Team team;
    @Import boolean spawnedByCore, dead;
    @Import Floor lastDrownFloor;
    @Import float drownTime, x, y, hitSize, rotation, speedMultiplier;

    @Override
    public void updateDrowning(){
        Floor floor = drownFloor();

        if(floor != null && floor.isLiquid && floor.drownTime > 0 && canDrown()){
            lastDrownFloor = floor;
            drownTime += Time.delta / (hitSize / 8f * type.drownTimeMultiplier * floor.drownTime);
            if(Mathf.chanceDelta(0.02f)){
                floor.drownUpdateEffect.at(x, y, rotation, floor.mapColor);
            }
            //Quicksand doesn't destroy units
            if(floor == NetroBlocks.quicksand.asFloor()) drownTime = Mathf.clamp(drownTime, 0, 0.99f);
            if(drownTime >= 0.999f && !net.client()){
                kill();
                Events.fire(new UnitDrownEvent(self()));
            }
        }else{
            drownTime -= Time.delta / 50f;
        }

        drownTime = Mathf.clamp(drownTime);
    }

    /// In quicksand, makes units lose speed gradually.
    @Override
    @Replace
    public float floorSpeedMultiplier(){
        Floor on = (isFlying() || type.hovering ? Blocks.air.asFloor() : floorOn());

        if(on == NetroBlocks.quicksand){
            return (float)Math.pow(Mathf.lerp(1,  on.speedMultiplier, drownTime), type.floorMultiplier) * speedMultiplier;
        }
        else{
            return (float)Math.pow(on.speedMultiplier, type.floorMultiplier) * speedMultiplier;
        }
    }
}