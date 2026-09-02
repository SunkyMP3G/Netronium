package netro.type;

import arc.math.*;
import mindustry.type.*;
import netro.graphics.*;

public class NetroUnitType extends UnitType{
    /// If >0, sets a custom unit cap for this unit type. Useful for summons.
    public static int unitCap = -1;

    public NetroUnitType(String name){
        super(name);
        researchCostMultiplier = 0f;
        lowAltitude = true;
        outlineColor = NetroPal.netroOutline;
    }

     ///Counts fall damage to show in stats for kamikaze units.
    public float fallDmgStat(){
        return Mathf.round(Mathf.pow(this.hitSize, 0.75f) * this.crashDamageMultiplier * 2.5f);
    }
}