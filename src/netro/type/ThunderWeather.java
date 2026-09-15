package netro.type;

import arc.graphics.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import mindustry.content.*;
import mindustry.core.*;
import mindustry.entities.*;
import mindustry.entities.bullet.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.weather.*;
import mindustry.world.*;
import netro.content.*;

import static mindustry.Vars.*;

/** Rain, but also spawns damaging lighnings. Only one lightning strike can be active at a time */
public class ThunderWeather extends RainWeather{
    private static final Rand random = new Rand();
    private static int lastSeed = 0;
    /** Chance of lightning appearing each tick. */
    public float strikeChance = 0.005f;
    /** Lightning strike radius. */
    public float strikeRadius = 24f;
    /** Approximate lightning strike damage. Affected by armor and distance from center */
    public float strikeDamage = 250f;
    /** Time between effects staring and the actual strike (ticks). */
    public float strikeDelay = 120f;
    /** How long is the visual lightning. */
    public int lightningLength = 4;
    /** How many fireballs are created on strike. */
    public int fireCount = 4;
    /** Chance of small lightning branches appearing per each "turn" of the bolt. */
    public static float branchChance = 0.33f;
    /** Branch length. */
    public static int branchLength = 2;

    protected float strikeX, strikeY;
    protected float strikeTime;
    protected boolean strike;

    public ThunderWeather(String name){
        super(name);
    }

    @Override
    public void update(WeatherState state){
        //Ends normal rain
        if(Weathers.rain.isActive() && Weathers.rain.instance().life() > 241){
            Weathers.rain.instance().life(240);
        }
        if(Mathf.chance(strikeChance) && !strike){
            //Picks random position for lightning strike
            strikeX = Mathf.random(world.width() * 8); strikeY = Mathf.random(world.height() * 8);
            strikeTime = 0;
            strike = true;
            NetroFx.thunderWarn.at(strikeX, strikeY, 0, this);
        }
        if(strike){
            if(strikeTime > strikeDelay){
                strikeTime = 0;
                strike = false;

                if(fireCount > 0){
                    for(int i = 0; i < fireCount; i++){
                        Call.createBullet(Bullets.fireball, Team.derelict, strikeX, strikeY, Mathf.random(360f), Bullets.fireball.damage, 1, 1);
                    }
                }

                Damage.damage(null, strikeX, strikeY, strikeRadius, strikeDamage, true);
                float pitch = Mathf.range(0.1f) + 0.5f;
                //Plays sound near the strike
                Sounds.acceleratorLightning1.at(strikeX, strikeY, pitch, 1.2f);
                //Plays sound everywhere
                Sounds.acceleratorLightning1.play(0.2f, pitch, 0f);
                Effect.shake(15, 10, strikeX, strikeY);
                Fx.dynamicExplosion.at(strikeX, strikeY, strikeRadius / 14f);
                //Creates visual lightning
                createBolt(lastSeed++, Team.derelict, Pal.lancerLaser, strikeX, strikeY, Mathf.random(20f) + 90f, lightningLength + Mathf.range(2));
            }else{
                strikeTime += Time.delta;
            }
        }
    }

    /** Stripped off version of createLightningInternal. Only visual. */
    public static void createBolt(int seed, Team team, Color color, float x, float y, float rotation, int length){
        random.setSeed(seed);

        Seq<Vec2> lines = new Seq<>();

        for(int i = 0; i < length; i++){
            NetroBullets.lightningVisual.create(null, team, x, y, rotation, 0, 1f, 1f, null);
            lines.add(new Vec2(x + Mathf.range(0f), y + Mathf.range(0f)));

            //Create mini branches
            if(Mathf.chance(branchChance) && i > branchLength){
                miniBolt(lastSeed++, team, color, x, y, rotation + random.range(60f) + 180f, branchLength);
            }
            rotation += random.range(10f);
            x += Angles.trnsx(rotation, 15f);
            y += Angles.trnsy(rotation, 15f);
        }

        NetroFx.thunderStrike.at(x, y, rotation, color, lines);
    }

    /** Creates small bolt branches. */
    public static void miniBolt(int seed, Team team, Color color, float x, float y, float rotation, int length){
        random.setSeed(seed);

        Seq<Vec2> branchLines = new Seq<>();

        for(int i = 0; i < length; i++){
            NetroBullets.lightningVisual.create(null, team, x, y, rotation, 0, 1f, 1f, null);
            branchLines.add(new Vec2(x + Mathf.range(0f), y + Mathf.range(0f)));

            rotation += random.range(20f);
            x += Angles.trnsx(rotation, 15f);
            y += Angles.trnsy(rotation, 15f);
        }

        NetroFx.thunderStrike.at(x, y, rotation, color, branchLines);
    }
}