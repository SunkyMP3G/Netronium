package netro.content;

import arc.graphics.*;
import mindustry.content.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.meta.*;
import netro.type.weathers.*;

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
            density = 500f;
            color = Color.valueOf("4346ab");

            lightningLength = 30;
            strikeRadius = 32f;
            strikeDelay = 180f;
            strikeDamage = 600f;
            strikeChance = 0.007f;
            branchChance = 0.7f;
            branchLength = 20;
        }};
    }
}