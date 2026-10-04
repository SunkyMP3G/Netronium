package netro.type.units;

public class NetroBossUnit extends NetroUnitType{
    public NetroBossUnit(String name){
        super(name);
        canDrown = false;
        crashDamageMultiplier = 5f;
    }
}