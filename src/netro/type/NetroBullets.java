package netro.type;

import mindustry.content.*;
import mindustry.entities.bullet.*;

public class NetroBullets{
    public static BulletType
    lightningVisual;

    public static void load(){
        lightningVisual = Bullets.damageLightning.copy();
        lightningVisual.collidesGround = false;
        lightningVisual.collidesTiles = false;
        lightningVisual.collidesAir = false;
    }
}