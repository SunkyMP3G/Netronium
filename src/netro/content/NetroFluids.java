package netro.content;

import arc.graphics.*;
import mindustry.content.*;
import mindustry.type.*;
import netro.type.*;

public class NetroFluids{
    public static Liquid
    //region Contents
    undergroundWater, gatrideFuel, phomaxite, steam,
    lava, cryonite, neutronium;
    //endregion Contents

    public static void load(){
        undergroundWater = new Liquid("underground-water", Color.valueOf("596ab8")){{
            heatCapacity = 0.4f;

            effect = StatusEffects.wet;

            alwaysUnlocked = true;
        }};
        gatrideFuel = new Liquid("gatride-fuel", Color.valueOf("000000")){{
            viscosity = 0.75f;
            flammability = 0.8f;
            explosiveness = 0.6f;
            heatCapacity = 0.7f;
            temperature = 0.4f;

            effect = StatusEffects.tarred;
        }};
        phomaxite = new CellLiquid("phomaxite", Color.valueOf("000000")){{
            viscosity = 0.9f;
            flammability = 0.2f;
            heatCapacity = 0.5f;
            temperature = 0.4f;
        }};
        steam = new Liquid("steam", Color.valueOf("888888")){{
            gas = true;
            viscosity = 0f;
            heatCapacity = 0.4f;
            temperature = 0.7f;
        }};
        lava = new Liquid("lava", Color.valueOf("ffa166")){{
            temperature = 1.2f;
            viscosity = 0.9f;

            effect = StatusEffects.melting;
            lightColor = Color.valueOf("f0511d").a(0.4f);
        }};
        cryonite = new Liquid("cryonite", Color.valueOf("8888ff")){{
            temperature = 0.1f;
            heatCapacity = 0.2f;
            viscosity = 0.6f;

            effect = StatusEffects.freezing;
        }};
        neutronium = new ObscureFluid("neutronium", Color.valueOf("ff8888")){{
            gas = true;
            viscosity = 0f;
            temperature = 0.6f;
            heatCapacity = 0.8f;
            explosiveness = 0.7f;
        }};
    }
}