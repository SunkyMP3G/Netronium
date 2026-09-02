package netro.type;

import arc.graphics.*;
import mindustry.type.*;
import mindustry.world.meta.*;

/**
 * Item that shows some other text instead of normal stats.
 * TODO maybe make them switch texts each frame?
 */
public class ObscureItem extends Item{
    public ObscureItem(String name, Color color){
        super(name);
        this.color = color;
    }
    public ObscureItem(String name){
        this(name, new Color(Color.black));
    }

    /// What stat numbers will be replaced to
    public String value = "?%";

    @Override
    public void setStats(){
        stats.add(Stat.explosiveness, value, "");
        stats.add(Stat.flammability, value, "");
        stats.add(Stat.radioactivity, value, "");
        stats.add(Stat.charge, value, "");
    }
}