package netro.content;

import arc.graphics.*;
import arc.math.geom.*;
import mindustry.ai.types.*;
import mindustry.content.*;
import mindustry.entities.bullet.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.type.weapons.*;
import netro.ai.*;
import netro.gen.*;
import netro.type.*;
import netro.type.abilities.*;

import static mindustry.Vars.*;

public class NetroUnits{
    public static UnitType
    //region Contents
    ///Core
    point, direct, target,

    ///Siege ground (tanks)
    beam, //shell, flame, array, cascade,

    ///Support ground
    //lighten, shine, gleam, luminate, radiate,

    ///Assault air
    //prox, epsi, sol, sirius, arcturus,

    ///Utility air
    fly, //lusci, falco, casso, dromornis,

    ///Assault naval
    //alfa, bravo, charlie, delta, echo,

    ///Campaign
    //wavePortal;

    ///Boss units
    bomber;
    //endregion Contents

    public static void load(){
        //region Core
        point = EntityRegistry.content("point", PayloadNetroUnit.class, name -> new NetroUnitType(name){{
            health = 180;
            armor = 0f;
            hitSize = 40f;
            speed = 3.5f;
            accel = drag = 0.07f;

            mineTier = 1;
            mineSpeed = 2.5f;
            buildSpeed = 1f;
            faceTarget = true;
            flying = true;
            isEnemy = false;
            itemCapacity = 60;
            payloadCapacity = (5.5f * 5.5f) * tilePayload;

            abilities.add(new IdleShieldAbility(){{
                addedHealth = 3f;
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

        }});
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
        beam = EntityRegistry.content("beam", NetroUnit.class, name -> new NetroUnitType(name){{
            health = 800;
            hitSize = 16f;
            speed = 1.1f;
            rotateSpeed = 2f;

            this.constructor = TankUnit::create;
            flying = false;
            itemCapacity = 0;
            researchCostMultiplier = 0f;

            treadPullOffset = 0;
            treadRects = new Rect[] {
            new Rect(13f, -28f, 11, 56)
            };

            weapons.add(new Weapon("netroniummod-beam-weapon"){{
                reload = cooldownTime = 90f;
                layerOffset = 0.0001f;
                mirror = false;
                top = true;
                x = y = 0;
                shootY = 10f;
                recoil = 2f;
                rotate = true;
                rotateSpeed = 3.2f;
                shootCone = 2f;
                shootSound = Sounds.shootLancer;
                heatColor = Color.valueOf("f9350f");
                bullet = new LaserBulletType(32f){{
                    sideAngle = 45f;
                    sideWidth = 1f;
                    sideLength = 10f;
                    length = 35f;
                    buildingDamageMultiplier = 1.3f;
                    pierce = false;
                    colors = new Color[]{Pal.neoplasm1.cpy().a(0.4f), Pal.neoplasm1, Color.white};
                }};
            }});

            squareShape = true;
            omniMovement = false;
            rotateMoveFirst = true;
        }});
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

        //region Campaign
        //endregion Campaign

        //region Boss
        bomber = EntityRegistry.content("bomber", BossNetroUnit.class, name -> new NetroBossUnit(name){{
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
        }});
        //endregion Boss
    }
}