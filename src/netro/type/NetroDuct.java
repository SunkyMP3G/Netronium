package netro.type;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import ent.anno.Annotations.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.blocks.distribution.*;

import static mindustry.Vars.*;

/// Duct, but with cap (like reinforced/plated conduits) and customizable bottom sprite on icon
@SuppressWarnings("unused")
public class NetroDuct extends Duct{
    public TextureRegion capRegion;

    public NetroDuct(String name){
        super(name);
    }

    @Override
    public void load(){
        super.load();

        region = Core.atlas.find(name);

        ContentRegions.loadRegions(this);

        //load specific team regions
        teamRegions = new TextureRegion[Team.all.length];
        for(Team team : Team.all){
            teamRegions[team.id] = teamRegion.found() && team.hasPalette ? Core.atlas.find(name + "-team-" + team.name, teamRegion) : teamRegion;
        }

        if(variants != 0){
            variantRegions = new TextureRegion[variants];

            for(int i = 0; i < variants; i++){
                variantRegions[i] = Core.atlas.find(name + (i + 1));
            }
            region = variantRegions[0];

            if(customShadow){
                variantShadowRegions = new TextureRegion[variants];
                for(int i = 0; i < variants; i++){
                    variantShadowRegions[i] = Core.atlas.find(name + "-shadow" + (i + 1));
                }
            }
        }
        capRegion = Core.atlas.find(name + "-cap");
    }

    @Override
    public TextureRegion[] icons(){
        return new TextureRegion[]{Core.atlas.find(name + "-bottom", "duct-bottom"), topRegions[0]};
    }

    public class NetroDuctBuild extends DuctBuild{
        public boolean capped, backCapped = false;
        public @Nullable Building prev;

        @Override
        public void draw(boolean under){
            float rotation = rotdeg();
            int r = this.rotation;

            Draw.z(Layer.blockUnder + 0.2f);

            //draw extra ducts facing this one for tiling purposes
            for(int i = 0; i < 4; i++){
                if((blending & (1 << i)) != 0){
                    int dir = r - i;
                    float rot = i == 0 ? rotation : (dir) * 90;
                    drawAt(x + Geometry.d4x(dir) * tilesize * 0.75f, y + Geometry.d4y(dir) * tilesize * 0.75f, 0, rot, i != 0 ? SliceMode.bottom : SliceMode.top, under);
                }
            }

            //draw item
            if(!under && current != null){
                Draw.z(Layer.blockUnder + 0.1f);
                Tmp.v1.set(Geometry.d4x(recDir) * tilesize / 2f, Geometry.d4y(recDir) * tilesize / 2f)
                .lerp(Geometry.d4x(r) * tilesize / 2f, Geometry.d4y(r) * tilesize / 2f,
                Mathf.clamp((progress + 1f) / (2f - 1f / speed)));

                Draw.rect(current.fullIcon, x + Tmp.v1.x, y + Tmp.v1.y, itemSize, itemSize);
            }

            Draw.scl(xscl, yscl);
            Draw.z(Layer.blockUnder + 0.2f);
            drawAt(x, y, blendbits, rotation, SliceMode.none, under);
            Draw.reset();

            if(!under) return;

            if(capped && capRegion.found()) Draw.rect(capRegion, x, y, rotdeg());
            if(backCapped && capRegion.found()) Draw.rect(capRegion, x, y, rotdeg() + 180);
        }

        @Override
        public void onProximityUpdate(){
            super.onProximityUpdate();
            recache();

            int[] bits = buildBlending(tile, rotation, null, true);
            blendbits = bits[0];
            xscl = bits[1];
            yscl = bits[2];
            blending = bits[4];
            next = front();
            nextc = next instanceof DuctBuild d ? d : null;

            prev = back();
            capped = next == null || next.team != team || !next.block.hasItems;
            backCapped = blendbits == 0 && (prev == null || prev.team != team || !prev.block.hasItems);
        }
    }
}