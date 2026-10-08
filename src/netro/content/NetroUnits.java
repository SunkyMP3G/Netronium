package netro.content;

import arc.graphics.*;
import arc.math.geom.*;
import ent.anno.Annotations.*;
import mindustry.ai.types.*;
import mindustry.content.*;
import mindustry.entities.bullet.*;
import mindustry.entities.part.*;
import mindustry.entities.pattern.*;
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
    /** Core */
    public static @EntityDef(value = {Unitc.class, Netroc.class, Payloadc.class}, genIO = false) UnitType point, direct, target;
    /** Siege ground (tanks) */
    public static @EntityDef(value = {Unitc.class, Netroc.class, Tankc.class}, genIO = false) UnitType beam, shell, pierce;
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
            health = 460;
            armor = 1f;
            hitSize = 12f;
            speed = 0.9f;
            rotateSpeed = 2.1f;

            flying = false;
            itemCapacity = 0;
            researchCostMultiplier = 0f;

            treadRects = new Rect[]{
                new Rect(-20f, -20f, 16, 40)
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
                    length = 48f;
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
        shell = new NetroUnitType("shell"){{
            health = 1100;
            armor = 4f;
            hitSize = 18f;
            speed = 0.75f;
            rotateSpeed = 1.9f;

            flying = false;
            itemCapacity = 0;
            researchCostMultiplier = 0f;
            floorMultiplier = 0.9f;
            crushFragile = true;

            treadRects = new Rect[]{
                new Rect(-29f, -40f, 14, 80)
            };

            weapons.add(new Weapon("netro-shell-weapon"){{
                reload = 90f;
                layerOffset = 0.0001f;
                mirror = false;
                top = true;
                x = y = 0;
                shootY = 7f;
                recoil = 2.2f;
                rotate = true;
                rotateSpeed = 2.2f;
                inaccuracy = 3f;
                shootCone = 5f;
                shootSound = Sounds.shootArtillery;
                bullet = new BasicBulletType(4f, 60f){{
                    splashDamage = 30f;
                    splashDamageRadius = 24f;
                    shootEffect = Fx.shootBig;
                    despawnEffect = hitEffect = Fx.blastExplosion;
                    hitSound = despawnSound = Sounds.explosionCrawler;
                    lifetime = 20f;
                    hitShake = 2f;
                    despawnShake = 1f;
                    width = height = 12f;
                    hitSize = 5f;
                    buildingDamageMultiplier = 1.3f;
                }};
            }});

            squareShape = true;
            omniMovement = false;
            rotateMoveFirst = true;
        }};
        pierce = new NetroUnitType("pierce"){{
            health = 3500;
            armor = 7f;
            hitSize = 24f;
            speed = 0.65f;
            rotateSpeed = 1.7f;

            flying = false;
            itemCapacity = 0;
            researchCostMultiplier = 0f;
            floorMultiplier = 0.75f;
            crushFragile = true;

            treadRects = new Rect[] {
                new Rect(-51f, -56f, 22, 112)
            };

            abilities.add(new IdleShieldAbility(){{
                z = Layer.groundUnit + 1;
                addedHealth = 2f;
                isHardAbility = true;
            }});

            weapons.add(new Weapon("netro-pierce-weapon"){{
                reload = 110f;
                layerOffset = 0.001f;
                mirror = false;
                top = true;
                x = y = 0;
                shootY = 12f;
                recoil = 2.2f;
                rotate = true;
                rotateSpeed = 1.6f;
                shootCone = 5f;
                shootSound = Sounds.shootTank;
                inaccuracy = 3f;

                float barrelSpread = 6f;
                shoot = new ShootAlternate(barrelSpread){{
                    shots = 2;
                    shotDelay = 20f;
                }};

                recoils = 2;
                for(int i = 0; i < 2; i++){
                    int f = i;
                    parts.add(new RegionPart("-barrel"){{
                        x = f == 1 ? barrelSpread/2 : -barrelSpread/2;
                        recoilIndex = f;
                        under = true;
                        moves.add(new PartMove(PartProgress.recoil, 0f, -2f, 0f));
                    }});
                }

                bullet = new BasicBulletType(6f, 60f){{
                    pierce = pierceBuilding = true;
                    pierceCap = 3;
                    armorMultiplier = 0.75f;

                    //Makes hitting small targets a bit easier
                    homingRange = 16f;
                    homingPower = 0.01f;

                    shootEffect = Fx.shootBig;
                    hitEffect = Fx.hitBulletBig;
                    despawnEffect = Fx.blastExplosion;
                    trailWidth = 1.5f;
                    trailLength = 6;

                    lifetime = 28f;
                    hitShake = 1f;
                    width = 6f;
                    height = 12f;
                    hitSize = 3f;
                    buildingDamageMultiplier = 1.3f;
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