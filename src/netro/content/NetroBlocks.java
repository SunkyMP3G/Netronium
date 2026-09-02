package netro.content;

import mindustry.content.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.defense.*;
import mindustry.world.blocks.defense.turrets.*;
import mindustry.world.blocks.distribution.*;
import mindustry.world.blocks.power.*;
import mindustry.world.meta.*;
import netro.type.*;

import static mindustry.type.ItemStack.*;

public class NetroBlocks{
    public static Block

    //region Contents
    //Environment floors

    //Env walls

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
    cameraSkip;
    //endregion Contents

    public static void load(){
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
        }};
        //endregion Turrets

        //region Walls
        dioniteWall = new Wall("dionite-wall"){{
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
        //endregion Editor
    }
}