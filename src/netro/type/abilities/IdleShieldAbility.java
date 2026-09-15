package netro.type.abilities;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import mindustry.entities.abilities.*;
import mindustry.gen.*;
import mindustry.graphics.*;

/**
 * ArmorPlateAbility, but reversed to be active when NOT attacking. Also turns off when carrying items/payload
 */
public class IdleShieldAbility extends Ability{
    public TextureRegion plateRegion;
    public TextureRegion shineRegion;
    public String plateSuffix = "-armor";
    public String shineSuffix = "-shine";
    /** Color of the shine. If null, uses team color. */
    public @Nullable Color color = null;
    public float shineSpeed = 1f;
    /** Shield layer*/
    public float z = -1;
    /** Opacity of shield. */
    public float alpha = 0.3f;
    /** Whether the shield also disables when unit is carrying items or payload */
    public boolean offWithItems = true, offWithPayload = true;
    /** How fast the shield fully turns on/off. 0.1 is around 2 seconds. */
    public float activationTime = 0.2f;
    /** Whether to draw the plate region. */
    public boolean drawPlate = true;
    /** Whether to draw the shine over the plate region. */
    public boolean drawShine = true;
    /** How much the health is increased/decreased. */
    public float addedHealth = 0.5f;
    /** Shield activation progress. */
    protected float warmup;

    @Override
    public void update(Unit unit){
        super.update(unit);
        //No point in increasing received damage when idle. Also prevents division by 0.
        addedHealth = Math.max(addedHealth, 0f);

        warmup = Mathf.lerpDelta(warmup, unit.isShooting() || (offWithPayload && unit instanceof Payloadc p && p.hasPayload()) || (offWithItems && unit.hasItem()) ? 1f : 0f, activationTime);
        unit.healthMultiplier += (1f - warmup) * addedHealth;
    }

    @Override
    public void addStats(Table t){
        super.addStats(t);
        t.add(abilityStat("idledamagered", Strings.autoFixed(100f - (100f / (1f + addedHealth)), 1)));
    }

    @Override
    public void draw(Unit unit){
        if(!drawPlate && !drawShine) return;

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