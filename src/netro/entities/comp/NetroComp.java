package netro.entities.comp;

import ent.anno.Annotations.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.type.*;
import netro.type.*;

import static mindustry.Vars.state;

@SuppressWarnings("unused")
@EntityComponent
abstract class NetroComp implements Unitc{
    @Import UnitType type;
    @Import Team team;
    @Import boolean spawnedByCore, dead;

    /// Same as vanilla, but checks for custom unit cap.
    @Override
    public void add(){
        team.data().updateCount(type, 1);

        //check if over unit cap
        if(NetroUnitType.unitCap > 0){
            if(count() > NetroUnitType.unitCap){
                Call.unitCapDeath(self());
                team.data().updateCount(type, -1);
            }
        }else if(type.useUnitCap && count() > cap() && !spawnedByCore && !dead && !state.rules.editor){
            Call.unitCapDeath(self());
            team.data().updateCount(type, -1);
        }
    }
}