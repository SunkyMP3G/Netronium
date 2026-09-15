package netro.type;

import mindustry.gen.*;
import mindustry.world.blocks.defense.*;

///Unhealable block
@SuppressWarnings({"unused", "InnerClassMayBeStatic"})
public class SpecialTargetBlock extends Wall{
    public SpecialTargetBlock(String name){
        super(name);
    }
    public class SpecialTargetBlockBuild extends Building{
        //It's too boring if special target could just be covered with heal blocks. Players must actually protect it.
        //Maybe it'll be fair if it would heal by some % each wave, but that would be handled in the sector instead.
        @Override
        public void heal(float amount){}
        //Makes healing buildings ignore this block
        @Override
        public boolean isHealSuppressed(){
            return true;
        }
        //Makes healing units ignore this block
        @Override
        public boolean damaged(){
            return false;
        }
    }
}