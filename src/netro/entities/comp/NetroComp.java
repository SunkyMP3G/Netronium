package netro.entities.comp;

import arc.*;
import arc.math.*;
import arc.util.*;
import ent.anno.Annotations.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.game.*;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.blocks.environment.*;
import netro.content.*;

import static mindustry.Vars.*;

@SuppressWarnings("unused")
@EntityComponent
abstract class NetroComp implements Unitc{
    @Import transient UnitType type;
    @Import transient Team team;
    @Import transient boolean spawnedByCore, dead;
    @Import transient Floor lastDrownFloor;
    @Import transient float drownTime, x, y, hitSize, rotation, speedMultiplier;

    public boolean hasHardAbility = false, hasEradAbility = false;

    @Override
    public void add(){
        team.data().updateCount(type, 1);

        //check if over unit cap
        if(type.useUnitCap && count() > cap() && !spawnedByCore && !dead && !state.rules.editor){
            Call.unitCapDeath(self());
            team.data().updateCount(type, -1);
        }

        unitPhysics.add(self());

        //Has 40% chance to get special hard+ ability for each enemy unit.
        if(Mathf.chance(0.4f) && team == state.rules.waveTeam) hasHardAbility = true;
        //On eradication, more units get the ability, for total of ~70% units with ability.
        if(!hasHardAbility && Mathf.chance(0.5f) && team == state.rules.waveTeam) hasEradAbility = true;
    }

    @Override
    @Replace(100)
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
    @Replace(100)
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