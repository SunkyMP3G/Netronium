package netro.type;

import arc.graphics.*;
import mindustry.type.*;

/// Item that doesn't show any stats.
public class LoreItem extends Item{
    public LoreItem(String name, Color color){
        super(name);
        this.color = color;
    }
    public LoreItem(String name){
        this(name, new Color(Color.black));
    }

    /// No stats.
    @Override
    public void setStats(){}
}