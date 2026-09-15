package netro.graphics.draw;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.util.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.world.draw.*;

//Add flashing?
/**
 * Draws sprite with color depending on block's current health. Default is from Pal.heal to Pal.health
 */
public class DrawHealth extends DrawBlock{
    public TextureRegion region;
    public String suffix = "";
    /** If set, overrides the region name. */
    public @Nullable String name;
    public @Nullable Color color1 = Pal.heal, color2 = Pal.health;
    public float x, y, rotation;
    /** Any number <=0 disables layer changes. */
    public float layer = -1;

    public DrawHealth(String suffix){
        this.suffix = suffix;
    }

    public DrawHealth(){
    }

    @Override
    public void draw(Building build){
        float z = Draw.z();
        if(layer > 0) Draw.z(layer);
        if(color1 != null && color2 != null) Draw.color(color1.lerp(color2, 1 - build.healthf()));
        Draw.rect(region, build.x + x, build.y + y, rotation);
        if(color1 != null && color2 != null) Draw.color();
        Draw.z(z);
    }

    @Override
    public void load(Block block){
        region = Core.atlas.find(name != null ? name : block.name + suffix);
    }
}