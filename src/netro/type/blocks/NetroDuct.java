package netro.type.blocks;

import arc.*;
import arc.graphics.g2d.*;
import mindustry.world.blocks.distribution.*;

/// Duct, but with customizable bottom sprite on icon
@SuppressWarnings("unused")
public class NetroDuct extends Duct{
    public TextureRegion capRegion;

    public NetroDuct(String name){
        super(name);
    }

    @Override
    public TextureRegion[] icons(){
        return new TextureRegion[]{Core.atlas.find(name + "-bottom", "duct-bottom"), topRegions[0]};
    }
}