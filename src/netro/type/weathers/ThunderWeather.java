package netro.type.weathers;

import arc.graphics.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.ctype.*;
import mindustry.entities.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.weather.*;
import mindustry.world.*;
import mindustry.world.blocks.defense.*;
import mindustry.world.blocks.power.PowerGenerator.*;
import netro.content.*;
import netro.type.blocks.*;
import netro.type.blocks.LightningRod.*;
import netro.ui.*;

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
    public int lightningLength = 60;
    /** How many fireballs are created on strike. */
    public int fireCount = 6;
    /** Chance of small lightning branches appearing per each "turn" of the bolt. */
    public static float branchChance = 0.33f;
    /** Branch length. */
    public static int branchLength = 2;

    public float origX, origY, strikeX, strikeY, strikeTime;
    protected boolean strike, diverted;
    protected Seq<Building> rods;
    protected Building rod;

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
            origX = Mathf.random(world.width() * 8); origY = Mathf.random(world.height() * 8);
            strikeX = origX; strikeY = origY;

            float prevRange = 0f;
            rods = null;

            var allRods = Vars.content.blocks().select(b -> b instanceof LightningRod);
            for(var b : allRods){
                //Rods with higher range have higher priority
                if(b instanceof LightningRod r && r.catchRange > prevRange){
                    prevRange = r.catchRange;
                    var candidates = locateBlocks(strikeX, strikeY, r.catchRange, r);
                    //If it finds any rods that have higher range, replace candidates.
                    if(candidates != null){
                        rods = candidates;
                    }
                }
            }
            //If it detects any rod, change position to hit it instead.
            if(rods != null){
                rod = Geometry.findClosest(strikeX, strikeY, rods);
                strikeX = rod.getX();
                strikeY = rod.getY();
                diverted = true;
            }

            strikeTime = 0;
            strike = true;
            NetroFx.thunderWarn.at(strikeX, strikeY, 0, this);
        }
        if(strike){
            if(strikeTime > strikeDelay){

                //Creates fire on strike position and then shoots more fire
                Call.createBullet(Bullets.fireball, Team.derelict, strikeX, strikeY, 0, Bullets.fireball.damage, 0, 1);
                if(fireCount > 0){
                    for(int i = 0; i < fireCount; i++){
                        Call.createBullet(Bullets.fireball, Team.derelict, strikeX, strikeY, Mathf.random(360f), Bullets.fireball.damage, 1, 1);
                    }
                }

                float pitch = Mathf.range(0.1f) + 0.5f;

                if(strikeDamage > 0){
                    if(!diverted){
                        Damage.damage(null, strikeX, strikeY, strikeRadius, strikeDamage);
                    }else{
                        //Deals less damage to rod
                        Damage.damage(null, strikeX, strikeY, strikeRadius * 0.3f, strikeDamage * 0.3f);
                        if(rod instanceof LightningRodBuild u){
                            u.striked();
                        }
                    }
                }

                //Plays sound near the strike
                Sounds.acceleratorLightning1.at(strikeX, strikeY, pitch, 1.2f);
                //Plays sound everywhere
                Sounds.acceleratorLightning1.play(0.3f, pitch, 0f);
                Effect.shake(13, 10, strikeX, strikeY);
                Fx.dynamicExplosion.at(strikeX, strikeY, strikeRadius / 14f);

                strikeTime = 0;
                strike = false;
                diverted = false;
                rod = null;
                //Creates visual lightning
                createBolt(lastSeed++, Pal.lancerLaser, strikeX, strikeY, Mathf.random(10f) + 90f, lightningLength + Mathf.range(5));
            }else{
                strikeTime += Time.delta;
                //Checks if rod is destroyed before the strike
                if(rod == null || rod.dead && diverted){
                    diverted = false;
                }
                if(Mathf.chance(0.4)){
                    Fx.overclocked.at(strikeX + Mathf.range(strikeRadius/3), strikeY + Mathf.range(strikeRadius/3), Pal.lancerLaser);
                }
            }
        }
    }

    /** Stripped off version of createLightningInternal. Only visual. */
    public static void createBolt(int seed, Color color, float x, float y, float rotation, int length){
        random.setSeed(seed);

        Seq<Vec2> lines = new Seq<>();

        for(int i = 0; i < length; i++){
            lines.add(new Vec2(x, y));
            if(i == length - 1){
                NetroFx.thunderCloud.at(x, y, 12f, Pal.darkerGray);
                for(int e = 0; e < 7; e++){
                    NetroFx.thunderCloud.at((x + Mathf.range(16f)), y + Mathf.range(16f), 12f, Pal.darkerGray);
                }
            }

            //Create mini branches
            if(Mathf.chance(branchChance) && i > branchLength){
                miniBolt(lastSeed++, color, x, y, rotation + random.range(60f) + 180f, branchLength);
            }
            rotation += random.range(10f);
            //See: https://discord.com/channels/391020510269669376/653293028869537843/1549659447151038476
            rotation = Mathf.clamp(rotation, 40f, 140f);

            x += Angles.trnsx(rotation, 10f);
            y += Angles.trnsy(rotation, 10f);
        }
        NetroFx.thunderStrike.at(x, y, rotation, color, lines);
    }

    /** Creates small bolt branches. */
    public static void miniBolt(int seed, Color color, float x, float y, float rotation, int length){
        random.setSeed(seed);

        Seq<Vec2> branchLines = new Seq<>();

        for(int i = 0; i < length; i++){
            branchLines.add(new Vec2(x, y));

            rotation += random.range(20f);
            x += Angles.trnsx(rotation, 10f);
            y += Angles.trnsy(rotation, 10f);
        }

        Fx.lightning.at(x, y, rotation, color, branchLines);
    }

    /** Checks for specific blocks in a radius. Returns all found blocks, or null if none found. */
    public static Seq<Building> locateBlocks(float x, float y, float range, Block block){
        Seq<Building> all = new Seq<>(Building.class);
        all.clear();
        //Goes through all blocks of a type in a radius, and adds all of them in a list.
        indexer.allBuildings(x, y, range, b -> {
            if(b.block == block){
                all.add(b);
            }
        });

        if(all.size < 1) return null;
        //Picks random block from the list and returns it.
        return all;
    }
}