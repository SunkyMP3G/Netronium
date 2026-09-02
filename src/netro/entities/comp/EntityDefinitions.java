package netro.entities.comp;

import ent.anno.Annotations.*;
import mindustry.gen.*;
import netro.gen.*;

@SuppressWarnings("unused")
class EntityDefinitions<E>{
    @EntityDef({Unitc.class, Netroc.class}) E NetroUnit;
    @EntityDef({Unitc.class, Bossc.class}) E BossUnit;
}