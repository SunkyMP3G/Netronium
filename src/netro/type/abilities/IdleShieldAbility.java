package netro.type.abilities;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import mindustry.*;
import mindustry.entities.abilities.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import netro.content.*;
import netro.gen.*;

/**
 * ArmorPlateAbility, but reversed to be active when NOT attacking. Also turns off when carrying items/payload
 */
public class IdleShieldAbility extends NetroAbility{
    public TextureRegion plateRegion;
    public TextureRegion shineRegion;
    /** Visual regions. If plate has no sprite, copies unit. If shine has no sprite, copies plate sprite. */
    public String plateSuffix = "-armor";
    public String shineSuffix = "-shine";

    /** Color of the shine. If null, uses team color. */
    public @Nullable Color color = null;
    /** How fast the shine moves. */
    public float shineSpeed = 1f;
    /** Shield layer. */
    public float z = -1;
    /** Opacity of shield. */
    public float alpha = 0.3f;
    /** Whether the shield also disables when unit is carrying items or payload */
    public boolean offWithItems = true, offWithPayload = true;
    /** How fast the shield fully turns on/off. Bigger number - faster. 1 is instant. */
    public float activationTime = 0.2f;
    /** Whether to draw the plate region. */
    public boolean drawPlate = true;
    /** Whether to draw the shine over the plate region. */
    public boolean drawShine = true;
    /** Added percentage of health. I.e. with 0.5 (+50%), unit with 100 health would have 150 health, which essentially makes received damage 33% less effective. */
    public float addedHealth = 0.5f;
    /** Internal shield activation progress. */
    protected float warmup = 1f;

    @Override
    public void update(Unit unit){
        super.update(unit);
        if(isHardAbility && unit instanceof Netroc u){
            //Only active if the enemy unit is "lucky" (more units are "lucky" in erad) and difficulty is Hard+. "lucky" is set in NetroComp.
            if(hardActive(u)){
                warmup = Mathf.lerpDelta(warmup, notIdle(unit) ? 1f : 0f, activationTime);
                unit.healthMultiplier += (1f - warmup) * addedHealth;
                unit.apply(NetroStatuses.hardBuff, 10f);
            }else{
                //Disable ability if difficulty is not high enough or unit is not "lucky"
                warmup = 1f;
            }
        }else{
            //Normal ability function
            warmup = Mathf.lerpDelta(warmup, notIdle(unit) ? 1f : 0f, activationTime);
            unit.healthMultiplier += (1f - warmup) * addedHealth;
        }
    }

    /** Checks if unit is not "idle". Returns true if unit is shooting or carrying any items/payload */
    public boolean notIdle(Unit unit){
        return unit.isShooting() || (offWithPayload && unit instanceof Payloadc p && p.hasPayload()) || (offWithItems && unit.hasItem());
    }

    @Override
    public void addStats(Table t){
        super.addStats(t);
        t.add(abilityStat("idledamagered", addedHealth == -1 ? "???" : Strings.autoFixed(100f - (100f / (1f + addedHealth)), 1)));
    }

    /** Copies ArmorPlate draw, but activates with a different condition. */
    @Override
    public void draw(Unit unit){
        //No point in trying to draw shield if it's explicitly stated not to draw it or unit is not "lucky".
        if((!drawPlate && !drawShine) || (isHardAbility && unit instanceof Netroc u && !hardActive(u))) return;

        //When idle, warmup is 1.
        if(warmup <= 0.999f){
            if(plateRegion == null){
                plateRegion = Core.atlas.find(unit.type.name + plateSuffix, unit.type.region);
                shineRegion = Core.atlas.find(unit.type.name + shineSuffix, plateRegion);
            }

            float pz = Draw.z();
            if(z > 0) Draw.z(z);

            if(drawPlate){
                Draw.alpha((1f - warmup) * alpha);
                Draw.rect(plateRegion, unit.x, unit.y, unit.rotation - 90f);
                Draw.alpha(1f);
            }

            if(drawShine){
                Draw.draw(Draw.z(), () -> {
                    Shaders.armor.region = shineRegion;
                    Shaders.armor.progress = 1f - warmup;
                    Shaders.armor.time = -Time.time / 20f * shineSpeed;

                    Draw.color(color == null ? unit.team.color : color, alpha);
                    Draw.shader(Shaders.armor);
                    Draw.rect(shineRegion, unit.x, unit.y, unit.rotation - 90f);
                    Draw.shader();

                    Draw.reset();
                });
            }

            Draw.z(pz);
        }
    }
}