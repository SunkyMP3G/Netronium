package netro.ui;

import arc.*;
import arc.graphics.*;
import arc.math.*;
import arc.scene.*;
import arc.scene.event.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import mindustry.content.*;
import mindustry.game.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.ui.*;
import netro.content.*;

import static mindustry.Vars.*;

/**
 * Shows health bar of a specific block in a specific sector.
 */
public class ProtectBar{
    Table hudLabel = null;

    public void build(Group parent){
        parent.fill(t -> {
            //Which side to place on
            t.left();
            var protectb = new StringBuilder();
            var protectText = Core.bundle.get("protectbar");

            t.table(v -> v.margin(10f)
            .add(new Bar(() -> {
                //Clears and sets text
                protectb.setLength(0);
                protectb.append(protectText);
                return protectb;
            }, () -> Pal.heal, () -> {
                if(!checkSector(SectorPresets.onset) && state.rules.defaultTeam.data().buildings.size == 0) return 0f;

                float max = 0f, val = 0f;
                for(var build : state.rules.defaultTeam.data().buildings){
                    //Which block
                    if(build.block == NetroBlocks.testProtect){
                        max += build.maxHealth;
                        val += build.health;
                    }
                }
                return max == 0f ? 0f : val / max;
            }).blink(Color.white).outline(new Color(0, 0, 0, 0.6f), 4f)).grow()) // Conditions to show the bar \/ In this case, it shows up in Onset sector if it's not captured or lost.
            .fillX().width(mobile ? 200f : 350f).height(50f).name("protect").visible(() -> checkSector(SectorPresets.onset)).padLeft(5).row();

            t.table(Styles.black3, p -> p.margin(4).label(() -> "").style(Styles.outlineLabel)).touchable(Touchable.disabled).with(p -> hudLabel = p)
            .with(p -> p.visible(() -> (p.color.a = Mathf.lerpDelta(p.color.a, Mathf.num(false), 0.2f)) >= 0.001f));
        });
    }
    public boolean checkSector(SectorPreset sector){
        if(state.rules.sector == null) return false;
        return state.isCampaign() && state.rules.sector.preset == sector && !state.rules.sector.isCaptured() && !state.gameOver;
    }
}