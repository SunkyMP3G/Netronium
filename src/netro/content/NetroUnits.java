package netro.content;

import arc.graphics.*;
import arc.math.geom.*;
import ent.anno.Annotations.*;
import mindustry.ai.types.*;
import mindustry.content.*;
import mindustry.entities.bullet.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.type.weapons.*;
import netro.ai.*;
import netro.gen.*;
import netro.type.abilities.*;
import netro.type.units.*;

public class NetroUnits{
    //region Contents

    //other unit names (WIP)
    //lighten, shine, gleam, luminate, radiate,
    //alfa, bravo, charlie, delta, echo,
    //prox, epsi, sol, sirius, arcturus,
    //lusci, falco, casso, dromornis,
    //shell, flame, array, cascade,

    /** Core */
    public static @EntityDef(value = {Unitc.class, Netroc.class}, genIO = false) UnitType point, direct, target;
    /** Siege ground (tanks) */
    public static @EntityDef(value = {Unitc.class, Netroc.class, Tankc.class}, genIO = false) UnitType beam;
    /** Utility air */
    public static @EntityDef(value = {Unitc.class, Netroc.class}, genIO = false) UnitType fly;
    /** Phomaxite air */
    public static @EntityDef(value = {Unitc.class, Netroc.class}, genIO = false) UnitType spit;
    /** Phomaxite ground */
    public static @EntityDef(value = {Unitc.class, Netroc.class, Mechc.class}, genIO = false) UnitType longSpit, bauld;
    /** Boss units */
    public static @EntityDef(value = {Unitc.class, Netroc.class, Bossc.class}, genIO = false) UnitType bomber;
    //endregion Contents

