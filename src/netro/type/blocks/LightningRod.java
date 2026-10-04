package netro.type.blocks;

import arc.*;
import arc.math.*;
import arc.util.*;
import mindustry.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import mindustry.world.blocks.power.*;
import netro.ui.*;

import static mindustry.Vars.*;

/** Generates power by diverting lightning strikes on itself. Lightnings only deal partial damage when diverted. */
public class LightningRod extends PowerGenerator{
    protected float powerTimer;
    /** How much power this block generates per tick when struck. */
    public float strikePower = 500f;
    /** Time in ticks before power generation stops. */
    public float powerTime = 180f;
    /** Radius in which lightning can be caught. */
    public float catchRange = 80f;

    public LightningRod(String name){
        super(name);
        powerProduction = 1f;
        canOverdrive = false;
    }

    /** Draws radius */
    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid){
        super.drawPlace(x, y, rotation, valid);

        Drawf.dashCircle(x*tilesize + offset, y*tilesize + offset, catchRange, Pal.lancerLaser);
    }

    @Override
    public void setBars(){
        super.setBars();

        if(hasPower && outputsPower){
            addBar("power", (GeneratorBuild entity) -> new Bar(() ->
            Core.bundle.format("bar.poweroutput",
            Strings.fixed(entity.getPowerProduction() * 60f * entity.timeScale(), 0)),
            () -> Pal.lancerLaser,
            () -> powerTimer/powerTime));
        }
    }
    @Override
    public void setStats(){
        super.setStats();
        stats.remove(generationType);
        stats.add(NetroStats.rodPowerGen, strikePower * powerTime, NetroStatUnits.powerStrike);
    }

    public class LightningRodBuild extends GeneratorBuild{
        @Override
        public void updateTile(){
            productionEfficiency = powerTimer > 0f ? strikePower : 0f;
            powerTimer -= Time.delta;
            powerTimer = Mathf.clamp(powerTimer, 0f, powerTime);
        }
        @Override
        public void drawSelect(){
            Drawf.dashCircle(x, y, catchRange, Pal.lancerLaser);
        }

        public void striked(){
            powerTimer = powerTime;
        }
    }
}