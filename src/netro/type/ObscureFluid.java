package netro.type;

import arc.graphics.*;
import mindustry.type.*;
import mindustry.world.meta.*;

/// @see ObscureItem
public class ObscureFluid extends Liquid{
    public ObscureFluid(String name, Color color){
        super(name);
        this.color = new Color(color);
    }
    public ObscureFluid(String name){
        this(name, new Color(Color.black));
    }

    /// What stat numbers will be replaced to
    public String value = "?%";

    @Override
    public void setStats(){
        stats.add(Stat.explosiveness, value, "");
        stats.add(Stat.flammability, value, "");
        stats.add(Stat.temperature, value, "");
        stats.add(Stat.heatCapacity, value, "");
        stats.add(Stat.viscosity, value, "");
    }
}