package netro.content;

import arc.graphics.*;
import mindustry.content.*;
import mindustry.entities.bullet.*;
import mindustry.entities.part.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.defense.*;
import mindustry.world.blocks.defense.turrets.*;
import mindustry.world.blocks.distribution.*;
import mindustry.world.blocks.environment.*;
import mindustry.world.blocks.power.*;
import mindustry.world.draw.*;
import mindustry.world.meta.*;
import netro.gen.*;
import netro.graphics.*;
import netro.type.*;

import static mindustry.type.ItemStack.*;

/**
 * TODO biomes:
 * Sands: Hard sand, quicksand and Sandstone floors. Sandstone wall.
 * Volcanic: Lava, basalt
 */

public class NetroBlocks{
    public static Block

    //region Contents
    //Env floors
    hardSand, sandstone,
    quicksand,

    //Env walls
    sandstoneWall,

    //Env props

    //Distribution
    mechanicalDuct, mechanicalRouter, mechanicalBridge,

    //Fluids

    //Production

    //Crafting

    //Power
    smallNode, mediumNode, longNode, largeNode,
    smallBattery, bigBattery,
    smallSolarPanel, bigSolarPanel,

    //Turrets
    origin,

    //Walls
    dioniteWall, //platedWall, redPlatedWall, spectreWall,

    //Core
    coreShield, coreBarrier, coreDome,

    //Storage

    //Editor
    cameraSkip, testProtect;
    //endregion Contents

