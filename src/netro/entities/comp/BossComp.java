package netro.entities.comp;

import arc.*;
import arc.audio.*;
import ent.anno.Annotations.*;
import mindustry.content.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.type.*;
import netro.*;


import static mindustry.Vars.*;

/** Shows name on screen when first boss unit is spawned.*/
@SuppressWarnings("unused")
@EntityComponent
abstract class BossComp implements Unitc{
    @Import
    UnitType type;
    @Import
    Team team;
    @Import boolean spawnedByCore, dead;

    @Override
    public void add(){
        // Do not show name if there's already a unit of that type, or outside of campaign.
        if(team == state.rules.waveTeam && state.isCampaign() && count() <= 1){
            String bossName = Core.bundle.get("@unit." + type.name + ".name");
            //Displays boss name in all caps. For bomber boss it would look like ">>> BOMBER <<<"
            ui.announce("[scarlet]>>> " + bossName.toUpperCase() + " <<<", 5f);
        }
    }
}