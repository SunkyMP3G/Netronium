package netro.ai;

import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import mindustry.*;
import mindustry.ai.types.*;
import mindustry.entities.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.world.meta.*;

import static mindustry.Vars.*;
import static mindustry.world.meta.BlockFlag.*;

/**
 * Used in flying kamikaze units that deal damage by falling.
 * Targets core or random stuff if "unpredictable AI" is enabled.
 * Uses its velocity and hit size to fall more precisely with any speed in reasonable range.
 */
public class FlyingKamikazeAI extends AIController{
    /// List of possible random targets.
    final static Rand rand = new Rand(); BlockFlag[] randomTargets = {core, generator, factory, battery, reactor, turret, unitAssembler};
    /// If true, only targets cores no matter what. Used for bosses, cuz they'd be a waste otherwise.
    public boolean onlyTargetCore;


    public FlyingKamikazeAI(){
        this(false);
    }
    public FlyingKamikazeAI(boolean onlyTargetCore){
        this.onlyTargetCore = onlyTargetCore;
    }

    @Override
    public void updateUnit(){
        updateVisuals();
        updateTargeting();
        updateMovement();

        if(Units.invalidateTarget(target, unit.team, unit.x, unit.y, Float.MAX_VALUE)){
            target = null;
        }
        if(retarget()){
            target = target(unit.x, unit.y, unit.range(), unit.type.targetAir, unit.type.targetGround);
        }
        //Tries to find core
        if(target == null){
            target = unit.closestEnemyCore();
        }

        boolean shoot = false;
        //If target is null, game would crash
        if(target != null){
            //Use current velocity, fallSpeed and hitSize to land on targets.
            float dst = (unit.vel().len() / 1.5f) * (1f / unit.type.fallSpeed) + (unit.type.hitSize / 2f);
            shoot = unit.within(target, dst);
        }
        unit.controlWeapons(shoot, shoot);
    }

    @Override
    public void updateMovement(){
        unloadPayloads();

        //Moves to target almost without slowing down
        if(target != null){
            moveTo(target, 0f);
            unit.lookAt(target);
        }
    }
    @Override
    public Teamc findTarget(float x, float y, float range, boolean air, boolean ground){
        var result = findMainTarget(x, y, range, unit.type.targetAir, unit.type.targetGround);

        //if the main target is in range, use it, otherwise target whatever is closest
        return checkTarget(result, x, y, range) ? target(x, y, range, unit.type.targetAir, unit.type.targetGround) : result;
    }

    @Override
    public Teamc findMainTarget(float x, float y, float range, boolean air, boolean ground){
        var core = targetFlag(x, y, BlockFlag.core, true);

        //If it can only target core, then go for it. If core is null, it won't do anything so it won't waste itself
        if(onlyTargetCore){
            return core;
        }

        //If no core, attack stuff that gets nearby.
        if(core == null){
            checkTarget(target, x, y, range);
            return target(x, y, range, unit.type.targetAir, unit.type.targetGround);
        }

        if(state.rules.randomWaveAI){
            //If waves, all units from one wave will go for one block
            rand.setSeed(unit.type.id + (state.rules.waves ? state.wave : unit.id));
            //Try a few random flags first
            for(int attempt = 0; attempt < 5; attempt++){
                Teamc result = targetFlagActive(x, y, randomTargets[rand.random(randomTargets.length - 1)], true);
                if(result != null) return result;
            }
            //If random targeting didn't find anything, just go for core.
            return core;
        }
        return core;
    }
}