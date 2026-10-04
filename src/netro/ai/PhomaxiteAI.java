package netro.ai;

import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import mindustry.*;
import mindustry.ai.types.*;
import mindustry.entities.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.meta.*;
import netro.content.*;

import static mindustry.Vars.*;

/** PHOMAXITE:
 * <p>Blocks:
 * <p>Core. Abominations will roam around it when idle.
 * <p>The bases are essentially nests. They won't grow.
 * <p>AI:
 * <p> When no enemy is nearby, roam around the nearest phomaxite core.
 * <p> When one abomination detects any attackable enemy unit, that unit calls other nearby abominations to attack that target.
 * <p> The target is marked until it's dead or lost.
 */
@SuppressWarnings("unused")
public class PhomaxiteAI extends AIController{
    /** Abomination will roam around this core. */
    public Teamc roamCore;
    /** Minimum delay between each move when roaming and its randomness. */
    public float roamDelay = 180f;
    /** After delay passes, there will be a chance each tick to pick new position. */
    public float roamChance = 0.008f;
    /** Max range from core this unit can roam to. */
    public float roamRange = 160f;
    /** Position the unit will try to roam to. */
    public Vec2 roamTarget = new Vec2();

    /** Range of detecting enemy units. */
    public float detectRange = 160f;
    /** Range in which nearby abominations will be called to attack something. */
    public float callRange = 480f;
    /** If target is out of this range, it's counted as lost. */
    public float chaseRange = 800f;
    /** If abomination is below this hp percentage, retreat back to nearest core. */
    public float retreatThreshold = 0.2f;

    /** Time in ticks ground abominations can be stuck before losing target. */
    public float stuckThreshold = 120f;

    /** Whether this unit is support (heal/rebuild) and won't follow other units in invasion. */
    public boolean supporter;
    /** Whether this unit will retreat back to core at low hp. If no core, it won't retreat. */
    public boolean retreats;

    protected float roamTimer;

    public PhomaxiteAI(){
        this(false, true);
    }
    public PhomaxiteAI(boolean support){
        this(support, true);
    }
    public PhomaxiteAI(boolean support, boolean canRetreat){
        this.supporter = support;
        this.retreats = canRetreat;
    }
    
    @Override
    public void updateMovement(){
        unloadPayloads();

        roamCore = targetFlag(unit.x, unit.y, BlockFlag.core, false);
        //If no target or at low hp, find the nearest core and roam around it.
        if(target == null || (unit.healthf() <= retreatThreshold && retreats && roamCore != null)){
            //Doesn't pick another roam point until nearby its current roam point.
            if(unit.within(roamTarget, 40f) || (roamTarget.x == 0 && roamTarget.y == 0)){
                if(roamTimer > roamDelay && Mathf.chance(roamChance)){
                    if(roamCore != null){
                        roamTarget.set(Mathf.clamp(roamCore.x() + Mathf.range(roamRange), 8f, world.width()*8-8), Mathf.clamp(roamCore.y() + Mathf.range(roamRange), 8f, world.height()*8-8));
                    }else{
                        //If no core, roam around anywhere.
                        roamTarget.set(Mathf.clamp(unit.x + Mathf.range(roamRange), 8f, world.width()*8-8), Mathf.clamp(unit.y + Mathf.range(roamRange), 8f, world.height()*8-8));
                    }
                    roamTimer = 0f;
                }else{
                    roamTimer += Time.delta;
                }
            }else{
                if(unit.isFlying()){
                    moveTo(roamTarget, 4f);
                    unit.lookAt(roamTarget);
                }else{
                    var result = controlPath.getPathPosition(unit, roamTarget);
                    if(result.move){
                        moveTo(result.dest, 4f, Tmp.v2.epsilonEquals(result.dest, 4.1f) ? 30f : 0f);
                    }
                    faceTarget();
                }
            }
        }
        //If it has a target, move and attack it.
        if(target != null){
            unit.apply(NetroStatuses.phomaxiteChase, 60f);
            roamTimer = roamDelay / 2f;
            roamTarget.set(0, 0);
            if(!supporter){
                if(unit.type.circleTarget){
                    circleAttack(unit.type.circleTargetRadius);
                }else{
                    if(unit.isFlying()){
                        moveTo(target, unit.type.range * 0.4f);
                        unit.lookAt(target);
                    }else{
                        var result = controlPath.getPathPosition(unit, Tmp.v2.set(target));
                        if(result.move){
                            moveTo(result.dest, unit.type.range * 0.4f, Tmp.v2.epsilonEquals(result.dest, 4.1f) ? 30f : 0f);
                        }
                        faceTarget();
                    }
                }
            }else{
                //Only chases units if it's nearby core, is already attacking or if there's no core at all.
                if(roamCore == null || unit.within(roamCore, roamRange) || unit.isShooting){
                    if(unit.type.circleTarget){
                        circleAttack(unit.type.circleTargetRadius);
                    }else{
                        if(unit.isFlying()){
                            moveTo(target, unit.type.range * 0.4f);
                            unit.lookAt(target);
                        }else{
                            var result = controlPath.getPathPosition(unit, Tmp.v2.set(target));
                            if(result.move){
                                moveTo(result.dest, unit.type.range * 0.4f, Tmp.v2.epsilonEquals(result.dest, 4.1f) ? 30f : 0f);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public Teamc findTarget(float x, float y, float range, boolean air, boolean ground){
        if(target == null){
            //Checks for any unit
            var targetCand = target(x, y, detectRange, true, true);
            if(targetCand instanceof Unit t){
                //When finds possible enemy, sets it as target for all nearby abominations that can attack it.
                Units.nearby(unit.team, unit.x, unit.y, callRange, u -> {
                    if(u.controller() instanceof PhomaxiteAI ally){
                        if(ally.target == null && (t.isFlying() && ally.unit.type.targetAir) || (t.isGrounded() && ally.unit.type.targetGround)){
                            ally.target = targetCand;
                        }
                    }
                });
                //Sets target for itself.
                if((t.isFlying() && unit.type.targetAir) || (t.isGrounded() && unit.type.targetGround)){
                    target = targetCand;
                }
            }
        }
        //Loses target if it's out of chaseRange or dead.
        return checkTarget(target, x, y, chaseRange) ? null : target;
    }
}