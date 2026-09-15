package netro.content;

import arc.graphics.*;
import mindustry.content.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.meta.*;
import netro.type.*;

public class NetroWeathers{
    public static Weather
    //region Contents
    thunder;
    //endregion Contents

    public static void load(){
        thunder = new ThunderWeather("thunder"){{
            attrs.set(Attribute.light, -0.8f);
            attrs.set(Attribute.water, 0.4f);
            status = StatusEffects.wet;
            sound = Sounds.rain;
            soundVol = 0.8f;
            density = 1000f;
            color = Color.valueOf("4346ab");

            lightningLength = 20;
            strikeRadius = 32f;
            strikeDelay = 150f;
            strikeDamage = 420f;
            strikeChance = 0.01f;
            branchChance = 0.33f;
            branchLength = 4;
        }};
    }
}