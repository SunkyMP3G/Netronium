package netro.content;

import arc.math.*;
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
import netro.type.*;

public class NetroUnits{
    public static UnitType
    //region Contents
    ///Core
    point, direct, target,

    ///Siege ground (tanks)
    //beam, shell, flame, array, cascade,

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
        point = new NetroUnitType("point"){{
            health = 200;
            armor = 0f;
            hitSize = 12f;
            speed = 2.8f;
            accel = drag = 0.07f;

            mineTier = 1;
            mineSpeed = 3f;
            buildSpeed = 1f;
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
                beamWidth = 0.5f;
                repairSpeed = 1f;
                fractionRepairSpeed = 0.05f;
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
                    maxRange = 60f;
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

        //region Utility air
        fly = new NetroUnitType("fly"){{
            health = 60;
            armor = 0f;
            hitSize = 9f;
            speed = 2f;
            accel = drag = 0.09f;
            unitCap = 3;

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
        bomber = EntityRegistry.content("bomber", BossUnit.class, name -> new NetroBossUnit("bomber"){{
            health = 10000;
            armor = 4f;
            hitSize = 16f;
            speed = 0.6f;
            accel = drag = 0.08f;

            flying = true;
            targetAir = false;
            targetGround = true;
            unitCap = 8;

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