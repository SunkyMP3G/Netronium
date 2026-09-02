package netro.type;

import arc.graphics.g2d.*;
import mindustry.game.*;
import mindustry.world.blocks.storage.*;

public class NetroCoreBlock extends CoreBlock{
    public NetroCoreBlock(String name){
        super(name);
        squareSprite = false;
        incinerateNonBuildable = true;
    }

    /// Makes team region display on icon
    @Override
    protected TextureRegion[] icons() {
        return teamRegion.found() ? new TextureRegion[]{region, teamRegions[Team.sharded.id]} : new TextureRegion[]{region};
    }
}