    public static void load(){
        //region Core
        point = new NetroUnitType("point"){{
            health = 180;
            armor = 0f;
            hitSize = 10f;
            speed = 3.5f;
            accel = drag = 0.07f;

            mineTier = 1;
            mineSpeed = 2.5f;
            buildSpeed = 1f;
            faceTarget = true;
            flying = true;
            isEnemy = false;
            itemCapacity = 60;

            //For testing purposes.
            abilities.add(new IdleShieldAbility(){{
                addedHealth = 3f;
                isHardAbility = true;
            }});

            controller = u -> new BuilderAI(true, 160);

            weapons.add(new RepairBeamWeapon(){{
                widthSinMag = 0.11f;
                reload = 30f;
                x = 0f;
                y = 4f;
                rotate = false;
                shootY = 0f;
                beamWidth = 1f;
                repairSpeed = 0.6f;
                fractionRepairSpeed = 0.03f;
                recentDamageMultiplier = 0.05f;
                aimDst = 0f;
                shootCone = 15f;
                mirror = false;

                targetUnits = false;
                targetBuildings = true;
                autoTarget = false;
                controllable = true;
                laserColor = Pal.accent;
                healColor = Pal.accent;

                bullet = new BulletType(){{
                    maxRange = 50f;
                }};
            }});

        }};
        direct = new NetroUnitType("direct"){{
            health = 360;
            armor = 3f;
            hitSize = 16f;
            speed = 3.2f;
            accel = drag = 0.06f;

            mineTier = 2;
            mineSpeed = 5f;
            buildSpeed = 1.5f;
            faceTarget = true;
            flying = true;
            isEnemy = false;

            controller = u -> new BuilderAI(true, 200);

            weapons.add(new RepairBeamWeapon(){{
                widthSinMag = 0.11f;
                reload = 30f;
                x = 0f;
                y = 4f;
                rotate = false;
                shootY = 0f;
                beamWidth = 0.65f;
                repairSpeed = 1.5f;
                fractionRepairSpeed = 0.06f;
                aimDst = 0f;
                shootCone = 15f;
                mirror = false;

                targetUnits = false;
                targetBuildings = true;
                autoTarget = false;
                controllable = true;
                laserColor = Pal.accent;
                healColor = Pal.accent;

                bullet = new BulletType(){{
                    maxRange = 80f;
                }};
            }});
        }};
        target = new NetroUnitType("target"){{
            health = 650;
            armor = 8f;
            hitSize = 20f;
            speed = 3.8f;
            accel = drag = 0.06f;

            mineTier = 3;
            mineSpeed = 7f;
            buildSpeed = 2f;
            faceTarget = true;
            flying = true;
            isEnemy = false;

            controller = u -> new BuilderAI(true, 200);

            weapons.add(new RepairBeamWeapon(){{
                widthSinMag = 0.11f;
                reload = 30f;
                x = 5f;
                y = 4f;
                rotate = false;
                shootY = 0f;
                beamWidth = 0.8f;
                repairSpeed = 1f;
                fractionRepairSpeed = 0.035f;
                aimDst = 0f;
                shootCone = 15f;
                mirror = true;

                targetUnits = false;
                targetBuildings = true;
                autoTarget = false;
                controllable = true;
                laserColor = Pal.accent;
                healColor = Pal.accent;

                bullet = new BulletType(){{
                    maxRange = 100f;
                }};
            }});
        }};
        //endregion Core

        //region Siege ground
        beam = new NetroUnitType("beam"){{
            health = 800;
            hitSize = 12f;
            speed = 0.9f;
            rotateSpeed = 2f;

            flying = false;
            itemCapacity = 0;
            researchCostMultiplier = 0f;
            floorMultiplier = 0.7f;

            treadPullOffset = 0;
            treadRects = new Rect[]{
                new Rect(-20f, -20f, 40, 40)
            };

            weapons.add(new Weapon("netro-beam-weapon"){{
                reload = cooldownTime = 75f;
                layerOffset = 0.0001f;
                mirror = false;
                top = true;
                x = y = 0;
                shootY = 5f;
                recoil = 1f;
                rotate = true;
                rotateSpeed = 3.2f;
                shootCone = 2f;
                shootSound = Sounds.shootLancer;
                heatColor = Color.valueOf("f9350f");
                bullet = new LaserBulletType(32f){{
                    buildingDamageMultiplier = 1.3f;
                    pierce = false;

                    width = 12f;
                    length = 35f;
                    sideAngle = 45f;
                    sideWidth = 0.9f;
                    sideLength = 10f;
                    colors = new Color[]{Pal.neoplasm1.cpy().a(0.4f), Pal.neoplasm1, Color.white};
                }};
            }});

            squareShape = true;
            omniMovement = false;
            rotateMoveFirst = true;
        }};
        //endregion Siege ground

        //region Utility air
        fly = new NetroUnitType("fly"){{
            health = 60;
            armor = 0f;
            hitSize = 9f;
            speed = 2f;
            accel = drag = 0.09f;

            //Unit seeking range
            range = 160f;
            flying = true;
            targetAir = false;
            targetGround = true;

            crashDamageMultiplier = 4f;
            wreckHealthMultiplier = 3f;

            controller = u -> new FlyingKamikazeAI();

            weapons.add(new Weapon(){{
                shootOnDeath = true;
                reload = 6f;
                shootCone = 180f;
                ejectEffect = Fx.none;
                shootSound = Sounds.explosion;
                x = y = shootX = shootY = 0f;
                mirror = false;
                bullet = new BulletType(0, fallDmgStat()){{
                    collides = false;

                    hitEffect = shootEffect = Fx.none;
                    instantDisappear = true;
                    killShooter = true;
                    hittable = false;
                }};
            }});
        }};
        //endregion Utility air

        //region Phomaxite
        spit = new PhomaxiteUnitType("spit"){{
            health = 430f;
            armor = 0f;
            hitSize = 11f;
            speed = 1.5f;
            accel = drag = 0.09f;
            flying = true;
            controller = u -> new PhomaxiteAI();

            weapons.add(new Weapon(){{
                top = false;
                shootY = 3f;
                reload = 30f;
                ejectEffect = Fx.none;
                recoil = 0f;
                shootSound = Sounds.shootAtrax;

                bullet = new LiquidBulletType(Liquids.arkycite){{
                    damage = 32f;
                    speed = 2f;
                    drag = 0.009f;
                    shootEffect = Fx.shootSmall;
                    lifetime = 30f;
                }};
            }});
        }};
        //endregion Phomaxite

        //region Boss
        bomber = new NetroBossUnit("bomber"){{
            health = 10000;
            armor = 4f;
            hitSize = 16f;
            speed = 0.6f;
            accel = drag = 0.08f;

            flying = true;
            targetAir = false;
            targetGround = true;

            crashDamageMultiplier = 999f;
            wreckHealthMultiplier = 10f;

            controller = u -> new FlyingKamikazeAI(true);

            weapons.add(new Weapon(){{
                shootOnDeath = true;
                reload = 6f;
                shootCone = 180f;
                ejectEffect = Fx.none;
                shootSound = Sounds.explosion;
                x = y = shootX = shootY = 0f;
                mirror = false;
                bullet = new BulletType(0, fallDmgStat()){{
                    collides = false;

                    hitEffect = shootEffect = Fx.none;
                    instantDisappear = true;
                    killShooter = true;
                    hittable = false;
                }};
            }});
        }};
        //endregion Boss
    }
}