    public static void load(){
        //region Env floors
        hardSand = new Floor("hard-sand"){{
            variants = 3;
            attributes.set(Attribute.sand, 0.6f);
        }};
        sandstone = new Floor("sandstone"){{
            variants = 5;
            attributes.set(Attribute.sand, 0.4f);
        }};
        quicksand = new Floor("quicksand"){{
            variants = 3;
            speedMultiplier = 0.1f; //It will gradually decrease speed to this value.
            dragMultiplier = 5f;
            drownTime = 300f; //How many ticks until the min speed is reached for 1x1 sized units. Units WON'T be destroyed in quicksand.
            drownUpdateEffect = Fx.unitDust;
            isLiquid = true;
            walkEffect = Fx.unitDust;
            walkSound = Sounds.stepMud;
            attributes.set(Attribute.sand, 0.8f);
        }};
        //endregion Env floors

        //region Env walls
        sandstoneWall = new StaticWall("sandstone-wall"){{
            variants = 3;
            hardSand.asFloor().wall = sandstone.asFloor().wall = this;
            attributes.set(Attribute.sand, 0.6f);
        }};
        //endregion Env walls

        //region Distribution
        mechanicalDuct = new NetroDuct("mechanical-duct"){{
            requirements(Category.distribution, with(Items.copper, 1));
            health = 70;

            speed = 5f;
        }};
        mechanicalRouter = new DuctRouter("mechanical-router"){{
            requirements(Category.distribution, with(Items.copper, 1));
            health = 80;

            speed = 5f;
        }};
        mechanicalBridge = new DuctBridge("mechanical-bridge"){{
            requirements(Category.distribution, with(Items.copper, 1));
            health = 100;

            speed = 5f;
            ((NetroDuct)mechanicalDuct).bridgeReplacement = this;
        }};
        //endregion Distribution

        //region Power
        smallNode = new PowerNode("small-node"){{
            requirements(Category.power, with(Items.copper, 1));
            health = 120;

            maxNodes = 5;
            laserRange = 5f;
        }};
        mediumNode = new PowerNode("medium-node"){{
            requirements(Category.power, with(Items.copper, 1));
            health = 350;
            size = 2;

            maxNodes = 12;
            laserRange = 12f;
        }};
        longNode = new PowerNode("long-node"){{ //YOUR LONG
            requirements(Category.power, with(Items.copper, 1));
            health = 500;
            size = 2;

            maxNodes = 2;
            laserRange = 30f;
        }};
        largeNode = new PowerNode("large-node"){{
            requirements(Category.power, with(Items.copper, 1));
            health = 800;
            size = 3;

            maxNodes = 25;
            laserRange = 18f;
        }};

        smallBattery = new Battery("small-battery"){{
            requirements(Category.power, with(Items.copper, 1));
            health = 400;
            size = 2;

            consumePowerBuffered(10000f);
            baseExplosiveness = 2f;
        }};
        bigBattery = new Battery("big-battery"){{
            requirements(Category.power, with(Items.copper, 1));
            health = 800;
            size = 3;

            consumePowerBuffered(40000f);
            baseExplosiveness = 5f;
        }};

        smallSolarPanel = new SolarGenerator("small-solar-panel"){{
            requirements(Category.power, with(Items.copper, 1));
            health = 400;
            size = 2;

            powerProduction = 0.2f;
        }};

        bigSolarPanel = new SolarGenerator("large-solar-panel"){{
            requirements(Category.power, with(Items.copper, 1));
            health = 800;
            size = 3;

            powerProduction = 2f;
        }};
        //endregion Power

        //region Turrets
        origin = new ItemTurret("origin"){{
            requirements(Category.turret, with(Items.copper, 1));
            health = 250;
            size = 2;

            range = 120f;
            reload = 40f;
            inaccuracy = 2f;
            shootCone = 3f;
            rotateSpeed = 6f;
            recoil = 1f;
            outlineColor = NetroPal.netroOutline;
            cooldownTime = 15f;

            ammo(
            NetroItems.dionite,  new BasicBulletType(3f, 25){{
                width = 6f;
                height = 8f;
                lifetime = 40f;
                ammoMultiplier = 2;

                buildingDamageMultiplier = 0.5f;

                hitEffect = despawnEffect = Fx.hitBulletColor;
                frontColor = NetroPal.dioniteBulletFront;
                backColor = hitColor = NetroPal.dioniteBulletBack;
            }});

            drawer = new DrawTurret(){{
                parts.add(new RegionPart("-barrel"){{
                    progress = PartProgress.recoil;
                    under = true;
                    moveY = -1.5f;
                }});
            }};
        }};
        //endregion Turrets

        //region Walls
        dioniteWall = new Wall("dionite-wall"){{
            requirements(Category.defense,  with(Items.copper, 1));
            health = 400;
        }};
        //endregion Walls

        //region Core
        coreShield = new NetroCoreBlock("core-shield"){{
            requirements(Category.effect, with(Items.copper, 1));
            health = 3000;
            armor = 6f;
            size = 3;

            itemCapacity = 2500;
            unitType = NetroUnits.point;
            unitCapModifier = 12;
            isFirstTier = true;
            requiresCoreZone = true;

            alwaysUnlocked = true;
        }};
        coreBarrier = new NetroCoreBlock("core-barrier"){{
            requirements(Category.effect, with(Items.copper, 1));
            health = 12000;
            armor = 16f;
            size = 4;

            itemCapacity = 5000;
            unitType = NetroUnits.direct;
            unitCapModifier = 16;
        }};
        coreDome = new NetroCoreBlock("core-dome"){{
            requirements(Category.effect, with(Items.copper, 1));
            health = 25000;
            armor = 28f;
            size = 5;

            itemCapacity = 12000;
            unitType = NetroUnits.target;
            unitCapModifier = 24;
        }};
        //endregion Core

        //region Editor
        cameraSkip = new Wall("camera-skip"){{ //Only useful when I start making sectors
            requirements(Category.effect, BuildVisibility.hidden, with(Items.copper, 1));
            health = 1;
        }};
        testProtect = new SpecialTargetBlock("test-protect"){{
            requirements(Category.effect, BuildVisibility.sandboxOnly, with(Items.copper, 1));
            health = 20000;
            armor = 0;
            size = 6;
        }};
        //endregion Editor
    }
}